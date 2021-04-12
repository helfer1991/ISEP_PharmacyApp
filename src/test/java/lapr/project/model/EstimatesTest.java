/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author catarinaserrano
 */
public class EstimatesTest {

    DeliveryRun deliveryRun1 = new DeliveryRun(2, "2018");
    Estimates instance;
    LinkedHashMap<Address, Double> energyCostMap;
    LinkedHashMap<Address, Double> distanceCostMap;
    Address a3 = new Address(3, 41.152325, -8.626058, "Rua Julio Dinis", "4450-116", 72);
    Address a4 = new Address(4, 41.148171, -8.624156, "Rua Dom Manuel II", "4450-133", 77);
    Address a12 = new Address(12, 41.155336, -8.613358, "Rua da Lapa", "4450-136", 114);
    Order order1 = new Order(20, 15, 2, "2000");
    Order order2 = new Order(20, 15, 2, "2011");
    Order order3 = new Order(20, 15, 2, "2011");
    LinkedList<Deliverable> ordersToDelivery = new LinkedList<>();

    public EstimatesTest() {
        ordersToDelivery.add(order1);
        ordersToDelivery.add(order2);
        ordersToDelivery.add(order3);
        deliveryRun1.setDeliverables(ordersToDelivery);
        instance = new Estimates(deliveryRun1);

    }

    /**
     * Test of getDeliveryRun method, of class Estimates.
     */
    @Test
    public void testGetDeliveryRun() {
        DeliveryRun result = instance.getDeliveryRun();
        assertEquals(deliveryRun1, result);
    }

    /**
     * Test of getDistanceCostMap method, of class Estimates.
     */
    @Test
    public void testGetDistanceCostMap() {
        instance.setDistanceCostMap(distanceCostMap);
        assertEquals(distanceCostMap, instance.getDistanceCostMap());
    }

    /**
     * Test of getEnergyCostMap method, of class Estimates.
     */
    @Test
    public void testGetEnergyCostMap() {
        instance.setEnergyCostMap(energyCostMap);
        assertEquals(energyCostMap, instance.getEnergyCostMap());
    }

    /**
     * Test of getRequiredBatteryToCompletePath method, of class Estimates.
     */
    @Test
    public void testGetRequiredBatteryToCompletePath() {
        instance.setRequiredBatteryToCompletePath(100.0);
        double result = instance.getRequiredBatteryToCompletePath();
        assertEquals(100.0, result, 0.0);
    }

    /**
     * Test of getRequiredBatteryToReachNextPharmacy method, of class Estimates.
     */
    @Test
    public void testGetRequiredBatteryToReachNextPharmacy() {
        instance.setRequiredBatteryToReachNextPharmacy(20.0);
        double result = instance.getRequiredBatteryToReachNextPharmacy();
        assertEquals(20.0, result, 0.0);
    }

    /**
     * Test of getDistanceTotalLengh method, of class Estimates.
     */
    @Test
    public void testGetDistanceTotalLengh() {
        instance.setDistanceTotalLengh(10);
        double result = instance.getDistanceTotalLengh();
        assertEquals(10, result, 0.0);
    }

    /**
     * Test of getTimeDuration method, of class Estimates.
     */
    @Test
    public void testGetTimeDuration() {
        instance.setTimeDuration(10);
        double result = instance.getTimeDuration();
        assertEquals(10, result, 0.0);
    }

    /**
     * Test of equals method, of class Estimates.
     */
    @Test
    public void testEquals() {
        boolean result1 = instance.equals(a3); //false
        boolean result2 = instance.equals(instance);
        Estimates exp1 = new Estimates(deliveryRun1);
        boolean result3 = instance.equals(exp1);
        assertEquals(false, result1);
        assertEquals(true, result2);
        assertEquals(true, result3);

    }

    /**
     * Test of hashCode method, of class Estimates.
     */
    @Test
    public void testHashCode() {
        Estimates exp1 = new Estimates(deliveryRun1);
        assertEquals(exp1.hashCode(), instance.hashCode());
    }

    /**
     * Test of comparingPositions method, of class Estimates.
     */
    @Test
    public void testComparingPositions() {
        LinkedHashMap<Address, Double> a = new LinkedHashMap<>();
        a.put(a3, 8.0);
        a.put(a4, 2.0);
        a.put(a12, 7.0);
        LinkedHashMap<Address, Double> b = new LinkedHashMap<>();
        b.put(a3, 8.0);
        b.put(a4, 2.0);
        b.put(a12, 7.0);
        boolean result1 = Estimates.comparingPositions(a, b);
        assertEquals(true, result1);

        LinkedHashMap<Address, Double> c = new LinkedHashMap<>();
        c.put(a3, 8.0);
        c.put(a4, 2.0);
        boolean result2 = Estimates.comparingPositions(a, c);
        assertEquals(false, result2);
        
        LinkedHashMap<Address, Double> d = new LinkedHashMap<>();
        d.put(a3, 8.0);
        d.put(a4, 1.0);
        d.put(a12, 7.0);
        boolean result3 = Estimates.comparingPositions(a, d);
        assertEquals(false, result3);

    }
}
