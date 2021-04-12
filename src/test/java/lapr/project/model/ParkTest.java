package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkTest {

    Park park;

    @BeforeEach
    void setUp() {
        park = new Park(1,10,10,3,3);
    }
    
    /**
     * Test of getId method, of class Park.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        int expResult = 1;
        int result = park.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class Park.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 0;
        park.setId(id);
        assertEquals(0, park.getId());
        
    }

    /**
     * Test of getScooterChargersNumber method, of class Park.
     */
    @Test
    public void testGetScooterChargersNumber() {
        System.out.println("getScooterChargersNumber");
        int expResult = 10;
        int result = park.getScooterChargersNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of setScooterChargersNumber method, of class Park.
     */
    @Test
    public void testSetScooterChargersNumber() {
        System.out.println("setScooterChargersNumber");
        int scooterChargersNumber = 0;
        park.setScooterChargersNumber(scooterChargersNumber);
        assertEquals(0, park.getScooterChargersNumber());
    }

    /**
     * Test of getDroneChargersNumber method, of class Park.
     */
    @Test
    public void testGetDroneChargersNumber() {
        System.out.println("getDroneChargersNumber");
        int expResult = 10;
        int result = park.getDroneChargersNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDroneChargersNumber method, of class Park.
     */
    @Test
    public void testSetDroneChargersNumber() {
        System.out.println("setDroneChargersNumber");
        int droneChargersNumber = 0;
        park.setDroneChargersNumber(droneChargersNumber);
        assertEquals(0, park.getDroneChargersNumber());
    }

    /**
     * Test of getScooterChargerCapacity method, of class Park.
     */
    @Test
    public void testGetScooterChargerCapacity() {
        System.out.println("getScooterChargerCapacity");
        float expResult = 3.0F;
        float result = park.getScooterChargerCapacity();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setScooterChargerCapacity method, of class Park.
     */
    @Test
    public void testSetScooterChargerCapacity() {
        System.out.println("setScooterChargerCapacity");
        float scooterChargerCapacity = 0.0F;
        park.setScooterChargerCapacity(scooterChargerCapacity);
        assertEquals(0,park.getScooterChargerCapacity(),0.00001);
    }

    /**
     * Test of getDroneChargerCapacity method, of class Park.
     */
    @Test
    public void testGetDroneChargerCapacity() {
        System.out.println("getDroneChargerCapacity");
        float expResult = 3.0F;
        float result = park.getDroneChargerCapacity();
        assertEquals(expResult, result, 0.0);

    }

    /**
     * Test of setDroneChargerCapacity method, of class Park.
     */
    @Test
    public void testSetDroneChargerCapacity() {
        System.out.println("setDroneChargerCapacity");
        float droneChargerCapacity = 0.0F;
        park.setDroneChargerCapacity(droneChargerCapacity);
        assertEquals(0.0, park.getDroneChargerCapacity(), 0.00001);
    }


    @Test
    void testEquals() {
        Park park1 = new Park(1,10,10,3,3);
        Park park2 = new Park(1,10,10,3,3);

        assertEquals(park1, park1);
        assertEquals(park1, park2);

        assertNotEquals(park1, null);
        assertNotEquals(park1, new Battery(1, 4));

        park2 = new Park(2,100,10,3,3);
        assertNotEquals(park1, park2);
        park2 = new Park(1,100,10,3,3);
        assertNotEquals(park1, park2);
        park2 = new Park(1,10,100,3,3);
        assertNotEquals(park1, park2);
        park2 = new Park(1,10,10,33,3);
        assertNotEquals(park1, park2);
        park2 = new Park(1,10,10,3,4);
        assertNotEquals(park1, park2);
    }

    @Test
    void testHashCode() {
        Park park1 = new Park(1,10,10,3,3);
        Park park2 = new Park(1,10,10,3,3);

        assertEquals(park1.hashCode(), park1.hashCode());
        assertEquals(park1.hashCode(), park2.hashCode());

        park2 = new Park(1,10,10,3,4);
        assertNotEquals(park1.hashCode(), park2.hashCode());
    }
}