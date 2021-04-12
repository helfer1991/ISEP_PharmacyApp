package lapr.project.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import lapr.project.model.*;

/**
 *
 * @author Asus
 */
public class VehicleDB extends DataHandler {

    public Vehicle insertVehicle(Vehicle vehicle, Pharmacy pharmacy) throws SQLException {

        int vehicle_type = 0;
        if (vehicle instanceof Scooter) {
            vehicle_type = 1;
        } else if (vehicle instanceof Drone) {
            vehicle_type = 2;
        } else {
            throw new IllegalStateException("Drones and Scooters are the only vehicles supported for now.");
        }

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            //id vehicle and battery
            rs = stmt.executeQuery("select max(id_Vehicle) id from vehicle");
            int id = 1;
            if (rs.next()) {
                id = rs.getInt("id");
            }
            vehicle.setId(id + 1);
            vehicle.setQrCode(id + 1);

            //Verify if battery capacity exists
            rs = stmt.executeQuery("select id_battery from battery where capacity = " + vehicle.getBattery().getCapacity());
            int id_Battery = 0;
            if (rs.next()) {
                id_Battery = rs.getInt("id_battery");
            }
            if (id_Battery == 0) {
                rs = stmt.executeQuery("select max(id_battery) id from battery");
                if (rs.next()) {
                    id_Battery = rs.getInt("id");
                }
                vehicle.getBattery().setIdBattery(id_Battery + 1);
                stmt.executeQuery(CommonDB.insertBatteryQuery(vehicle.getBattery()));
            } else {
                vehicle.getBattery().setIdBattery(id_Battery);
            }

            //Insert vehicle status
            rs = stmt.executeQuery("select max(id_vehicle_status) id from vehicle_status");
            int id_vehicle_status = 0;
            if (rs.next()) {
                id_vehicle_status = rs.getInt("id");
            }
            id_vehicle_status = id_vehicle_status + 1;

            stmt.executeQuery(CommonDB.insertVehicleQuery(vehicle, vehicle_type, pharmacy));
            stmt.executeQuery(CommonDB.insertVehicleStatus(vehicle, id_vehicle_status, 1, 100));

            //return result;
            return vehicle;
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

    public Vehicle updateVehicle(Vehicle vehicle, Pharmacy pharmacy) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            //Verify if battery capacity exists
            rs = stmt.executeQuery("select id_battery from battery where capacity = " + vehicle.getBattery().getCapacity());
            int id_Battery = 0;
            if (rs.next()) {
                id_Battery = rs.getInt("id_battery");
            }
            if (id_Battery == 0) {
                rs = stmt.executeQuery("select max(id_battery) id from battery");
                if (rs.next()) {
                    id_Battery = rs.getInt("id");
                }
                vehicle.getBattery().setIdBattery(id_Battery + 1);
                stmt.executeQuery(CommonDB.insertBatteryQuery(vehicle.getBattery()));
            } else {
                vehicle.getBattery().setIdBattery(id_Battery);
            }

            rs = stmt.executeQuery("UPDATE VEHICLE SET "
                    + "WEIGHT = " + vehicle.getWeight() + ", "
                    + "ISAVAILABLE = '" + vehicle.getIsAvailable() + "', "
                    + "FK_BATTERY_ID =" + vehicle.getBattery().getIdBattery() + " "
                    + "WHERE ID_VEHICLE = " + vehicle.getId() + " AND FK_PHARMACY_ID = " + pharmacy.getId());

            rs.close();
            return vehicle;
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

    public boolean removeVehicle(Vehicle vehicle, Pharmacy pharmacy) throws SQLException {
        boolean success;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {

            conn = getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery("UPDATE Vehicle SET "
                    + "ISAVAILABLE = 'false' WHERE FK_PHARMACY_ID = " + pharmacy.getId() + " AND id_Vehicle = " + vehicle.getId());
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

    public Iterable<Vehicle> getAvailableVehiclesByPharmacy(Pharmacy pharmacy) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from vehicle inner join vehicle_status on vehicle.id_vehicle = vehicle_status.fk_vehicle_id "
                    + "join battery on battery.id_battery = vehicle.fk_battery_id "
                    + "where (vehicle_status.fk_vehicle_id, vehicle_status.date_entry, vehicle_status.fk_vehicle_type_status_id) in "
                    + "    (select vehicle_status.fk_vehicle_id, vehicle_status.date_entry, vehicle_status.fk_vehicle_type_status_id from vehicle_status "
                    + "    where (vehicle_status.fk_vehicle_id, vehicle_status.date_entry) in "
                    + "        (select vehicle_status.fk_vehicle_id, max(vehicle_status.date_entry) "
                    + "        from vehicle_status group by vehicle_status.fk_vehicle_id)) "
                    + "and vehicle.isavailable = 'true' and vehicle_status.fk_vehicle_type_status_id= 1 and vehicle.fk_pharmacy_id = "+pharmacy.getId() + " "
                    + "order by vehicle_status.date_entry asc");

            Scooter scooter;
            Drone drone;
            List<Vehicle> tmp = new LinkedList<>();
            while (rs.next()) {
                if (rs.getInt("fk_vehicle_type_id") == 1) {
                    scooter = new Scooter(rs.getInt("id_Vehicle"), rs.getInt("qrCode"), rs.getInt("weight"), new Battery(rs.getInt("fk_Battery_id"), rs.getFloat("capacity")), rs.getInt("actualcharge"));
                    tmp.add(scooter);
                } else {
                    drone = new Drone(rs.getInt("id_Vehicle"), rs.getInt("qrCode"), rs.getInt("weight"), new Battery(rs.getInt("fk_Battery_id"), rs.getFloat("capacity")), rs.getInt("actualcharge"));
                    tmp.add(drone);
                }
            }
            return tmp;
            //callStmt = getConnection().prepareCall("select userrole.description, user.email, user.password from user join userrole on userrole.id_userrole=user.fk_userrole_id");
        } catch (SQLException e) {
            throw  new SQLException(e.getMessage());
            
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

    private int getVehicleActualCharge(int vehicleId) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select ActualCharge "
                    + "from vehicle_status "
                    + "where fk_vehicle_id = " + vehicleId + " "
                    + "order by date_entry desc "
                    + "fetch first 1 row only "
            );
            rs.next();
            int actualCharge = rs.getInt("ACTUALCHARGE");

            rs.close();
            stmt.close();
            return actualCharge;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
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

    private boolean insertVehicleStatus(Vehicle vehicle, int status) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            int affectedRows = stmt.executeUpdate("INSERT INTO VEHICLE_STATUS(ID_VEHICLE_STATUS, FK_VEHICLE_ID, FK_VEHICLE_TYPE_STATUS_ID, DATE_ENTRY, ACTUALCHARGE) "
                    + "VALUES((select max(ID_VEHICLE_STATUS)+1 from VEHICLE_STATUS), " + vehicle.getId() + ", " + status + " , sysdate," + vehicle.getActualCharge() + ") ");

            return affectedRows == 1;
        } catch (SQLException e) {
            throw new SQLException("Error at markCourierAsBusy" + e.getMessage());
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

    public boolean markVehicleAsTransiting(Vehicle vehicle) throws SQLException {
        int statusTransiting = 5;
        return insertVehicleStatus(vehicle, statusTransiting);
    }

    public Iterable<Drone> getDronesByPharmacy(int pharmacyId) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from vehicle INNER JOIN battery ON vehicle.fk_battery_id = battery.id_battery where fk_pharmacy_id = '"
                    + pharmacyId + "' AND vehicle.fk_vehicle_type_id = '" + 2 + "'" + "AND vehicle.isavailable='true'");

