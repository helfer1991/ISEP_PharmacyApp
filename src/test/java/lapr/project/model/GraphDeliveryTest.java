/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import lapr.project.data.AddressDB;
import lapr.project.data.RoadDB;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 *
 * @author catarinaserrano
 */
public class GraphDeliveryTest {

    GraphDelivery instance = new GraphDelivery();
    GraphDelivery instance1 = new GraphDelivery();
    GraphDelivery graphScooterDistance = new GraphDelivery();
    GraphDelivery graphDroneDistance = new GraphDelivery();
    GraphDelivery graphScooterEnergy = new GraphDelivery();
    GraphDelivery graphDroneEnergy = new GraphDelivery();

    AddressDB addressDb = Mockito.mock(AddressDB.class);
    RoadDB roadDb = Mockito.mock(RoadDB.class);
    LinkedList<Address> expResultAddressDB = new LinkedList<>();
    Address a3 = new Address(3, 41.152325, -8.626058, "Rua Julio Dinis", "4450-116", 72);
    Address a4 = new Address(4, 41.148171, -8.624156, "Rua Dom Manuel II", "4450-133", 77);
    Address a12 = new Address(12, 41.155336, -8.613358, "Rua da Lapa", "4450-136", 114);
    Address a7 = new Address(7, 41.150722, -8.617192, "Rua de Cedofeita", "4450-001", 98);
    Address a6 = new Address(6, 41.152665, -8.620806, "Rua da Torrinha", "4450-057", 87);
    Address a10 = new Address(10, 41.156299, -8.619319, "Rua da Boavista", "4450-333", 85);
    LinkedList<Road> expResultRoadDB = new LinkedList<>();
    Road r1 = new Road(1, a3, a4);
    Road r2 = new Road(2, a4, a6);
    Road r3 = new Road(3, a6, a3);
    Road r4 = new Road(4, a6, a10);
    Road r5 = new Road(5, a10, a12);
    Road r6 = new Road(6, a12, a10);
    Road r7 = new Road(7, a10, a7);
    Road r8 = new Road(8, a7, a6);
    Road r9 = new Road(9, a6, a7);

    LinkedList<Road> expResultAirRoadRestrictionDB = new LinkedList<>();

    public GraphDeliveryTest() throws SQLException {

        instance.setAddressDb(addressDb);
        instance.setRoadDb(roadDb);
        instance1.setAddressDb(addressDb);
        instance1.setRoadDb(roadDb);
        graphScooterDistance.setAddressDb(addressDb);
        graphScooterDistance.setRoadDb(roadDb);
        graphScooterEnergy.setAddressDb(addressDb);
        graphScooterEnergy.setRoadDb(roadDb);
        graphDroneDistance.setAddressDb(addressDb);
        graphDroneDistance.setRoadDb(roadDb);
        graphDroneEnergy.setAddressDb(addressDb);
        graphDroneEnergy.setRoadDb(roadDb);
        
        expResultAddressDB.add(a3);
        expResultAddressDB.add(a4);
        expResultAddressDB.add(a6);
        expResultAddressDB.add(a7);
        expResultAddressDB.add(a10);
        expResultAddressDB.add(a12);
        when(addressDb.getAllAddresses()).thenReturn(expResultAddressDB);
        expResultRoadDB.add(r1);
        expResultRoadDB.add(r2);
        expResultRoadDB.add(r3);
        expResultRoadDB.add(r4);
        expResultRoadDB.add(r5);
        expResultRoadDB.add(r6);
        expResultRoadDB.add(r7);
        expResultRoadDB.add(r8);
        expResultRoadDB.add(r9);
        when(roadDb.getTerrestrialRestrictions()).thenReturn(expResultRoadDB);
        expResultAirRoadRestrictionDB.add(r1);
        expResultAirRoadRestrictionDB.add(r2);
        expResultAirRoadRestrictionDB.add(r6);
        when(roadDb.getAirRestricitons()).thenReturn(expResultAirRoadRestrictionDB);
        expResultAirRoadRestrictionDB.add(r5);
        expResultAirRoadRestrictionDB.add(r1);

    }

    /**
     * Test of createGraph method, of class GraphDelivery.
     */
    @Test
    public void testCreateGraph() {

        instance.createGraph();
        assertEquals(6, instance.getGraph().numVertices());
        assertEquals(30, instance.getGraph().numEdges());

    }

    /**
     * Test of loadAirRestrictions method, of class GraphDelivery.
     */
    @Test
    public void testLoadAirRestrictions() {
        instance.createGraph();
        boolean result = instance.loadAirRestrictions();

        assertSame(true, result);
    }

