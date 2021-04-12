/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import lapr.project.utils.Configuration;
import lapr.project.utils.Utils;

/**
 *
 * @author 1100241
 */
public class Eficciency {
    
    //physics constants
    static float G_FORCE = 9.8f;
    static float AIR_DENSITY = 1.204f; //24ºC


    //Wind
    float windVelocity = Configuration.getSpecsWindKmH();
    float windAngle = Configuration.getSpecsWindAng();
    float frictionCoefficient = Configuration.getSpecsFriction();
   
    //Scooter
    float frontalAreaScooter = Configuration.getSpecsFrontalAreaM2();
    float airDragCoefficient = Configuration.getSpecsAirResistance();
    float scooterVelocity = Configuration.getSpecsVScooterkmH();
    float scooterWeight = Configuration.getSpecsWeightScooterkG();
    float courierWeight = Configuration.getSpecsWeightCourierkG();
    
    //Drone
    float droneVelocityHorizontal = Configuration.getSpecsVVerDronekmH();
    float droneVelocityVetical = Configuration.getSpecsVHorDronekmH();
    float droneWeight = Configuration.getSpecsWeightDronekG();
    float liftDragRatio = Configuration.getSpecsLifDragRatio();
    //  the lift-to-drag ratio is set to 3, a pessimistic value, and is meant to capture a 
    //vehicle that is capable of vertical takeoff and landing
    float powerTransferEfficiency = Configuration.getSpecsPowerTransferEfficiency();//100% efficiency
    float powerConsumptionElectronics = Configuration.getSpecsPowerConsumptionEletronicsW(); //kW
    float frontalAreaDrone = 0.12F; 

    
    
    /**
     * This method is based in
     * https://x-engineer.org/automotive-engineering/vehicle/electric-vehicles/ev-design-energy-consumption/
     * Where Ftotal= Fa+Fs+Fr (Fi is ignored since we consider a constant
     * velocity and accelaration =0) Fs (road slope force)=m*G*sin(inclination
     * degree) Fr (rode load force) = m*G*frictionCoefficient*cos(inclination
     * degree)
     *
     * @param road
     * @param productsWeight
     * @return
     */
    public double calcSpentEnergyScooter(Road road, float productsWeight) {
        
        float totalWeight = productsWeight + scooterWeight + courierWeight;
        float windVelocityMS = (float) (windVelocity * Math.pow(10, 3)) / 3600; 
        float scooterVelocityMS = (float) (scooterVelocity* Math.pow(10, 3)) / 3600;
        Address addressOrig = road.getAddressOrig();
        Address addressDest = road.getAddressDest();
        
        double flatDistance = Utils.distBetweenPointsOnEarth(addressOrig.getLatitude(), addressOrig.getLongitude(), addressDest.getLatitude(), addressDest.getLongitude());
        double difAltitude = getDifAltitude(addressDest, addressOrig);
        double realDistance = getRealDistance(flatDistance, difAltitude);//m

        double fs = totalWeight * G_FORCE * (difAltitude / realDistance);

        double fr = totalWeight * G_FORCE * frictionCoefficient * (flatDistance / realDistance);

        double fa = 0.5 * AIR_DENSITY * frontalAreaScooter * airDragCoefficient * Math.pow((scooterVelocityMS * windVelocityMS * Math.cos(Math.toRadians(windAngle))), 2);

        double power = (fs + fr + fa) * scooterVelocityMS; //watts

        if (power < 0) {
            power = 0;
        }

        double time = realDistance / scooterVelocityMS; //s

        double energyJoules = power * time;  //Energy in Joules (w.s)

        double energyKwh = energyJoules * Math.pow(10, -3) / 3600; //kw.h

        return energyKwh;
    }

    /**
     * This method is based in
     * https://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=6827242
     *and 
     * https://drive.google.com/file/d/1q_zd5jGTlV-PnAVj1ygPf9OBViNdo_bJ/view
     * @param road
     * @param productsWeight
     * @param hasUpLift
     * @param hasDownLift
     * @return
     */
    public double calcSpentEnergyDrone(Road road, float productsWeight, boolean hasUpLift, boolean hasDownLift) {

        float totalWeight = productsWeight + droneWeight;
        Address addressOrig = road.getAddressOrig();
        Address addressDest = road.getAddressDest();

        double flatDistance = Utils.distBetweenPointsOnEarth(addressOrig.getLatitude(), addressOrig.getLongitude(), addressDest.getLatitude(), addressDest.getLongitude());

        double difAltitude = getDifAltitude(addressDest, addressOrig);

        double realDistance = getRealDistance(flatDistance, difAltitude)*Math.pow(10, -3); //m to km
        
        double energyRequiredHorizontalMovKwh = calcSpentEnergyDroneHorizontal(realDistance, totalWeight);

        double energyRequiredVerticalMovKwh = 0;
        if(hasUpLift){
            energyRequiredVerticalMovKwh = calcSpentEnergyDroneVertical(totalWeight) + energyRequiredVerticalMovKwh;
        }
        if(hasDownLift){
            energyRequiredVerticalMovKwh = calcSpentEnergyDroneVertical(totalWeight) + energyRequiredVerticalMovKwh;
        }      
        double energyKwh = energyRequiredHorizontalMovKwh + energyRequiredVerticalMovKwh;

        return energyKwh;
    }
    
