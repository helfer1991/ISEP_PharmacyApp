package lapr.project.service;

import lapr.project.data.OrderDB;
import lapr.project.data.StorageDB;
import lapr.project.data.TransferDB;
import lapr.project.data.VehicleDB;
import lapr.project.model.*;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServiceTransfer {

    StorageDB storageDB;
    TransferDB transferDB;
    VehicleDB vehicleDB;
    OrderDB orderDB;

    public ServiceTransfer() {
        storageDB = new StorageDB();
        transferDB = new TransferDB();
        vehicleDB = new VehicleDB();
        orderDB = new OrderDB();
    }

    public Iterable<Transfer> createTransferRequests(Pharmacy requestingPharmacy, ShoppingCart productsToTransfer, Iterable<Pharmacy> reachablePharmacies, int orderId) throws SQLException {

        ArrayList<Pharmacy> listPharmacies = new ArrayList<>();
        reachablePharmacies.forEach(listPharmacies::add);

        //list to return
        List<Transfer> transfersList = new ArrayList<>();

        //reachablePharmacies ordered by how far away they are from the requestingPharmacy (closer to farther away)
        sortPharmaciesByDistance(listPharmacies, requestingPharmacy);

        // for each pharmacy in reachablePharmacies iterates over all the needed products and
        // if the pharmacy has the product AND ALL the quantity needed in stock then the transfer is requested/created.
        //(this prioritizes shorter trips (closer pharmacies))
        createTransfersByProximity(requestingPharmacy, listPharmacies, productsToTransfer, transfersList, orderId);

        //if, at the end, there are still items left to transfer
        //those transfers will be created prioritizing visits to the pharmacies that have the highest amount of the required product
        //(minimizes the amount of trips needed, even if they are farther away)
        if (!productsToTransfer.getProductMap().isEmpty()) {
            createPartialTransfers(requestingPharmacy, listPharmacies, productsToTransfer, transfersList, orderId);
        }

        if (!productsToTransfer.getProductMap().isEmpty()) {
            throw new IllegalStateException("Error creating TransferRequests. Not all products were requested");
        }

        return transfersList;
    }

    public void createTransfersByProximity(Pharmacy requestingPharmacy,
            List<Pharmacy> listPharmacies, ShoppingCart productsToTransfer,
            List<Transfer> transfersList, int orderId) throws SQLException {

        Transfer tempTransfer;
        Map<Product, Integer> reachablePharmacyStock;

        //the transfered products will be marked to be removed after the for loop to avoid concurrent modification of the map
        ArrayList<Product> removeFromMap = new ArrayList<>();

        for (Pharmacy ph : listPharmacies) {
            for (Product product : productsToTransfer.getProductMap().keySet()) {

                reachablePharmacyStock = storageDB.getProductsByPharmacy(ph).getProductMap();
                //if neighbour pharmacy has the product and all the quantity needed the transfer is created and added
                if (reachablePharmacyStock.containsKey(product)
                        && reachablePharmacyStock.get(product) >= productsToTransfer.getProductMap().get(product)
                        && !removeFromMap.contains(product)) {

                    tempTransfer = new Transfer(requestingPharmacy, ph, product, productsToTransfer.getProductMap().get(product), orderId);
                    transfersList.add(tempTransfer);
                    removeFromMap.add(product);
                }
            }
        }

        for (Product p : removeFromMap) {
            productsToTransfer.getProductMap().remove(p);
        }

    }

    public void createPartialTransfers(Pharmacy requestingPharmacy,
            List<Pharmacy> listPharmacies, ShoppingCart productsToTransfer,
            List<Transfer> transfersList, int orderId) throws SQLException {

        Map<Product, Integer> reachablePharmacyProducts;
        Transfer tempTransfer;

        //the transferred products will be marked to be removed after the for loop, to avoid concurrent modification of the map
        ArrayList<Product> removeFromMap = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : productsToTransfer.getProductMap().entrySet()) {

            //sort pharmacies by product quantity of 'product' (more product first)
            sortPharmaciesByProductStockDesc(listPharmacies, entry.getKey());

            for (Pharmacy pharmacy : listPharmacies) {

                reachablePharmacyProducts = storageDB.getProductsByPharmacy(pharmacy).getProductMap();

                if (reachablePharmacyProducts.containsKey(entry.getKey()) && !removeFromMap.contains(entry.getKey())) {
                    //if the pharmacy(that sends) has more than the necessary amount
                    if (reachablePharmacyProducts.get(entry.getKey()) >= productsToTransfer.getProductMap().get(entry.getKey())) {
                        tempTransfer = new Transfer(requestingPharmacy, pharmacy, entry.getKey(), productsToTransfer.getProductMap().get(entry.getKey()), orderId);
                        transfersList.add(tempTransfer);
                        removeFromMap.add(entry.getKey());
                    } else { // the pharmacy(that sends) has the product but doesn't have all the quantity needed
                        tempTransfer = new Transfer(requestingPharmacy, pharmacy, entry.getKey(), reachablePharmacyProducts.get(entry.getKey()), orderId);
                        transfersList.add(tempTransfer);
                        entry.setValue(productsToTransfer.getProductMap().get(entry.getKey()) - reachablePharmacyProducts.get(entry.getKey()));
                    }

                }
            }
        }

        for (Product p : removeFromMap) {
            productsToTransfer.getProductMap().remove(p);
        }
    }

    public void sortPharmaciesByDistance(List<Pharmacy> listPharmacies, Pharmacy requestingPharmacy) {
        //sorted according to the min charge required to get to that pharmacy (by Scooter)
        //we simplified by assuming that by drone it was roughly the same order
        listPharmacies.sort(Comparator.comparingDouble(
                ph -> getMinEnergyForTripByScooter(requestingPharmacy.getAddress(), ph.getAddress())
        ));
    }

    public void sortPharmaciesByProductStockDesc(List<Pharmacy> listPharmacies, Product product) {
        listPharmacies.sort(Comparator.comparing(ph -> {
            try {
                return storageDB.getProductsByPharmacy((Pharmacy) ph).getProductMap().get(product);
            } catch (SQLException e) {
                Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            }
            return null;
        }).reversed());
    }

    public boolean insertTransfer(Transfer transfer) throws SQLException {
        int transferNoteId = insertTransferDocument(transfer, TransferDocType.TransferNote);
        if (transferNoteId == 0) {
            throw new IllegalStateException("Error inserting transferNote");
        }
        return transferDB.insertTransfer(transfer, transferNoteId);
    }

    /**
     * Returns the minimum energy needed (using a Scooter) to complete the trip
     * between two addresses.
     *
     * @param startAddress
     * @param endAddress
     * @return
     */
    public double getMinEnergyForTripByScooter(Address startAddress, Address endAddress) {
        
        if (startAddress.equals(endAddress)) {
            return 0;
        }

        GraphDelivery graph = PharmaDeliveriesApp.getInstance().getTerrestrialGraph();
        LinkedList<Address> bestPathScooter = graph.mostEfficientPath(startAddress, endAddress, new LinkedList<>());
        
        if (bestPathScooter == null) {
            return Double.MAX_VALUE;
        }

        return graph.getWeightFromPath(bestPathScooter);

    }

    /**
     * Returns the minimum energy needed (using a Drone) to complete the trip
     * between two addresses.
     *
     * @param startAddress
     * @param endAddress
     * @return
     */
    public double getMinEnergyForTripByDrone(Address startAddress, Address endAddress) {
        
        if (startAddress.equals(endAddress)) {
            return 0;
        }

        GraphDelivery graph = PharmaDeliveriesApp.getInstance().getAerialGraph();
        LinkedList<Address> bestPathDrone = graph.mostEfficientPath(startAddress, endAddress, new LinkedList<>());


        if (bestPathDrone == null) {
            return Double.MAX_VALUE;
        }

        return graph.getWeightFromPath(bestPathDrone);
    }

    /**
     * Inserts the transferdocument in the database and returns the id that was generated in the Transfer_Document.
     * @param transfer
     * @param transferDocType
     * @return
     * @throws SQLException
     */
    public int insertTransferDocument(Transfer transfer, TransferDocType transferDocType) throws SQLException {
        String transferDocument = "";
        if (transferDocType == TransferDocType.TransferNote) {
            transferDocument = createTransferNote(transfer);
        } else {
            if (transferDocType == TransferDocType.DeliveryNote) {
                transferDocument = createDeliveryNote(transfer);
            }
        }
        return transferDB.insertTransferDocument(transferDocument);
    }

    public String createDeliveryNote(Transfer transfer) {
        String deliveryNote = "Delivery note: "
                + transfer.getQuantity() + " unit(s) of " + transfer.getProduct().getDescription()
                + " (Id: " + transfer.getProduct().getId() + ") were sent from pharmacy \""
                + transfer.getPharmacySending().getName() + "\" (Id: " + transfer.getPharmacySending().getId() + ") and received in pharmacy \""
                + transfer.getPharmacyAsking().getName() + "\" (Id: " + transfer.getPharmacyAsking().getId() + ").";
        return deliveryNote;
    }

    public String createTransferNote(Transfer transfer) {
        String transferNote = "Transfer note: "
                + transfer.getQuantity() + " unit(s) of " + transfer.getProduct().getDescription()
                + " (Id: " + transfer.getProduct().getId() + ") were sent from pharmacy \""
                + transfer.getPharmacySending().getName() + "\" (Id: " + transfer.getPharmacySending().getId() + ") to pharmacy \""
                + transfer.getPharmacyAsking().getName() + "\" (Id: " + transfer.getPharmacyAsking().getId() + ").";
        return transferNote;
    }

    public boolean validateIncomingTransfer(Transfer transfer) throws SQLException {
        return transferDB.validateIncomingTransfer(transfer);
    }


    public void markTransferAsReceived(Transfer transfer) throws SQLException {
        transferDB.markTransferAsReceived(transfer);
    }

    public boolean associateDeliveryNote(Transfer transfer, int deliveryNoteId) throws SQLException {
        return transferDB.associateDeliveryNoteToTransfer(transfer, deliveryNoteId);
    }

    public boolean checkIfOrderHasOpenTransfers(int orderId) throws SQLException {
        return transferDB.checkIfOrderHasOpenTransfers(orderId);
    }

    public void markOrderAsReady(int orderId) throws SQLException {
        orderDB.markOrderAsReady(orderId);
    }

    public boolean removeStockFromSendingPharmacy(Transfer transfer) throws SQLException {
        Map.Entry<Product, Integer> mapEntry = new AbstractMap.SimpleEntry<>(transfer.getProduct(), transfer.getQuantity()*-1);
            return ( storageDB.insertProductQuantity(transfer.getPharmacySending(), mapEntry) != null);
    }

    public void setStorageDB(StorageDB storageDB) {
        this.storageDB = storageDB;
    }

    public void setTransferDB(TransferDB transferDB) {
        this.transferDB = transferDB;
    }
}
