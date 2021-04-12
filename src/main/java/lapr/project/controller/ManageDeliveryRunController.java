package lapr.project.controller;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import lapr.project.model.*;
import lapr.project.service.ServiceDeliveryRun;
import lapr.project.service.ServiceParkSystemFiles;
import lapr.project.service.ServicePharmacy;
import lapr.project.ui.dto.*;

import static lapr.project.controller.DTOConverter.*;

import lapr.project.service.ServiceEstimate;
import lapr.project.service.ServiceVehicle;

/**
 *
 */
public class ManageDeliveryRunController {

    private PharmaDeliveriesApp pharmaDeliveriesApp;
    private ServiceDeliveryRun serviceDeliveryRun;
    private ServicePharmacy servicePharmacy;
    private ServiceVehicle serviceVehicle;
    private GraphDelivery terrestrialGraph;
    private GraphDelivery aerialGraph;
    private GraphDelivery distTerrestrialGraph;
    private GraphDelivery distAerialGraph;
    private ServiceParkSystemFiles servicePark;
    private ServiceEstimate serviceEstimate;

    /**
     *
     */
    public ManageDeliveryRunController() {
        this.pharmaDeliveriesApp = PharmaDeliveriesApp.getInstance();
        this.serviceDeliveryRun = pharmaDeliveriesApp.getServiceDelivery();
        this.servicePharmacy = pharmaDeliveriesApp.getPharmacyService();
        this.serviceVehicle = pharmaDeliveriesApp.getServiceVehicle();
        this.terrestrialGraph = pharmaDeliveriesApp.getTerrestrialGraph();
        this.aerialGraph = pharmaDeliveriesApp.getAerialGraph();
        this.distTerrestrialGraph = pharmaDeliveriesApp.getDistanceTerrestrialGraph();
        this.distAerialGraph = pharmaDeliveriesApp.getDistanceAerialGraph();
        this.aerialGraph = pharmaDeliveriesApp.getAerialGraph();
        this.servicePark = new ServiceParkSystemFiles();
        this.serviceEstimate = new ServiceEstimate();
    }

    /**
     * Starts up a timer(for every pharmacy) that every hour creates
     * deliveryRuns with the orders that have been waiting for more than an hour
     *
     * @return
     */
    public boolean startDeliveryRunTimer() throws SQLException {
        List<Pharmacy> allPharmacies = (List<Pharmacy>) servicePharmacy.getAllPharmacies();
        return serviceDeliveryRun.startTimerForHourlyDeliveries(allPharmacies);
    }

    /**
     * Returns the deliveryRuns (that have a list of order and/or transfers)
     * that could be created right now. Creation of delivery runs takes into
     * account the orders that are ready to be delivered and the limitations of
     * min/max Payload
     *
     * @param pharmacyDTO
     * @return
     * @throws SQLException
     */
    public List<DeliveryRunDTO> fetchAvailableDeliveryRuns(PharmacyDTO pharmacyDTO) throws SQLException {
        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);

