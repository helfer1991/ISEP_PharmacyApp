/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Scanner;

import lapr.project.model.*;

import static lapr.project.utils.Constants.DELIVERY_FEE;

/**
 *
 * @author 1100241
 */
public class CommonDB extends DataHandler {

    public static String insertPharmacyQuery(Pharmacy pharmacy, User administrator) {
        if (pharmacy == null) {
            return "";
        }
        return "INSERT INTO PHARMACY (ID_PHARMACY, NAME, MAXIMUM_PAYLOAD, MINIMUM_PAYLOAD, FK_ADDRESS_ID, FK_ADMINISTRATOR_EMAIL, ISACTIVE) VALUES("
                + pharmacy.getId() + ", '"
                + pharmacy.getName() + "', "
                + pharmacy.getMaximumPayloadCourier() + ","
                + pharmacy.getMinimumLoadCourier() + ", "
                + pharmacy.getAddress().getId() + ", '"
                + administrator.getEmail() + "', "
                + "'true')";
    }

    public static String insertAddressQuery(Address address) {
        if (address == null) {
            return "";
        }

        return "INSERT INTO ADDRESS (ID_ADDRESS, LATITUDE, LONGITUDE, ADDRESS, ZIP_CODE, ELEVATION) VALUES("
                + address.getId() + ","
                + address.getLatitude() + ","
                + address.getLongitude() + ",'"
                + address.getAddress() + "','"
                + address.getZipcode() + "', "
                + address.getElevation() + ")";
    }

    public static String insertParkQuery(Park park) {
        if (park == null) {
            return "";
        }
        return "INSERT INTO PARK (FK_PHARMACY_ID, SCOOTER_CHARGERS_NUMBER, DRONE_CHARGERS_NUMBER, SCOOTER_CHARGER_CAPACITY, DRONE_CHARGER_CAPACITY) VALUES("
                + park.getId() + ", "
                + park.getDroneChargersNumber() + ", "
                + park.getScooterChargersNumber() + ", "
                + park.getScooterChargerCapacity() + ", "
                + park.getDroneChargerCapacity() + ")";
    }

    public static String updatePharmacyQuery(Pharmacy pharmacy) {
        if (pharmacy == null) {
            return "";
        }
        return "UPDATE PHARMACY SET "
                + "NAME = '" + pharmacy.getName() + "', "
                + "MAXIMUM_PAYLOAD = " + pharmacy.getMaximumPayloadCourier() + ", "
                + "MINIMUM_PAYLOAD =" + pharmacy.getMaximumPayloadCourier() + ", "
                + "FK_ADDRESS_ID =" + pharmacy.getAddress().getId() + " "
                + "WHERE ID_PHARMACY =" + pharmacy.getId();
    }

    public static String removePharmacyQuery(Pharmacy pharmacy) {
        if (pharmacy == null) {
            return "";
        }
        return "UPDATE PHARMACY SET "
                + "ISACTIVE = 'false' "
                + "WHERE ID_PHARMACY =" + pharmacy.getId();
    }

    public static String updateAddressQuery(Address address) {
        if (address == null) {
            return "";
        }
        return "UPDATE ADDRESS SET "
                + "LATITUDE = " + address.getLatitude() + ", "
                + "LONGITUDE= " + address.getLongitude() + ", "
                + "ADDRESS= '" + address.getAddress() + "', "
                + "ZIP_CODE= '" + address.getZipcode() + "' "
                + "WHERE ID_ADDRESS = " + address.getId();

    }

    public static String updateParkQuery(Park park) {
        if (park == null) {
            return "";
        }
        return "UPDATE PARK SET SCOOTER_CHARGERS_NUMBER =" + park.getScooterChargersNumber() + ", "
                + "DRONE_CHARGERS_NUMBER =" + park.getDroneChargersNumber() + ", "
                + "SCOOTER_CHARGER_CAPACITY =" + park.getScooterChargerCapacity() + ", "
                + "DRONE_CHARGER_CAPACITY =" + park.getDroneChargerCapacity() + " "
                + "WHERE FK_PHARMACY_ID =" + park.getId();
    }

    public static String insertVehicleQuery(Vehicle vehicle, int vehicle_type, Pharmacy pharmacy) {
        if (vehicle == null) {
            return "";
        }
        return "INSERT INTO Vehicle (ID_Vehicle, FK_PHARMACY_ID, FK_BATTERY_ID, FK_VEHICLE_TYPE_ID, QRCODE, WEIGHT, ISAVAILABLE) VALUES("
                + vehicle.getId() + ", "
                + pharmacy.getId() + ", "
                + vehicle.getBattery().getIdBattery() + ", "
                + vehicle_type + ", "
                + vehicle.getQrCode() + ", "
                + vehicle.getWeight() + ", '"
                + vehicle.getIsAvailable() + "')";
    }

    public static String insertBatteryQuery(Battery battery) {
        if (battery == null) {
            return "";
        }
        return "INSERT INTO battery (ID_BATTERY, CAPACITY) VALUES("
                + battery.getIdBattery() + ", "
                + battery.getCapacity() + ")";
    }

