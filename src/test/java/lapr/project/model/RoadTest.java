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
public class RoadTest {
    
    Address addressOrig1;
    Address addressDest1;
    Road instance1;
    double roadRollingResistence1;
    
    Road instance2;
    
    Road instance3;
    double RoadRollingResistence3;
    
    
    public RoadTest() {
        addressOrig1 = new Address(3,41.156851,-8.61639, "rua11", "2500", 98);
        addressDest1 = new Address(4, 41.156299, -8.619319, "rua10", "2500", 85);
        instance1 = new Road(0, addressOrig1, addressDest1); 
        instance2 = new Road(0,addressDest1, addressOrig1);
        instance3 = new Road(0,addressOrig1, addressDest1); 
        
    }

    /**
     * Test of getAddressOrig method, of class Road.
     */
    @Test
    public void testGetAddressOrig() {
       
        Address expResult = new Address(3,41.156851,-8.61639, "rua11", "2500", 98);
        Address result = instance1.getAddressOrig();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSetId(){
        instance1.setId(1);
        int result = instance1.getId();
        assertEquals(1, result);
    }

    /**
     * Test of getAddressDest method, of class Road.
     */
    @Test
    public void testGetAddressDest() {
        Address expResult = new Address(4, 41.156299, -8.619319, "rua10", "2500", 85);
        Address result = instance1.getAddressDest();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testEquals(){
        boolean result1 = instance1.equals(instance1);
        boolean result2 = instance1.equals(addressOrig1);
        Road road1 = new Road(0, addressOrig1, addressDest1);
        boolean result3 = instance1.equals(road1);
        assertEquals(true, result1);
        assertEquals(false, result2);
        assertEquals(true, result3);
    }
    
    @Test
    public void testHashCode(){
        Road road1 = new Road(0, addressOrig1, addressDest1);
        assertEquals(road1.hashCode(), instance1.hashCode());
    }
    
}
