package lapr.project.controller;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import lapr.project.model.*;
import lapr.project.service.ServiceVehicle;
import lapr.project.ui.dto.DroneDTO;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.ui.dto.ScooterDTO;
import lapr.project.ui.dto.VehicleDTO;

import static lapr.project.controller.DTOConverter.*;

/**
 *
 */
public class ManageVehiclesController {
    
    private ServiceVehicle serviceVehicle;
    
    public ManageVehiclesController(){
        serviceVehicle = PharmaDeliveriesApp.getInstance().getServiceVehicle();
    }
    
    /**
     * This method retrieves a list of scooters for a specific pharmacy
     * @return
     * @throws SQLException 
     */
    public List<ScooterDTO> getScooters(PharmacyDTO pharmacyDTO) throws SQLException {
        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);
        Iterable<Scooter> scooters = serviceVehicle.getScootersByPharmacy(pharmacy);
        LinkedList<ScooterDTO> scootersDTO = new LinkedList<>();

        for (Scooter s : scooters){
            scootersDTO.add(convertScooter(s));
        }
        if (scootersDTO.isEmpty()) {
            return null;
        } else {
            return scootersDTO;
        }
    }

    /**
     * This method retrieves a list of drones for a specific pharmacy
     * @param pharmacyDTO
     * @return
     * @throws SQLException
     */
    public LinkedList<DroneDTO> getDrones(PharmacyDTO pharmacyDTO) throws SQLException {
        Iterable<Drone> temp;
        temp = serviceVehicle.getDronesFromPharmacy(convertPharmacyDTO(pharmacyDTO));

        LinkedList<DroneDTO> drones = new LinkedList<>();

        for (Drone d : temp){
            drones.add(convertDrone(d));
        }
        if (!drones.isEmpty()) {
            return drones;
        } else {
            return null;
        }
    }
   
    public VehicleDTO insertVehicle(VehicleDTO vehicleDTO, PharmacyDTO pharmacy) throws SQLException {
        if(vehicleDTO==null){
            return null;
        }
        return convertVehicle(serviceVehicle.insertVehicle(convertVehicleDTO(vehicleDTO), convertPharmacyDTO(pharmacy)));
    }
    
    public VehicleDTO updateVehicle(VehicleDTO vehicleDTO, PharmacyDTO pharmacyDTO) throws SQLException {
        if(vehicleDTO == null) {
            return null;
        }
        return convertVehicle(serviceVehicle.updateVehicle(convertVehicleDTO(vehicleDTO), convertPharmacyDTO(pharmacyDTO)));
    }
    
    public boolean removeVehicle(VehicleDTO vehicleDTO, PharmacyDTO pharmacyDTO) throws SQLException {
        Vehicle vehicle = convertVehicleDTO(vehicleDTO);
        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);
        return serviceVehicle.removeVehicle(vehicle, pharmacy);
    }



     

}
