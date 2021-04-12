package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryTest {

    Battery bat;

    @BeforeEach
    void setUp() {
        bat = new Battery(1, 500);
    }

    @Test
    void getIdBattery() {
        assertEquals(1, bat.getIdBattery());
    }
    @Test
    void getCapacity() {
        assertEquals(500, bat.getCapacity());
    }

    @Test
    void setIdBattery() {
        bat.setIdBattery(3);
        assertEquals(3, bat.getIdBattery());
    }


    @Test
    void setCapacity() {
        bat.setCapacity(100);
        assertEquals(100, bat.getCapacity());
    }

    @Test
    void testEquals() {

        Battery bat1 = new Battery(1, 500);
        Battery bat2 = new Battery(1, 500);
        Battery bat3 = new Battery();
        Drone drone = new Drone(1, 2, 3, new Battery(4, 5),99);
        assertEquals(bat1, bat1);
        assertEquals(bat2, bat1);
        assertNotEquals(bat3, bat1);
        assertNotEquals(bat1, drone);

        bat2 = new Battery(2, 500);
        assertNotEquals(bat2, bat1);

        bat2 = new Battery(1, 501);
        assertNotEquals(bat2, bat1);
    }


    @Test
    void testHashCode() {
        assertNotEquals(null, new Battery(1, 500));
        assertNotEquals("string", new Battery(1, 500));
        Battery bat1 = new Battery(1, 500);
        Battery bat2 = new Battery(1, 500);
        assertEquals(bat1.hashCode(), bat2.hashCode());

        bat2 = new Battery(2, 500);
        assertNotEquals(bat1.hashCode(), bat2.hashCode());

        bat2 = new Battery(1, 501);
        assertNotEquals(bat1.hashCode(), bat2.hashCode());
    }
}