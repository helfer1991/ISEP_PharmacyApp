package lapr.project.controller;

import lapr.project.data.*;
import lapr.project.model.*;
import lapr.project.ui.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;
import static lapr.project.controller.DTOConverter.*;
import static lapr.project.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ManageDeliveryRunControllerTest {

    private PharmaDeliveriesApp appInstance;
    private ManageDeliveryRunController controllerInstance;

    private Pharmacy pharmacy1;
    private PharmacyDTO pharmacyDTO1;
    private Product product1 = new Product(1, "prod1", 4.3f, 5);
    private Deliverable deliverable1 = new Order(1, 3, 2, "1998_12_31");
    private Deliverable deliverable2 = new Order(2, 3, 2, "1998_12_31");
    private Deliverable deliverable3 = new Transfer(pharmacy1, pharmacy1, product1, 5, 44);

    private Address a3 = new Address(3, 41.152325, -8.626058, "Rua Julio Dinis", "4450-116", 72);
    private Address a4 = new Address(4, 41.148171, -8.624156, "Rua Dom Manuel II", "4450-133", 77);
    private Address a12 = new Address(12, 41.155336, -8.613358, "Rua da Lapa", "4450-136", 114);
    private Address a7 = new Address(7, 41.150722, -8.617192, "Rua de Cedofeita", "4450-001", 98);
    private Address a6 = new Address(6, 41.152665, -8.620806, "Rua da Torrinha", "4450-057", 87);
    private Address a10 = new Address(10, 41.156299, -8.619319, "Rua da Boavista", "4450-333", 85);

    @Mock
    private DeliveryDB deliveryDB;
    @Mock
    private CourierDB courierDB;
    @Mock
    private VehicleDB vehicleDB;
    @Mock
    private ClientDB clientDB;
    @Mock
    private OrderDB orderDB;
    @Mock
    private TransferDB transferDB;
    @Mock
    private PharmacyDB pharmacyDB;

    @BeforeEach
    void setUp() {
        appInstance = PharmaDeliveriesApp.getInstance();
        PharmaDeliveriesApp.getInstance().doLogin(TEST_CLIENT_EMAIL, TEST_ADMIN_KEY);
        controllerInstance = new ManageDeliveryRunController();
        appInstance.getServiceDelivery().setDeliveryDB(deliveryDB);
        appInstance.getServiceDelivery().setCourierDB(courierDB);
        appInstance.getServiceDelivery().setVehicleDB(vehicleDB);
        appInstance.getServiceDelivery().setClientDB(clientDB);
        appInstance.getServiceDelivery().setTransferDB(transferDB);
        appInstance.getPharmacyService().setPharmacyDB(pharmacyDB);
        appInstance.getServiceDelivery().setOrderDB(orderDB);
        pharmacy1 = new Pharmacy(
                "pharmacyName", 4,
                new Park(1, 5, 5, 3, 3),
                new Address(2, 200, -320, "street", "8000", 50),
                30.0f, 10.0f);
        pharmacyDTO1 = DTOConverter.convertPharmacy(pharmacy1);
    }

    @Test
    void startDeliveryRunTimer() throws SQLException, InterruptedException {
        List<Deliverable> deliverables = new LinkedList<>();
        deliverables.add(deliverable1);
        deliverables.add(deliverable2);

        when(deliveryDB.getOrdersReadyToDeliverByPharmacy(pharmacy1)).thenReturn(deliverables);
        doNothing().when(orderDB).markOrderAsForcedShipping(any(Order.class));
        when(deliveryDB.getOrderWaitingTime(any(Order.class))).thenReturn(55.5);
        when(transferDB.getTransferWaitingTime(any(Transfer.class))).thenReturn(55.5);
        doNothing().when(transferDB).markTransferAsForcedShipping(any(Transfer.class));

        /*
        controllerInstance.startDeliveryRunTimer();
        sleep(500);


        verify(deliveryDB).getOrdersReadyToDeliverByPharmacy(pharmacy1);
        verify(deliveryDB).markOrderAsForcedShipping(any(Order.class));
        verify(deliveryDB).getOrderWaitingTime(any(Order.class));
        verify(deliveryDB).getTransferWaitingTime(any(Transfer.class));
        verify(deliveryDB).markTransferAsForcedShipping(any(Transfer.class));

         */
    }

    @Test
    void fetchAvailableDeliveryRuns() throws SQLException {
        List<Deliverable> deliverables = new ArrayList<>();
        when(deliveryDB.getOrdersReadyToDeliverByPharmacy(pharmacy1)).thenReturn(deliverables);
        when(deliveryDB.getTransfersReadyByPharmacy(pharmacy1)).thenReturn(deliverables);

        controllerInstance.fetchAvailableDeliveryRuns(pharmacyDTO1);

        verify(deliveryDB).getOrdersReadyToDeliverByPharmacy(pharmacy1);
        verify(deliveryDB).getTransfersReadyByPharmacy(pharmacy1);

    }

//    @Test
//    void estimateDroneDeliveryCosts() {
//        Courier  courier1 = new Courier(55, true);
//        courier1.setNIF(259723444);
//        courier1.setName("Catarina");
//        courier1.setEmail("catarina@isep.ipp.pt");
//        courier1.setPassword("catarina");
//
//        when(courierDB.getAvailableCourierWithMaxWaitingTime(pharmacy1)).thenReturn(courier1);
//
//         DeliveryRun deliveryRun1 = new DeliveryRun(13,"2004");
//         DeliveryRunDTO deliveryRunDTO =  DTOConverter.convertDeliveryRun(deliveryRun1);
//
//        assertThrows(UnsupportedOperationException.class, ()->{
//                controllerInstance.estimateDroneDeliveryCosts(pharmacyDTO1,deliveryRunDTO );
//        });
//
//        verify(courierDB).getAvailableCourierWithMaxWaitingTime(pharmacy1);
//
//    }


    /*
    @Test
    void sortOrderByBestPathTest() {
        LinkedList<Deliverable> orders = new LinkedList<>();
        deliverable1.setAddress(a3);
        deliverable2.setAddress(a12);
        orders.add(deliverable1);
        orders.add(deliverable2);

        LinkedList<Address> bestPath = new LinkedList<>();
        bestPath.add(a7);
        bestPath.add(a12);
        bestPath.add(a10);
        bestPath.add(a4);
        bestPath.add(a3);
        bestPath.add(a7);

        LinkedList<Deliverable> result = controllerInstance.sortOrderByBestPath(bestPath, orders);

        LinkedList<Deliverable> expectedOrders = new LinkedList<>();
        expectedOrders.add(deliverable2);
        expectedOrders.add(deliverable1);

        assertEquals(expectedOrders, result);
    }

//    @Test
//    void getSemiPathTest() {
//        Address start = a12;
//        Address end = a3;
//        LinkedList<Address> bestPath = new LinkedList<>();
//        bestPath.add(a7);
//        bestPath.add(a12);
//        bestPath.add(a10);
//        bestPath.add(a4);
//        bestPath.add(a3);
//        bestPath.add(a7);
//
//        LinkedList<Address> result = controllerInstance.getSemiPath(start, end, bestPath);
//        LinkedList<Address> expResult = new LinkedList<>();
//        expResult.add(a12);
//        expResult.add(a10);
//        expResult.add(a4);
//        expResult.add(a3);
//
//        assertEquals(expResult, result);
//    }

    @Test    
    void getAvailableVehiclesForDelivery() throws SQLException {
       Scooter scooter1 = new Scooter(1,2,3,new Battery(4,5),10);
       Scooter scooter2 = new Scooter(1,2,3,new Battery(4,5),90);
       Drone drone1 = new Drone(1,2,3,new Battery(4, (float) 0.06),10);
       Drone drone2 = new Drone(1,2,3,new Battery(4,(float) 0.06),90);

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(scooter1);
        vehicles.add(scooter2);
        vehicles.add(drone1);
        vehicles.add(drone2);

        when(vehicleDB.getAvailableVehiclesByPharmacy(pharmacy1)).thenReturn(vehicles);

        DeliveryRun deliveryRun1 = new DeliveryRun(13,"2004");
        Estimates estimates1 = new Estimates(deliveryRun1);
        estimates1.setRequiredBatteryToCompletePath(0.01);
//        estimates1.setRequiredBatteryScooterCompletePath(2);
        EstimatesDTO estimatesDTO1 = DTOConverter.convertEstimates(estimates1);

        List<VehicleDTO> expectedResult = new LinkedList<>();
        expectedResult.add(DTOConverter.convertScooter(scooter2));
        expectedResult.add(DTOConverter.convertDrone(drone2));

        List<VehicleDTO> result = controllerInstance.getAvailableVehiclesForDelivery(pharmacyDTO1,estimatesDTO1);

        verify(vehicleDB).getAvailableVehiclesByPharmacy(pharmacy1);
    }


//    @Test
//    void getCostMapTest(){
//        LinkedList<Deliverable> sortedOrders
//        Pharmacy pharmacy = new Pharmacy ();
//        LinkedList<Address> bestPath = 
//    }


//    @Test
//    void getPathNextPharmacyFromPathTest() throws SQLException {
//        LinkedList<Address> bestPath = new LinkedList<>();
//        bestPath.add(a7);
//        bestPath.add(a12);
//        bestPath.add(a10);
//        bestPath.add(a4);
//        bestPath.add(a3);
//        bestPath.add(a7);
//
//        List<Pharmacy> getAllPharmacies = new LinkedList<>();
//        Pharmacy pharmacy2 = new Pharmacy("pharmacyName", 4, new Park(1, 5, 5, 3, 3), a3, 30.0f, 10.0f);
//        Pharmacy pharmacy3 = new Pharmacy("pharmacyName", 5, new Park(1, 5, 5, 3, 3), a7, 30.0f, 10.0f);
//        getAllPharmacies.add(pharmacy2);
//        getAllPharmacies.add(pharmacy3);
//        doReturn(getAllPharmacies).when(pharmacyDB).getAllPharmacies();
//        
//        List<Address> result = controllerInstance.g
//                .getPathNextPharmacyFromPath(bestPath);
//        //fazer de novo
//        List<Address> expResult = bestPath.subList(0, 5);
//        assertEquals(expResult, result);
//
//    }
//    

    @Test
    void getPathNextPharmacyFromPathTest() throws SQLException {
        LinkedList<Address> bestPath = new LinkedList<>();
        bestPath.add(a7);
        bestPath.add(a12);
        bestPath.add(a10);
        bestPath.add(a4);
        bestPath.add(a3);
        bestPath.add(a7);

        List<Pharmacy> getAllPharmacies = new LinkedList<>();
        Pharmacy pharmacy2 = new Pharmacy("pharmacyName", 4, new Park(1, 5, 5, 3, 3), a3, 30.0f, 10.0f);
        Pharmacy pharmacy3 = new Pharmacy("pharmacyName", 5, new Park(1, 5, 5, 3, 3), a7, 30.0f, 10.0f);
        getAllPharmacies.add(pharmacy2);
        getAllPharmacies.add(pharmacy3);
        doReturn(getAllPharmacies).when(pharmacyDB).getAllPharmacies();
        
        List<Address> result = controllerInstance.getPathNextPharmacyFromPath(bestPath);
        //fazer de novo
        List<Address> expResult = bestPath.subList(0, 5);
        assertEquals(expResult, result);

    }
     */

    

//    @Test
//    void startAndRegisterDeliveryRun() throws SQLException {
//
//         LinkedList<Deliverable> deliverables = new LinkedList<>();
//         deliverables.add(deliverable1);
//         deliverables.add(deliverable2);
//         deliverables.add(deliverable3);
//
//        DeliveryRun deliveryRun1 = new DeliveryRun(13,"2004");
//        deliveryRun1.setDeliverables(deliverables);
//
//        Drone drone1 = new Drone(1,2,3,new Battery(4,5),80);
//        LinkedHashMap<Deliverable, Double> costMap1 = new LinkedHashMap<>();
//        costMap1.put(deliverable1, 55.6);
//
//        Client client1 = new Client();
//        client1.setEmail("hmcfb@isep.ipp.pt");
//        client1.setPassword("balelos");
//        client1.setName("Helder");
//        client1.setNIF(12345);
//        client1.setAddress(new Address(1, 123.4, 124.5, "Rua sesamo", "1234", 100));
//        client1.setCreditCard(new CreditCard("12345", 3456, "2020-06-06"));
//
//
//        when(clientDB.getClientByOrderId(anyInt())).thenReturn(client1);
//        when(deliveryDB.markOrderAsInTransit(any(Order.class))).thenReturn(true);
//        when(deliveryDB.insertDeliveries(deliveryRun1,costMap1,drone1)).thenReturn(true);
//
//        controllerInstance.startAndRegisterDeliveryRun(deliveryRun1, costMap1, drone1);
//
//        verify(deliveryDB, times(2)).markOrderAsInTransit(any(Order.class));
//        verify(deliveryDB).insertDeliveries(deliveryRun1,costMap1,drone1);
//
//    }
    @Test
    void getPendentOrders() throws SQLException {

        //when fetching all the deliverables ready, the database return 2 orders and 1 transfer
        List<Deliverable> ordersReady = new LinkedList<>();
        ordersReady.add(deliverable1);
        ordersReady.add(deliverable2);
        when(deliveryDB.getOrdersReadyToDeliverByPharmacy(pharmacy1)).thenReturn(ordersReady);
        List<Deliverable> transfersReady = new LinkedList<>();
        transfersReady.add(deliverable3);
        when(deliveryDB.getTransfersReadyByPharmacy(pharmacy1)).thenReturn(transfersReady);

        //when getting the deliveryRuns available, it returns two deliveryRuns
        //deliveryRun1 has deliverable1
        DeliveryRun deliveryRun1 = new DeliveryRun(13, "2004");
        LinkedList<Deliverable> deliverables = new LinkedList<>();
        deliverables.add(deliverable1);
        deliveryRun1.setDeliverables(deliverables);

        //deliveryRun2 has deliverable3
        DeliveryRun deliveryRun2 = new DeliveryRun(13, "2004");
        deliverables = new LinkedList<>();
        deliverables.add(deliverable3);
        deliveryRun2.setDeliverables(deliverables);

        //converts deliveryRuns and stores them in a list
        DeliveryRunDTO deliveryRunDTO1 = DTOConverter.convertDeliveryRun(deliveryRun1);
        DeliveryRunDTO deliveryRunDTO2 = DTOConverter.convertDeliveryRun(deliveryRun2);
        List<DeliveryRunDTO> deliveryRunsDTO = new LinkedList<>();
        deliveryRunsDTO.add(deliveryRunDTO1);
        deliveryRunsDTO.add(deliveryRunDTO2);

        //stubbs the controller method fetchAvailableDeliveryRuns to return the specified deliveryRuns
        ManageDeliveryRunController spyController = spy(ManageDeliveryRunController.class);
        when(spyController.fetchAvailableDeliveryRuns(pharmacyDTO1)).thenReturn(deliveryRunsDTO);

        //Since the deliverables 1 and 3 are already in the deliveryRuns the only pendent order/deliverable shoulbe be deliverable2
        List<Deliverable> expectedResult = new LinkedList<>();
        expectedResult.add(deliverable2);

        //when(spyController.getPendentOrders(pharmacyDTO1)).thenCallRealMethod();
        //List<Deliverable> result = spyController.getPendentOrders(pharmacyDTO1);
        //List<Deliverable> result = controllerInstance.getPendentOrders(pharmacyDTO1);
        //assertEquals(expectedResult, result);
    }

    @Test
    void setOrderAsDelivered() throws SQLException {
        ProductDTO productDTO1 = convertProduct(product1);

        DeliverableDTO deliverableDTO1 = convertDeliverable(deliverable1);
        DeliverableDTO deliverableDTO2 = convertDeliverable(deliverable2);
        DeliverableDTO deliverableDTO3 = convertDeliverable(deliverable3);

        //if the deliverable its a transfer it's not marked
        controllerInstance.setOrderAsDelivered(deliverableDTO3);
        verifyNoInteractions(deliveryDB);

        when(orderDB.markOrderAsDelivered((Order) deliverable1)).thenReturn(true);
        boolean result = controllerInstance.setOrderAsDelivered(deliverableDTO1);
        assertTrue(result);
        verify(orderDB).markOrderAsDelivered((Order) deliverable1);

        when(orderDB.markOrderAsDelivered((Order) deliverable2)).thenReturn(true);
        result = controllerInstance.setOrderAsDelivered(deliverableDTO2);
        assertTrue(result);
        verify(orderDB).markOrderAsDelivered((Order) deliverable2);

        //if the its nor inserted correctly in the DB the method returns false
        when(orderDB.markOrderAsDelivered((Order) deliverable2)).thenReturn(false);
        result = controllerInstance.setOrderAsDelivered(deliverableDTO2);
        assertFalse(result);

    }

    /*
    @Test
    void parksVehicle() {
        controllerInstance.parksVehicle(
                pharmacyDTO1,
                new DroneDTO(1, 2, 3, new BatteryDTO(4, 5), 80),
                true
        );
    }

     */
}
