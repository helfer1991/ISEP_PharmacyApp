package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    Address add;

    @BeforeEach
    void setUp() {
        add = new Address(3,20,-32, "rua", "2500", 100);
    }

    @Test
    void getId() {
        assertEquals(3, add.getId());
    }

    @Test
    void getLatitude() {
        assertEquals(20, add.getLatitude());
    }

    @Test
    void getLongitude() {
        assertEquals(-32, add.getLongitude());
    }

    @Test
    void getAddress() {
        assertEquals("rua", add.getAddress());
    }

    @Test
    void getZipcode() {
        assertEquals("2500", add.getZipcode());
    }

    @Test
    void setId() {
        add.setId(6);
        assertEquals(6, add.getId());
    }

    @Test
    void setLatitude() {
        add.setLatitude(4);
        assertEquals(4, add.getLatitude());
    }

    @Test
    void setLongitude() {
        add.setLongitude(5000);
        assertEquals(5000, add.getLongitude());
    }

    @Test
    void setAddress() {
        add.setAddress("avenida");
        assertEquals("avenida", add.getAddress());
    }

    @Test
    void setZipcode() {
        add.setZipcode("5000AA");
        assertEquals("5000AA", add.getZipcode());
    }

    @Test
    void testEquals() {
        assertNotEquals(add, null);
        assertNotEquals(add, "street");

        Address a1 = new Address(3,20,-32, "rua", "2500", 100);
        Address a2 = new Address(3,20,-32, "rua", "2500", 100);

        assertEquals(a1, a2);

        a2 = new Address(88,20,-32, "rua", "2500", 100);
        assertNotEquals(a1, a2);

        a2 = new Address(3,55,-32, "rua", "2500", 100);
        assertNotEquals(a1, a2);

        a2 = new Address(3,20,-1, "rua", "2500", 100);
        assertNotEquals(a1, a2);

        a2 = new Address(3,20,-32, "1rua", "2500", 100);
        assertNotEquals(a1, a2);

        a2 = new Address(3,20,-32, "rua", "2520", 100);
        assertNotEquals(a1, a2);

        a2 = new Address(3,20,-32, "rua", "2500", 111);
        assertNotEquals(a1, a2);

    }

    @Test
    void testHashCode() {
        assertTrue(new Address(3,20,-32, "rua", "2500", 100).hashCode()
                == new Address(3,20,-32, "rua", "2500", 100).hashCode());

        assertFalse(new Address(3,20,-32, "rua", "2500",100).hashCode()
                == new Address(4,20,-32, "rua", "2500", 100).hashCode());

        assertFalse(new Address(3,20,-32, "rua", "2500", 100).hashCode()
                == new Address(333,211,-32, "rua", "2500", 100).hashCode());

        assertFalse(new Address(3,20,-32, "rua", "2500", 100).hashCode()
                == new Address(4,20,-1, "rua", "2500", 100).hashCode());

        assertFalse(new Address(3,20,-32, "rua", "2500", 100).hashCode()
                == new Address(4,20,-32, "ruaa", "2500", 100).hashCode());

        assertFalse(new Address(3,20,-32, "rua", "2500", 100).hashCode()
                == new Address(4,20,-32, "ruaa", "2501", 100).hashCode());

    }
}