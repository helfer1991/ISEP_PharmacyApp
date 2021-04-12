package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PharmacyTest {


    Pharmacy pharmacy;

    @BeforeEach
    void setUp() {
      pharmacy = new Pharmacy("nome",
        555, new Park(11, 50,50,30,30),
              new Address(3,20,-32, "rua", "2500", 100),
              33.2f, 15.5f);
        }

    @Test
    void getName() {
        assertEquals("nome", pharmacy.getName());
    }

    @Test
    void getId() {
        assertEquals(555, pharmacy.getId());
    }

    @Test
    void getPark() {
        assertEquals(new Park(11, 50,50,30,30),pharmacy.getPark() );
    }

    @Test
    void getAddress() {
        assertEquals(new Address(3,20,-32, "rua", "2500", 100),pharmacy.getAddress());
    }

    @Test
    void getMaximumPayloadCourier() {
        assertEquals(33.2f, pharmacy.getMaximumPayloadCourier(), 0.0001);
    }

    @Test
    void getMinimumLoadCourier() {
        assertEquals(15.5f, pharmacy.getMinimumLoadCourier(), 0.0001);
    }

    @Test
    void setName() {
        pharmacy.setName("aaa");
        assertEquals("aaa", pharmacy.getName());
    }

    @Test
    void setId() {
        pharmacy.setId(9);
        assertEquals(9, pharmacy.getId());
    }

    @Test
    void setPark() {
        pharmacy.setPark(new Park(2, 2,2,3,3));
        assertEquals(new Park(2, 2,2,3,3), pharmacy.getPark());
    }

    @Test
    void setAddress() {
        pharmacy.setAddress(new Address(666,2055,-3255, "rua 45", "2100", 100));
        assertEquals(new Address(666,2055,-3255, "rua 45", "2100", 100), pharmacy.getAddress());
    }

    @Test
    void setMaximumPayloadCourier() {
        pharmacy.setMaximumPayloadCourier(2.2f);
        assertEquals(2.2f, pharmacy.getMaximumPayloadCourier());
    }

    @Test
    void setMinimumLoadCourier() {
        pharmacy.setMinimumLoadCourier(1.1f);
        assertEquals(1.1f, pharmacy.getMinimumLoadCourier());
    }
    
    @Test
    void testEquals(){

        Pharmacy pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 50,50,30,30),
                new Address(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);

        assertEquals(pharmacy, pharmacy);
        assertEquals(pharmacy, pharmacy2);

        assertNotEquals(pharmacy, new Battery(1,2));
        assertNotEquals(pharmacy, null);

        pharmacy2 = new Pharmacy("nomee", 555,
                new Park(11, 50,50,30,30),
                new Address(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",5556,
                new Park(11, 50,50,30,30),
                new Address(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(111, 50,50,30,30),
                new Address(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 500,50,30,30),
                new Address(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 50,500,30,30),
                new Address(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 50,50,300,30),
                new Address(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 50,50,30,300),
                new Address(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 50,50,30,30),
                new Address(33,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 50,50,30,30),
                new Address(3,200,-32, "rua", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 50,50,30,30),
                new Address(3,20,-320, "rua", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 50,50,30,30),
                new Address(3,20,-32, "ruaa", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 50,50,30,30),
                new Address(3,20,-32, "ruaa", "2500", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 50,50,30,30),
                new Address(3,20,-32, "ruaa", "25005", 100),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);

        pharmacy2 = new Pharmacy("nome",555,
                new Park(11, 50,50,30,30),
                new Address(3,20,-32, "ruaa", "2500", 1001),
                33.2f, 15.5f);
        assertNotEquals(pharmacy, pharmacy2);


    }

    @Test
    void testHashCode(){

        Pharmacy pharmacy2 = new Pharmacy("nome",
                555, new Park(11, 50,50,30,30),
                new Address(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);

        assertEquals(pharmacy.hashCode(), pharmacy.hashCode());
        assertEquals(pharmacy.hashCode(), pharmacy2.hashCode());

        pharmacy2.getAddress().setZipcode("2000");
        assertNotEquals(pharmacy.hashCode(),pharmacy2.hashCode());
    }
}