    /**
     * Test of loadTerrestrialRestrictions method, of class GraphDelivery.
     */
    @Test
    public void testLoadTerrestrialRestrictions() {
        instance.createGraph();
        boolean result = instance.loadTerrestrialRestrictions();

        assertSame(true, result);
    }

    @Test
    public void testloadDistanceIntoEdges() {
        graphScooterDistance.createGraph();
        graphScooterDistance.loadTerrestrialRestrictions();

        graphDroneDistance.createGraph();
        graphDroneDistance.loadAirRestrictions();

        Vehicle drone = new Drone(0, 0, 0, new Battery(0, 0), 0);
        Vehicle scooter = new Scooter(0, 0, 0, new Battery(0, 0), 0);
        Order order1 = new Order(20, 15, 2, "2000");
        Order order2 = new Order(20, 15, 2, "2011");
        DeliveryRun deliveryRun = new DeliveryRun(2, "2018");
        LinkedList<Deliverable> ordersToDelivery = new LinkedList<>();
        ordersToDelivery.add(order1);
        ordersToDelivery.add(order2);
        deliveryRun.setDeliverables(ordersToDelivery);
        graphDroneDistance.loadDistanceIntoEdges(drone, deliveryRun);
        double distance1 = graphDroneDistance.getGraph().getEdge(a4, a6).getWeight();
        graphScooterDistance.loadDistanceIntoEdges(scooter, deliveryRun);
        double distance2 = graphScooterDistance.getGraph().getEdge(a4, a6).getWeight();
        assertEquals(573.04, distance1, 00.1);
        assertEquals(573.04, distance2, 0.01);
    }
    
    @Test
    public void testloadDistanceIntoEdges2() {
        instance1.createGraph();
        instance1.loadTerrestrialRestrictions();
        instance1.loadDistanceIntoEdges();
        double distance1 = instance1.getGraph().getEdge(a4, a6).getWeight();
        double distance2 = instance1.getGraph().getEdge(a10, a12).getWeight();
        assertEquals(573.04, distance1, 00.1);
        assertEquals(510.42, distance2, 0.01);
    }
    
    @Test
    public void testloadEnergyIntoEdges() {
        graphScooterEnergy.createGraph();
        graphScooterEnergy.loadTerrestrialRestrictions();

        graphDroneEnergy.createGraph();
        graphDroneEnergy.loadAirRestrictions();

        Vehicle drone = new Drone(0, 0, 0, new Battery(0, 0), 0);
        Vehicle scooter = new Scooter(0, 0, 0, new Battery(0, 0), 0);
        Order order1 = new Order(20, 15, 2, "2000");
        Order order2 = new Order(20, 15, 2, "2011");
        DeliveryRun deliveryRun = new DeliveryRun(2, "2018");
        LinkedList<Deliverable> ordersToDelivery = new LinkedList<>();
        ordersToDelivery.add(order1);
        ordersToDelivery.add(order2);
        deliveryRun.setDeliverables(ordersToDelivery);
        graphDroneEnergy.loadEnergyIntoEdges(drone, 4, deliveryRun);
        double energy1 = graphDroneEnergy.getGraph().getEdge(a4, a6).getWeight();
        graphScooterEnergy.loadEnergyIntoEdges(scooter, 4, deliveryRun);
        double energy2 = graphScooterEnergy.getGraph().getEdge(a4, a6).getWeight();
        assertEquals(0.0181, energy1, 00.1);
        assertEquals(0.1081, energy2, 0.01);

    }

    /**
     * Test of mostEfficientPath method, of class GraphDelivery.
     */
    @Test
    public void testMostEfficientPath() {
        graphScooterEnergy.createGraph();
        graphScooterEnergy.loadTerrestrialRestrictions();

        List<Address> stopAddresses = new LinkedList<Address>();
        stopAddresses.add(a4);
        stopAddresses.add(a7);
        
        LinkedList<Address> expectedFinalPath = new LinkedList<Address>();
        expectedFinalPath.add(a3);
        expectedFinalPath.add(a4);
        expectedFinalPath.add(a7);
        expectedFinalPath.add(a3);

       
        LinkedList<Address> finalPath = graphScooterEnergy.getBestPath(a3, a3, stopAddresses);
        assertEquals(expectedFinalPath, finalPath);
    }
    
