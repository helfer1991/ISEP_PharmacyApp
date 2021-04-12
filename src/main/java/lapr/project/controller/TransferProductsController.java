package lapr.project.controller;

import lapr.project.model.*;
import lapr.project.service.ServicePharmacy;
import lapr.project.service.ServiceStorage;
import lapr.project.service.ServiceTransfer;
import lapr.project.ui.dto.ProductDTO;
import lapr.project.ui.dto.TransferDTO;

import java.util.AbstractMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransferProductsController {

    Logger logger;
    PharmaDeliveriesApp appInstance;
    ServicePharmacy servicePharmacy;
    ServiceTransfer serviceTransfer;
    ServiceStorage serviceStorage;


    public TransferProductsController (){
        logger = Logger.getGlobal();
        appInstance = PharmaDeliveriesApp.getInstance();
        servicePharmacy = appInstance.getPharmacyService();
        serviceTransfer = appInstance.getServiceTransfer();
        serviceStorage = appInstance.getServiceStorage();
    }

    public boolean transferProducts(Pharmacy requestingPharmacy, ShoppingCart productsToTransfer,
                                    Iterable<Pharmacy> reachablePharmacies, int orderId){

        try{

            Iterable<Transfer> transfers =  serviceTransfer.createTransferRequests(requestingPharmacy,
                    productsToTransfer, reachablePharmacies, orderId);

            if(transfers == null){
                throw new IllegalStateException("Transfer requests could not be created.");
            }

            for(Transfer t : transfers){

                if(!serviceTransfer.removeStockFromSendingPharmacy(t)
                    || !serviceTransfer.insertTransfer(t)){
                    throw new IllegalStateException("Transfer was processed correctly.");
                }

            }
            return true;

        }catch(Exception e){
            //logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }



    public Map.Entry<ProductDTO, Integer> receiveTransfer(TransferDTO transferDTO) {

        Transfer transfer = DTOConverter.convertTransferDTO(transferDTO);
        try{
            //check if the system is expecting a transfer with these characteristics and (it it is) puts the TransferId in the transfer object
            // (if there are multiple transfers like this, gets the id from the transfer that was created first)
            if(! serviceTransfer.validateIncomingTransfer(transfer) ){
                throw new IllegalStateException("There are no transfer requests for this product/pharmacy combination.\n Input correct information or add the product regularly");
            }

            int deliveryNoteId = serviceTransfer.insertTransferDocument(transfer, TransferDocType.DeliveryNote);
            if( !serviceTransfer.associateDeliveryNote(transfer , deliveryNoteId)){
                throw new IllegalStateException("Delivery note couldn't be inserted in the Database");
            }


            Map.Entry<Product, Integer> inputEntry = new AbstractMap.SimpleEntry<>(transfer.getProduct(), transfer.getQuantity());

            Map.Entry<Product, Integer> outputEntry =  serviceStorage.insertProductQuantity(transfer.getPharmacyAsking(), inputEntry);
            if (outputEntry == null){
                return null;
            }

            serviceTransfer.markTransferAsReceived(transfer);

            //if this transfer was the only/last transfer that the order was missing then the order is marked as ready to deliver
            if( !serviceTransfer.checkIfOrderHasOpenTransfers(transfer.getOrderId())){
                serviceTransfer.markOrderAsReady(transfer.getOrderId());
            }

            return DTOConverter.convertMapEntry(outputEntry);

        }catch(Exception e){
            //Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
        }

        return null;
    }








}
