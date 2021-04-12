package lapr.project.utils;

public class Constants {

    /**
     * List of defined constants
     */
    //public final static String PLATFORM_NAME = "PharmaDeliveriesLda";

    //public final static String INPUT_FILES_DIRECTORY = "C:\\ARQCP\\partilha\\Park_system\\output_files\\";
    public final static String INPUT_FILES_DIRECTORY = "src/main/resources/";
    public final static String OUTPUT_FILES_DIRECTORY = "src/main/resources/";
    //public final static String OUTPUT_FILES_DIRECTORY = "C:\\ARQCP\\partilha\\Park_system\\input_files\\";

    //Credentials used in tests
    public final static String TEST_ADMIN_EMAIL = "admin1@isep.ipp.pt";
    public final static String TEST_ADMIN_KEY = "admin1";
    public final static String TEST_CLIENT_EMAIL = "client10@isep.ipp.pt";
    public final static String TEST_CLIENT_KEY = "client10";
    public final static String TEST_COURIER_EMAIL = "courier1@isep.ipp.pt";
    public final static String TEST_COURIER_KEY = "courier1";


    //User Role
    public static final String ROLE_ADMINISTRATOR = "Administrator";
    public static final String ROLE_CLIENT = "Client";
    public static final String ROLE_COURIER = "Courier";
    public static final String ROLE_DISABLED = "Disabled";

    //UIs titles
    public static final String MAIN_APP_FILE = "/fxml/MainAppScene.fxml";
    public static final String MAIN_APP_TITLE = "Ride Sharing";

    public static final String PARAMS_FILE = "src/main/resources/application.properties";

    public static final String LOGIN_FILE = "/fxml/LoginScene.fxml";
    public static final String LOGIN_TITLE = "Authentication";

    public static final String SIGNUP_FILE = "/fxml/SignUpScene.fxml";
    public static final String SIGNUP_TITLE = "Sign Up";

    public static final String TAB_MANAGE_COURIERS_FILE = "/fxml/TabManageCouriersScene.fxml";
    public static final String TAB_MANAGE_COURIERS_TITLE = "Couriers";

    public static final String TAB_MANAGE_DELIVERIES_FILE = "/fxml/TabManageDeliveriesScene.fxml";
    public static final String TAB_MANAGE_DELIVERIES_TITLE = "Deliveries";
    
    public static final String TAB_MANAGE_DELIVERIES_ESTIMATE_FILE = "/fxml/TabManageDeliveriesEstimateScene.fxml";
    public static final String TAB_MANAGE_DELIVERIES_ESTIMATE_TITLE = "Cost Analysis";

    public static final String TAB_MANAGE_ORDERS_FILE = "/fxml/TabManageOrdersScene.fxml";
    public static final String TAB_MANAGE_ORDERS_TITLE = "Purchase";

    public static final String TAB_MANAGE_PHARMACIES_FILE = "/fxml/TabManagePharmaciesScene.fxml";
    public static final String TAB_MANAGE_PHARMACIES_TITLE = "Pharmacies";

    public static final String TAB_MANAGE_SCOOTERS_FILE = "/fxml/TabManageScootersScene.fxml";
    public static final String TAB_MANAGE_SCOOTERS_TITLE = "Scooters";

    public static final String TAB_MANAGE_DRONES_FILE = "/fxml/TabManageDronesScene.fxml";
    public static final String TAB_MANAGE_DRONES_TITLE = "Drones";

    public static final String TAB_MANAGE_STOCKS_FILE = "/fxml/TabManageStocksScene.fxml";
    public static final String TAB_MANAGE_STOCKS_TITLE = "Stock";

    public static final String CSS_FILE_STYLES = "/styles/Styles.css";

    //Messages Titles
    public static final String MSG_APPLICATION = "Application";
    public static final String MSG_ROBLEM_AT_START_APP = "Problem at start application";
    public static final String MSG_CONFIRM = "Confirm";
    public static final String MSG_ARE_SURE_EXIT = "Are you sure you wish to exit application?";

    //Pharmacy
    public static final int DELIVERY_FEE = 3;
    public static final int VAT = 23;
    
    
}
