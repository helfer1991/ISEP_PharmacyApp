package lapr.project.dto;

import lapr.project.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import lapr.project.ui.dto.ProductDTO;
import lapr.project.ui.dto.ShoppingCartDTO;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartDTOTest {

    private ShoppingCartDTO cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCartDTO();
    }


    @Test
    void getProductMap() {
        Map<ProductDTO, Integer> map =  cart.getProductMap();
        assertTrue(map.isEmpty());

        ProductDTO testProd = new ProductDTO(2,"gelo", 5.33f, 400);
        cart.addProductToShoppingCart(testProd, 5);
        map = cart.getProductMap();

        assertFalse(map.isEmpty());
        assertEquals(map.get(testProd), 5);
    }

    @Test
    void getShoppingCartWeight() {

        Map<ProductDTO, Integer> map =  cart.getProductMap();
        assertTrue(map.isEmpty());
        float result;
        float expectedResult;
        //emppty
        assertEquals(0,cart.getShoppingCartWeight(), 0.0001);

        //one product
        ProductDTO testProd = new ProductDTO(2,"gelo", 5.33f, 400);
        cart.addProductToShoppingCart(testProd, 5);
        result = cart.getShoppingCartWeight();
        expectedResult = 5*400;
        assertEquals(expectedResult,result,0.001);

        //two products
        ProductDTO testProd2 = new ProductDTO(1,"vacina", 5.33f, 100);
        cart.addProductToShoppingCart(testProd2, 2);
        expectedResult = 5*400 + 2*100;
        result = cart.getShoppingCartWeight();
        assertEquals(expectedResult,result,0.001);

    }
    
    @Test
    void testGetgetTotalCostShoppingCart(){
        System.out.println("Test - GetgetTotalCostShoppingCart");
        //Test with no products in the shopping cart
        double expectedResult = 0;
        assertEquals(expectedResult,cart.getTotalCostShoppingCart(), 0.0001);

        //Test total price with 1 Product
        ProductDTO testProd = new ProductDTO(2,"gelo", 5.33f, 400);
        cart.addProductToShoppingCart(testProd, 5);
        expectedResult = 26.65;
        assertEquals(expectedResult,cart.getTotalCostShoppingCart(), 0.0001);
        //Test total price with 2 Product
        ProductDTO testProd2 = new ProductDTO(1,"vacina", 5.33f, 100);
        cart.addProductToShoppingCart(testProd2, 2);
        expectedResult = expectedResult + 10.66;
        assertEquals(expectedResult,cart.getTotalCostShoppingCart(), 0.0001);
    }

    @Test
    void testHashCode() {
        Product product1 = new Product(1,"vacina", 55.55f, 100);

        Map<Product, Integer> map1 =  new HashMap<>();
        map1.put(product1,1);
        Map<Product, Integer> map2 =  new HashMap<>();
        map2.put(product1,1);
        assertEquals(map1.hashCode(), map2.hashCode());

        Product product2 = new Product(2, "vacina2",55.55f, 100);
        map1.put(product1,1);
        map2.put(product2,1);
        assertNotEquals(map1.hashCode(), map2.hashCode());

    }
}