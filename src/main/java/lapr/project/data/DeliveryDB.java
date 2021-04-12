package lapr.project.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lapr.project.model.*;

public class DeliveryDB extends DataHandler {

    /**
     * Returns the orders for this pharmacy that are have status 'ready to
     * delivery'
     *
     * @param pharmacy
     * @return
     * @throws SQLException
     */
    public List<Deliverable> getOrdersReadyToDeliverByPharmacy(Pharmacy pharmacy) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int statusReadyToDeliver = 1;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from order_entry inner join order_status on order_status.fk_order_id = order_entry.id_order "
                    + "where (order_status.fk_order_id, order_status.date_entry, order_status.fk_status_id) in "
                    + "( select order_status.fk_order_id, order_status.date_entry, order_status.fk_status_id from order_status "
                    + "where (order_status.fk_order_id, order_status.date_entry) in (select order_status.fk_order_id, max(order_status.date_entry) "
                    + "from order_status group by order_status.fk_order_id)) and order_status.fk_status_id= " + statusReadyToDeliver + " and order_entry.fk_pharmacy_id = " + pharmacy.getId() + " "
                    + "order by order_status.date_entry asc");

            AddressDB addressDB = new AddressDB();
            List<Deliverable> ordersReady = new LinkedList<>();
            Order order;

            while (rs.next()) {
                order = new Order(rs.getInt("id_Order"), rs.getInt("delivery_fee"), rs.getInt("fk_status_id"), rs.getNString("date_entry"));
                order.setPharmacyId(rs.getInt("fk_pharmacy_id"));
                order.setAddress(addressDB.getAddressById(rs.getInt("fk_shipment_address_id")));
                order.setShopCart(getShoppingCart(order));

                ordersReady.add(order);
            }
            return ordersReady;

        } catch (SQLException e) {
            throw new SQLException("Error at getOrdersToDeliverByFarmacy" + e.getMessage());
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

    /**
     * Returns the shopping cart of a specific order
     *
     * @param order
     * @return
     * @throws SQLException
     */
    public ShoppingCart getShoppingCart(Order order) throws SQLException {
       Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from order_detail "
                    + "join order_entry on order_entry.id_order = order_detail.fk_order_id "
                    + "join product on product.id_product = order_detail.fk_product_id "
                    + "where order_entry.id_order = " + order.getIdOrder());

            ShoppingCart shoppingCart = new ShoppingCart();
            Product product = null;

            while (rs.next()) {
                product = new Product(rs.getInt("fk_product_id"), rs.getString("description"), rs.getFloat("price"), rs.getFloat("weight"));
                shoppingCart.addProductToShoppingCart(product, rs.getInt("quantity"));
            }

            return shoppingCart;
        } catch (SQLException e) {
            throw new SQLException("Error at getShoppingCart" + e.getMessage());
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

    /**
     * Inserts the information related to a deliveryRun (delivery table and
     * delivery_stops table)
     *
     * @param deliveryRun contains the information on the delivery run
     * @param costMap contains list of stops during a delivery and the the cost
     * @param vehicle contains information on the drone or scooter that made the
     * delivery
     * @return
     * @throws SQLException
     */
    public boolean insertDeliveries(DeliveryRun deliveryRun, LinkedHashMap<Address, Double> costMap, Vehicle vehicle, Courier courier) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            //total cost
            double cost = 0;
            for (Map.Entry<Address, Double> entry : costMap.entrySet()) {
                Address key = entry.getKey();
                Double value = entry.getValue();
                cost = cost + value;
            }
            
            //delivery
            rs = stmt.executeQuery("select max(id_delivery) id from delivery");
            int id = 1;
            if (rs.next()) {
                id = rs.getInt("id");
            }
            deliveryRun.setId(id + 1);
            rs = stmt.executeQuery(CommonDB.insertDeliveryRun(deliveryRun, cost));

            //stops
            for (Deliverable d : deliveryRun.getDeliverables()) {
                    rs = stmt.executeQuery(CommonDB.insertDeliveryStop(deliveryRun, d, courier, vehicle));

            }

            return true;
        } catch (SQLException e) {
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



    /**
     * Returns the number of minutes passed since the order status was updated
     * to to 'ready'.
     *
     * @param order
     * @return
     * @throws SQLException
     */
    public double getOrderWaitingTime(Order order) throws SQLException {
      Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int readyStatus = 2;
        double minutes;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            //assuming that for each order there can only be one status of 'ready'
            rs = stmt.executeQuery("SELECT ( sysdate -  DATE_ENTRY ) * 1440 FROM ORDER_STATUS "
                    + "where FK_ORDER_ID = " + order.getIdOrder() + " and ORDER_STATUS.FK_STATUS_ID = " + readyStatus);

            minutes = rs.getDouble(1);

            return minutes;

        } catch (SQLException e) {
            throw new SQLException("Error at getOrderWaitingTime" + e.getMessage());
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



    /**
     * Devolve as transferencias (pedidas por esta farmácia) que o
     * courirer/drone ainda nao foi buscar
     *
     * @param pharmacy
     * @return
     */
    public List<Deliverable> getTransfersReadyByPharmacy(Pharmacy pharmacy) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int statusReadyToDeliver = 1;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from transfer\n"
                    + "    inner join transfer_status on transfer_status.FK_Transfer_Id = transfer.ID_TRANSFER\n"
                    + "    where (transfer_status.fk_transfer_id, transfer_status.date_entry, transfer_status.fk_transfer_id) in\n"
                    + "    ( select transfer_status.fk_transfer_id, transfer_status.date_entry, transfer_status.fk_transfer_id from transfer_status\n"
                    + "    where (transfer_status.fk_transfer_id, transfer_status.date_entry) in (select transfer_status.fk_transfer_id, max(transfer_status.date_entry)\n"
                    + "    from transfer_status group by transfer_status.fk_transfer_id)) and transfer_status.fk_transfer_status_id= " + statusReadyToDeliver + " and transfer.FK_PHARMACY_ID_ISSUER = " + pharmacy.getId() + "\n"
                    + "    order by transfer_status.date_entry asc ");

            Transfer transfer;
            List<Deliverable> transfersReady = new LinkedList<>();
            PharmacyDB pharmacyDB = new PharmacyDB();
            StorageDB storageDB = new StorageDB();

            while (rs.next()) {
                Pharmacy pharmacySending = pharmacyDB.getPharmacyById(rs.getInt("FK_pharmacy_id_issuer"));
                Pharmacy pharmacyReceiving = pharmacyDB.getPharmacyById(rs.getInt("FK_pharmacy_id_receiver"));
                Product product = storageDB.getProductById(rs.getInt("fk_product_id"));
                int productQuantity = rs.getInt("quantity");
                int orderId = rs.getInt("fk_order_id");
                int transferId = rs.getInt("id_transfer");
                transfer = new Transfer(pharmacySending, pharmacyReceiving, product, productQuantity, orderId);
                transfer.setTransferId(transferId);
                transfersReady.add(transfer);
            }
            return transfersReady;

        } catch (SQLException e) {
            throw new SQLException("Error at getOrdersToDeliverByFarmacy" + e.getMessage());
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

}
