package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    Order order1;
    Order order2 = new Order(23, new ShoppingCart());

    @BeforeEach
    void setUp() {
       order1 = new Order(23, 5, 1, "2018-12-32");
    }

    @Test
    void getShopCart() {
        assertNotNull( order1.getShopCart());
    }

    @Test
    void getIdOrder() {
        assertEquals(23, order1.getIdOrder());
    }

    @Test
    void getDeliveryFee() {
        assertEquals(5, order1.getDeliveryFee());
    }

    @Test
    void getStatus() {
        assertEquals(1, order1.getStatus());
    }

    @Test
    void getDate() {
        assertEquals("2018-12-32", order1.getDate() );
    }
    @Test
    void setShopCart() {
        Product testProd = new Product(1,"vacina", 55.55f, 100);
        ShoppingCart cart = new ShoppingCart();
        cart.addProductToShoppingCart(testProd, 3);
        order1.setShopCart(cart);
        assertNotNull( order1.getShopCart());
        assertEquals(cart, order1.getShopCart());
    }

    @Test
    void setIdOrder() {
        order1.setIdOrder(88);
        assertEquals(88, order1.getIdOrder());
    }

    @Test
    void setDeliveryFee() {
        order1.setDeliveryFee(66);
        assertEquals(66, order1.getDeliveryFee());
    }

    @Test
    void setStatus() {
        order1.setStatus(55);
        assertEquals(55, order1.getStatus());
    }

    @Test
    void setDate() {
        order1.setDate("1994");
        assertEquals("1994", order1.getDate());
    }
    
    @Test
    void testSetAddress(){
        Address expResult = new Address(2,200,-320, "street", "8000", 50);
        order1.setAddress(expResult);
        assertEquals(expResult, order1.getAddress());
    }
    
    @Test
    void testGetAddress(){
        Address expResult = new Address(2,200,-320, "street", "8000", 50);
        order1.setAddress(expResult);
        Address result = order1.getAddress();
        assertEquals(expResult,result);
    }
    
    @Test
    void testGetPharmacyID(){
        int result = order1.getPharmacyId();
        int expResult = 0;
        assertEquals(expResult, result);
    }
    
    @Test
    void testSetPharmacyID(){
        order1.setPharmacyId(25);
        assertEquals(25, order1.getPharmacyId());
    }
    
    @Test
    void testEquals(){
        order1 = new Order(23, 5, 1, "2018-12-32");
        order2 = new Order(23, 5, 1, "2018-12-32");
        assertEquals(order1, order2);
        assertEquals(order1,order2);

        order2 = new Order(233, 5, 1, "2018-12-32");
        assertNotEquals(order1, order2);
        order2 = new Order(23, 55, 1, "2018-12-32");
        assertNotEquals(order1, order2);
        order2 = new Order(23, 5, 11, "2018-12-32");
        assertNotEquals(order1, order2);
        order2 = new Order(23, 5, 1, "2018-12-33");
        assertNotEquals(order1, order2);




        order1 = new Order(23, 5, 1, "2018-12-32");
        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.addProductToShoppingCart(new Product(1, "vacina", 5.5f, 100),5 );
        order1.setShopCart(shoppingCart1);
        order1.setPharmacyId(5);
        order1.setAddress(new Address(1, 2, 3, "rua", "codigo", 20));

        order2 = new Order(23, 5, 1, "2018-12-32");
        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.addProductToShoppingCart(new Product(1, "vacina", 5.5f, 100),5 );
        order2.setShopCart(shoppingCart1);
        order2.setPharmacyId(5);
        order2.setAddress(new Address(1, 2, 3, "rua", "codigo", 20));

        assertEquals(order1, order1);
        assertEquals(order1, order2);

        assertNotEquals(order1, null);
        assertNotEquals(order1, new Product(1, "vacina", 5.5f, 100));

        shoppingCart2.addProductToShoppingCart(new Product(1, "vacina", 5.5f, 100),5);
        order2.setShopCart(shoppingCart2);
        assertNotEquals(order1, order2);
    }
    
    @Test
    void testHashCode() {
        order1 = new Order(23, new ShoppingCart());
        order2 = new Order(23, new ShoppingCart());
        assertEquals(order1.hashCode(), order2.hashCode());

        order2 = new Order(24, new ShoppingCart());
        assertNotEquals(order1.hashCode(), order2.hashCode());

        Product product1 = new Product(1,"vacina", 55.55f, 100);
        ShoppingCart cart = new ShoppingCart();
        cart.addProductToShoppingCart(product1,1);
        order2 = new Order(23, cart);
        assertNotEquals(order1.hashCode(), order2.hashCode());

    }
}