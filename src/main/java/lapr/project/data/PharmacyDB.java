package lapr.project.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import lapr.project.model.Address;
import lapr.project.model.Pharmacy;
import lapr.project.model.Park;
import lapr.project.model.User;

public class PharmacyDB extends DataHandler {

    public Iterable<Pharmacy> getPharmaciesByAdministrator(User administrator) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from pharmacy "
                    + "join user_entry on user_entry.email = pharmacy.fk_administrator_email "
                    + "join address on address.id_address = pharmacy.fk_address_id "
                    + "join park on park.fk_pharmacy_id = pharmacy.id_pharmacy "
                    + "where user_entry.email='" + administrator.getEmail() + "' "
                    + "and pharmacy.isactive = 'true' "
                    + "order by pharmacy.name");

            Pharmacy pharmacy = null;
            Park park = null;
            Address address = null;
            List<Pharmacy> tmp = new LinkedList<Pharmacy>();
            while (rs.next()) {
                address = new Address(rs.getInt("fk_address_id"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("address"), rs.getString("zip_code"), rs.getFloat("elevation"));
                park = new Park(rs.getInt(CommonDB.tablePharmacy.id_pharmacy), rs.getInt("scooter_chargers_number"), rs.getInt("drone_chargers_number"), rs.getFloat("scooter_charger_capacity"), rs.getFloat("drone_charger_capacity"));
                pharmacy = new Pharmacy(rs.getString("name"), rs.getInt(CommonDB.tablePharmacy.id_pharmacy), park, address, rs.getFloat("maximum_payload"), rs.getFloat("minimum_payload"));
                tmp.add(pharmacy);
            }
            return tmp;
        } catch (SQLException e) {
            throw new SQLException("Error at getPharmaciesByAdministrator" + e.getMessage());
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

    public Pharmacy getPharmacyByCourier(User courier) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from pharmacy "
                    + "join address on address.id_address = pharmacy.fk_address_id "
                    + "join courier on pharmacy.id_pharmacy = courier.fk_pharmacy_id "
                    + "join park on park.fk_pharmacy_id = pharmacy.id_pharmacy "
                    + "join person on courier.fk_person_nif = person.nif "
                    + "where person.email='" + courier.getEmail() + "' ");

            Pharmacy pharmacy = null;
            Park park = null;
            Address address = null;
            rs.next();
            address = new Address(rs.getInt("fk_address_id"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("address"), rs.getString("zip_code"), rs.getFloat("elevation"));
            park = new Park(rs.getInt(CommonDB.tablePharmacy.id_pharmacy), rs.getInt("scooter_chargers_number"), rs.getInt("drone_chargers_number"), rs.getFloat("scooter_charger_capacity"), rs.getFloat("drone_charger_capacity"));
            pharmacy = new Pharmacy(rs.getString("name"), rs.getInt(CommonDB.tablePharmacy.id_pharmacy), park, address, rs.getFloat("maximum_payload"), rs.getFloat("minimum_payload"));

            return pharmacy;
        } catch (SQLException e) {
            throw new SQLException("Error at getPharmacyByCourier " + e.getMessage());
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

    public Pharmacy getPharmacyById(int pharmacyId) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from pharmacy "
                    + "join address on address.id_address = pharmacy.fk_address_id "
                    + "join park on park.fk_pharmacy_id = pharmacy.id_pharmacy "
                    + "where ID_PHARMACY = " + pharmacyId);

            Pharmacy pharmacy = null;
            Park park = null;
            Address address = null;
            rs.next();
            address = new Address(rs.getInt("fk_address_id"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("address"), rs.getString("zip_code"), rs.getFloat("elevation"));
            park = new Park(rs.getInt(CommonDB.tablePharmacy.id_pharmacy), rs.getInt("scooter_chargers_number"), rs.getInt("drone_chargers_number"), rs.getFloat("scooter_charger_capacity"), rs.getFloat("drone_charger_capacity"));
            pharmacy = new Pharmacy(rs.getString("name"), rs.getInt(CommonDB.tablePharmacy.id_pharmacy), park, address, rs.getFloat("maximum_payload"), rs.getFloat("minimum_payload"));

            return pharmacy;
        } catch (SQLException e) {
            throw new SQLException("Error at getPharmacyById " + e.getMessage());
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

    public Iterable<Pharmacy> getAllPharmacies() throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from pharmacy "
                    + "join address on address.id_address = pharmacy.fk_address_id "
                    + "join park on park.fk_pharmacy_id = pharmacy.id_pharmacy "
                    + "where pharmacy.isActive = 'true' "
                    + "order by pharmacy.name");
            Pharmacy pharmacy = null;
            Park park = null;
            Address address = null;
            List<Pharmacy> tmp = new LinkedList<Pharmacy>();
            while (rs.next()) {
                address = new Address(rs.getInt("fk_address_id"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("address"), rs.getString("zip_code"), rs.getFloat("elevation"));
                park = new Park(rs.getInt(CommonDB.tablePharmacy.id_pharmacy), rs.getInt("scooter_chargers_number"), rs.getInt("drone_chargers_number"), rs.getFloat("scooter_charger_capacity"), rs.getFloat("drone_charger_capacity"));
                pharmacy = new Pharmacy(rs.getString("name"), rs.getInt(CommonDB.tablePharmacy.id_pharmacy), park, address, rs.getFloat("maximum_payload"), rs.getFloat("minimum_payload"));
                tmp.add(pharmacy);
            }
            rs.close();
            return tmp;
        } catch (SQLException e) {
            throw new SQLException("Error at getAllPharmacys" + e.getMessage());
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

    public Pharmacy insertPharmacy(Pharmacy pharmacy, User administrator) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            //id pharmacy and park
            rs = stmt.executeQuery("select max(id_pharmacy) id from pharmacy");
            int id = 1;
            if (rs.next()) {
                id = rs.getInt("id");
            }
            pharmacy.setId(id + 1);
            pharmacy.getPark().setId(id + 1);

            //result 
            rs = stmt.executeQuery(CommonDB.insertPharmacyQuery(pharmacy, administrator));
            rs = stmt.executeQuery(CommonDB.insertParkQuery(pharmacy.getPark()));

            return pharmacy;
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

    public Pharmacy updatePharmacy(Pharmacy pharmacy) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(CommonDB.updatePharmacyQuery(pharmacy));
            rs = stmt.executeQuery(CommonDB.updateParkQuery(pharmacy.getPark()));
            rs.close();
            return pharmacy;
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

    public boolean removePharmacy(Pharmacy pharmacy) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            
            stmt = conn.createStatement();

            rs = stmt.executeQuery(CommonDB.removePharmacyQuery(pharmacy));
            rs.close();
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

}
