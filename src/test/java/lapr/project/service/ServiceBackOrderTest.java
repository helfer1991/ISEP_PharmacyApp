package lapr.project.service;

import lapr.project.data.OrderDB;
import lapr.project.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceBackOrderTest {

    private Pharmacy pharmacy1;
    private ShoppingCart shoppingCart1;
    private Product product1;


    @Mock
    OrderDB orderDB;

    ServiceBackOrder serviceBackOrder;

    @BeforeEach
    void setUp() {
        serviceBackOrder = new ServiceBackOrder();
        serviceBackOrder.setOrderDB(orderDB);


        pharmacy1 = new Pharmacy("nome", 555,
                new Park(11, 50,50,30,30),
                new Address(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);

        shoppingCart1 = new ShoppingCart();
        product1 = new Product(1, "prod1", 4.3f, 5);
        shoppingCart1.addProductToShoppingCart(product1,5);
    }
//
//    @Test
//    void getNextOrderId() {
//        when(orderDB.getNextOrderId()).thenReturn(5);
//
//        assertEquals(5, serviceBackOrder.getNextOrderId());
////    }
//
//    @Test
//    void createOrder() {
//        Pharmacy pharmacy1 = new Pharmacy("nome", 555,
//                new Park(11, 50,50,30,30),
//                new Address(3,20,-32, "rua", "2500", 100),
//                33.2f, 15.5f);
//        ShoppingCart shoppingCart1 = new ShoppingCart();
//        Product product1 = new Product(1, "prod1", 4.3f, 5);
//        shoppingCart1.addProductToShoppingCart(product1,5);
//
//        Order result = serviceBackOrder.createOrder(pharmacy1,shoppingCart1,789);
//        assertEquals(shoppingCart1, result.getShopCart());
//        assertEquals(pharmacy1.getId(), result.getPharmacyId());
//        assertEquals(789, result.getIdOrder());
//        assertNotNull(result.getDate());
//        assertFalse(result.getDate().isEmpty());
//
//    }
//
//    @Test
//    void insertOrder() throws SQLException {
//        Order order1 = new Order(2, 3, 2, "1998_12_31");
//       Client client1 = new Client(); 
//        client1.setEmail("hmcfb@isep.ipp.pt");
//        client1.setPassword("balelos");
//        client1.setName("Helder");
//        client1.setNIF(12345);
//        client1.setAddress(new Address(1, 123.4, 124.5, "Rua sesamo", "1234", 100));
//        client1.setCreditCard(new CreditCard("12345", 3456, "2020-06-06"));
//        when(orderDB.insertOrder(pharmacy1, order1, client1)).thenReturn(order1);
//        assertTrue(serviceBackOrder.insertOrder(pharmacy1, order1,client1));
//    }
}