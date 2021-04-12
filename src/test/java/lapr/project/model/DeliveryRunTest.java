package lapr.project.model;

import java.util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryRunTest {
    

    DeliveryRun deliveryRun1;
    Order order1 = new Order(20, 15, 2, "2000");
    Order order2 = new Order(20, 15, 2, "2011");

    @BeforeEach
    void setUp() {
        deliveryRun1 = new DeliveryRun(2, "2018");
    }

    @Test
    void setId() {
        deliveryRun1.setId(5);
        assertEquals(5, deliveryRun1.getId());
    }

    @Test
    void getDate() {
        assertEquals(2, deliveryRun1.getId());
    }

    @Test
    void setDate() {
        deliveryRun1.setDate("2000");
        assertEquals("2000", deliveryRun1.getDate());
    }


    @Test
    void setOrdersToDelivery() {
        LinkedList<Deliverable> lstOrders = new LinkedList<>();
        lstOrders.add(order1);
        lstOrders.add(order2);


        DeliveryRun deliveryRun1 = new DeliveryRun(1, "2004");
        assertTrue(deliveryRun1.getDeliverables().isEmpty());

        deliveryRun1.setDeliverables(lstOrders);
        assertFalse(deliveryRun1.getDeliverables().isEmpty());

        assertEquals(lstOrders, deliveryRun1.getDeliverables());

        deliveryRun1.setDeliverables(null);
        assertFalse(deliveryRun1.getDeliverables().isEmpty());

    }
    
    @Test
    void testSetEnergyCost(){
        deliveryRun1.setEnergyCost(200);
        assertEquals(200, deliveryRun1.getEnergyCost());
    }
    
    @Test
    void testEquals(){
        boolean result1 = deliveryRun1.equals(deliveryRun1);
        boolean result2 = deliveryRun1.equals(order1);
        DeliveryRun deliveryRun2 = new DeliveryRun(2, "2018");
        boolean result3 = deliveryRun1.equals(deliveryRun2);
        assertEquals(true, result1);
        assertEquals(false, result2);
        assertEquals(true, result3);
    }
    
    @Test
    void testHash(){
        DeliveryRun deliveryRun2 = new DeliveryRun(2, "2018");
        assertEquals(deliveryRun2.hashCode(), deliveryRun1.hashCode());
    }
}
