/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import lapr.project.data.PharmacyDB;
import lapr.project.model.Address;
import lapr.project.model.Battery;
import lapr.project.model.Courier;
import lapr.project.model.Deliverable;
import lapr.project.model.DeliveryRun;
import lapr.project.model.Drone;
import lapr.project.model.Estimates;
import lapr.project.model.GraphDelivery;
import lapr.project.model.Pharmacy;
import lapr.project.model.Scooter;
import lapr.project.model.Vehicle;
import lapr.project.utils.Configuration;

/**
 *
 * @author 1100241
 */
public class ServiceEstimate {

    private PharmacyDB pharmacyDB;

    public ServiceEstimate() {
        pharmacyDB = new PharmacyDB();
    }

    public Estimates getEstimate(Pharmacy pharmacy, DeliveryRun deliveryRun, GraphDelivery graphEnergyDelivery, GraphDelivery graphDistanceDelivery, Vehicle vehicle) {

        Estimates estimate = new Estimates(deliveryRun);

        List<Address> addresses = new LinkedList<>();

        // Get the products weight for a specific delivery
        double productsWeight = 0;
        for (Deliverable p : deliveryRun.getDeliverables()) {
            productsWeight = productsWeight + p.getShopCart().getShoppingCartWeight();
            if (!addresses.contains(p.getAddress())) {
                addresses.add(p.getAddress());
            }
        }
        
        graphDistanceDelivery.loadDistanceIntoEdges(vehicle, deliveryRun);
        graphEnergyDelivery.loadEnergyIntoEdges(vehicle, (float) productsWeight, deliveryRun);

        /* best path */
        LinkedList<Address> energyBestPath = graphEnergyDelivery.getBestPath(pharmacy.getAddress(), pharmacy.getAddress(), addresses);
        LinkedList<Address> distanceBestPath = graphDistanceDelivery.mostEfficientPath(pharmacy.getAddress(), pharmacy.getAddress(), addresses);

        if (energyBestPath != null) {
            /* energy cost */
            double energyCostTotalPath = graphEnergyDelivery.getWeightFromPath(energyBestPath);
            estimate.setRequiredBatteryToCompletePath(energyCostTotalPath);

            /* energy cost map */
            LinkedHashMap<Address, Double> energyCostMap = getCostMap(pharmacy, energyBestPath, graphEnergyDelivery);
            estimate.setEnergyCostMap(energyCostMap);

            /* path next pharmcy */
            List<Address> pathNextPharmacyFromPath = getPathNextPharmacyFromPath(energyBestPath);
            double energyCostToNextPharmacy = graphEnergyDelivery.getWeightFromPath(pathNextPharmacyFromPath);
            estimate.setRequiredBatteryToReachNextPharmacy(energyCostToNextPharmacy);
        }

        /* distance cost */
        double distanceCostTotalPath = graphDistanceDelivery.getWeightFromPath(distanceBestPath);
        estimate.setDistanceTotalLengh(distanceCostTotalPath);

        /* distance cost map */
        LinkedHashMap<Address, Double> distanceCostMap = getCostMap(pharmacy, distanceBestPath, graphDistanceDelivery);
        estimate.setDistanceCostMap(distanceCostMap);
        if (vehicle instanceof Scooter) {
            estimate.setTimeDuration((distanceCostTotalPath / 1000 / Configuration.getSpecsVScooterkmH()) * 60);
        } else {
            estimate.setTimeDuration((distanceCostTotalPath / 1000 / Configuration.getSpecsVHorDronekmH()) * 60);
        }

        return estimate;
    }

    public List<Address> getPathNextPharmacyFromPath(LinkedList<Address> bestPath) {

        try {
            LinkedList<Address> bestPathFinal = new LinkedList<>(bestPath);
            Iterable<Pharmacy> pharmacies = pharmacyDB.getAllPharmacies();
            LinkedList<Pharmacy> pharmaciesList = new LinkedList<>();
            pharmacies.forEach(pharmaciesList::add);
            bestPathFinal.remove(0);
            int index = bestPathFinal.size() - 1;
            for (Address a : bestPathFinal) {
                for (Pharmacy p : pharmacies) {
                    if (p.getAddress().equals(a) && bestPathFinal.indexOf(a) < index) {
                        index = bestPathFinal.indexOf(a);
                    }
                }

            }
            return bestPath.subList(0, index + 2);
        } catch (Exception e) {
            return null;
        }

    }

    public LinkedHashMap<Address, Double> getCostMap(Pharmacy pharmacy, LinkedList<Address> bestPath, GraphDelivery graphDelivery) {

        LinkedHashMap<Address, Double> costMap = new LinkedHashMap<>();

        for (int i = 1; i < bestPath.size(); i++) {
            Address addressStart = bestPath.get(i - 1);
            Address addressEnd = bestPath.get(i);

            //LinkedList<Address> SemiPath = graphDelivery.mostEfficientPath(addressStart, addressEnd, new LinkedList<>());
            LinkedList<Address> SemiPath = new LinkedList<>();
            SemiPath.add(addressStart);
            SemiPath.add(addressEnd);
            
            if(costMap.containsKey(bestPath.get(i))){
                bestPath.set(i, new Address(bestPath.get(i).getId(), bestPath.get(i).getLatitude(),
                        bestPath.get(i).getLongitude(), bestPath.get(i).getAddress(), bestPath.get(i).getZipcode()+"0", bestPath.get(i).getElevation()));
            }
            
            if(addressStart.equals(addressEnd)){
                costMap.put(bestPath.get(i), 0.0);
            }else{
                costMap.put(bestPath.get(i), (double) graphDelivery.getWeightFromPath(SemiPath));
            }
        }
        return costMap;
    }

//    public LinkedList<Deliverable> sortOrderByBestPath(LinkedList<Address> bestPath, LinkedList<Deliverable> orders) {
//        LinkedList<Deliverable> tempSorted = new LinkedList<>();
//
//        for (Address a : bestPath) {
//            for (Deliverable d : orders) {
//                if (d.getAddress().equals(a)) {
//                    if (!tempSorted.contains(d)) {
//                        tempSorted.add(d);
//                    }
//                }
//            }
//        }
//        return tempSorted;
//    }
//    public LinkedList<Address> getSemiPath(Address start, Address end, LinkedList<Address> bestPath) {
//        int posStart = bestPath.indexOf(start);
//        int posEnd = bestPath.indexOf(end);
//        LinkedList<Address> temp = new LinkedList<>();
//        for (int i = posStart; i <= posEnd; i++) {
//            temp.add(bestPath.get(i));
//        }
//        return temp;
//    }
    public void setPharmacyDB(PharmacyDB pharmacyDB) {
        this.pharmacyDB = pharmacyDB;
    }

}