    public static String insertVehicleStatus(Vehicle vehicle, int id_vehicle_status, int id_vehicle_type_status, int actual_charge) {
        if (vehicle == null) {
            return "";
        }
        return "INSERT INTO VEHICLE_STATUS (ID_VEHICLE_STATUS, FK_VEHICLE_ID, FK_VEHICLE_TYPE_STATUS_ID, DATE_ENTRY, ACTUALCHARGE) VALUES("
                + id_vehicle_status + ", "
                + vehicle.getId() + ", "
                + id_vehicle_type_status + ", "
                + "to_date(sysdate,'YYYY-MM-DD HH24:MI:SS'), "
                + actual_charge + ")";
    }

    public static String updateBatteryQuery(Battery battery) {
        if (battery == null) {
            return "";
        }
        return "UPDATE BATTERY SET CAPACITY = " + battery.getCapacity() + " "
                + "WHERE ID_BATTERY = " + battery.getIdBattery();
    }

    public static String insertClientQuery(Client client) {
        if (client == null) {
            return "";
        }
        return "INSERT INTO client (FK_PERSON_NIF, FK_RESIDENTIAL_ADDRESS_ID) VALUES("
                + client.getNIF() + ", "
                + client.getAddress().getId()
                + ")";
    }

    public static String insertCreditCardQuery(Client client) {
        if (client == null) {
            return "";
        }
        return "INSERT INTO Credit_Card (numero, CCV, Valid_Thru, FK_Person_NIF) VALUES ('"
                + client.getCreditCard().getNumber() + "', "
                + client.getCreditCard().getCcv() + ", "
                + "to_date('" + client.getCreditCard().getValidThru() + "', 'YYYY-MM-DD')" + ", '" //testar na db
                + client.getNIF() + "')";
    }

    public static String insertPharmacyProductQuery(Pharmacy pharmacy, Map.Entry<Product, Integer> entry, int id_transaction) {
        if (entry == null || pharmacy == null) {
            return "";
        }

        return "INSERT INTO PHARMACY_PRODUCT (ID_TRANSACTION, FK_PHARMACY_ID, FK_PRODUCT_ID, QUANTITY, DATE_ENTRY) VALUES("
                + id_transaction + ", "
                + pharmacy.getId() + ", "
                + entry.getKey().getId() + ", "
                + entry.getValue() + ", "
                + "sysdate)";
    }

    public static String updateProductQuery(Pharmacy pharmacy, Product product) {
        if (pharmacy == null || product == null) {
            return "";
        }
        return "UPDATE PRODUCT SET DESCRIPTION = '" + product.getDescription() + "', "
                + "PRICE = " + product.getPrice() + ", "
                + "WEIGHT = " + product.getWeight() + " "
                + "WHERE ID_PRODUCT =" + product.getId();
    }

    public static String insertProductQuery(Product product) {
        if (product == null) {
            return "";
        }
        return "INSERT INTO PRODUCT (ID_PRODUCT, DESCRIPTION, PRICE, WEIGHT ) VALUES("
                + product.getId() + ", '"
                + product.getDescription() + "', "
                + product.getPrice() + ", "
                + product.getWeight() + ")";
    }

    public static String insertOrderEntry(Pharmacy pharmacy, Client client, Order order) {

        return "INSERT INTO ORDER_ENTRY (ID_ORDER, DATE_ENTRY, "
                + "FK_PHARMACY_ID, FK_CLIENT_NIF,FK_SHIPMENT_ADDRESS_ID, "
                + "DELIVERY_FEE, FK_CREDIT_CARD_NUMERO) VALUES ("
                + order.getIdOrder() + ", "
                + "sysdate" + ", "
                + pharmacy.getId() + ", "
                + client.getNIF() + ", "
                + client.getAddress().getId() + ", "
                + DELIVERY_FEE + ", '"
                + client.getCreditCard().getNumber() + "')";
    }

    public static String insertOrderStatus(int id_order, int status) {

        return "INSERT INTO ORDER_STATUS (FK_ORDER_ID, FK_STATUS_ID, DATE_ENTRY) VALUES ("
                + id_order + ", "
                + status + ", "
                + "sysdate" + ") ";
    }

    public static String insertOrderDetail(Order order, Product product, Integer quantity) {

        return "INSERT INTO ORDER_DETAIL (FK_ORDER_ID, FK_PRODUCT_ID, QUANTITY, FK_ID_VAT) VALUES ("
                + order.getIdOrder() + ", "
                + product.getId() + ", "
                + quantity + ", "
                + 23 + ") ";

    }

    public static String insertDeliveryRun(DeliveryRun deliveryRun, double energycost) {

        return "INSERT INTO DELIVERY (ID_DELIVERY, ENERGYCOST, DATE_ENTRY) VALUES ("
                + deliveryRun.getId() + ", "
                + energycost + ", "
                + "sysdate)";
    }

