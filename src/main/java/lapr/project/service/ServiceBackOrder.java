package lapr.project.service;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import lapr.project.data.ClientDB;

import lapr.project.data.OrderDB;
import lapr.project.data.PharmacyDB;
import lapr.project.model.*;
import lapr.project.utils.Constants;

import static lapr.project.utils.Constants.ROLE_CLIENT;
import static lapr.project.utils.Constants.VAT;

/**
 *
 */
public class ServiceBackOrder {

    OrderDB orderDB;
    ClientDB clientDB;
    PharmacyDB pharmacyDB;

    public ServiceBackOrder() {
        orderDB = new OrderDB();
        clientDB = new ClientDB();
        pharmacyDB = new PharmacyDB();
    }

    public Pharmacy getNearestPharmacyByUser(User user) throws SQLException {
        
        if (user == null) {
            return null;
        }
        Client client = clientDB.getClientByUserSession(user);
        return getNearestPharmacy(client.getAddress());
    }


    public Pharmacy getNearestPharmacy(Address address) {
        Iterable<Pharmacy> pharmacies = null;
        try{
             pharmacies = pharmacyDB.getAllPharmacies();
        }catch(SQLException e){

        }
        Pharmacy nearestPharmacy = null;
        double distanceMin = Double.MAX_VALUE;
        if(pharmacies == null){
            return null;
        }

        for (Pharmacy pharmacy : pharmacies) {


            LinkedList<Address> tmp = PharmaDeliveriesApp.getInstance().getDistanceTerrestrialGraph().mostEfficientPath(pharmacy.getAddress(), address, new LinkedList<Address>());
                if(tmp!=null){
                    double distance = PharmaDeliveriesApp.getInstance().getDistanceTerrestrialGraph().getWeightFromPath(tmp);
            if (distance<distanceMin){
                distanceMin = distance;
                nearestPharmacy = pharmacy;
            } 
            }
        }
        return nearestPharmacy;
    }





    public Order insertOrder(Pharmacy pharmacy, Order order, Client client, boolean transferStatus) throws SQLException {

        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null
                || (!PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_CLIENT))) {
            return null;
        }

        if (order.getShopCart().getShoppingCartWeight() > pharmacy.getMaximumPayloadCourier()) {
            throw new SQLException("Shopping cart has " + order.getShopCart().getShoppingCartWeight() + " kg and limit is " + pharmacy.getMaximumPayloadCourier() + " kg.");
        }

        if (client.getCredits() < 0) {
            order.setInvoice(new Invoice((float) (order.getShopCart().getTotalCostShoppingCart() * ((float) VAT / 100 + 1) + Constants.DELIVERY_FEE+ client.getCredits())));
            order.setCredits((int) (order.getShopCart().getTotalCostShoppingCart() * 0.1) + client.getCredits());
        } else {
            order.setInvoice(new Invoice((float) (order.getShopCart().getTotalCostShoppingCart() * ((float) VAT / 100 + 1) + Constants.DELIVERY_FEE)));
            order.setCredits((int) (order.getShopCart().getTotalCostShoppingCart() * 0.1));
        }

        Order tmp = orderDB.insertOrder(pharmacy, order, client, transferStatus);
        if (tmp != null) {
            sendInvoiceEmail(client, order.getInvoice(), order.getShopCart());
        }
        return tmp;
    }

    public boolean sendInvoiceEmail(Client client, Invoice invoice, ShoppingCart shoppingCart) throws SQLException {

        String content = "";
        if (shoppingCart.getProductMap().size() > 0) {
            System.out.println("");
            for (Map.Entry<Product, Integer> en : shoppingCart.getProductMap().entrySet()) {
                Product key = en.getKey();
                Integer value = en.getValue();
                if (key.getDescription() == null) {
                    content = content + String.format("%-25s %3d  --------------------- (x %3d) %.2f € \n", "Product Id: ", key.getId(), value, key.getPrice() * value);
                } else {
                    content = content + String.format("%-25s  ------------------------- (x %3d) %.2f € \n", key.getDescription(), value, key.getPrice() * value);
                }
            }
            content = content + String.format("%-25s  --------------------------------- %.2f € \n", "Total (c/ Iva)", invoice.getTotalCost());
        }
        //System.out.println(invoice.getTag());
        //System.out.println(content);
        return PharmaDeliveriesApp.getInstance().getServiceEmail().sendEmail(client.getEmail(), invoice.getTag(), content);
    }

    public void setOrderDB(OrderDB orderDB) {
        this.orderDB = orderDB;
    }

    public void setClientDB(ClientDB clientDB) {
        this.clientDB = clientDB;
    }

    public void setPharmacyDB(PharmacyDB pharmacyDB) {
        this.pharmacyDB = pharmacyDB;
    }
}
