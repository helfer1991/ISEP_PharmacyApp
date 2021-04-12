package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
    }

    @Test
    void addProductToShoppingCart() {
        Map<Product, Integer> map =  cart.getProductMap();
        assertTrue(map.isEmpty());

        Product testProd = new Product(1,"vacina", 55.55f, 100);
        map.put(testProd, 3);
        ShoppingCart cart1 = new ShoppingCart(map);
        assertEquals(map, cart1.getProductMap());

        cart.addProductToShoppingCart(testProd, 4);
        map = cart.getProductMap();

        assertFalse(map.isEmpty());
        assertEquals(map.get(testProd), 7);

        cart.addProductToShoppingCart(null, 5);
        cart.addProductToShoppingCart(testProd, 0);

        assertEquals(map.get(testProd), 7);
    }

    @Test
    void getProductMap() {
        Map<Product, Integer> map =  cart.getProductMap();
        assertTrue(map.isEmpty());

        Product testProd = new Product(2,"gelo", 5.33f, 400);
        cart.addProductToShoppingCart(testProd, 5);
        map = cart.getProductMap();

        assertFalse(map.isEmpty());
        assertEquals(map.get(testProd), 5);
    }

    @Test
    void getShoppingCartWeight() {

        Map<Product, Integer> map =  cart.getProductMap();
        assertTrue(map.isEmpty());
        float result;
        float expectedResult;
        //emppty
        assertEquals(0,cart.getShoppingCartWeight(), 0.0001);

        //one product
        Product testProd = new Product(2,"gelo", 5.33f, 400);
        cart.addProductToShoppingCart(testProd, 5);
        result = cart.getShoppingCartWeight();
        expectedResult = 5*400;
        assertEquals(expectedResult,result,0.001);

        //two products
        Product testProd2 = new Product(1,"vacina", 5.33f, 100);
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
        Product testProd = new Product(2,"gelo", 5.33f, 400);
        cart.addProductToShoppingCart(testProd, 5);
        expectedResult = 26.65;
        assertEquals(expectedResult,cart.getTotalCostShoppingCart(), 0.0001);
        //Test total price with 2 Product
        Product testProd2 = new Product(1,"vacina", 5.33f, 100);
        cart.addProductToShoppingCart(testProd2, 2);
        expectedResult = expectedResult + 10.66;
        assertEquals(expectedResult,cart.getTotalCostShoppingCart(), 0.0001);
    }

    @Test
    void testHashCode() {
        Product product1 = new Product(1,"vacina", 55.55f, 100);

        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.addProductToShoppingCart(product1,3);

        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.addProductToShoppingCart(product1,3);

        assertEquals(shoppingCart1.hashCode(), shoppingCart2.hashCode());

        Product product2 = new Product(2, "vacina2",55.55f, 100);
        shoppingCart1.addProductToShoppingCart(product1, 1);
        shoppingCart2.addProductToShoppingCart(product2, 1);
        assertNotEquals(shoppingCart1.hashCode(), shoppingCart2.hashCode());

    }

    @Test
    void testEquals(){
        Product product1 = new Product(1,"vacina", 55.55f, 100);

        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.addProductToShoppingCart(product1,3);

        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.addProductToShoppingCart(product1,3);


        assertNotEquals(shoppingCart1, null);
        assertNotEquals(shoppingCart1, "String");
        assertEquals(shoppingCart1, shoppingCart1);
        assertEquals(shoppingCart1, shoppingCart2);

        shoppingCart2.addProductToShoppingCart(product1,5);
        assertNotEquals(shoppingCart1, shoppingCart2);


    }
}