    public static String insertDeliveryStop(DeliveryRun deliveryRun, Deliverable deliverable, Courier courier, Vehicle vehicle) {

        if(deliverable instanceof Transfer){
            return "INSERT INTO DELIVERY_STOPS (FK_DELIVERY_ID, FK_ORDER_ID, FK_COURIER_NIF, FK_VEHICLE_ID) VALUES ("
                    + deliveryRun.getId() + ", NULL , "
                    + courier.getNIF() + ", "
                    + vehicle.getId() + ")";
        }else{
            return "INSERT INTO DELIVERY_STOPS (FK_DELIVERY_ID, FK_ORDER_ID, FK_COURIER_NIF, FK_VEHICLE_ID) VALUES ("
                    + deliveryRun.getId() + ", "
                    + deliverable.getIdOrder() + ", "
                    + courier.getNIF() + ", "
                    + vehicle.getId() + ")";
        }

    }

    public static String insertInvoice(Order order, String invoiceTag, double totalCost) {
        return "INSERT INTO INVOICE (FK_ORDER_ID, INVOICE_TAG, TOTAL_COST) VALUES ("
                + order.getIdOrder() + ", '"
                + invoiceTag + "', "
                + totalCost + ")";
    }

    public static String insertPayment(Order order) {
        return "INSERT INTO PAYMENT (FK_ORDER_ID, DATE_ENTRY) VALUES ("
                + order.getIdOrder() + ", "
                + "to_date(sysdate, 'YYYY-MM-DD HH24:MI:SS'))";
    }

    public static String insertCredits(Order order, Client c) {
        return "INSERT INTO CREDITS (FK_ORDER_ID, FK_CLIENT_NIF, EARNEDCREDITS, DATE_ENTRY) VALUES ("
                + order.getIdOrder() + ", "
                + c.getNIF() + ", "
                + order.getCredits() + ", "
                + "to_date(sysdate, 'YYYY-MM-DD HH24:MI:SS'))";
    }

    public static String insertRoadRestrictionQuery(Road road) {
        if (road == null) {
            return "";
        }

        return "INSERT INTO Road_Restriction (ID_Road_Restriction, FK_ADDRESS_ID_START, FK_ADDRESS_ID_END) VALUES("
                + road.getId() + ","
                + road.getAddressOrig().getId() + ","
                + road.getAddressDest().getId() + ")";
    }

    public static String insertAirRestrictionQuery(Road road) {
        if (road == null) {
            return "";
        }

        return "INSERT INTO AERIAL_RESTRICTION (id_Aerial_Restriction, FK_ADDRESS_ORIGIN, FK_ADDRESS_END) VALUES("
                + road.getId() + ", "
                + road.getAddressOrig().getId() + ", "
                + road.getAddressDest().getId() + ")";
    }
    
    public static class tablePharmacy {

        public static final String id_pharmacy = "ID_PHARMACY";

    }

    public boolean runScriptOnDatabase(String fileString, boolean dropFirst) throws FileNotFoundException, SQLException {

        Scanner s = new Scanner(new FileReader(fileString));
        s.useDelimiter("(;(\r)?\n)|(--\n)");

        Connection conn = null;
        Statement st = null;

        try {
            conn = getConnection();
            st = conn.createStatement();
            if (dropFirst) {
                st.execute(dropProcedure());
            }
            while (s.hasNext()) {
                String line = s.next();
                if (line.startsWith("/*!") && line.endsWith("*/")) {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " */".length());
                }

                if (line.trim().length() > 0) {
                    st.execute(line);
                }
            }
            //st.execute(triggerInsertCourier());
            return true;
        } catch (SQLException ex) {
            throw new SQLException("Error while running script: " + ex.getMessage());
        } finally {
            if (st != null) {
                st.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }
    }

    private String dropProcedure() {
        return "begin \n"
                + "for r in (select 'drop table ' || table_name || ' cascade constraints' cmd from user_tables order by table_name) \n"
                + "loop \n"
                + "execute immediate(r.cmd); \n"
                + "end loop; \n"
                + "end; ";
    }

//    private String triggerInsertCourier() {
//
//        return "CREATE OR REPLACE TRIGGER trginsertCourierTypeStatus "
//                + "AFTER "
//                + "INSERT "
//                + "ON courier "
//                + "FOR EACH ROW "
//                + "DECLARE "
//                + "status_id COURIER_STATUS.FK_COURIER_TYPE_STATUS_ID%TYPE; "
//                + "BEGIN "
//                + "SELECT ID_COURIER_TYPE_STATUS INTO status_id "
//                + "FROM COURIER_TYPE_STATUS "
//                + "WHERE description='Free'; "
//                + "INSERT INTO COURIER_STATUS (FK_PERSON_NIF, FK_COURIER_TYPE_STATUS_ID,Date_Entry) VALUES (:new.FK_PERSON_NIF, status_id, to_date(sysdate, 'YYYY-MM-DD HH24:MI:SS')); "
//                + "END; ";
//    }
    
}
