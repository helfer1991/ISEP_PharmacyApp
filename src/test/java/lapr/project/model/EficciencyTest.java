/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;


import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

/**
 *
 * @author catarinaserrano
 */
public class EficciencyTest {

    Eficciency instance1 = new Eficciency();
    Address addressOrig1 = new Address(3, 41.156851, -8.61639, "rua11", "2500", 98);
    Address addressDest1 = new Address(4, 41.156299, -8.619319, "rua10", "2500", 85);
    Road r1 = new Road(0, addressOrig1, addressDest1);
    Road r2 = new Road(0, addressDest1, addressOrig1);

    public EficciencyTest() {
    }

    /**
     * Test of calcSpentEnergyScooter method, of class Eficciency.
     */
    @Test
    public void testCalcSpentEnergyScooter() {
        instance1.setCourierWeight(80);
        instance1.setScooterWeight(50);
        instance1.setScooterVelocity(30);
        instance1.setFrictionCoefficient(0.5f);
        instance1.setWindAngle(0);
        instance1.setWindVelocity(0);
        instance1.setFrontalAreaScooter(0.3f);
        
        double result1 = instance1.calcSpentEnergyScooter(r1, 5.0f);
        double result2 = instance1.calcSpentEnergyScooter(r1, 15.0f);
        double result3 = instance1.calcSpentEnergyScooter(r2, 15.0f);
        assertEquals(0.04167, result1, 0.001);
        assertEquals(0.04475, result2, 0.001);
        assertEquals(0.05502, result3, 0.001);
        instance1.setWindAngle(48);
        instance1.setWindVelocity(12);
        double result4 = instance1.calcSpentEnergyScooter(r2, 15.0f);
        assertEquals(0.05984, result4, 0.001);
        instance1.setWindVelocity(60);
        double result5 = instance1.calcSpentEnergyScooter(r2, 15.0f);
        assertEquals(0.17565, result5, 0.001);
        instance1.setWindAngle(20);
        double result6 = instance1.calcSpentEnergyScooter(r2, 15.0f);
        assertEquals(0.29294, result6, 0.001);
        instance1.setWindAngle(80);
        double result7 = instance1.calcSpentEnergyScooter(r2, 15.0f);
        assertEquals(0.063145, result7, 0.001);
        instance1.setFrictionCoefficient(0.9f);
        double result8 = instance1.calcSpentEnergyScooter(r2, 15.0f);
        assertEquals(0.10305, result8, 0.001);
        
    }

    /**
     * Test of calcSpentEnergyDrone method, of class Eficciency.
     */
    @Test
    public void testCalcSpentEnergyDrone() {
        instance1.setWindAngle(0);
        instance1.setWindVelocity(0);
        instance1.setDroneVelocityHorizontal(57.6f);
        instance1.setDroneVelocityVetical(21.6f);
        instance1.setFrontalAreaDrone(0.3f);
        instance1.setDroneWeight(3);
        instance1.setLiftDragRatio(3);
        instance1.setPowerConsumptionElectronics(0);
        instance1.setPowerTransferEfficiency(1);
        
        double result1 = instance1.calcSpentEnergyDrone(r1, 5.0f, false, false);
        double result2 = instance1.calcSpentEnergyDrone(r1, 15.0f, false, false);
        double result3 = instance1.calcSpentEnergyDrone(r2, 15.0f, true, true);
        assertEquals(0.00182, result1, 0.001);
        assertEquals(0.00410, result2, 0.001);
        assertEquals(0.00679, result3, 0.001);
        
        instance1.setWindAngle(48);
        instance1.setWindVelocity(12);
        double result4 = instance1.calcSpentEnergyDrone(r2, 15.0f, false, false);
        assertEquals(0.004769, result4, 0.001);
        instance1.setWindVelocity(60);
        double result5 = instance1.calcSpentEnergyDrone(r2, 15.0f, false, false);
        assertEquals(0.01354, result5, 0.001);
        instance1.setWindAngle(20);
        double result6 = instance1.calcSpentEnergyDrone(r2, 15.0f, false, false);
        assertEquals(0.19403, result6, 0.001);
        instance1.setWindAngle(80);
        double result7 = instance1.calcSpentEnergyDrone(r2, 15.0f, false, false);
        assertEquals(0.00501, result7, 0.001);
    }

}
