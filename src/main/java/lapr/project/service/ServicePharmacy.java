package lapr.project.service;

import java.sql.SQLException;
import lapr.project.data.ClientDB;
import lapr.project.data.PharmacyDB;
import lapr.project.model.Client;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.Pharmacy;
import lapr.project.model.User;
import lapr.project.utils.Constants;
import static lapr.project.utils.Constants.ROLE_ADMINISTRATOR;
import lapr.project.utils.Utils;

public class ServicePharmacy {

    PharmacyDB pharmacyDB;
    ClientDB clientDB;

    public ServicePharmacy() {
        pharmacyDB = new PharmacyDB();
        clientDB = new ClientDB();
    }

    public Pharmacy insertPharmacy(Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null
                || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)
                || pharmacy == null) {
            return null;
        }
        return pharmacyDB.insertPharmacy(pharmacy, PharmaDeliveriesApp.getInstance().getCurrentSession().getUser());
    }

    public Pharmacy updatePharmacy(Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null
                || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)
                || pharmacy == null) {
            return null;
        }
        return pharmacyDB.updatePharmacy(pharmacy);
    }

    public boolean removePharmacy(Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null
                || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)
                || pharmacy == null) {
            return false;
        }
        return pharmacyDB.removePharmacy(pharmacy);
    }

    public Pharmacy getPharmacyByCourier(User courier) throws SQLException {
        if (courier == null || !courier.getRole().getDescription().equals(Constants.ROLE_COURIER)) {
            return null;
        }
        return pharmacyDB.getPharmacyByCourier(courier);
    }

    public Iterable<Pharmacy> getPharmaciesByAdministrator(User administrator) throws SQLException {
        if (administrator == null || !administrator.getRole().getDescription().equals(ROLE_ADMINISTRATOR)) {
            return null;
        }
        return pharmacyDB.getPharmaciesByAdministrator(administrator);
    }

    public Iterable<Pharmacy> getAllPharmacies() throws SQLException {
        return pharmacyDB.getAllPharmacies();
    }

    public void setPharmacyDB(PharmacyDB pharmacyDB) {
        this.pharmacyDB = pharmacyDB;
    }

}
