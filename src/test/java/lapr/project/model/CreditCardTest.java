package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardTest {

    CreditCard cc;
    CreditCard cc3;
    //Drone drone;

    @BeforeEach
    void setUp() {
        cc = new CreditCard("111222333", 345, "2020");
        //drone = new Drone(1, 2, 3, 4, 5,99);
    }

    @Test
    void getNumber() {
        assertEquals("111222333", cc.getNumber());
    }

    @Test
    void setNumber() {
        cc.setNumber("3");
        assertEquals("3", cc.getNumber());
    }

    @Test
    void getCcv() {
        assertEquals(345, cc.getCcv());
    }

    @Test
    void setCcv() {
        cc.setCcv(666);
        assertEquals(666, cc.getCcv());
    }

    @Test
    void getValidThru() {
        assertEquals("2020", cc.getValidThru());
    }


    @Test
    void testEquals() {
        CreditCard  cc1 = new CreditCard("111222333", 345, "2020");
        CreditCard cc2 = new CreditCard("111222333", 345, "2020");
        assertEquals(cc1, cc1);
        assertEquals(cc2, cc1);


        cc2 = new CreditCard("11", 345, "2020");
        assertNotEquals(cc2, cc1);

        cc2 = new CreditCard("111222333", 3, "2020");
        assertNotEquals(cc2, cc1);

        cc2 = new CreditCard("111222333", 345, "20");
        assertNotEquals(cc2, cc1);

        assertNotEquals(cc1, new String(""));
        assertNotEquals(cc1, null);
    }

    @Test
    void testHashCode() {
        CreditCard  cc1 = new CreditCard("111222333", 345, "2020");
        CreditCard cc2 = new CreditCard("111222333", 345, "2020");
        assertEquals(cc1.hashCode(), cc2.hashCode());

        cc2 = new CreditCard("11", 345, "2020");
        assertNotEquals(cc1.hashCode(), cc2.hashCode());

        cc2 = new CreditCard("111222333", 3, "2020");
        assertNotEquals(cc1.hashCode(), cc2.hashCode());

        cc2 = new CreditCard("111222333", 345, "20");
        assertNotEquals(cc1.hashCode(), cc2.hashCode());

    }

    @Test
    void testToString() {
        assertEquals(cc.toString(), cc.toString());
    }
}