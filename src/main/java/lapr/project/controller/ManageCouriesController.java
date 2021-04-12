package lapr.project.controller;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lapr.project.ui.dto.CourierDTO;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.model.Courier;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.Pharmacy;

import static lapr.project.controller.DTOConverter.convertPharmacyDTO;

/**
 *
 * @author catarinaserrano
 */
public class ManageCouriesController {
    private final PharmaDeliveriesApp pharmaDeliveriesApp;
    private final CommonController commonController;
    
    public ManageCouriesController(){
        this.pharmaDeliveriesApp = PharmaDeliveriesApp.getInstance();
        commonController = new CommonController();
    }
    
    /**
     * This method retrieves a list of working corries for a specific pharmacy
     * @param pharmacyDTO
     * @return
     * @throws SQLException 
     */
    public List<CourierDTO> getCouries(PharmacyDTO pharmacyDTO) throws SQLException {
        Iterable<Courier> temp = new LinkedList<>();
        temp =this.pharmaDeliveriesApp.getServiceCourier().getCouriesFromPharmacy(convertPharmacyDTO(pharmacyDTO));
        
        List<CourierDTO> couries = new LinkedList<>();

        for (Courier c: temp){
            couries.add(DTOConverter.convertCourier(c));
        }
        if (!couries.isEmpty()) {
            return couries;
        } else {
            return null;
        }
    }
    
    public boolean insertCourier(CourierDTO courierDTO, PharmacyDTO pharmacyDTO) throws SQLException {
        if(pharmacyDTO==null|| courierDTO==null){
            return false;
        }
        Courier courier = DTOConverter.convertCourierDTO(courierDTO);
        Pharmacy pharmacy = DTOConverter.convertPharmacyDTO(pharmacyDTO);
        return this.pharmaDeliveriesApp.getServiceCourier().insertCourier(courier, pharmacy);
    }
    
    public boolean updateCourier(CourierDTO courierDTO, PharmacyDTO pharmacyDTO) throws SQLException {
        if(pharmacyDTO==null|| courierDTO==null){
            return false;
        }
        Courier courier = DTOConverter.convertCourierDTO(courierDTO);
        Pharmacy pharmacy = convertPharmacyDTO(pharmacyDTO);
        return this.pharmaDeliveriesApp.getServiceCourier().updateCourier(courier, pharmacy);
    }
    
     public boolean removeCourier(CourierDTO courierDTO, PharmacyDTO pharmacyDTO) throws SQLException {
        if(pharmacyDTO==null|| courierDTO==null){
            return false;
        }
        Courier courier = DTOConverter.convertCourierDTO(courierDTO);
        Pharmacy pharmacy = DTOConverter.convertPharmacyDTO(pharmacyDTO);
        return this.pharmaDeliveriesApp.getServiceCourier().removeCourier(courier, pharmacy);
    }
 
}
