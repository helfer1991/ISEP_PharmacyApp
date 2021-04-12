/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lapr.project.data.AddressDB;
import lapr.project.data.RoadDB;
import lapr.project.utils.Configuration;
import lapr.project.utils.Edge;
import lapr.project.utils.Graph;
import lapr.project.utils.GraphAlgorithms;
import lapr.project.utils.Utils;

/**
 *
 * @author catarinaserrano
 */
public class GraphDelivery {

    private final Graph<Address, Road> graph;
    private Iterable<Road> restrictions;
    AddressDB addressDb;
    RoadDB roadDb;
    private LinkedList<Address>[][] shortestPaths;
    private double vehicleMaxBatCapacity;

    /**
     * Constructor, initialize variables for directed graph
     * This graph is directed with vertex as Addresses and edges as Roads
     */
    public GraphDelivery() {
        graph = new Graph<>(true);
        addressDb = new AddressDB();
        roadDb = new RoadDB();
        restrictions = new LinkedList<>();
        shortestPaths = null;
    }

    /**
     * Populates a graph, where vertex are all addresses in db and remove
     * restrictions
     */
    public void createGraph() {
        try {
            Iterable<Address> addresses = addressDb.getAllAddresses();

            List<Address> addressesList = new ArrayList<>();
            addresses.forEach(addressesList::add);

            for (Address a : addresses) {
                graph.insertVertex(a);
            }
            for (int i = 0; i < graph.numVertices(); i++) {
                for (int j = i + 1; j < graph.numVertices(); j++) {
                    Road roadA = new Road(0, addressesList.get(i), addressesList.get(j));
                    graph.insertEdge(roadA.getAddressOrig(), roadA.getAddressDest(), roadA, 0);
                    Road roadB = new Road(0, addressesList.get(j), addressesList.get(i));
                    graph.insertEdge(roadB.getAddressOrig(), roadB.getAddressDest(), roadB, 0);
                }
            }

            for (Road r : restrictions) {
                graph.removeEdge(r.getAddressOrig(), r.getAddressDest());
            }
            shortestPaths = null;
        } catch (Exception ex) {

        }
    }

