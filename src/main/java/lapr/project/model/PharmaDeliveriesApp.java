package lapr.project.model;

import lapr.project.service.*;
import lapr.project.model.autorization.AutorizationFacade;
import lapr.project.model.autorization.UserSession;
import lapr.project.service.ServiceClient;
import lapr.project.service.ServiceDeliveryRun;
import lapr.project.service.ServiceVehicle;
import lapr.project.service.ServiceStorage;
import lapr.project.utils.Configuration;

import static lapr.project.utils.Constants.PARAMS_FILE;

/**
 *
 *
 */
public class PharmaDeliveriesApp {

    /**
     *
     */
    private final AutorizationFacade autorizationInterface;
    private static PharmaDeliveriesApp pharmaDeliveriesAppSingleton = null;
    private final ServicePharmacy servicePharmacy;
    private final ServiceCourier serviceCourier;
    private final ServiceVehicle serviceVehicle;
    private final ServiceClient serviceClient;
    private final ServiceStorage serviceStorage;
    private final ServiceDeliveryRun serviceDeliveryRun;
    private final ServiceTransfer serviceTransfer;
    private final ServiceBackOrder serviceBackOrder;
    private final ServiceCommon serviceCommon;
    private final ServiceParkSystemFiles serviceParkSystemFiles;
    private  ServiceEmail serviceEmail;
    private final ServiceEstimate serviceEstimate;
    
    private final  GraphDelivery terrestrialGraph;
    private final  GraphDelivery aerialGraph;
    private final  GraphDelivery distanceTerrestrialGraph;
    private final  GraphDelivery distanceAerialGraph;

    /**
     *
     */
    private PharmaDeliveriesApp() {
        
        /*Services*/
        this.autorizationInterface = new AutorizationFacade();
        this.servicePharmacy = new ServicePharmacy();
        this.serviceCourier = new ServiceCourier();
        this.serviceVehicle = new ServiceVehicle();
        this.serviceClient = new ServiceClient();
        this.serviceStorage = new ServiceStorage();
        this.serviceDeliveryRun = new ServiceDeliveryRun();
        this.serviceTransfer = new ServiceTransfer();
        this.serviceBackOrder = new ServiceBackOrder();
        this.serviceCommon = new ServiceCommon();
        this.serviceParkSystemFiles = new ServiceParkSystemFiles();
        this.serviceEstimate = new ServiceEstimate();
        
        /*Graphs*/
        this.terrestrialGraph = new GraphDelivery();
        this.aerialGraph = new GraphDelivery();
        this.distanceTerrestrialGraph = new GraphDelivery();
        this.distanceAerialGraph = new GraphDelivery();
        
        terrestrialGraph.loadTerrestrialRestrictions();
        distanceTerrestrialGraph.loadTerrestrialRestrictions();
        aerialGraph.loadAirRestrictions();
        distanceAerialGraph.loadAirRestrictions();
        
        terrestrialGraph.createGraph();
        aerialGraph.createGraph();
        distanceTerrestrialGraph.createGraph();
        distanceAerialGraph.createGraph();
        
        distanceTerrestrialGraph.loadDistanceIntoEdges();
        distanceAerialGraph.loadDistanceIntoEdges();
    }

    /**
     * Get unique instance of PharmaDeliveries.
     *
     * @return PharmaDeliveries
     */
    public static PharmaDeliveriesApp getInstance() {
        if (pharmaDeliveriesAppSingleton == null) {
            synchronized (PharmaDeliveriesApp.class) {
                pharmaDeliveriesAppSingleton = new PharmaDeliveriesApp();
            }
        }
        return pharmaDeliveriesAppSingleton;
    }

    /**
     *
     * @return
     */
    public ServicePharmacy getPharmacyService() {
       return servicePharmacy;
    }

    /**
     *
     * @return
     */
    public ServiceCommon getServiceCommon() {
        return serviceCommon;
    }

    /**
    * This method returns a instance of a service for Storage objects to manage
    * data to db.
    * @return ServiceStorage instance
    */
    public ServiceStorage getServiceStorage() {
        return serviceStorage;
    }
    
    /**
     * This method returns a instance of a service to insert the objects into the db
     * @return courierRegistry 
     */
    public ServiceCourier getServiceCourier() {
        return serviceCourier;
    }
    
    /**
     * This method returns a instance of a service to insert the objects into the db
     * @return scooterRegistry 
     */
    public ServiceVehicle getServiceVehicle() {
        return serviceVehicle;
    }
    

    /**
     * This method returns a instance of a service to insert the objects into the db
     * @return clientRegistry 
     */
    public ServiceClient getServiceClient() {
        return serviceClient;
    }
    
    
    /**
     * Returns the current session in the system
     *
     * @return currentSession
     */
    public UserSession getCurrentSession() {
        return this.autorizationInterface.getCurrentSession();
    }

    public ServiceDeliveryRun getServiceDelivery() {
        return serviceDeliveryRun;
    }

    /**
     * Returns instance of class ServiceInstance.
     * @return serviceTransfer instance
     */
    public ServiceTransfer getServiceTransfer() {
        return serviceTransfer;
    }

    public ServiceBackOrder getServiceBackOrder() {
        return serviceBackOrder;
    }
    
    /**
     * If ServiceEmail instance is not initialized, reads properties files and instantiates apropriate email adapter
     * @return the ServiceEmail instance to be used throughout the application
     */
    public ServiceEmail getServiceEmail() {

        if(serviceEmail != null){
            return this.serviceEmail;
        }

        try {
           
            String classe = Configuration.getEmailServiceClass();

            Class<?> oClass = Class.forName(classe);
            ServiceEmail oEmailService = (ServiceEmail) oClass.getDeclaredConstructor().newInstance();

            this.serviceEmail = oEmailService;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.serviceEmail;
    }

    
    public GraphDelivery getTerrestrialGraph() {
        return terrestrialGraph;
    }
    
    public GraphDelivery getAerialGraph() {
        return aerialGraph;
    }

    public ServiceParkSystemFiles getServiceParkSystemFiles() {
        return serviceParkSystemFiles;
    }

    public GraphDelivery getDistanceTerrestrialGraph() {
        return distanceTerrestrialGraph;
    }

    public GraphDelivery getDistanceAerialGraph() {
        return distanceAerialGraph;
    }
    
    

    /**
     * Do a login with the received parameters
     *
     * @param strEmail the user email
     * @param strPwd the user password
     * @return boolean
     */
    public boolean doLogin(String strEmail, String strPwd) {
        return this.autorizationInterface.doLogin(strEmail, strPwd) != null;
    }

    /**
     * Logouts off the system
     */
    public void doLogout() {
        this.autorizationInterface.doLogout();
    }


    
    
}
