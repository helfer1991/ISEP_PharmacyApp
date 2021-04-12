package lapr.project.service;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import lapr.project.data.VehicleDB;
import lapr.project.model.*;

import static lapr.project.utils.Constants.ROLE_ADMINISTRATOR;

/**
 *
 * @author Asus
 */
public class ServiceVehicle {
    VehicleDB vehicleDB;

    
    public ServiceVehicle() {
        this.vehicleDB = new VehicleDB();
    }


    public Iterable<Vehicle> getAvailableVehiclesByPharmacy(Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null ||PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole() == null || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)) {
            return null;
        }
        return vehicleDB.getAvailableVehiclesByPharmacy(pharmacy);
    }
    
    public Iterable<Scooter> getScootersByPharmacy(Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null ||PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole() == null || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)) {
            return null;
        }
        return vehicleDB.getScootersByPharmacy(pharmacy.getId());
    }
    
    public Iterable<Drone> getDronesFromPharmacy(Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null ||PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole() == null || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)) {
            return null;
        }
        return vehicleDB.getDronesByPharmacy(pharmacy.getId());
    }
    
    public Vehicle insertVehicle(Vehicle vehicle, Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null ||PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole() == null || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)) {
            return null;
        }
        return vehicleDB.insertVehicle(vehicle, pharmacy);
    }

    
    public Vehicle updateVehicle(Vehicle vehicle, Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null ||PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole() == null || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)) {
            return null;
        }
        return vehicleDB.updateVehicle(vehicle, pharmacy);
    }

    public boolean removeVehicle(Vehicle vehicle, Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null ||PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole() == null || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)) {
            return false;
        }
        vehicle.setIsAvailable("false");
        return vehicleDB.removeVehicle(vehicle, pharmacy);
    }

    public float getScooterMaxBatteryCapacity() throws SQLException {
        return vehicleDB.getScooterMaxBatteryCapacity();
    }

    public float getDroneMaxBatteryCapacity() throws SQLException {
        return vehicleDB.getDroneMaxBatteryCapacity();
    }

    /**
     * Simulates the charging of the vehicle that has the vehicleId that's in the ParkSensorFile passed as parameter
     *
     * @param sensorFile File containing the information of the vehicle (received from the parking system)
     * @return boolean if setup correctly
     *          false if some error occurred
     */
    public boolean simulateVehicleCharging(ParkSensorFile sensorFile){
         //time to full charge / percentage left to charge = time it takes to charge 1%
        //multiply by 60.000 to convert to milliseconds
        long onePercentInterval = (long)( 60 * 1000 * sensorFile.getEstimatedTimeToFullCharge() / ( 100- sensorFile.getVehicleBatteryPercentage() ) );
        return setChargingTimer(sensorFile, onePercentInterval);
    }

    /**
     * Initializes and starts the timer and timerTask that simulate scooterCharging
     * @param sensorFile file with the data necessary to
     * @param updateInterval time it takes to increase the battery level by 1%
     * @return boolean true if setup was successful
     *                  false if the setup was interrupted
     */
    public boolean setChargingTimer(ParkSensorFile sensorFile, long updateInterval){

        Timer timer = new Timer();
        //this stores the value between database writes
        final int[] tempBatteryPercentage = {sensorFile.getVehicleBatteryPercentage()};


        TimerTask increaseBattery = new TimerTask() {
            @Override
            public void run() {
                if(sensorFile.getVehicleBatteryPercentage() >= 100){
                    timer.cancel();
                }else{

                    try{
                        tempBatteryPercentage[0]++;
                        sensorFile.setVehicleBatteryPercentage(tempBatteryPercentage[0]);
                        boolean updated = vehicleDB.updateScooterStatus(sensorFile, tempBatteryPercentage[0]);//------------> this can be set to run only every 1% or 5% or 10% increase (easier to demonstrate)
                        if (!updated){throw new IllegalStateException();}                                       //------------> or update only when its at 100% (more realistic and doesn't pollute the database)

                    }catch(Exception e){
                        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINE, e.getMessage(), e);
                    }
                }
            }
        };

        timer.schedule(increaseBattery,0, updateInterval);
        return true;
    }
    
    public void setVehicleDB(VehicleDB vehicleDB) {
        this.vehicleDB = vehicleDB;
    }
    
    public double getScooterWeight () throws SQLException {
        return vehicleDB.getScootersWeight();
    }
    
    public double getDroneWeight () throws SQLException {
        return vehicleDB.getDronessWeight();
    }

}