    /**
     * Test of setAddressDb method, of class GraphDelivery.
     */
    @Test
    public void testSetAddressDb() {
        System.out.println("test - setAddressDB to AddressGraph");
        AddressDB address = null;
        instance.setAddressDb(address);
        assertEquals(instance.addressDb, null);
        address = new AddressDB();
        instance.setAddressDb(address);
        assertEquals(instance.addressDb, address);
    }

    /**
     * Test of setRoadDb method, of class GraphDelivery.
     */
    @Test
    public void testSetRoadDb() {
        System.out.println("test - setRoadDb to AddressGraph");
        RoadDB road = null;
        instance.setRoadDb(road);
        assertEquals(instance.roadDb, null);
        road = new RoadDB();
        instance.setRoadDb(road);
        assertEquals(instance.roadDb, road);
    }
    /*
    @Test
    void mostEfficientPathWithCharging() {
        GraphDelivery graph = PharmaDeliveriesApp.getInstance().getTerrestrialGraph();
        Address a1 = new Address(3,41.1604978,-8.6437290,"Farmacia Avenida","4000-000",0);
        Address a2 = new Address(31,41.1724915,-8.6549371,"Pereiro PostOffice","4000-000",0);
        Address a3 = new Address(16,41.1845917,-8.6821122,"centro comercial parque","4000-000", 0);
        Address a4 = new Address(15,41.1823475,-8.6922944,"Centro Comercial NewCity","4000-000",0);


        LinkedList<Address> pathWithNoEnds = new LinkedList<>();
        pathWithNoEnds.add(a2);
        pathWithNoEnds.add(a3);
        pathWithNoEnds.add(a4);

        Scooter scooter1 = new Scooter(13, 55564, 450, new Battery(1, 150), 75);
        DeliveryRun deliveryRun1 = new DeliveryRun(13, "2004");


        graph.loadEnergyIntoEdges(scooter1, (float) 85, (float) 90, WindVelocity, WindAngle, deliveryRun1);
        LinkedList<Address> recommendedPath = graph.mostEfficientPath(a1, a1, pathWithNoEnds);
        recommendedPath.forEach(System.out::println);
        System.out.println("Energia do path: "+ graph.getWeightFromPath(recommendedPath));
        System.out.println();

        graph.mostEfficientPathWithCharging(recommendedPath, new LinkedList<>(), new LinkedList<>());
        recommendedPath.forEach(System.out::println);
        System.out.println("Energia do path: "+ graph.getWeightFromPath(recommendedPath));

    }

    @Test
    void mostEfficientPathWithCharging_2() {
        GraphDelivery graph = PharmaDeliveriesApp.getInstance().getTerrestrialGraph();
        Address a1 = new Address(7,41.1260143,-8.6059455,"Farmacia Couto","4000-000",0);
        Address a2 = new Address(23,41.1330849,-8.6081726,"Praceta Salvador Caetano","4000-000",0);
        Address a3 = new Address(22,41.1436585,-8.6033817,"Espiral Colossal","4000-000",0);
        Address a4 = new Address(17,41.1534775,-8.6047492,"Traditional Shopping Street","4000-000",0);
        Address a5 = new Address(29,41.1714565,-8.6128568,"CTT","4000-000",0);


        LinkedList<Address> pathWithNoEnds = new LinkedList<>();
        pathWithNoEnds.add(a2);
        pathWithNoEnds.add(a3);
        pathWithNoEnds.add(a4);
        pathWithNoEnds.add(a5);


        Scooter scooter1 = new Scooter(13, 55564, 450, new Battery(1, 150), 75);
        DeliveryRun deliveryRun1 = new DeliveryRun(13, "2004");


        graph.loadEnergyIntoEdges(scooter1, (float) 85, (float) 90, WindVelocity, WindAngle, deliveryRun1);
        LinkedList<Address> recommendedPath = graph.mostEfficientPath(a1, a1, pathWithNoEnds);
        recommendedPath.forEach(System.out::println);
        System.out.println("Energia do path: "+ graph.getWeightFromPath(recommendedPath));
        System.out.println();


        graph.mostEfficientPathWithCharging(recommendedPath, new LinkedList<>(), new LinkedList<>());
        recommendedPath.forEach(System.out::println);
        System.out.println("Energia do path: "+ graph.getWeightFromPath(recommendedPath));

        System.out.println("---------------------");
        LinkedList<Address> result = graph.getBestPath(a1, a1, pathWithNoEnds);
        result.forEach(System.out::println);
        System.out.println("Energia do path: "+ graph.getWeightFromPath(result));

    }

     */

}
