package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {


    private Pharmacy pharmacy1;
    private Pharmacy pharmacy2;
    private Product product1;
    private Product product2;
    Transfer transfer1;
    Transfer transfer2;

    @BeforeEach
    void setUp() {
        product1 = new Product(7, "vacina77", 5.5f, 60.7f);
        product2 = new Product(8, "vacina88", 5.58f, 60.8f);

        pharmacy1 = new Pharmacy("nome",
                1, new Park(1, 50,50,30,30),
                new Address(1,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);
        pharmacy2 = new Pharmacy("nome2",
                2, new Park(2, 50,50,30,30),
                new Address(2,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);

        transfer1 = new Transfer(pharmacy1,pharmacy2, product1, 5, 44 );
        transfer2 = new Transfer(pharmacy2,pharmacy1, product1, 5, 44 );
    }

    @Test
    void getPharmacyAsking() {
        assertEquals(pharmacy1, transfer1.getPharmacyAsking());
    }

    @Test
    void getPharmacySending() {
        assertEquals(pharmacy2, transfer1.getPharmacySending());
    }

    @Test
    void getProduct() {
        assertEquals(product1, transfer1.getProduct());
    }

    @Test
    void getQuantity() {
        assertEquals(5, transfer1.getQuantity());
    }

    @Test
    void getOrderId() {
        assertEquals(44, transfer1.getOrderId());
    }

    @Test
    void testEquals() {
        assertEquals(transfer1, transfer1);
        assertNotEquals(transfer2, transfer1);
        assertNotEquals(transfer2, "string");

        transfer2 = new Transfer(pharmacy1,pharmacy2, product1, 5, 44 );
        assertEquals(transfer2, transfer1);

        transfer2 = new Transfer(pharmacy2,pharmacy1, product1, 5, 44 );
        assertNotEquals(transfer2, transfer1);

        transfer2 = new Transfer(pharmacy1,pharmacy1, product1, 5, 44 );
        assertNotEquals(transfer2, transfer1);

        transfer2 = new Transfer(pharmacy1,pharmacy2, product2, 5, 44 );
        assertNotEquals(transfer2, transfer1);

        transfer2 = new Transfer(pharmacy1,pharmacy2, product1, 55, 44 );
        assertNotEquals(transfer2, transfer1);

        transfer2 = new Transfer(pharmacy1,pharmacy2, product1, 5, 444 );
        assertNotEquals(transfer2, transfer1);

        transfer2 = new Transfer(pharmacy1,pharmacy2, product1, 5, 44 );
        assertEquals(transfer2, transfer1);

    }

    @Test
    void testHashCode() {
        assertEquals(transfer1.hashCode(), transfer1.hashCode());
        assertNotEquals(transfer2.hashCode(), transfer1.hashCode());
        //same arguments as transfer2
        transfer1 = new Transfer(pharmacy2, pharmacy1, product1, 5, 44);
        assertEquals(transfer2.hashCode(), transfer1.hashCode());
    }
}