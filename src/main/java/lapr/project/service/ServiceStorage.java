package lapr.project.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import lapr.project.data.OrderDB;
import lapr.project.data.StorageDB;
import lapr.project.model.*;

import static lapr.project.utils.Constants.ROLE_ADMINISTRATOR;


public class ServiceStorage {

    StorageDB storageDB;
    OrderDB orderDB;
    
    public ServiceStorage() {
        this.storageDB = new StorageDB();
        this.orderDB = new OrderDB();
    }

    public Map.Entry<Product, Integer> insertProductQuantity(Pharmacy pharmacy, Map.Entry<Product, Integer> entry) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null) {
            return null;
        }
        //updateOrdersWaitingForTransfer(pharmacy, entry.getKey(), entry.getValue());
        return this.storageDB.insertProductQuantity(pharmacy, entry);
    }


    public Map.Entry<Product, Integer> removeProductQuantity(Pharmacy pharmacy, Map.Entry<Product, Integer> entry) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null
                || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)) {
            return null;
        }
        entry.setValue(entry.getValue() * -1);
        return this.storageDB.insertProductQuantity(pharmacy, entry);
    }

    public Product updateProduct(Pharmacy pharmacy, Product product) throws SQLException {
       if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null
               || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)) {
            return null;
        }
        return this.storageDB.updateProduct(pharmacy, product);
    }

    public Product insertProduct(Product product) throws SQLException {
       if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null
               || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)) {
            return null;
        }
        return this.storageDB.insertProduct(product);
    }

    public Storage getProductsByPharmacy(Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null) {
            return null;
        }
        return storageDB.getProductsByPharmacy(pharmacy);
    }
    
    public Storage getAllProducts() throws SQLException {
        return this.storageDB.getAllProducts();
    }

    public Storage productsRequiringTransfer(Pharmacy pharmacy, ShoppingCart shoppingCart) throws SQLException {
        Storage pharmacyProducts = getProductsByPharmacy(pharmacy);
        Storage productsNeeded = new Storage(shoppingCart.getProductMap());
        productsNeeded.subtractStorage(pharmacyProducts);
        return productsNeeded;
    }

    
    /*
    * Assignes a new StorageDB to the current ServiceOrder.
    * Used to run Tests with mocked StorageDB
    */
    public void setStorageDB(StorageDB storageDB) {
        this.storageDB = storageDB;
    }

}
