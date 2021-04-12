/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import lapr.project.model.Client;
import lapr.project.model.Order;
import lapr.project.model.Pharmacy;
import lapr.project.model.Product;

/**
 *
 * @author astfr
 */
public class OrderDB extends DataHandler {

    /*
    Method takes Pharmacy, ShoppingCart and Client data and executes Insert commands
    to Order_Entry, Order_Detail, Invoice and Payment tables in dataBase.
     */
    public Order insertOrder(Pharmacy pharmacy, Order order, Client client , boolean transferStatus) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
             stmt = conn.createStatement();

            //Verify if exists credit card and if not add and use id on insertoderd
             rs = stmt.executeQuery("select * from credit_card where numero = '" + client.getCreditCard().getNumber() + "'");
            if (!rs.next()) {
                rs = stmt.executeQuery(CommonDB.insertCreditCardQuery(client));
            }

            //Insert Order
            rs = stmt.executeQuery("select max(id_order) id from order_entry");
            int newOrderId = 0;
            if (rs.next()) {
                newOrderId = rs.getInt("id");
            }
            order.setIdOrder(newOrderId + 1);

            rs = stmt.executeQuery(CommonDB.insertOrderEntry(pharmacy, client, order));

            //Order Details
            for (Map.Entry<Product, Integer> entry : order.getShopCart().getProductMap().entrySet()) {
                Product p = entry.getKey();
                Integer qt = entry.getValue();
                rs = stmt.executeQuery(CommonDB.insertOrderDetail(order, p, qt));
                //Remove quantity
                rs = stmt.executeQuery("select max(id_transaction) id from pharmacy_product");
                int id = 1;
                if (rs.next()) {
                    id = rs.getInt("id") + 1;
                };
                entry.setValue(entry.getValue() * -1);
                rs = stmt.executeQuery(CommonDB.insertPharmacyProductQuery(pharmacy, entry, id));
                entry.setValue(entry.getValue() * -1);
            }

            //OrderStatus
            if(transferStatus){
                rs = stmt.executeQuery(CommonDB.insertOrderStatus(order.getIdOrder(), 2));
            }else{
                rs = stmt.executeQuery(CommonDB.insertOrderStatus(order.getIdOrder(), 1));
            }
            

            //Insert Invoice for Order newOrderId
            rs = stmt.executeQuery(CommonDB.insertInvoice(order, order.getInvoice().toString(), order.getInvoice().getTotalCost()));

            //Insert Payment
            rs = stmt.executeQuery(CommonDB.insertPayment(order));

            //Credit Orders
            rs = stmt.executeQuery(CommonDB.insertCredits(order, client));

            rs.close();
            return order;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
         } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }

    }


    public boolean insertOrderStatus(int orderId, int status) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            int affectedRows = stmt.executeUpdate("INSERT INTO ORDER_STATUS (FK_ORDER_ID, FK_STATUS_ID, DATE_ENTRY) VALUES(" + orderId + "," + status + ", sysdate) ");

            return affectedRows == 1;
        } catch (SQLException e) {
            throw new SQLException("Error at insertOrderStatus" + e.getMessage());
         } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }

    }
    public boolean markOrderAsInTransit(Order order) throws SQLException {
        int deliveredStatus = 4;
        OrderDB orderDB = new OrderDB();
        return orderDB.insertOrderStatus(order.getIdOrder(), deliveredStatus);
    }

    public boolean markOrderAsDelivered(Order order) throws SQLException {
        int deliveredStatus = 5;
        OrderDB orderDB = new OrderDB();
        return orderDB.insertOrderStatus(order.getIdOrder(), deliveredStatus);
    }

    public void markOrderAsForcedShipping(Order order) throws SQLException {
        int forcedShippingStatus = 3;
        OrderDB orderDB = new OrderDB();
        orderDB.insertOrderStatus(order.getIdOrder(), forcedShippingStatus);
    }

    public void markOrderAsReady(int orderId) throws SQLException {
        int forcedShippingStatus = 1;
        OrderDB orderDB = new OrderDB();
        orderDB.insertOrderStatus(orderId, forcedShippingStatus);
    }

}
