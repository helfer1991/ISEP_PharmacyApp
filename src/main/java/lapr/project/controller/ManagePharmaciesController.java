package lapr.project.controller;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import lapr.project.service.ServicePharmacy;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.ParkDTO;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.Pharmacy;

import static lapr.project.controller.DTOConverter.*;

/**
 *
 */
public class ManagePharmaciesController {

    private PharmaDeliveriesApp pharmaDeliveriesApp;
    private CommonController commonController;
    private ServicePharmacy servicePharmacy;


    /**
     *
     */
    public ManagePharmaciesController() {
        this.pharmaDeliveriesApp = PharmaDeliveriesApp.getInstance();
        servicePharmacy = pharmaDeliveriesApp.getPharmacyService();
        this.commonController = new CommonController();
    }

    public LinkedList<PharmacyDTO> getPharmaciesByAdministrator() throws SQLException {
        if (this.pharmaDeliveriesApp.getCurrentSession() == null) {
            return null;
        }

        Iterable<Pharmacy> tmp = servicePharmacy.getPharmaciesByAdministrator(this.pharmaDeliveriesApp.getCurrentSession().getUser());
        if (tmp == null) {
            return null;
        }
        LinkedList<PharmacyDTO> pharmacies = new LinkedList<>();
        for (Pharmacy p : tmp) {
           pharmacies.add(DTOConverter.convertPharmacy(p));
        }
        if (pharmacies.size() != 0) {
            return pharmacies;
        } else {
            return null;
        }
    }

    public LinkedList<PharmacyDTO> getAllPharmacies() throws SQLException {
        Iterable<Pharmacy> tmp = servicePharmacy.getAllPharmacies();
        if (tmp == null) {
            return null;
        }
        LinkedList<PharmacyDTO> pharmacies = new LinkedList<>();
        for (Pharmacy p : tmp) {
            pharmacies.add(DTOConverter.convertPharmacy(p));
        }
        if (pharmacies.size() != 0) {
            return pharmacies;
        } else {
            return null;
        }
    }
    
    public List<AddressDTO> getAllAddress() {
        return commonController.getAllAddress();
    }

    public PharmacyDTO insertPharmacy(PharmacyDTO pharmacy) throws SQLException {
        if (pharmacy == null) {
            return null;
        }
        return convertPharmacy(servicePharmacy.insertPharmacy(convertPharmacyDTO(pharmacy)));
    }

    public PharmacyDTO updatePharmacy(PharmacyDTO pharmacy) throws SQLException {
        if (pharmacy == null) {
            return null;
        }
        return convertPharmacy(servicePharmacy.updatePharmacy(convertPharmacyDTO(pharmacy)));
    }

    public boolean removePharmacy(PharmacyDTO pharmacy) throws SQLException {
        if (pharmacy == null) {
            return false;
        }
        return servicePharmacy.removePharmacy(convertPharmacyDTO(pharmacy));
    }

}
