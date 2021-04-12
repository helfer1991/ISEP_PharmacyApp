/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.utils.Constants;

/**
 * FXML Controller class
 *
 * @author 1100241
 */
public class MainAppUI implements Initializable {

//    @FXML
//    private MenuItem menuItemQuit;
//    @FXML
//    private MenuItem menuItemAbout;
    @FXML
    private TabPane tabPane;

    @FXML
    private MenuItem menuItemLogin;

    @FXML
    private MenuItem menuItemLogout;
    @FXML
    private Menu menuAccount;

    private TabManageCouriersUI tabManageCouriersUI;
    private TabManageDeliveriesUI tabManageDeliveriesUI;
    private TabManageDeliveriesEstimateUI tabManageDeliveriesEstimateUI;
    private TabManageOrdersUI tabManageOrdersUI;
    private TabManagePharmaciesUI tabManagePharmaciesUI;
    private TabManageScootersUI tabManageScootersUI;
    private TabManageDronesUI tabManageDronesUI;
    private TabManageStocksUI tabManageStocksUI;
    private LoginUI loginUI;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadTabsByUserRole(null);
        enableLogin();
    }

    public void loadTabsByUserRole(String role) {

        try {
            unloadAllTabs();
            if (role == null) {
                loadTab(Constants.TAB_MANAGE_ORDERS_FILE, Constants.TAB_MANAGE_ORDERS_TITLE);
                tabManageOrdersUI.btnBuyAction.setDisable(true);
            } else if (role.equals(Constants.ROLE_CLIENT)) {
                loadTab(Constants.TAB_MANAGE_ORDERS_FILE, Constants.TAB_MANAGE_ORDERS_TITLE);
                tabManageOrdersUI.btnBuyAction.setDisable(false);
                tabManageOrdersUI.btnAddProduct.setDisable(false);
                tabManageOrdersUI.btnRemoveProduct.setDisable(false);
                tabManageOrdersUI.loadClient();
            } else if (role.equals(Constants.ROLE_ADMINISTRATOR)) {
                loadTab(Constants.TAB_MANAGE_PHARMACIES_FILE, Constants.TAB_MANAGE_PHARMACIES_TITLE);
                loadTab(Constants.TAB_MANAGE_STOCKS_FILE, Constants.TAB_MANAGE_STOCKS_TITLE);
                loadTab(Constants.TAB_MANAGE_SCOOTERS_FILE, Constants.TAB_MANAGE_SCOOTERS_TITLE);
                loadTab(Constants.TAB_MANAGE_DRONES_FILE, Constants.TAB_MANAGE_DRONES_TITLE);
                loadTab(Constants.TAB_MANAGE_COURIERS_FILE, Constants.TAB_MANAGE_COURIERS_TITLE);
                loadTab(Constants.TAB_MANAGE_DELIVERIES_ESTIMATE_FILE, Constants.TAB_MANAGE_DELIVERIES_ESTIMATE_TITLE);
                //loadTab(Constants.TAB_MANAGE_ORDERS_FILE, Constants.TAB_MANAGE_ORDERS_TITLE);
            } else {
                loadTab(Constants.TAB_MANAGE_DELIVERIES_FILE, Constants.TAB_MANAGE_DELIVERIES_TITLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unloadAllTabs() {

        try {
            tabPane.getTabs().removeAll(tabPane.getTabs());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void menuItemQuitAction(ActionEvent event) {
        //Close current stage
        Stage currentStage = (Stage) tabPane.getScene().getWindow();

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);

        alerta.setTitle(Constants.MSG_APPLICATION);
        alerta.setHeaderText(Constants.MSG_CONFIRM);
        alerta.setContentText(Constants.MSG_ARE_SURE_EXIT);

        if (alerta.showAndWait().get() == ButtonType.OK) {
            currentStage.close();
            System.exit(0);
        }

    }

    @FXML
    private void menuItemAboutAction(ActionEvent event) {
        try {
            URI uri = new URI("https://bitbucket.org/lei-isep/lapr3-2020-g019/src/master/");
            java.awt.Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, Constants.MSG_APPLICATION, "It was not possible to access website.").show();
        }
    }

    @FXML
    private void menuItemLoginAction(Event event) {
        try {
            //Inciar stage Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.LOGIN_FILE));
            Parent root = loader.load();
            loginUI = loader.getController();
            loginUI.associateParentUI(this);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(Constants.CSS_FILE_STYLES);
            stage.setTitle(Constants.LOGIN_TITLE);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException ex) {
            AlertsUI.createErrorAlert(ex, "Login", "Error at Login").show();
        }
    }

    @FXML
    private void menuItemLogoutAction(ActionEvent event) {
        //dologout
        loginUI.getLoginController().doLogout();
        loadTabsByUserRole(null);
        enableLogin();
    }

    private void loadTab(String TAB_FILE, String TAB_TITLE) {

        Tab t = null;
        switch (TAB_FILE) {
            case (Constants.TAB_MANAGE_COURIERS_FILE):
                tabManageCouriersUI = (TabManageCouriersUI) loadFXMLTab(TAB_FILE, TAB_TITLE);
                tabManageCouriersUI.associateParentUI(this);
                break;
            case (Constants.TAB_MANAGE_DELIVERIES_FILE):
                tabManageDeliveriesUI = (TabManageDeliveriesUI) loadFXMLTab(TAB_FILE, TAB_TITLE);
                tabManageDeliveriesUI.associateParentUI(this);
                break;
            case (Constants.TAB_MANAGE_DELIVERIES_ESTIMATE_FILE):
                tabManageDeliveriesEstimateUI = (TabManageDeliveriesEstimateUI) loadFXMLTab(TAB_FILE, TAB_TITLE);
                tabManageDeliveriesEstimateUI.associateParentUI(this);
                break;
            case (Constants.TAB_MANAGE_ORDERS_FILE):
                tabManageOrdersUI = (TabManageOrdersUI) loadFXMLTab(TAB_FILE, TAB_TITLE);
                tabManageOrdersUI.associateParentUI(this);
                break;
            case (Constants.TAB_MANAGE_SCOOTERS_FILE):
                tabManageScootersUI = (TabManageScootersUI) loadFXMLTab(TAB_FILE, TAB_TITLE);
                tabManageScootersUI.associateParentUI(this);
                break;
            case (Constants.TAB_MANAGE_DRONES_FILE):
                tabManageDronesUI = (TabManageDronesUI) loadFXMLTab(TAB_FILE, TAB_TITLE);
                tabManageDronesUI.associateParentUI(this);
                break;
            case (Constants.TAB_MANAGE_STOCKS_FILE):
                tabManageStocksUI = (TabManageStocksUI) loadFXMLTab(TAB_FILE, TAB_TITLE);
                tabManageStocksUI.associateParentUI(this);
                break;
            case (Constants.TAB_MANAGE_PHARMACIES_FILE):
                tabManagePharmaciesUI = (TabManagePharmaciesUI) loadFXMLTab(TAB_FILE, TAB_TITLE);
                tabManagePharmaciesUI.associateParentUI(this);
                break;
            default:
                break;
        }
    }

    private Object loadFXMLTab(String fileFXML, String titleFXML) {
        try {
            Tab tab = new Tab();
            tab.setText(titleFXML);
            tabPane.getTabs().add(tab);

            //Load tab    
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fileFXML));
            tab.setContent(loader.load());

            //Return controller from loaded fxml
            Object obj = loader.getController();

            return obj;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void syncPharmaciesWithOtherViews(ObservableList<PharmacyDTO> pharmacies) {
        tabManageCouriersUI.refreshPharmacies(pharmacies);
        tabManageScootersUI.refreshPharmacies(pharmacies);
        tabManageDronesUI.refreshPharmacies(pharmacies);
        tabManageStocksUI.refreshPharmacies(pharmacies);
        tabManageDeliveriesEstimateUI.refreshPharmacies(pharmacies);
    }

    public void enableLogout(String user) {
        menuAccount.setText(user);
        menuItemLogin.setDisable(true);
        menuItemLogout.setDisable(false);
    }

    public void enableLogin() {
        menuAccount.setText("Account");
        menuItemLogin.setDisable(false);
        menuItemLogout.setDisable(true);
    }
}