            Drone drone;

            List<Drone> pharmacyDrones = new LinkedList<>();
            while (rs.next()) {

                int vehicleId = rs.getInt("id_Vehicle");
                int actualCharge = getVehicleActualCharge(vehicleId);

                drone = new Drone(vehicleId, rs.getInt("qrCode"), rs.getInt("weight"), new Battery(rs.getInt("id_Battery"), rs.getFloat("capacity")), actualCharge);

                pharmacyDrones.add(drone);
            }
            rs.close();
            stmt.close();
            return pharmacyDrones;
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

    public Iterable<Scooter> getScootersByPharmacy(int pharmacyId) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from vehicle INNER JOIN battery ON vehicle.fk_battery_id = battery.id_battery where fk_pharmacy_id = '"
                    + pharmacyId + "' AND vehicle.fk_vehicle_type_id = '" + 1 + "'" + "AND vehicle.isavailable='true'");

            Scooter scooter;

            List<Scooter> pharmacyScooters = new LinkedList<>();
            while (rs.next()) {

                int vehicleId = rs.getInt("id_Vehicle");
                int actualCharge = getVehicleActualCharge(vehicleId);

                scooter = new Scooter(vehicleId, rs.getInt("qrCode"), rs.getInt("weight"), new Battery(rs.getInt("id_Battery"), rs.getFloat("capacity")), actualCharge);

                pharmacyScooters.add(scooter);
            }
            rs.close();
            stmt.close();
            return pharmacyScooters;
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
     * Inserts new ScooterStatus in the ScooterStatus table of the DB according
     * the information received in the ParSensorFile. -Battery percentage level
     * should be passed separately from ParkSensorfile to allow method reuse.-
     *
     * @param sensorFile Object with the information read from the file produced
     * by the ParkingSystem
     * @param batteryPercentage remaining scooter battery level
     * (sensorFile.ScooterBatteryPercentage will be ignored)
     * @return true if inserted/updated correclty false if not updated correclty
     */
    public boolean updateScooterStatus(ParkSensorFile sensorFile, int batteryPercentage) throws SQLException {

        Connection conn = null;
        Statement Stmt = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            Stmt = conn.createStatement();

            rs = Stmt.executeQuery("select FK_VEHICLE_TYPE_ID from VEHICLE where VEHICLE.ID_VEHICLE = "+ sensorFile.getVehicleId());
            rs.next();
            int vehicleTypeId = rs.getInt("fk_vehicle_type_id");

            prepStmt = conn.prepareStatement("INSERT INTO VEHICLE_STATUS VALUES( (select max(id_Vehicle_status)+1 from Vehicle_STATUS) ,?,?,to_date(?, 'yyyy_mm_dd_hh24_mi_ss'),?)");

            prepStmt.setInt(1, sensorFile.getVehicleId());
            prepStmt.setInt(2, vehicleTypeId);
            prepStmt.setString(3, sensorFile.getTimeStamp());
            prepStmt.setInt(4, batteryPercentage );

            int rowsAffected = prepStmt.executeUpdate();


            return (rowsAffected == 1);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            if (Stmt != null) {
                Stmt.close();
            }
            if (rs != null) {
                rs.close();
            }

            }

    }

    public double getScootersWeight() throws SQLException {
        int vehicleIdScooter = 1;
        return getWeightByVehicleId(vehicleIdScooter);
    }

    public double getDronessWeight() throws SQLException {
        int vehicleIdDrone = 2;
        return getWeightByVehicleId(vehicleIdDrone);
    }

    public double getWeightByVehicleId(int vehicleId) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select max(WEIGHT) "
                    + "from VEHICLE "
                    + "WHERE FK_VEHICLE_TYPE_ID = " + vehicleId);
            double weight = 0;
            if (rs.next()) {
                weight = rs.getDouble(1);
            }

            return weight;

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

    public float getScooterMaxBatteryCapacity() throws SQLException {
        int vehicleIdDrone = 1;
        return getMaxBatteryCapacityByVehicleId(vehicleIdDrone);
    }

    public float getDroneMaxBatteryCapacity() throws SQLException {
        int vehicleIdDrone = 2;
        return getMaxBatteryCapacityByVehicleId(vehicleIdDrone);
    }

    public float getMaxBatteryCapacityByVehicleId(int vehicleId) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select max(CAPACITY) "
                    + "from BATTERY "
                    + "join VEHICLE on VEHICLE.FK_BATTERY_ID = BATTERY.ID_BATTERY "
                    + "WHERE FK_VEHICLE_TYPE_ID = " + vehicleId);
            float cap = 0;
            if (rs.next()) {
                cap = rs.getFloat(1);
            }

            return cap;

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