    /**
     * Load all air restrictions
     *
     * @return boolean
     */
    public boolean loadAirRestrictions() {
        try {
            restrictions = roadDb.getAirRestricitons();
            shortestPaths = null;
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * Load all terrestrial restrictions
     *
     * @return true if success
     */
    public boolean loadTerrestrialRestrictions() {

        try {
            restrictions = roadDb.getTerrestrialRestrictions();
            shortestPaths = null;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Return graph
     *
     * @return Graph<Address, Road>
     */
    public Graph<Address, Road> getGraph() {
        return graph;
    }

    /**
     * For each vertex of the graph it will set the weight distance into a intial graph
     */
    public void loadDistanceIntoEdges() {
        for (Edge<Address, Road> graphEdge : graph.edges()) {

            double weight = Utils.distBetweenPointsOnEarth(graphEdge.getElement().getAddressOrig().getLatitude(),
                    graphEdge.getElement().getAddressOrig().getLongitude(),
                    graphEdge.getElement().getAddressDest().getLatitude(),
                    graphEdge.getElement().getAddressDest().getLongitude());

        
            graphEdge.setWeight(weight);

        }
        shortestPaths = null;
    }
    
    /**
     * For each vertex of the graph it will set the weight as distance into the drone graph and scooter graph
     */
    public void loadDistanceIntoEdges(Vehicle v, DeliveryRun deliveryRun) {
        for (Edge<Address, Road> graphEdge : graph.edges()) {

            Address[] tmp = graphEdge.getEndpoints();
            double weight = Utils.distBetweenPointsOnEarth(graphEdge.getElement().getAddressOrig().getLatitude(),
                    graphEdge.getElement().getAddressOrig().getLongitude(),
                    graphEdge.getElement().getAddressDest().getLatitude(),
                    graphEdge.getElement().getAddressDest().getLongitude());

            for (Deliverable d : deliveryRun.getDeliverables()) {
                if (d.getAddress() == tmp[0]) {
                    weight += (double) Configuration.getSpecsflyHeight();
                }
                if (d.getAddress() == tmp[1]) {
                    weight += (double) Configuration.getSpecsflyHeight();
                }
            }

            graphEdge.setWeight(weight);

        }
        shortestPaths = null;
    }

    /**
     * For each vertex of the graph it will set the weight as the energy
     * consumption in kwH
     *
     * @param vehicle
     * @param productsWeight
     * @param deliveryRun
     */
    public void loadEnergyIntoEdges(Vehicle vehicle, float productsWeight, DeliveryRun deliveryRun) {
        double energy = 0;
        Eficciency e = new Eficciency();

        if (vehicle instanceof Scooter) {
            vehicleMaxBatCapacity = Configuration.getSpecsBatteryScooterCapacitykWh();
        } else if (vehicle instanceof Drone) {
            vehicleMaxBatCapacity = Configuration.getSpecsBatteryDroneCapacitykWh();
        }

        for (Edge<Address, Road> graphEdge : graph.edges()) {
            if (vehicle instanceof Scooter) {
                energy = e.calcSpentEnergyScooter(graphEdge.getElement(), productsWeight);
            } else if (vehicle instanceof Drone) {
                Address[] tmp = graphEdge.getEndpoints();
                boolean hasUp = false;
                boolean hasDown = false;
                for (Deliverable d : deliveryRun.getDeliverables()) {
                    if (d.getAddress() == tmp[0]) {
                        hasUp = true;
                    }
                    if (d.getAddress() == tmp[1]) {
                        hasDown = true;
                    }
                }
                energy = e.calcSpentEnergyDrone(graphEdge.getElement(), productsWeight, hasUp, hasDown);
            }
            graphEdge.setWeight(energy);
        }
        shortestPaths = null;
    }

    /**
     * Returns the most efficient path. If the path requires more energy than
     * the maximum battery capacity of the vehicle, then the necessary charging
     * stops (reachable pharmacy addresses) are added accordingly.
     *
     * @param start start address for the path (usually the pharmacy)
     * @param end end point for the path (usually the same pharmacy)
     * @param stopAddresses list of addresses that the path must include
     * @return List or addresses sorted according to the most efficient way to
     * traverse them
     */
    public LinkedList<Address> getBestPath(Address start, Address end, List<Address> stopAddresses) {
        LinkedList<Address> mostEfficientPath = mostEfficientPath(start, end, stopAddresses);

        if (mostEfficientPath == null) {
            return null;
        }
        LinkedList<Address> result = new LinkedList<>(mostEfficientPath);

        if (getWeightFromPath(mostEfficientPath) > vehicleMaxBatCapacity) {
            result = null;
            mostEfficientPathWithCharging(mostEfficientPath, new LinkedList<>(), new LinkedList<>());

            //validates if the path generated by mostEfficientPathWithCharging is valid
            if ( !mostEfficientPath.isEmpty() && mostEfficientPath.getFirst().equals(mostEfficientPath.getLast())) {
                result = mostEfficientPath;
            }
        }

        return result;
    }

    /**
     * Recursive method that receives a path and, if the path requires more energy than the vehicle max battery, then it tries to include
     * pharmacies (for recharging the battery) along the path, to make the end of the path reachable.
     *
     * @param originalPath The original path passed as parameter -> will be altered to reflect the new path!!
     *                     If the energy required for this path is greater than vehicleMaxBatCapacity and after the
     *                     method is called the original path doesn't include all original stops + pharmacy(ies), then
     *                     the method failed to generate a possible path.
     * @param pathRemaining this should be an empty list when the method is called the first time
     * @param visitedPharmacies this should be an empty list when the method is called the first time
     */
    public void mostEfficientPathWithCharging(LinkedList<Address> originalPath, LinkedList<Address> pathRemaining, LinkedList<Pharmacy> visitedPharmacies) {
        if (getWeightFromPath(originalPath) < vehicleMaxBatCapacity) {
            return;
        }
        Address removed = originalPath.removeLast();
        pathRemaining.addFirst(removed);

        if (getWeightFromPath(originalPath) > vehicleMaxBatCapacity) {
            mostEfficientPathWithCharging(originalPath, pathRemaining, visitedPharmacies);
        } else {
            if(originalPath.isEmpty()) {return;}
            Pharmacy pharmacy = PharmaDeliveriesApp.getInstance().getServiceBackOrder().getNearestPharmacy(originalPath.getLast());
            if (visitedPharmacies.contains(pharmacy)) {
                return;
            }
            originalPath.addLast(pharmacy.getAddress());
            double newWeight = getWeightFromPath(originalPath);
            originalPath.removeLast();
            visitedPharmacies.add(pharmacy);

            if (newWeight < vehicleMaxBatCapacity) {
                pathRemaining.addFirst(pharmacy.getAddress());
                if (getWeightFromPath(pathRemaining) < vehicleMaxBatCapacity) {
                    originalPath.addAll(pathRemaining);
                } else {
                    mostEfficientPathWithCharging(pathRemaining, new LinkedList<>(), new LinkedList<>());
                    originalPath.addAll(pathRemaining);
                }

            } else {
                mostEfficientPathWithCharging(originalPath, pathRemaining, visitedPharmacies);
            }
        }

    }

    /**
     * Returns the most efficient path
     *
     * @param start - start and end of delievery (phamarcy)
     * @param end
     * @param stopList - addresses required to pass
     * @return finalPath
     */
    public LinkedList<Address> mostEfficientPath(Address start, Address end, List<Address> stopList) {

        LinkedList<Address> stopAddresses = new LinkedList<>(stopList);
        if (start == null || end == null || stopAddresses == null) {
            return null;
        }
        LinkedList<Address> finalPath = null;

        //Get paths to reach any vertex from any vertex
        if (shortestPaths == null) {
            shortestPaths = GraphAlgorithms.transitivePathsClosure(graph);
        }

        //Get all possible paths
        stopAddresses.add(0, start);
        stopAddresses.add(end);
        LinkedList<Address[]> possiblePaths = new LinkedList<>();
        Address temp[] = new Address[stopAddresses.size()];
        int z = 0;
        for (Address a : stopAddresses) {
            temp[z] = a;
            z++;
        }
        if (stopAddresses.size() == 2) {
            possiblePaths.add(temp);
        } else {
            permute(possiblePaths, temp, 1, temp.length - 2);
        }

        float minLenght = Float.MAX_VALUE;
        LinkedList<Address> tmpPath = null;
        float length = 0;

        //Iterar possible solutions
        for (Address[] addresses : possiblePaths) {

            length = 0;
            tmpPath = new LinkedList<>();
            boolean found = true;
            //Para cada solução verificar quais os vertices que as compoe e respetivas distancias
            int i = 0;
            while (i < addresses.length - 1 && found == true) {
                Address origin = addresses[i];
                Address destiny = addresses[i + 1];

                //Para cada vertice ver o caminho mais perto que chega la
                LinkedList<Address> path = shortestPaths[graph.getKey(origin)][graph.getKey(destiny)];
                if (path != null && !path.isEmpty()) {
                    found = true;
                    if (path.getLast() != destiny) {
                        path.addLast(destiny);
                    }
                    for (int j = 0; j < path.size() - 1; j++) {
                        Address a = path.get(j);
                        Address b = path.get(j + 1);
                        if(a.equals(b)){
                            length = 0;
                        }else{
                            length = length + (float) graph.getEdge(a, b).getWeight();
                            tmpPath.add(a);
                        }
                    }

                } else {
                    found = false;
                }
                i++;
            }

            if (length < minLenght && found == true) {
                tmpPath.add(end);
                finalPath = tmpPath;
                minLenght = length;
            }
        }
        return finalPath;
    }

    /**
     * Permute address - Heaps algorithm
     *
     * @param possibleCombinations
     * @param addresses
     * @param columnStart
     * @param columnEnd
     */
    private void permute(List<Address[]> possibleCombinations, Address[] addresses, int columnStart, int columnEnd) {

        if (columnEnd == columnStart) {
            possibleCombinations.add(addresses.clone());
        } else {
            for (int i = columnStart; i <= columnEnd; i++) {
                addresses = swap(addresses, columnStart, i);
                permute(possibleCombinations, addresses, columnStart + 1, columnEnd);
                addresses = swap(addresses, columnStart, i);
            }
        }

    }

    /**
     * Swap to indexs of array
     *
     * @param array
     * @param i
     * @param j
     * @return array
     */
    private Address[] swap(Address[] array, int i, int j) {
        Address temp;
        temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        return array;
    }

    /**
     * This method receives a graph and a list containing a path and returns the
     * potency necessary to complete the delivery
     *
     * @param path
     * @return
     */
    public float getWeightFromPath(List<Address> path) {
        float efficiencyFromPath = 0.0f;
        for (int i = 0; i < path.size() - 1; i++) {
            Address a = path.get(i);
            Address b = path.get(i + 1);
            double vertWeigth = graph.getEdge(a, b).getWeight();
            efficiencyFromPath = (float) (efficiencyFromPath + vertWeigth);
        }
        return efficiencyFromPath;
    }

    public void setAddressDb(AddressDB addressDb) {
        this.addressDb = addressDb;
    }

    public void setRoadDb(RoadDB roadDb) {
        this.roadDb = roadDb;
    }

}
