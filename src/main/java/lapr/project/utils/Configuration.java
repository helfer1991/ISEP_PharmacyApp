/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author 1100241
 */
public class Configuration {

    public static boolean readProperties() {
        Properties props = null;

        try {
            File configFile = new File(Constants.PARAMS_FILE);
            FileReader reader = new FileReader(configFile);
            props = new Properties();
            props.load(reader);
            reader.close();

            emailServiceClass = props.getProperty("EmailService.Class");
            databaseUrl = props.getProperty("database.url");
            databaseUsername = props.getProperty("database.username");
            databasePassword = props.getProperty("database.password");
            specsWindKmH = Utils.convertStringToFloat(props.getProperty("specs.windKmH"));
            specsWindAng = Utils.convertStringToInt(props.getProperty("specs.windAng"));
            specsFriction = Utils.convertStringToFloat(props.getProperty("specs.Friction"));
            specsBatteryScooterCapacitykWh= Utils.convertStringToFloat(props.getProperty("specs.BatteryScooterCapacitykWh"));
            specsBatteryDroneCapacitykWh= Utils.convertStringToFloat(props.getProperty("specs.BatteryDroneCapacitykWh"));
            specsVScooterkmH = Utils.convertStringToFloat(props.getProperty("specs.VScooterkmH"));
            specsWeightScooterkG = Utils.convertStringToInt(props.getProperty("specs.WeightScooterkG"));
            specsFrontalAreaM2 = Utils.convertStringToFloat(props.getProperty("specs.FrontalAreaM2"));
            specsAirResistance = Utils.convertStringToFloat(props.getProperty("specs.AirResistance"));
            specsWeightCourierkG = Utils.convertStringToInt(props.getProperty("specs.WeightCourierkG"));
            specsVHorDronekmH = Utils.convertStringToFloat(props.getProperty("specs.VHorDronekmH"));
            specsVVerDronekmH = Utils.convertStringToFloat(props.getProperty("specs.VVerDronekmH"));
            specsWeightDronekG = Utils.convertStringToInt(props.getProperty("specs.WeightDronekG"));
            specsLifDragRatio = Utils.convertStringToFloat(props.getProperty("specs.LifDragRatio"));
            specsPowerTransferEfficiency = Utils.convertStringToFloat(props.getProperty("specs.PowerTransferEfficiency"));
            specsPowerConsumptionEletronicsW = Utils.convertStringToFloat(props.getProperty("specs.PowerConsumptionEletronicsW"));
            specsflyHeight= Utils.convertStringToFloat(props.getProperty("specs.flyHeight"));

            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static float getSpecsflyHeight() {
        { readProperties(); } return specsflyHeight;
    }

    
    public static String getEmailServiceClass() {
        { readProperties(); } return emailServiceClass;
    }

    public static String getDatabaseUrl() {
        { readProperties(); } return databaseUrl;
    }

    public static String getDatabaseUsername() {
        { readProperties(); } return databaseUsername;
    }

    public static String getDatabasePassword() {
        { readProperties(); } return databasePassword;
    }

    public static float getSpecsWindKmH() {
        { readProperties(); } return specsWindKmH;
    }

    public static int getSpecsWindAng() {
        { readProperties(); } return specsWindAng;
    }

    public static float getSpecsFriction() {
        { readProperties(); } return specsFriction;
    }

    public static float getSpecsVScooterkmH() {
        { readProperties(); } return specsVScooterkmH;
    }

    public static int getSpecsWeightScooterkG() {
        { readProperties(); } return specsWeightScooterkG;
    }

    public static float getSpecsFrontalAreaM2() {
        { readProperties(); } return specsFrontalAreaM2;
    }

    public static float getSpecsAirResistance() {
        { readProperties(); } return specsAirResistance;
    }

    public static int getSpecsWeightCourierkG() {
        { readProperties(); } return specsWeightCourierkG;
    }

    public static float getSpecsVHorDronekmH() {
        { readProperties(); } return specsVHorDronekmH;
    }

    public static float getSpecsVVerDronekmH() {
        { readProperties(); } return specsVVerDronekmH;
    }

    public static int getSpecsWeightDronekG() {
        { readProperties(); } return specsWeightDronekG;
    }

    public static float getSpecsBatteryScooterCapacitykWh() {
        { readProperties(); } return specsBatteryScooterCapacitykWh;
    }

    public static float getSpecsBatteryDroneCapacitykWh() {
        { readProperties(); } return specsBatteryDroneCapacitykWh;
    }

    

    public static float getSpecsLifDragRatio() {
        { readProperties(); } return specsLifDragRatio;
    }

    public static float getSpecsPowerTransferEfficiency() {
        { readProperties(); } return specsPowerTransferEfficiency;
    }

    public static float getSpecsPowerConsumptionEletronicsW() {
       { readProperties(); } return specsPowerConsumptionEletronicsW;
    }

    static String emailServiceClass;
    static String databaseUrl;
    static String databaseUsername;
    static String databasePassword;
    static float specsWindKmH;
    static int specsWindAng;
    static float specsFriction;
    static float specsVScooterkmH;
    static float specsBatteryScooterCapacitykWh;
    static float specsBatteryDroneCapacitykWh;
    static int specsWeightScooterkG;
    static float specsFrontalAreaM2;
    static float specsAirResistance;
    static int specsWeightCourierkG;
    static float specsVHorDronekmH;
    static float specsVVerDronekmH;
    static int specsWeightDronekG;
    static float specsLifDragRatio;
    static float specsPowerTransferEfficiency;
    static float specsPowerConsumptionEletronicsW;
    static float specsflyHeight;

}
