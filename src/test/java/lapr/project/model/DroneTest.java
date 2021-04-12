package lapr.project.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DroneTest {

    Drone drone1;
    Drone drone6;
    Battery bat6;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeEach
    void setUp() {
       bat6 = new Battery(6, 20);
       drone1 = new Drone(1,222, 50, new Battery(22, 500) ,90);
       drone6 = new Drone(6, 223, 50, bat6,90);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    void getIdDrone() {
        assertEquals(1, drone1.getId());
    }

    @Test
    void setIdDrone() {
        drone1.setId(33);
        assertEquals(33, drone1.getId() );
    }

    @Test
    void getQrCode() {
        assertEquals(222,drone1.getQrCode() );
    }

    @Test
    void setQrCode() {
        drone1.setQrCode(789);
        assertEquals(789, drone1.getQrCode());
    }

    @Test
    void getIsAvailable() {
        assertEquals("true", drone1.getIsAvailable());
    }

    @Test
    void setIsAvailable() {
        drone1.setIsAvailable("abc");
        assertEquals("abc", drone1.getIsAvailable());
    }


    @Test
    void getWeight() {
        assertEquals(50, drone1.getWeight());
    }

    @Test
    void setWeight() {
        drone1.setWeight(81);
        assertEquals(81, drone1.getWeight());
    }

    @Test
    void getBattery() {
        assertEquals(new Battery(22,500), drone1.getBattery());
    }

    @Test
    void setBattery() {
        drone1.setBattery(new Battery(1,1));
        assertEquals(new Battery(1,1), drone1.getBattery());
    }

    @Test
    void testEquals() {
        drone1 = new Drone(1,2, 3, new Battery(4, 5 ),99);
        drone1.setIsAvailable("true");

        assertEquals(drone1, drone1);
        assertNotEquals(drone1, null);
        assertNotEquals(drone1, new Battery(4, 55));

        Drone drone2 = new Drone(1,2,3,new Battery(4,5),99);
        drone2.setIsAvailable("true");

        assertEquals(drone1, drone2);

        drone2 = new Drone(1,2,3,new Battery(4,5),99);
        drone2.setIsAvailable("false");
        assertNotEquals(drone1, drone2);

        drone2 = new Drone(11,2,3,new Battery(4,5),99);
        drone2.setIsAvailable("true");
        assertNotEquals(drone1, drone2);

        drone2 = new Drone(1,22,3,new Battery(4,5),99);
        drone2.setIsAvailable("true");
        assertNotEquals(drone1, drone2);

        drone2 = new Drone(1,2,33,new Battery(4,5),99);
        drone2.setIsAvailable("true");
        assertNotEquals(drone1, drone2);

        drone2 = new Drone(1,2,3,new Battery(44,5),99);
        drone2.setIsAvailable("true");
        assertNotEquals(drone1, drone2);

        drone2 = new Drone(1,2,3,new Battery(4,55),99);
        drone2.setIsAvailable("true");
        assertNotEquals(drone1, drone2);

        drone2 = new Drone(1,2,3,new Battery(4,5),100);
        drone2.setIsAvailable("true");
        assertNotEquals(drone1, drone2);
    }

    @Test
    void testHashCode() {
        Drone drone1 = new Drone(1,2,3,new Battery(4,5),99);
        drone1.setIsAvailable("true");
        Drone drone2 = new Drone(1,2,3,new Battery(4,5),99);
        drone2.setIsAvailable("true");
        assertEquals(drone1.hashCode(), drone2.hashCode());

        Drone drone3 = new Drone(1,2,3,new Battery(4,6),99);
        drone3.setIsAvailable("true");
        assertNotEquals(drone1.hashCode(), drone3.hashCode());

        drone3 = new Drone(1,2,3,new Battery(4,5),100);
        drone3.setIsAvailable("true");
        assertNotEquals(drone1.hashCode(), drone3.hashCode());
    }
}