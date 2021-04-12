package lapr.project.data;

import static java.lang.Boolean.parseBoolean;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import lapr.project.model.Courier;
import lapr.project.model.Person;
import lapr.project.model.Pharmacy;
import lapr.project.model.User;
import static lapr.project.utils.Constants.ROLE_COURIER;

/**
 *
 * @author catarinaserrano
 */
public class CourierDB extends DataHandler {

    public Iterable<Courier> getWorkingCouriersByPharmacyId(int pharmacyId) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from courier "
                    + "inner join person on person.nif = courier.fk_person_nif "
                    + "inner join user_entry on person.email=user_entry.email "
                    + "where courier.fk_pharmacy_id = " + pharmacyId
                    + " AND courier.isWorking = 'true'");

            Courier courier = null;

            List<Courier> tmp = new LinkedList<Courier>();
            while (rs.next()) {
                courier = new Courier(rs.getDouble("weight"), parseBoolean(rs.getString("isWorking")));
                courier.setNIF(rs.getInt("nif"));
                courier.setName(rs.getString("name"));
                courier.setEmail(rs.getString("email"));
                //courier= new Courier(rs.getDouble("weight"), parseBoolean(rs.getString("isWorking")), rs.getInt("nif"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
                tmp.add(courier);
            }
            return tmp;
            //callStmt = getConnection().prepareCall("select userrole.description, user.email, user.password from user join userrole on userrole.id_userrole=user.fk_userrole_id");
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

    public Courier getWorkingCouriersById(int courierNIF) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from courier "
                    + "inner join person on person.nif = courier.fk_person_nif "
                    + "inner join user_entry on person.email=user_entry.email "
                    + "where courier.FK_PERSON_NIF='" + courierNIF + "' AND courier.isworking = 'true'");

            Courier courier = null;
            while (rs.next()) {
                courier = new Courier(rs.getDouble("weight"), parseBoolean(rs.getString("isWorking")));
                courier.setNIF(rs.getInt("nif"));
                courier.setName(rs.getString("name"));
                courier.setEmail(rs.getString("email"));
                //courier= new Courier(rs.getDouble("weight"), parseBoolean(rs.getString("isWorking")), rs.getInt("nif"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
            }
            return courier;
            //callStmt = getConnection().prepareCall("select userrole.description, user.email, user.password from user join userrole on userrole.id_userrole=user.fk_userrole_id");
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
     * Insert courier into the DB, by inserting first in the the user and person
     * tables
     *
     * @param courier
     * @param pharmacy
     * @return boolean
     * @throws SQLException
     */
    public boolean insertCourier(Courier courier, Pharmacy pharmacy) throws SQLException {

        boolean success = false;

        UserDB userDB = new UserDB();
        PersonDB personDB = new PersonDB();
        UserRoleDB userRoleDB = new UserRoleDB();

        User user = new User(courier.getEmail(), courier.getPassword());
        user.setRole(userRoleDB.getUserRoleByDescription(ROLE_COURIER));
        if (!userDB.insertUser(user)) {
            return false;
        }

        Person person = new Person(courier.getNIF(), courier.getName());
        person.setEmail(courier.getEmail());
        person.setPassword(courier.getPassword());
        if (!personDB.insertPerson(person)) {
            return false;
        }

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("INSERT INTO Courier (FK_PERSON_NIF, FK_PHARMACY_ID, ISWORKING, WEIGHT) VALUES("
                    + courier.getNIF() + ", "
                    + pharmacy.getId() + ", '"
                    + courier.getIsWorking() + "', "
                    + courier.getWeight() + ")");

            rs = stmt.executeQuery("select max(id_courier_status) id from courier_status");
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("id");
            }
            id = id + 1;

            rs = stmt.executeQuery("INSERT INTO Courier_Status (ID_COURIER_STATUS, FK_PERSON_NIF, FK_COURIER_TYPE_STATUS_ID, DATE_ENTRY) VALUES("
                    + id + ", "
                    + courier.getNIF() + ", "
                    + 1 + ", "
                    + "to_date(sysdate, 'YYYY-MM-DD HH24:MI:SS'))");

            rs.close();
            success = true;
            return success;
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

    public boolean updateCourier(Courier courier, Pharmacy pharmacy) throws SQLException {
        boolean success = false;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("UPDATE Courier SET "
                    + "WEIGHT = " + courier.getWeight() + ", "
                    + "ISWORKING = '" + courier.getIsWorking() + "' "
                    + "WHERE FK_PERSON_NIF = " + courier.getNIF() + " AND FK_PHARMACY_ID = " + pharmacy.getId());
            rs.close();
            success = true;
            return success;
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

    public boolean insertCourierStatus(Courier courier, int status) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            int affectedRows = stmt.executeUpdate("INSERT INTO COURIER_STATUS(id_courier_status, fk_person_nif, fk_courier_type_status_id, date_entry) "
                    + "VALUES((select max(ID_COURIER_STATUS)+1 from COURIER_STATUS), " + courier.getNIF() + ", " + status + " , sysdate) ");

            return affectedRows == 1;
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

    public boolean markCourierAsBusy(Courier courier) throws SQLException {
        int busyStatus = 2;
        return insertCourierStatus(courier, busyStatus);
    }
    public boolean markCourierAsFree(Courier courier) throws SQLException {
        int busyStatus = 1;
        return insertCourierStatus(courier, busyStatus);
    }

    public Courier getWeightAverageOfCourier(Pharmacy pharmacy) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select avg(weight) media from courier where fk_pharmacy_id =" + pharmacy.getId());

            float avg = 75;
            if (rs.next()) {
                avg = (float) rs.getDouble("media");
            }
            return new Courier(avg, true);
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

    public Courier getCourierByUserEmail(String userEmail) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from courier join person on person.nif = courier.fk_person_nif "
                    + "where person.email = '"+ userEmail +"'");

            rs.next();
            Courier courier = new Courier(rs.getDouble("weight"), rs.getBoolean("isworking"));
            courier.setEmail(rs.getString("email"));
            courier.setName(rs.getString("name"));
            courier.setNIF(rs.getInt("NIF"));

            return courier;
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

    /**
     * Fetches from the database the email of the courier who last used the
     * vehicle with the vehicleId passed as parameter
     *
     * @param vehicleId id of the vehicle
     * @return String containing the courier email
     */
    public String getCourierEmailByVehicleId(int vehicleId) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select PERSON.EMAIL\n" +
                    "from PERSON\n" +
                    "WHERE NIF in (\n" +
                    "    SELECT DS.FK_COURIER_NIF\n" +
                    "    FROM DELIVERY\n" +
                    "    JOIN DELIVERY_STOPS DS on DELIVERY.ID_DELIVERY = DS.FK_DELIVERY_ID\n" +
                    "    WHERE DS.FK_VEHICLE_ID = "+vehicleId+"\n" +
                    "    AND DATE_ENTRY = (\n" +
                    "        SELECT MAX(DATE_ENTRY)\n" +
                    "        FROM DELIVERY\n" +
                    "        JOIN DELIVERY_STOPS on DELIVERY.ID_DELIVERY = DELIVERY_STOPS.FK_DELIVERY_ID\n" +
                    "        WHERE DELIVERY_STOPS.FK_VEHICLE_ID = "+vehicleId+")\n" +
                    ")");


            rs.next();
            String courierEmail = rs.getString(1);

            return courierEmail;

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
