package lapr.project.dto;

import lapr.project.model.*;
import lapr.project.ui.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {


    @BeforeEach
    void setUp() {

    }

    @Test
    void getId() {
        ProductDTO prod = new ProductDTO(1, "prod", 4, 5);
        assertEquals(1, prod.getId());
    }

    @Test
    void setId() {
        ProductDTO prod = new ProductDTO(1, "prod", 4, 5);
        prod.setId(4);
        assertEquals(4, prod.getId());
    }

    @Test
    void getDescription() {
        ProductDTO prod = new ProductDTO(1, "prod", 4, 5);
        assertEquals("prod", prod.getDescription());
    }

    @Test
    void setDescription() {
        ProductDTO prod = new ProductDTO(1, "prod", 4, 5);
        prod.setDescription("aaa");
        assertEquals("aaa", prod.getDescription());
    }

    @Test
    void getPrice() {
        ProductDTO prod = new ProductDTO(1, "prod", 4.3f, 5);
        assertEquals(4.3, prod.getPrice(),0.0001);
    }

    @Test
    void setPrice() {
        ProductDTO prod = new ProductDTO(1, "prod", 4, 5);
        prod.setPrice(23);
        assertEquals(23, prod.getPrice());

    }

    @Test
    void getWeight() {
        ProductDTO prod = new ProductDTO(1, "prod", 4, 5);
        assertEquals(5, prod.getWeight());
    }

    @Test
    void setWeight() {
        ProductDTO prod = new ProductDTO(1, "prod", 4, 5);
        prod.setWeight(200);
        assertEquals(200, prod.getWeight());
    }
}