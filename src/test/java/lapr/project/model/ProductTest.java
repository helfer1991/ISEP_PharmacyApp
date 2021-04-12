package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product prod;


    @BeforeEach
    void setUp() {
        prod = new Product(1, "prod", 4.3f, 5);
    }

    @Test
    void getId() {
        assertEquals(1, prod.getId());
    }

    @Test
    void setId() {
        prod.setId(4);
        assertEquals(4, prod.getId());
    }

    @Test
    void getDescription() {
        assertEquals("prod", prod.getDescription());
    }

    @Test
    void setDescription() {
        prod.setDescription("aaa");
        assertEquals("aaa", prod.getDescription());
    }

    @Test
    void getPrice() {
        assertEquals(4.3, prod.getPrice(),0.0001);
    }

    @Test
    void setPrice() {
        prod.setPrice(23);
        assertEquals(23, prod.getPrice());

    }

    @Test
    void getWeight() {
        assertEquals(5, prod.getWeight());
    }

    @Test
    void setWeight() {
        prod.setWeight(200);
        assertEquals(200, prod.getWeight());
    }

    @Test
    void testHashCode() {
        Product prod2 = new Product(1, "prod", 4.3f, 5);
        assertEquals(prod.hashCode(), prod2.hashCode());
        prod2.setWeight(6);
        assertNotEquals(prod.hashCode(), prod2.hashCode());
    }

    @Test
    void testEquals() {

       Product prod2 = new Product(1, "prod", 4.3f, 5);

       assertEquals(prod, prod);
       assertEquals(prod, prod2);

       assertNotEquals(prod, null);
       assertNotEquals(prod, new Battery(2, 3));
       prod2.setWeight(6);
       assertNotEquals(prod,prod2);

    }
}