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

import lapr.project.model.*;
import lapr.project.utils.Constants;

/**
 *
 * @author Asus
 */
public class ClientDB extends DataHandler {

    /**
     * Insert client into the DB, by inserting first in the the user and person
     * tables
     *
     * @param client
     * @param client
     * @return boolean
     * @throws SQLException
     */
    public boolean insertClient(Client client) throws SQLException {
        boolean success = false;

        UserDB userDB = new UserDB();
        PersonDB personDB = new PersonDB();
        UserRoleDB userRoleDB = new UserRoleDB();

        User user = new User(client.getEmail(), client.getPassword());
        user.setRole(userRoleDB.getUserRoleByDescription(Constants.ROLE_CLIENT));

        if (!userDB.insertUser(user)) {
            return false;
        }

        Person person = new Person(client.getNIF(), client.getName());
        person.setEmail(client.getEmail());
        person.setPassword(client.getPassword());
        if (!personDB.insertPerson(person)) {
            return false;
        }

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(CommonDB.insertClientQuery(client));

            rs = stmt.executeQuery(CommonDB.insertCreditCardQuery(client));
            rs.close();
            success = true;
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

        return success;
    }

    public Client getClientByUserSession(User user) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select address.id_address, person.nif, person.name, person.email, address.latitude, address.longitude, address.address, address.zip_code, address.elevation,  "
                    + "credit_card.numero, credit_card.ccv, credit_card.valid_thru from person join client "
                    + "on client.fk_person_nif = person.nif "
                    + "join address on "
                    + "address.id_address = client.fk_residential_address_id "
                    + "join credit_card on "
                    + "credit_card.fk_person_nif = client.fk_person_nif "
                    + "where person.email = '" + user.getEmail() + "'");

            Client client = null;
            CreditCard credit = null;
            Address address = null;
            rs.next();
            address = new Address(rs.getInt("id_address"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("address"), rs.getString("zip_code"), rs.getFloat("elevation"));
            credit = new CreditCard(rs.getString("numero"), rs.getInt("ccv"), rs.getDate("valid_thru").toString());
            client = new Client(address, credit);
            client.setNIF(rs.getInt("nif"));
            client.setName(rs.getString("name"));
            client.setEmail(rs.getString("email"));

            //Credits
            rs = stmt.executeQuery("select sum(earnedcredits) total from credits where fk_client_nif = " + client.getNIF());
            int credits = 0;
            if (rs.next()) {
                credits = rs.getInt("total");
            }
            client.setCredits(credits);

            return client;
        } catch (SQLException e) {
            throw new SQLException("Error at getClientByUserSession" + e.getMessage());
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

    public Client getClientByOrderId(int orderId) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select FK_CLIENT_NIF from ORDER_ENTRY where ORDER_ENTRY.ID_ORDER = " + orderId);

            rs.next();
            Client client = getClientByNif(rs.getInt("fk_client_nif"));

            return client;

        } catch (SQLException e) {
            throw new SQLException("Error at getClientByOrderId " + e.getMessage());
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

    public Client getClientByNif(int nif) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Client client;
        CreditCard credit;
        Address address;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select address.id_address, person.nif, person.name, person.email, address.latitude, address.longitude, address.address, address.zip_code, address.elevation,  "
                    + "credit_card.numero, credit_card.ccv, credit_card.valid_thru from person join client "
                    + "on client.fk_person_nif = person.nif "
                    + "join address on "
                    + "address.id_address = client.fk_residential_address_id "
                    + "join credit_card on "
                    + "credit_card.fk_person_nif = client.fk_person_nif "
                    + "where person.NIF = " + nif);

            rs.next();
            address = new Address(rs.getInt("id_address"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("address"), rs.getString("zip_code"), rs.getFloat("elevation"));
            credit = new CreditCard(rs.getString("numero"), rs.getInt("ccv"), rs.getDate("valid_thru").toString());
            client = new Client(address, credit);
            client.setNIF(rs.getInt("nif"));
            client.setName(rs.getString("name"));
            client.setEmail(rs.getString("email"));
            client.setPassword("password");

            //Credits
            rs = stmt.executeQuery("select sum(earnedcredits) total from credits where fk_client_nif = " + client.getNIF());
            int credits = 0;
            if (rs.next()) {
                credits = rs.getInt("total");
            }
            client.setCredits(credits);

            return client;
        } catch (SQLException e) {
            throw new SQLException("Error at getClientByNif" + e.getMessage());
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
