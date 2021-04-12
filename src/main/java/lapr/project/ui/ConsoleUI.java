/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import lapr.project.controller.BackOrderController;
import lapr.project.controller.CommonController;
import lapr.project.controller.ManageClientsController;
import lapr.project.controller.ManageCouriesController;
import lapr.project.controller.ManageDeliveryRunController;
import lapr.project.controller.ManagePharmaciesController;
import lapr.project.controller.ManageVehiclesController;
import lapr.project.controller.ManageStocksController;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.DeliverableDTO;
import lapr.project.ui.dto.DeliveryRunDTO;
import lapr.project.ui.dto.EstimatesDTO;
import lapr.project.ui.dto.PharmacyDTO;

/**
 *
 * @author 1100241
 */
public class ConsoleUI {

    private String adminEmail = "admin1@isep.ipp.pt";
    private String adminKey = "admin1";
    private CommonController commonController;
    private ManagePharmaciesController managePharmaciesController;
    private ManageVehiclesController manageVehiclesController;
    private ManageCouriesController manageCourierController;
    private ManageStocksController manageStocksController;
    private ManageClientsController manageClientsController;
    private BackOrderController backOrderController;
    private ManageDeliveryRunController manageDeliveryRunController;

    public void start() {

        //Start controller
        commonController = new CommonController();
        managePharmaciesController = new ManagePharmaciesController();
        manageVehiclesController = new ManageVehiclesController();
        manageCourierController = new ManageCouriesController();
        manageStocksController = new ManageStocksController();
        manageClientsController = new ManageClientsController();
        backOrderController = new BackOrderController();
        manageDeliveryRunController = new ManageDeliveryRunController();

//        Estimate
       estimateDeliveryRun();
    }

    
    public void estimateDeliveryRun() {

        System.out.println("Estimate Delivery Run ...");
        try {
            //Do Login As Admin
            doLogin(adminEmail, adminKey);

            //Pharmacies
            System.out.println("");
            List<PharmacyDTO> pharmaciesList = new LinkedList<>();
            pharmaciesList = managePharmaciesController.getPharmaciesByAdministrator();
            if (pharmaciesList != null && !pharmaciesList.isEmpty()) {
                for (PharmacyDTO p : pharmaciesList) {
                    System.out.println("Pharmacy Id: " + p.getId() + "; Pharmacy Address Id: " + p.getAddress().getId() + "; Pharmacy Address: " + p.getAddress().getAddress());
                    //Deliveries
                    List<DeliveryRunDTO> deliveries = new LinkedList<>();
                    deliveries = manageDeliveryRunController.fetchAvailableDeliveryRuns(p);
                    if (deliveries != null && !deliveries.isEmpty()) {
                        for (DeliveryRunDTO d : deliveries) {
                            System.out.println("    Delivery Run Id: " + d.getId() + "; Date: " + d.getDate());
                            EstimatesDTO estimatesScooter = null;
                            EstimatesDTO estimatesDrone = null;
                            try {
                                estimatesScooter = manageDeliveryRunController.estimateScooterDeliveryCosts(p, d);
                                estimatesDrone = manageDeliveryRunController.estimateDroneDeliveryCosts(p, d);
                            } catch (Exception ex) {
                                System.out.println("    Error at estimate delivery cost");
                            }
                            //DeliverableDTO
                            LinkedList<DeliverableDTO> deliverables = new LinkedList<>();
                            deliverables = d.getDeliverables();
                            if (deliverables != null && !deliverables.isEmpty()) {
                                for (DeliverableDTO deliverable : deliverables) {
                                    System.out.println("        Deliverable Id: " + deliverable.getId() + "; Deliverable Address Id: " + deliverable.getAddress().getId() + "; Deliverable Address: " + deliverable.getAddress().getAddress());
                                }
                                if (estimatesDrone != null) {
                                    //Best Path Drone
                                    System.out.println("");
                                    System.out.println("        Drone Path:");
                                    Set<AddressDTO> dronePath = estimatesDrone.getEnergyCostMapDTO().keySet();
                                    if (dronePath != null && !dronePath.isEmpty()) {
                                        for (AddressDTO a : dronePath) {
                                            System.out.println("        Address Id: " + a.getId() + "; Address: " + a.getAddress());
                                        }
                                        System.out.println("        Total Cost: " + Math.round(estimatesDrone.getRequiredBatteryToCompletePathDTO()* 1000) + " W");
                                    } else {
                                        System.out.println("        No available path for drone");
                                    }
                                }
                                if (estimatesScooter!=null){
                                    //Best Path Scooter
                                    System.out.println("");
                                    System.out.println("        Scooter Path:");
                                    Set<AddressDTO> scooterPath = estimatesScooter.getEnergyCostMapDTO().keySet();
                                    if (scooterPath != null && !scooterPath.isEmpty()) {
                                        for (AddressDTO a : scooterPath) {
                                            System.out.println("        Address Id: " + a.getId() + "; Address: " + a.getAddress());
                                        }
                                        System.out.println("        Total Cost: " + Math.round(estimatesScooter.getRequiredBatteryToCompletePathDTO() * 1000) + " W");
                                    } else {
                                        System.out.println("        No available path for sccoter");
                                    }
                                }
                                
                            } else {
                                System.out.println("        No available deliverables");
                            }
                            System.out.println("");
                        }
                    } else {
                        System.out.println("    No available deliveries");
                    }
                    System.out.println("");
                }
            } else {
                System.out.println("No available pharmacies for " + adminEmail);
            }

            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at estimate: " + ex.getMessage());
        }

    }

    public boolean doLogin(String strEmail, String strPwd) {
        return PharmaDeliveriesApp.getInstance().doLogin(strEmail, strPwd);
    }

    public void doLogout() {
        PharmaDeliveriesApp.getInstance().doLogout();
    }

}
