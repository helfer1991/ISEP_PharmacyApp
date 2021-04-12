package lapr.project.service;

import lapr.project.model.*;
import lapr.project.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ServiceParkSystemFiles {

    /**
     * Validates if the detected file has a file name that starts with "estimate_" and ends in ".data.flag"
     * @param fileName name of the file to be validated
     * @return true if the file name passes the validation
     *          false is the file name does not pass the validation
     */
    public boolean validateDetectedFile(String fileName) {
        if (fileName == null){return false;}

        return (fileName.matches("^estimate[_]\\d{4}([_]\\d{2}){5}\\.data\\.flag$"));
    }

    /**
     * Checks if there is a file in the specified INPUT_FILES_DIRECTORY with the name passed as parameter
     * @param fileName name of the file
     * @return true if the file exists in the directory
     *          false if there is no file with that name in the directory
     */
    public boolean checkIfDataFileIsPresent(String fileName) {
        File dataFile = new File (Constants.INPUT_FILES_DIRECTORY+fileName);
        return dataFile.isFile();
    }

    /**
     * Reads the information in the estimate_[datetime].data file (specific file name passed as parameter)
     * @param dataFileName name of the data file to be read
     * @return ParkSensorFile object containing the information in the file.
     *          Returns null if file is empty or incorreclty formated.
     * @throws IOException to be managed in the NotifyCourierController
     * @throws NumberFormatException to be managed in the NotifyCourierController
     */
    public ParkSensorFile readDataFile(String dataFileName) throws IOException, NumberFormatException {

        String timeStamp = dataFileName.substring(9, dataFileName.length() - 5);
        //assigned -2 for debugging purposes
        int scooterId = -2;
        int batteryCapacity= -2;
        int batteryPercentage = -2;
        double estimatedTimeToFullCharge = -2.0;

        int reestimate = 0;

        File dataFile = new File(Constants.INPUT_FILES_DIRECTORY + dataFileName);

        Scanner sc = null;
        int countLines = 0; //count the number of lines read
        try {


            //processes file content line by line
            sc = new Scanner(dataFile);
            sc.useDelimiter("\\R");
            String temp;

            while (sc.hasNext()) {
                temp = sc.next();
                //valida que a linha não seja vazia
                if (temp.isEmpty()) {
                    sc.close();
                    return null;
                }
                //valida que a linha tenha minimo duas palavras separadas por espaço
                temp = temp.split(" ")[1];
                if (temp.isEmpty()) {
                    sc.close();
                    return null;
                }

                //tries to convert second word of the line to int/double and handles exception

                switch (countLines) {
                    case 0:
                        scooterId = Integer.parseInt(temp);
                        break;
                    case 1:
                        batteryCapacity = Integer.parseInt(temp);
                        break;
                    case 2:
                        batteryPercentage = Integer.parseInt(temp);
                        break;
                    case 3:
                        estimatedTimeToFullCharge = Double.parseDouble(temp);
                        break;
                    case 4:
                        reestimate = Integer.parseInt(temp);
                        break;
                    default:
                }
                countLines++;
                if (countLines >= 5) {
                    break; //ignores file content after the 4th line
                }
            }

        }finally{
            if (sc != null){
                sc.close();
            }
        }

        //if 4 or 5 (5th line is optional) lines were read correctly then creates the ParkSensorFile object
        if(countLines == 5){
            ParkSensorFile sensorFile = new ParkSensorFile(scooterId, batteryCapacity, batteryPercentage, estimatedTimeToFullCharge, timeStamp);
            if(reestimate != 0){
                sensorFile.setReestimate(true);
            }
            return sensorFile;
        }
        else
            return null;
    }

    /**
     * Delete the file that has the name passed in parameter, from the INPUT_FILES_DIRECTORY
     * @param fileName name of the file to be deleted
     * @return true if deleted correctly
     *          false if the file does not exist or couldn't be deleted
     */
    public boolean deleteFileInInputDirectory(String fileName) {
        File file = new File (Constants.INPUT_FILES_DIRECTORY+fileName);
        return file.delete();
    }

    /**
     * Validates the information contained in the ParkSensorFile (created using the input file form the Park System)
     * @param sensorFile ParkSensorFile object to be validated
     * @return true if all the values are valid
     *          false if any of the values are invalid/incorrect/impossible
     */
    public boolean validateParkSensorFile(ParkSensorFile sensorFile){

        return (sensorFile != null
                && sensorFile.getEstimatedTimeToFullCharge() >= 0)
                && sensorFile.getEstimatedTimeToFullCharge() < 2880 //48h
                && sensorFile.getVehicleId() >= 0
                && sensorFile.getVehicleBatteryCapacity() >= 0
                && sensorFile.getVehicleBatteryPercentage() >= 0
                && sensorFile.getVehicleBatteryPercentage() <= 100
                && sensorFile.getTimeStamp()
                .matches("^\\d{4}[_]([0]\\d|1[0-2])[_]([0-2]\\d|3[01])[_]([01]\\d|2[0-3])([_][0-5]\\d){2}$");
                //check if scooterId exists in DB??
    }

    public boolean createParkingFile(Pharmacy pharmacy, Vehicle vehicle, boolean incorrectPark) {
        ZonedDateTime date = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        String strDate = date.format(formatter);

        String datafileName = "lock_" + strDate + ".data";

        PrintWriter writer = null;
        try {
            File dataFile = new File(Constants.OUTPUT_FILES_DIRECTORY + datafileName);
            if(incorrectPark){
                if(!dataFile.createNewFile())
                return false;
            }else{
                writer = new PrintWriter(dataFile);
                if (vehicle instanceof Drone) {
                    writer.println("ChargerOutput: " + pharmacy.getPark().getDroneChargerCapacity()*1000);
                    writer.println("DroneId: " + vehicle.getId());
                }
                if (vehicle instanceof Scooter) {
                    writer.println("ScooterParkChargerOutput: " + pharmacy.getPark().getDroneChargerCapacity()*1000);
                    writer.println("ScooterId: " + vehicle.getId());
                }
                writer.println("BatteryCapacity: " + vehicle.getBattery().getCapacity()*1000);
                writer.println("BatteryPercentage: " + vehicle.getActualCharge());
                writer.close();
            }

            String flagFileName = datafileName + ".flag";
            File flagFile = new File(Constants.OUTPUT_FILES_DIRECTORY + flagFileName);
            return flagFile.createNewFile();

        } catch (Exception e) {
            return false;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
