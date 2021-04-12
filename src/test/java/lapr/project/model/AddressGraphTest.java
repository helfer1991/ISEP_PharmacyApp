///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package lapr.project.model;
//
//import java.sql.SQLException;
//import java.util.LinkedList;
//import java.util.List;
//import lapr.project.data.AddressDB;
//import lapr.project.data.RoadDB;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import static org.mockito.Mockito.when;
//
///**
// *
// * @author catarinaserrano
// */
//public class AddressGraphTest {
//
//    AddressGraph instance = new AddressGraph();
//    AddressDB addressDb = Mockito.mock(AddressDB.class);
//    RoadDB roadDb = Mockito.mock(RoadDB.class);
//    LinkedList<Address> expResultAddressDB = new LinkedList<>();
//    Address a3 = new Address(3, 41.152325, -8.626058, "Rua Julio Dinis", "4450-116", 72);
//    Address a4 = new Address(4, 41.148171, -8.624156, "Rua Dom Manuel II", "4450-133", 77);
//    Address a12 = new Address(12, 41.155336, -8.613358, "Rua da Lapa", "4450-136", 114);
//    Address a7 = new Address(7, 41.150722, -8.617192, "Rua de Cedofeita", "4450-001", 98);
//    Address a6 = new Address(6, 41.152665, -8.620806, "Rua da Torrinha", "4450-057", 87);
//    Address a10 = new Address(10, 41.156299, -8.619319, "Rua da Boavista", "4450-333", 85);
//    LinkedList<Road> expResultRoadDB = new LinkedList<>();
//    Road r1 = new Road(1,a3, a4);
//    Road r2 = new Road(2,a4, a6);
//    Road r3 = new Road(3,a6, a3);
//    Road r4 = new Road(4,a6, a10);
//    Road r5 = new Road(5,a10, a12);
//    Road r6 = new Road(6,a12, a10);
//    Road r7 = new Road(7,a10, a7);
//    Road r8 = new Road(8,a7, a6);
//    Road r9 = new Road(9,a6, a7);
//    
//     LinkedList<AirRoad> expResultAirRoadRestrictionDB = new LinkedList<>();
//
//    public AddressGraphTest() throws SQLException {
//        instance.setAddressDb(addressDb);
//        instance.setRoadDb(roadDb);
//        expResultAddressDB.add(a3);
//        expResultAddressDB.add(a4);
//        expResultAddressDB.add(a6);
//        expResultAddressDB.add(a7);
//        expResultAddressDB.add(a10);
//        expResultAddressDB.add(a12);
//        when(addressDb.getAllAddresses()).thenReturn(expResultAddressDB);
//        expResultRoadDB.add(r1);
//        expResultRoadDB.add(r2);
//        expResultRoadDB.add(r3);
//        expResultRoadDB.add(r4);
//        expResultRoadDB.add(r5);
//        expResultRoadDB.add(r6);
//        expResultRoadDB.add(r7);
//        expResultRoadDB.add(r8);
//        expResultRoadDB.add(r9);
//        when(roadDb.getTerrestrialRestrictions()).thenReturn(expResultRoadDB);
//        expResultAirRoadRestrictionDB.add(ar1);
//        expResultAirRoadRestrictionDB.add(ar2);
//        when(roadDb.getAllRestricitons()).thenReturn(expResultAirRoadRestrictionDB);
//    }
//
//    /**
//     * Test of createGraphs method, of class AddressGraph.
//     */
//    @Test
//    public void testCreateGraphs() {
//
//
//        instance.createGraphs();
//        assertEquals(6, instance.getTerrestrialGraph().numVertices());
//        assertEquals(9, instance.getTerrestrialGraph().numEdges());
//        assertEquals(6, instance.getAirGraph().numVertices());
//        assertEquals(30, instance.getAirGraph().numEdges());  
//
//    }
//    
//    @Test
//    public void testRemoveAirRoadRestrictide() throws SQLException{
//        instance.createGraphs();
//        instance.removeAirRoadRestrictide();
//        assertEquals(28, instance.getAirGraph().numEdges());
//        assertEquals(6, instance.getAirGraph().numVertices());
//    }
//
//    /**
//     * Test of terrestrialMostEfficientPath method, of class AddressGraph.
//     */
//    @Test
//    public void testTerrestrialMostEfficientPath() {
//        instance.createGraphs();
//        instance.loadWeightTerrestrialGraph(150.0F, 5.5F, 20.0F);
//
//        List<Address> stopAddresses = new LinkedList<Address>();
//        stopAddresses.add(a3);
//        stopAddresses.add(a4);
//        stopAddresses.add(a10);
//        stopAddresses.add(a3);
//        
//        LinkedList<Address> expectedFinalPath = new LinkedList<Address>();
//        expectedFinalPath.add(a3);
//        expectedFinalPath.add(a4);
//        expectedFinalPath.add(a6);
//        expectedFinalPath.add(a10);
//        expectedFinalPath.add(a7);
//        expectedFinalPath.add(a6);
//        expectedFinalPath.add(a3);
//        
//
//        LinkedList<Address> finalPath = instance.terrestrialMostEfficientPath(a3, stopAddresses);
//        assertEquals(expectedFinalPath, finalPath);
//    }
//    
//    /**
//     * Test of terrestrialMostEfficientPath method, of class AddressGraph.
//     */
//    @Test
//    public void testAirMostEfficientPath() {
//        instance.createGraphs();
//        instance.loadWeightAirGraph(40.0F, 5.5F, 20.0F);
//
//        List<Address> stopAddresses = new LinkedList<Address>();
//        stopAddresses.add(a3);
//        stopAddresses.add(a4);
//        stopAddresses.add(a10);
//        stopAddresses.add(a3);
//        
//        LinkedList<Address> expectedFinalPath = new LinkedList<Address>();
//        expectedFinalPath.add(a3);
//        expectedFinalPath.add(a4);
//        expectedFinalPath.add(a10);
//        expectedFinalPath.add(a3);
//        
//        LinkedList<Address> finalPath = instance.airMostEfficientPath(a3, stopAddresses);
//        assertEquals(expectedFinalPath, finalPath);
//    }
//
//    @Test
//    public void testGetEnergyFromPath() {
//        instance.createGraphs();
//        instance.loadWeightTerrestrialGraph(150.0F, 5.5F, 20.0F);
//        instance.loadWeightAirGraph(4.0F, 5.5F, 20.0F);
//
//        List<Address> path = new LinkedList<Address>();
//        path.add(a3);
//        path.add(a4);
//        path.add(a6);
//        path.add(a10);
//        path.add(a7);
//        path.add(a6);
//        path.add(a3);
//        
//        float expectedScooter = (float) (instance.getTerrestrialGraph().getEdge(a3, a4).getWeight()+instance.getTerrestrialGraph().getEdge(a4, a6).getWeight()
//                + instance.getTerrestrialGraph().getEdge(a6, a10).getWeight() + instance.getTerrestrialGraph().getEdge(a10, a7).getWeight()
//                + instance.getTerrestrialGraph().getEdge(a7, a6).getWeight() + instance.getTerrestrialGraph().getEdge(a6, a3).getWeight());
//
//        float expectedDrone = (float) (instance.getAirGraph().getEdge(a3, a4).getWeight()+instance.getAirGraph().getEdge(a4, a6).getWeight()
//                + instance.getAirGraph().getEdge(a6, a10).getWeight() + instance.getAirGraph().getEdge(a10, a7).getWeight()
//                + instance.getAirGraph().getEdge(a7, a6).getWeight() + instance.getAirGraph().getEdge(a6, a3).getWeight());
//
//        
//        float energyPathScooter = instance.getEnergyFromPath(path, instance.getTerrestrialGraph());
//        float energyPathDrone = instance.getEnergyFromPath(path, instance.getAirGraph());
//        
//        assertEquals(energyPathScooter, expectedScooter, 0.001);
//        assertEquals(energyPathDrone, expectedDrone, 0.001);
//    }
//
//    @Test
//    void testSetAddressDb() {
//        System.out.println("test - setAddressDB to AddressGraph");
//        AddressDB address = null;
//        instance.setAddressDb(address);
//        assertEquals(instance.addressDb, null);
//        address = new AddressDB();
//        instance.setAddressDb(address);
//        assertEquals(instance.addressDb, address);
//    }
//
//    @Test
//    void testSetRoadDb() {
//        System.out.println("test - setRoadDb to AddressGraph");
//        RoadDB road = null;
//        instance.setRoadDb(road);
//        assertEquals(instance.roadDb, null);
//        road = new RoadDB();
//        instance.setRoadDb(road);
//        assertEquals(instance.roadDb, road);
//    }
//
//}
