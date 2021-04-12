package lapr.project.controller;

import lapr.project.model.*;
import lapr.project.service.*;
import lapr.project.ui.dto.ClientDTO;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.ui.dto.StorageDTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import lapr.project.ui.dto.OrderDTO;

public class BackOrderController {

    Logger logger;
    PharmaDeliveriesApp appInstance;
    ServiceStorage serviceStorage;
    ServiceBackOrder serviceBackOrder;
    ServiceTransfer serviceTransfer;
    ServicePharmacy servicePharmacy;
    ServiceVehicle serviceVehicle;
    TransferProductsController transferProductsController;
    Iterable<Pharmacy> reachablePharmacies;

    public BackOrderController() {
        logger = Logger.getGlobal();
        appInstance = PharmaDeliveriesApp.getInstance();
        serviceStorage = appInstance.getServiceStorage();
        serviceBackOrder = appInstance.getServiceBackOrder();
        serviceTransfer = appInstance.getServiceTransfer();
        servicePharmacy = appInstance.getPharmacyService();
        serviceVehicle = appInstance.getServiceVehicle();
        transferProductsController = new TransferProductsController();
    }

    /**
     * Return the nearest pharmacy to the client making the order
     * @return nearest Pharmacy
     */
    public PharmacyDTO getNearestPharmacy() throws SQLException {
        if (appInstance.getCurrentSession() == null) {
            return null;
        }
        User currentUser = appInstance.getCurrentSession().getUser();
        Pharmacy nearestPharmacy = serviceBackOrder.getNearestPharmacyByUser(currentUser);
        return DTOConverter.convertPharmacy(nearestPharmacy);
    }

    /**
     * If there is a client logged in returns the reachable products for that client. Else retrieves all products.
     * @param pharmacyDTO
     * @return
     * @throws SQLException
     */
    public StorageDTO getProducts(PharmacyDTO pharmacyDTO) throws SQLException {

        if (PharmaDeliveriesApp.getInstance().getCurrentSession() != null) {
            return getReachableProducts(pharmacyDTO);
        } else {
            return getAllProducts();
        }
    }


    /**
     * Returns all products registered available in the system
     * @return
     * @throws SQLException
     */
    public StorageDTO getAllProducts() throws SQLException {
        Storage tmp = serviceStorage.getAllProducts();
        if (tmp == null) {
            return null;
        }
        return DTOConverter.convertStorage(tmp);
    }

    /**
     * Returns all the products that any given pharmacy has access too. These
     * products include products in stock and products available through
     * backOrder by transferring the products from reachable pharmacies
     * @param pharmacyDTO pharmacy
     * @return Storage containing all the products that are reachable by this pharmacy
     */
    public StorageDTO getReachableProducts(PharmacyDTO pharmacyDTO) throws SQLException {
        Pharmacy pharmacy = DTOConverter.convertPharmacyDTO(pharmacyDTO);

        Iterable<Pharmacy> reachablePharmacies = getReachablePharmacies(pharmacy);
        Storage reachableProducts = new Storage();
        Storage tempStorage;

            for (Pharmacy f : reachablePharmacies) {
                tempStorage = serviceStorage.getProductsByPharmacy(f);
                if (tempStorage != null) {
                    reachableProducts.addStorage(tempStorage);
                }
            }


        return DTOConverter.convertStorage(reachableProducts);
    }

    /**
     * All the pharmacies that are reachable in a single scooter/drone run
     * (without stops)
     * @param startPharmacy
     * @return
     */
    public Iterable<Pharmacy> getReachablePharmacies(Pharmacy startPharmacy) throws SQLException {
        ArrayList<Pharmacy> reachablePharmacies = new ArrayList<>();
        Iterable<Pharmacy> allPharmacies = servicePharmacy.getAllPharmacies();
        for (Pharmacy endPharmacy : allPharmacies){
            if ( serviceVehicle.getScooterMaxBatteryCapacity()
                    - serviceTransfer.getMinEnergyForTripByScooter(startPharmacy.getAddress(), endPharmacy.getAddress())
                    > 0){
                reachablePharmacies.add(endPharmacy);
            }
            if ( serviceVehicle.getDroneMaxBatteryCapacity()
                - serviceTransfer.getMinEnergyForTripByDrone(startPharmacy.getAddress(), endPharmacy.getAddress())
                    > 0){
                if( !reachablePharmacies.contains(endPharmacy))
                reachablePharmacies.add(endPharmacy);
            }
        }
        this.reachablePharmacies = reachablePharmacies;
        return reachablePharmacies;
    }

    /**
     *
     * @param pharmacyDTO
     * @param orderDTO
     * @param clientDTO
     * @return
     * @throws SQLException
     */
    public OrderDTO registerOrderRequest(PharmacyDTO pharmacyDTO, OrderDTO orderDTO, ClientDTO clientDTO) throws SQLException {

        Pharmacy pharmacy = DTOConverter.convertPharmacyDTO(pharmacyDTO);
        Order order = DTOConverter.convertOrderDTO(orderDTO);
        Client client = DTOConverter.convertClientDTO(clientDTO);

        Storage productsNeeded = serviceStorage.productsRequiringTransfer(pharmacy, order.getShopCart());

        Order createdOrder = null;
        if (productsNeeded.getProductMap().isEmpty()) { //all products are in stock -> transfer is not needed
            createdOrder = serviceBackOrder.insertOrder(pharmacy, order, client,false);
            if (createdOrder != null) {
                return DTOConverter.convertOrder(createdOrder);
            } else {
                throw new IllegalStateException("Couldn't insert order (order without transfer).");
            }

        } else { //transfer is needed
            if (!isTransferPossible(pharmacy, order.getShopCart())) {
                throw new IllegalStateException("This order requires products that are not reachable.");
            }

            //transfer is possible so order can be inserted
            createdOrder = serviceBackOrder.insertOrder(pharmacy, order, client, true);
            if (createdOrder != null) {
                if (transferProductsController.transferProducts(pharmacy, new ShoppingCart(productsNeeded.getProductMap()), reachablePharmacies, createdOrder.getIdOrder())) {
                    return DTOConverter.convertOrder(createdOrder);
                } else {
                    throw new IllegalStateException("Order inserted but the transfer request failed.");
                }
            } else {
                throw new IllegalStateException("Couldn't insert order (order with transfer).");
            }
        }
    }

    public boolean isTransferPossible(Pharmacy pharmacy, ShoppingCart productsNeeded) throws SQLException {
        PharmacyDTO pharmacyDTO = DTOConverter.convertPharmacy(pharmacy);
        StorageDTO totalProductsDTO = getReachableProducts(pharmacyDTO);
        Storage totalProducts = DTOConverter.convertStorageDTO(totalProductsDTO);

        Storage productsToTransfer = new Storage(productsNeeded.getProductMap());
        productsToTransfer.subtractStorage(totalProducts);
        //if productsToTransfer is empty it means it's a subset of all the reachable products -> return true
        return productsToTransfer.getProductMap().isEmpty();

    }

    public void setTransferProductsController(TransferProductsController transferProductsController) {
        this.transferProductsController = transferProductsController;
    }
}
