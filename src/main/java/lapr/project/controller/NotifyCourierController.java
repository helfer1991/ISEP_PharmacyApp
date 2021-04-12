package lapr.project.controller;

import lapr.project.data.CourierDB;
import lapr.project.data.VehicleDB;
import lapr.project.model.*;
import lapr.project.service.ServiceDeliveryRun;
import lapr.project.service.ServiceParkSystemFiles;
import lapr.project.service.ServiceVehicle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.*;


/**
 * Manages and delegates methods related to the US "NANB9-140-Notify the courier when the scooter is correctly docked"
 */
public class NotifyCourierController {

    /**
     * Logger used throughout the class
     */
    private final Logger logger = Logger.getGlobal();
    /**
     * Thread that runs an infinite loop looking for files inside the specified folder
     */
    private final Thread watchFolderThread;
    /**
     * Object containing the information transmitted from the parking System through .data file
     */
    private ParkSensorFile sensorFile;
    /**
     * Instance of class used to manage serviceParkFiles
     */
    private ServiceParkSystemFiles serviceParkFiles;
    /**
     * Class used to get/enter Scooter data from the Database
     */
    private VehicleDB vehicleDB;
    /**
     * Class used to get/enter Courier data from the Database
     */
    private CourierDB courierDB;
    /**
     * Instance of the emailService interface that is going to be used to send emails
     */
    private ServiceEmail emailService;
    /**
     *
     */
    private ServiceVehicle serviceVehicle;

    private ServiceDeliveryRun serviceDeliveryRun;




    /**
     * Empty constructor
     */
    public NotifyCourierController(){
        watchFolderThread = new Thread(new WatchFolderRunnable(this));
        serviceParkFiles = new ServiceParkSystemFiles();
        serviceVehicle = PharmaDeliveriesApp.getInstance().getServiceVehicle();
        emailService = PharmaDeliveriesApp.getInstance().getServiceEmail();
        serviceDeliveryRun = PharmaDeliveriesApp.getInstance().getServiceDelivery();
        vehicleDB = new VehicleDB();
        courierDB = new CourierDB();
    }

    /**
     * Returns the thread used in this instance
     * @return Thread watchFolderThread
     */
    public final Thread getWatchFolderThread() {
        return watchFolderThread;
    }

    /**
     * Creates and starts the watchFolderThread thread
     */
    public void startWatch() {
       watchFolderThread.setDaemon(true);
       watchFolderThread.start();
    }

    /**
     * Coordinates/manages all the methods related to the user story
     * @param flagFileName the name of the new file detected in the directory being watched
     */
    public boolean processDetectedFile(String flagFileName){

        if(serviceParkFiles.validateDetectedFile(flagFileName)){
            String dataFileName = flagFileName.substring(0, flagFileName.length() - 5);

            try
            {
                if( serviceParkFiles.checkIfDataFileIsPresent(dataFileName) )
                {
                        sensorFile = serviceParkFiles.readDataFile(dataFileName);
                        if (sensorFile == null) {
                            throw new IOException("Error reading estimate_[datetime].data file.");
                        }
                        if(!serviceParkFiles.validateParkSensorFile(sensorFile)){
                            throw new IOException("The contents of the Estimate_[datetime].data are note valid");
                        }
                        if(!updateScooter()){
                            throw new IllegalStateException("Estimate_[datetime].data file read successfully but ScooterStatus was not updated!");
                        }
                        if(!serviceVehicle.simulateVehicleCharging(sensorFile)){
                            throw new IOException("Error simulating scooter charging.");
                        }
                        if(!serviceParkFiles.deleteFileInInputDirectory(flagFileName)){
                            throw new IOException("Error deleting Estimate_[datetime].data.flag file.");
                        }
                        if(!serviceParkFiles.deleteFileInInputDirectory(dataFileName)){
                            throw new IOException("Error deleting Estimate_[datetime].data file.");
                        }

                        String courierEmail = courierDB.getCourierEmailByVehicleId(sensorFile.getVehicleId());
                        if(!sendEmail(courierEmail)){
                            throw new IllegalStateException("Courier notification email failed!");
                        }

                        serviceDeliveryRun.markCourierAsFree(courierEmail);

                        sensorFile = null;
                        return true;
                }
                else{
                    throw new IllegalStateException("Flag file detected but corresponding estimate_[datetime].data file doesn't exist.");
                }
            }catch(Exception e){
                e.printStackTrace();
                //logger.log(Level.SEVERE, e.toString(), e);
            }
        }
        return false;
    }

    /**
     * Get the courier email and invokes the methods necessary to build and send the email to the courier
     * @return boolean if sent successfully
     *          false if not sent
     */
    public boolean sendEmail (String courierEmail){



        if (courierEmail == null || courierEmail.isEmpty() || emailService == null){
            return false;
        }

        String[] email;
        if(sensorFile.isReestimate()){
            email = buildCourierReestimateEmail();
        }else{
            email = buildCourierNotificationEmail();
        }

        return emailService.sendEmail(courierEmail, email[0], email[1]);
    }

    /**
     * Builds the subject line and the email text/content of the email to be sent to the courier when he parks the vehicle
     * @return String[2] array containing: subject line in position [0] and email content in position[1]
     */
    public String[] buildCourierNotificationEmail(){

        String emailContent =
            "The vehicle with id " + sensorFile.getVehicleId() + " was parked correctly. "+
            "It is expected that the vehicle will be fully charged in "+ sensorFile.getEstimatedTimeToFullCharge()+" min.";

        String[] emailInformation = new String[2];
        //email subject line
        emailInformation[0] = "Vehicle delivery notification";
        //email content
        emailInformation[1] = emailContent;

        return emailInformation;
    }
    /**
     * Builds the subject line and the email text/content of the email to be sent to the courier in case the vehicle estimate is changed
     * @return String[2] array containing: subject line in position [0] and email content in position[1]
     */
    public String[] buildCourierReestimateEmail(){

        String emailContent =
                "Due to a change in the number of vehicles being charged simultaneously the estimated time to full charge of the vehicle with id "
                        + sensorFile.getVehicleId() + " was adjusted. It will now take "
                        + sensorFile.getEstimatedTimeToFullCharge()+" min to be fully charged.";

        String[] emailInformation = new String[2];
        //email subject line
        emailInformation[0] = "Estimation change notification";
        //email content
        emailInformation[1] = emailContent;

        return emailInformation;
    }

    /**
     *Updates the Scooter status in the DB with the content of the ParkSensorFile
     * @return true if the scooter status was updated correctly
     *       false if the scooter status update process was not completed
     * @throws SQLException may throw while updating database
     */
    public boolean updateScooter() throws SQLException {
        return vehicleDB.updateScooterStatus(sensorFile, sensorFile.getVehicleBatteryPercentage());

    }



}
