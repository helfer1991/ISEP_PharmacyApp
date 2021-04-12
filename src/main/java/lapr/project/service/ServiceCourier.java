package lapr.project.service;

import java.sql.SQLException;
import lapr.project.data.CourierDB;
import lapr.project.model.Courier;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.Pharmacy;
import static lapr.project.utils.Constants.ROLE_ADMINISTRATOR;


/**
 *
 * @author catarinaserrano
 */
public class ServiceCourier {
    CourierDB courierdb;
    
    public ServiceCourier(){
       courierdb = new CourierDB();
    }
    
    public boolean insertCourier(Courier courier, Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole() == null || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)
                || pharmacy==null|| courier==null) {
            return false;
        }
        return courierdb.insertCourier(courier, pharmacy);
        
    }
    
    public Iterable<Courier> getCouriesFromPharmacy(Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole() == null || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)) {
            return null;
        }
        return courierdb.getWorkingCouriersByPharmacyId(pharmacy.getId());
    }

    public boolean updateCourier(Courier courier, Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole() == null || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)
                || pharmacy==null|| courier==null) {
            return false;
        }
        return courierdb.updateCourier(courier, pharmacy);
    }

    public boolean removeCourier(Courier courier, Pharmacy pharmacy) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole() == null || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMINISTRATOR)
                || pharmacy==null|| courier==null) {
            return false;
        }
        courier.setIsWorking(Boolean.FALSE);
        return courierdb.updateCourier(courier, pharmacy);
    }
    
    public void setCourierDB(CourierDB courierDB) {
        this.courierdb = courierDB;
    }
    
    
}
