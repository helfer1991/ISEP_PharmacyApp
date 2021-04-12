package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScooterTest {

    Scooter scooter;


    @BeforeEach
    void setUp() {
        scooter = new Scooter(13,55564, 450, new Battery(1,150),99);
    }


    @Test
    void getIdScooter() {
        assertEquals(13, scooter.getId());
    }

    @Test
    void setId_Scooter() {
        scooter.setId(22);
        assertEquals(22, scooter.getId());
    }

    @Test
    void getQrCode() {
        assertEquals(55564, scooter.getQrCode());

    }

    @Test
    void setQrCode() {
        scooter.setQrCode(333);
        assertEquals(333, scooter.getQrCode());
    }

    @Test
    void getIsAvailable() {
        assertEquals("true", scooter.getIsAvailable());
    }

    @Test
    void setIsAvailable() {
        scooter.setIsAvailable("abc");
        assertEquals("abc", scooter.getIsAvailable());
    }


    @Test
    void getWeight() {
        assertEquals(450, scooter.getWeight());
    }

    @Test
    void setWeight() {
        scooter.setWeight(111);
        assertEquals(111, scooter.getWeight());
    }

    @Test
    void getBattery() {
        assertEquals(new Battery(1,150), scooter.getBattery());
    }

    @Test
    void setBattery() {
        scooter.setBattery(new Battery(1, 2));
        assertEquals(new Battery(1,2), scooter.getBattery());
    }

    @Test
    void testEquals() {

        Scooter scooter1 = new Scooter(1,2,3,new Battery(4,5),99);

        assertEquals(scooter1,scooter1);
        assertNotEquals(scooter1,"string");
        assertNotEquals(scooter1, null);
        assertNotEquals(scooter1, new Battery(4, 55));

        Scooter scooter2 = new Scooter(1,2,3,new Battery(4,5),99);
        assertEquals(scooter2, scooter1);

        scooter2 = new Scooter(1,2,3,new Battery(4,5),99);
        assertEquals(scooter2, scooter1);

        scooter2 = new Scooter(11,2,3,new Battery(4,5),99);
        assertNotEquals(scooter1, scooter2);

        scooter2 = new Scooter(1,22,3,new Battery(4,5),99);
        assertNotEquals(scooter1, scooter2);

        scooter2 = new Scooter(1,2,33,new Battery(4,5),99);
        assertNotEquals(scooter1, scooter2);

        scooter2 = new Scooter(1,2,3,new Battery(44,5),99);
        assertNotEquals(scooter1, scooter2);

        scooter2 = new Scooter(1,2,3,new Battery(4,55),99);
        assertNotEquals(scooter1, scooter2);

        scooter2 = new Scooter(1,2,3,new Battery(4,5),100);
        assertNotEquals(scooter1, scooter2);

    }

    @Test
    void testHashCode() {

        scooter = new Scooter(1,2,3,new Battery(4,5),99);

        assertEquals(scooter.hashCode(), scooter.hashCode());

        Scooter scooter2 = new Scooter(1,2,3,new Battery(4,5),99);
        assertEquals(scooter.hashCode(), scooter2.hashCode());

        scooter2 = new Scooter(1,2,3,new Battery(4,6),99);
        assertNotEquals(scooter.hashCode(), scooter2.hashCode());

        scooter2 = new Scooter(1,2,3,new Battery(4,5),100);
        assertNotEquals(scooter.hashCode(), scooter2.hashCode());
    }
}