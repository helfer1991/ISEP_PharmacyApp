package lapr.project.service;

import lapr.project.data.DeliveryDB;
import lapr.project.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceDeliveryRunTest {
    /*

    @Mock
    private DeliveryDB deliveryDB;
    private ServiceDelivery srvcDelivery;

    private Pharmacy pharmacy1;
    private Scooter scooter1;
    private Courier courier1;
    private Map<Product, Integer> map1;
    private Product product1;
    private Product product2;
    private ShoppingCart cart1;
    private Order order1;
    private List<Order> lstOrder1;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());

        srvcDelivery = new ServiceDelivery();
        srvcDelivery.setDeliveryDB(deliveryDB);

        pharmacy1 = new Pharmacy(
                "pharmacyName", 4,
                new Park(1, 5,5,3,3),
                new Address(2,200,-320, "street", "8000", 50),
                30.0f, 10.0f);

        scooter1 = new Scooter(1, 222, 400, 3, 190);

        courier1 = new Courier(55, true, 259723444, "Catarina", "catarina@isep.ipp.pt", "catarina");

        map1 =  new HashMap<>();
        product1 = new Product(2,"gelo", 5.33f, 400);
        product2 = new Product(1,"vacina", 5.33f, 100);
        map1.put(product1, 3);
        map1.put(product2, 6);
        cart1 = new ShoppingCart(map1);
        order1 = new Order(1333, cart1);
        lstOrder1 = new ArrayList<>();
        lstOrder1.add(order1);
        lstOrder1.add(order1);
    }

//    @Test
//    void getCourierToPerformARun() throws SQLException {
//        when(deliveryDB.getCourierToPerformARunByFarmacy(pharmacy1)).thenReturn(courier1);
//
//        Courier result = srvcDelivery.getCourierToPerformARun(pharmacy1);
//        assertEquals(courier1, result);
//    }

//    @Test
//    void getAvailableScootersByFarmacy() throws SQLException {
//
//        List<Scooter> expectedResult = new ArrayList<>();
//        expectedResult.add(scooter1);
//        expectedResult.add(scooter1);
//
//        when(deliveryDB.getAvailableScootersByDelivery(pharmacy1)).thenReturn(expectedResult);
//
//        Iterable<Scooter> result = srvcDelivery.getAvailableScootersByFarmacy(pharmacy1);
//        assertEquals(expectedResult,result);
//
//        verify(deliveryDB).getAvailableScootersByFarmacy(pharmacy1);
//
//    }

    @Test
    void getOrdersToDeliveryByPharmacy() throws SQLException {

        when(deliveryDB.getOrdersReadyToDeliverByPharmacy(pharmacy1)).thenReturn(lstOrder1);

        Iterable<Order> result = srvcDelivery.getOrdersToDeliverByPharmacy(pharmacy1);
        assertEquals(lstOrder1,result);

        verify(deliveryDB).getOrdersReadyToDeliverByPharmacy(pharmacy1);
    }

    @Test
    void getOrdersTODelivery() throws SQLException {

        when(deliveryDB.getOrdersReadyToDeliverByPharmacy(any(Pharmacy.class))).thenReturn(lstOrder1);

        Iterable<Order> result = srvcDelivery.getOrdersToDeliverByPharmacy(pharmacy1);
        assertEquals(lstOrder1,result);

        verify(deliveryDB).getOrdersReadyToDeliverByPharmacy(pharmacy1);
    }

    @Test
    void insertDeliveries() throws SQLException {

        when(deliveryDB.insertDeliveries(lstOrder1)).thenReturn(true);
        boolean result = srvcDelivery.insertDeliveryRuns(lstOrder1);
        assertTrue(result);

        when(deliveryDB.insertDeliveries(lstOrder1)).thenReturn(false);
        result = srvcDelivery.insertDeliveryRuns(lstOrder1);
        assertFalse(result);

        verify(deliveryDB, times(2)).insertDeliveries(lstOrder1);

    }
/*
    @Test
    void checkMinWeightForDelivery() throws SQLException {
        Iterable<Order> result = srvcDelivery.checkMinWeightForDelivery(pharmacy1, order1);
        //todo

    }

    @Test
    void createDeliverHourly() {

    }

    @Test
    void startTimer() {
    }

 */


}