    /**
     * This method calculates the energy required to the horizontal movement of the drone
     * @param realDistance
     * @param totalWeight
     * @return double
     */
    private double calcSpentEnergyDroneHorizontal(double realDistance, float totalWeight){
        // Headwind speed (or tailwind) = wind speed * cos ( α ) from: https://mediawiki.ivao.aero/index.php?title=Crosswind_and_Headwind_calculation
        double headWindToSpeed = windVelocity * Math.cos(Math.toRadians(windAngle));

        double ratioHeadWindAirSpeed = headWindToSpeed / droneVelocityHorizontal;

        double a = realDistance / (1 - ratioHeadWindAirSpeed);

        double b = ((totalWeight / (370 * powerTransferEfficiency * liftDragRatio)) + (powerConsumptionElectronics / droneVelocityHorizontal));

        double energyKwh = a * b;

        return energyKwh;
    }
    
    /**
     * This method calculates the energy required to the vertical movement of the drone
     * @param totalWeight
     * @return double
     */
    private double calcSpentEnergyDroneVertical(float totalWeight){
        float thrust = totalWeight*G_FORCE;
        double a = Math.pow(thrust, 3/2);
        double b =Math.sqrt(2*AIR_DENSITY*frontalAreaDrone);
                
        double powerW = (a/b);
        
        double time = ((0.14) / droneVelocityVetical)*3600; //s

        double energyJoules = powerW * time;  //Energy in Joules (w.s)

        double energyKwh = energyJoules * Math.pow(10, -3) / 3600; //kw.h

        return energyKwh;  
    }
    
    /**
     * Method to get the diference of elevation between addresses
     * @param addressDest
     * @param addressOrig
     * @return double
     */
    private static double getDifAltitude(Address addressDest, Address addressOrig){
        return addressDest.getElevation() - addressOrig.getElevation();
    }
    
    /**
     * Method that returns the real distance, considering the elevation and the 
     * minimium distance between places on earth
     * @param flatDistance
     * @param difAltitude
     * @return 
     */
    private static double getRealDistance( double flatDistance, double difAltitude){
        return (Math.sqrt(Math.pow(flatDistance, 2) + Math.pow(difAltitude, 2)));//metros
    }

    public void setWindVelocity(float windVelocity) {
        this.windVelocity = windVelocity;
    }

    public void setWindAngle(float windAngle) {
        this.windAngle = windAngle;
    }

    public void setFrictionCoefficient(float frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
    }

    public void setFrontalAreaScooter(float frontalAreaScooter) {
        this.frontalAreaScooter = frontalAreaScooter;
    }

    public void setLiftDragRatio(float liftDragRatio) {
        this.liftDragRatio = liftDragRatio;
    }

    public void setPowerTransferEfficiency(float powerTransferEfficiency) {
        this.powerTransferEfficiency = powerTransferEfficiency;
    }

    public void setPowerConsumptionElectronics(float powerConsumptionElectronics) {
        this.powerConsumptionElectronics = powerConsumptionElectronics;
    }

    public void setFrontalAreaDrone(float frontalAreaDrone) {
        this.frontalAreaDrone = frontalAreaDrone;
    }

    public void setScooterVelocity(float scooterVelocity) {
        this.scooterVelocity = scooterVelocity;
    }

    public void setScooterWeight(float scooterWeight) {
        this.scooterWeight = scooterWeight;
    }

    public void setCourierWeight(float courierWeight) {
        this.courierWeight = courierWeight;
    }

    public void setDroneVelocityHorizontal(float droneVelocityHorizontal) {
        this.droneVelocityHorizontal = droneVelocityHorizontal;
    }

    public void setDroneVelocityVetical(float droneVelocityVetical) {
        this.droneVelocityVetical = droneVelocityVetical;
    }

    public void setDroneWeight(float droneWeight) {
        this.droneWeight = droneWeight;
    }
    
    
    
    

}