        List<Deliverable> deliverables = serviceDeliveryRun.fetchDeliveriesReadyForShipping(pharmacy);
        List<DeliveryRun> deliveryRuns = serviceDeliveryRun.createDeliveryRuns(pharmacy, deliverables);
        List<DeliveryRunDTO> deliveryRunsDTO = new LinkedList<>();
        for (DeliveryRun d : deliveryRuns) {
            deliveryRunsDTO.add(convertDeliveryRun(d));
        }
        return deliveryRunsDTO;
    }

    
    public EstimatesDTO estimateScooterDeliveryCosts(PharmacyDTO pharmacyDTO, DeliveryRunDTO deliveryRunDTO) throws SQLException{
        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);
        DeliveryRun deliveryRun = convertDeliveryRunDTO(deliveryRunDTO);
        
        return DTOConverter.convertEstimates(serviceEstimate.getEstimate(pharmacy, deliveryRun, 
                terrestrialGraph, distTerrestrialGraph, new Scooter(0, 0, 0, new Battery(0,0), 0)));
    }
            
    /**
     * Gets the energy/distance estimates for this delivery Run
     *
     * @param deliveryRunDTO
     * @return
     */
    public EstimatesDTO estimateDroneDeliveryCosts(PharmacyDTO pharmacyDTO, DeliveryRunDTO deliveryRunDTO) throws SQLException {
        
        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);
        DeliveryRun deliveryRun = convertDeliveryRunDTO(deliveryRunDTO);
        return DTOConverter.convertEstimates(serviceEstimate.getEstimate(pharmacy, deliveryRun, 
                aerialGraph, distAerialGraph, new Drone(0, 0, 0, new Battery(0,0), 0)));
    }

   
    /**
     * Returns the courier that is going to execute the delivery run (if the
     * delivery run is made by scooter) Selects the courier that has been
     * waiting the longest.
     *
     * @param pharmacy
     * @return
     */
    private Courier getWeightAverageOfCourier(Pharmacy pharmacy) throws SQLException {
        return serviceDeliveryRun.getWeightAverageOfCourier(pharmacy);
    }

    /**
     * Gets all the vehicles from this pharmacy that are available and have
     * enough battery to perform the delivery run
     *
     * @param pharmacyDTO
     * @param estimatesDTO
     * @return
     * @throws SQLException
     */
    public List<VehicleDTO> getAvailableVehiclesForDelivery(PharmacyDTO pharmacyDTO, EstimatesDTO estimatesDTO) throws SQLException {
        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);
        Estimates estimates = DTOConverter.convertEstimatesDTO(estimatesDTO);
        List<Vehicle> tmp = serviceDeliveryRun.getAvailableVehiclesForDelivery(pharmacy, estimates);
        List<VehicleDTO> vehicles = null;
        if (tmp != null && !tmp.isEmpty()) {
            vehicles = new LinkedList<>();
            for (Vehicle v : tmp) {
                vehicles.add(DTOConverter.convertVehicle(v));
            }
        }
        return vehicles;
    }

//    public boolean startRunWithScooter(PharmacyDTO pharmacyDTO, EstimatesDTO estimatesDTO, ScooterDTO scooterDTO) throws SQLException {
//        Estimates estimates = convertEstimatesDTO(estimatesDTO);
//        Scooter scooter = convertScooterDTO(scooterDTO);
//        Courier courier = getWeightAverageOfCourier(DTOConverter.convertPharmacyDTO(pharmacyDTO));
//
//        startAndRegisterDeliveryRun(estimates.getDeliveryRun(),estimates.getCostMapScooter(), scooter);
//        serviceDeliveryRun.markCourierAsBusy(courier);
//        serviceDeliveryRun.markVehicleAsBusy(scooter);
//        return true;
//    }
    public boolean startRun(PharmacyDTO pharmacyDTO, EstimatesDTO estimatesDTO, VehicleDTO vehicleDTO) throws SQLException {
        Estimates estimates = convertEstimatesDTO(estimatesDTO);
        Vehicle vehicle = DTOConverter.convertVehicleDTO(vehicleDTO);
        Courier courier = serviceDeliveryRun.getCourierByUserSession();

        if (courier != null) {
            //if (vehicle instanceof Scooter) {
                startAndRegisterDeliveryRun(estimates.getDeliveryRun(), estimates.getDistanceCostMap(), vehicle, courier);
            //} else if (vehicle instanceof Drone) {
            //    startAndRegisterDeliveryRun(estimates.getDeliveryRun(), estimates.getDistanceCostMap(), vehicle, courier);
            //}
            serviceDeliveryRun.markVehicleAsBusy(vehicle);
            return true;
        }
        return false;

    }

    /**
     * When the deliveryRun is accepted/started by a courier/drone then the
     * client is notified, the order is marked as in-transit and the deliveryRun
     * info is stored in the DB
     *
     * @param deliveryRun
     * @param costMap
     * @param vehicle
     * @return
     * @throws SQLException
     */
    private boolean startAndRegisterDeliveryRun(DeliveryRun deliveryRun, LinkedHashMap<Address, Double> costMap, Vehicle vehicle, Courier courier) throws SQLException {
        Client client;
        int errCount = 0;
        for (Deliverable d : deliveryRun.getDeliverables()) {
            if (d instanceof Order) {
                client = serviceDeliveryRun.getClientByOrder((Order) d);
                if (!serviceDeliveryRun.sendDeliveryEmailToClient(client, ((Order) d).getIdOrder())) {
                    errCount++;
                }
            }
        }
        if (errCount > 0) {
            throw new IllegalStateException("Failed to send " + errCount + " email notifications to clients.");
        }

        if (!serviceDeliveryRun.markDeliverablesAsInTransit(deliveryRun.getDeliverables())) {
            throw new IllegalStateException("Couldn't alter order to 'In-transit'.");
        }
        if (!serviceDeliveryRun.insertDeliveryRun(deliveryRun, costMap, vehicle, courier)) {
            throw new IllegalStateException("Couldn't register Delivery Run");
        }
        return serviceDeliveryRun.markCourierAsBusy(courier);

    }

    /**
     * Fetches the orders and transfers that are ready to be shipped but have
     * not yet been selected into a deliveryRun. (probably because they don't
     * reach the minimumPayload to be sent alone and didn't fit with the other
     * deliveryRuns)
     *
     * @param pharmacyDTO the order and transfers selected will be from this
     * pharmacy
     * @return
     */
    public List<Deliverable> getPendentOrders(PharmacyDTO pharmacyDTO) throws SQLException {
        //gets all delivery runs that can were created
        List<DeliveryRunDTO> deliveryRunsDTO = fetchAvailableDeliveryRuns(pharmacyDTO);
        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);

        //get all deliverables that are ready for shipping
        List<Deliverable> pendentDeliverables = serviceDeliveryRun.fetchDeliveriesReadyForShipping(pharmacy);
        List<DeliveryRun> deliveryRuns = new LinkedList<>();

        //converts all deliveryRunDTOs to deliveryRuns
        for (DeliveryRunDTO deliveryRunDto : deliveryRunsDTO) {
            deliveryRuns.add(convertDeliveryRunDTO(deliveryRunDto));
        }
        //from all the deliverables that are ready for shipping removes the ones already in a deliveryRun
        for (DeliveryRun deliveryRun : deliveryRuns) {
            pendentDeliverables.removeAll(deliveryRun.getDeliverables());
        }

        return pendentDeliverables;
    }

    public PharmacyDTO getPharmacyByCourier() throws SQLException {
        if (this.pharmaDeliveriesApp.getCurrentSession() == null) {
            return null;
        }
        return DTOConverter.convertPharmacy(servicePharmacy.getPharmacyByCourier(this.pharmaDeliveriesApp.getCurrentSession().getUser()));
    }

    /**
     * Marks the order as delivered in the DB - simulates the client receiving
     * the order
     *
     * @param deliverableDTO
     * @return
     * @throws SQLException
     */
    public boolean setOrderAsDelivered(DeliverableDTO deliverableDTO) throws SQLException {
        if (deliverableDTO == null) {
            return false;
        }

        Deliverable deliverable = convertDeliverableDTO(deliverableDTO);
        if (deliverable instanceof Order){
            return serviceDeliveryRun.setOrderAsDelivered(deliverable);

        }else if (deliverable instanceof Transfer){
            TransferProductsController transferProductsController  = new TransferProductsController();
            TransferDTO transferDTO = DTOConverter.convertTransfer((Transfer)deliverable);
            return ( transferProductsController.receiveTransfer(transferDTO) != null);
        }else{
            return false;
        }
    }

    /**
     * Simulates the parking of a vehicle in the parking system by generating a
     * file with the vehicle information and feeding into to the parking system
     * (application written in C and assembly)
     *
     * @param pharmacyDTO
     * @param vehicleDTO
     * @param incorrectPark
     */
    public boolean parkVehicle(PharmacyDTO pharmacyDTO, VehicleDTO vehicleDTO, boolean incorrectPark) {
        Pharmacy pharmacy = DTOConverter.convertPharmacyDTO(pharmacyDTO);
        Vehicle vehicle = DTOConverter.convertVehicleDTO(vehicleDTO);

        return servicePark.createParkingFile(pharmacy, vehicle, incorrectPark);

    }

}
