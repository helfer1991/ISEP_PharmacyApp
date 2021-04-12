/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lapr.project.controller.ManageDeliveryRunController;
import lapr.project.controller.ManagePharmaciesController;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.DeliverableDTO;
import lapr.project.ui.dto.DeliveryRunDTO;
import lapr.project.ui.dto.EstimatesDTO;
import lapr.project.ui.dto.PharmacyDTO;

/**
 * FXML Controller class
 *
 * @author 1100241
 */
public class TabManageDeliveriesEstimateUI implements Initializable {

    @FXML
    private TableView<Map.Entry<AddressDTO, Double>> tableViewOrdersDrone;
    @FXML
    private TableColumn<Map.Entry<AddressDTO, Double>, Double> tableViewOrdersDroneColumnEnergyCost;
    @FXML
    private TableColumn<Map.Entry<AddressDTO, Double>, String> tableViewOrdersDroneColumnAddress;
    @FXML
    private TableView<Map.Entry<AddressDTO, Double>> tableViewOrdersScooter;
    @FXML
    private TableColumn<Map.Entry<AddressDTO, Double>, Double> tableViewOrdersScooterColumnEnergyCost;
    @FXML
    private TableColumn<Map.Entry<AddressDTO, Double>, String> tableViewOrdersScooterColumnAddress;
    @FXML
    private TableView<Map.Entry<AddressDTO, Double>> tableViewOrdersDroneDist;
    @FXML
    private TableColumn<Map.Entry<AddressDTO, Double>, String> tableViewOrdersDroneColumnAddressDist;
    @FXML
    private TableColumn<Map.Entry<AddressDTO, Double>, Double> tableViewOrdersDroneColumnEnergyCostDist;
    @FXML
    private TableView<Map.Entry<AddressDTO, Double>> tableViewOrdersScooterDist;
    @FXML
    private TableColumn<Map.Entry<AddressDTO, Double>, String> tableViewOrdersScooterColumnAddressDist;
    @FXML
    private TableColumn<Map.Entry<AddressDTO, Double>, Double> tableViewOrdersScooterColumnEnergyCostDist;
    @FXML
    private TableView<DeliverableDTO> tableViewOrders;
    @FXML
    private TableColumn<DeliverableDTO, Integer> tableViewOrdersColumnOrderId;
    @FXML
    private TableColumn<DeliverableDTO, String> tableViewOrdersColumnAddress;
    @FXML
    private ComboBox<PharmacyDTO> comboBoxPharmacies;
    @FXML
    private TableView<DeliveryRunDTO> tableViewDeliveries;
    @FXML
    private TableColumn<DeliveryRunDTO, String> tableViewDeliveriesColumnDate;
    @FXML
    private TableColumn<DeliveryRunDTO, Float> tableViewDeliveriesColumnWeight;
    @FXML
    private TextField txtTotalScooter;
    @FXML
    private TextField txtTotalDrone;
    @FXML
    private TextField txtTotalScooterDist;
    @FXML
    private TextField txtTotalDroneDist;
    @FXML
    private TextField timeScooter;
    @FXML
    private TextField timeDrone;

    private MainAppUI mainAppUI;

    private PharmacyDTO selectedPharmacy;

    private ManagePharmaciesController managePharmaciesController;

    private ManageDeliveryRunController manageDeliveryRunController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Instance controller
        managePharmaciesController = new ManagePharmaciesController();
        manageDeliveryRunController = new ManageDeliveryRunController();

        selectedPharmacy = null;

        //Non editable mode
        tableViewDeliveries.setEditable(false);
        tableViewOrdersDrone.setEditable(false);
        tableViewOrdersScooter.setEditable(false);

        //Set columns Delivery
        tableViewDeliveriesColumnDate.setCellValueFactory(new PropertyValueFactory<DeliveryRunDTO, String>("date"));
        tableViewDeliveriesColumnDate.setSortable(false);
        tableViewDeliveriesColumnWeight.setCellValueFactory(new PropertyValueFactory<DeliveryRunDTO, Float>("weight"));
        tableViewDeliveriesColumnWeight.setSortable(false);

        //Set columns Orders
        tableViewOrdersColumnOrderId.setSortable(false);
        tableViewOrdersColumnOrderId.setCellValueFactory(p -> {
            return new SimpleIntegerProperty(p.getValue().getId()).asObject();
        });
        tableViewOrdersColumnAddress.setSortable(false);
        tableViewOrdersColumnAddress.setCellValueFactory(p -> {
            return new SimpleStringProperty(p.getValue().getAddress().toString());
        });

        //Set columns best path drone
        tableViewOrdersDroneColumnAddress.setSortable(false);
        tableViewOrdersDroneColumnAddress.setCellValueFactory(p -> {
            return new SimpleStringProperty(p.getValue().getKey().toString());
        });
        tableViewOrdersDroneColumnEnergyCost.setSortable(false);
        tableViewOrdersDroneColumnEnergyCost.setCellValueFactory(p -> {
            return new SimpleDoubleProperty(Math.round(p.getValue().getValue() * 1000)).asObject();
        });

        //Set columns best path scooter
        tableViewOrdersScooterColumnAddress.setSortable(false);
        tableViewOrdersScooterColumnAddress.setCellValueFactory(p -> {
            return new SimpleStringProperty(p.getValue().getKey().toString());
        });
        tableViewOrdersScooterColumnEnergyCost.setSortable(false);
        tableViewOrdersScooterColumnEnergyCost.setCellValueFactory(p -> {
            return new SimpleDoubleProperty(Math.round(p.getValue().getValue() * 1000)).asObject();
        });

        //Set columns best path drone distance
        tableViewOrdersDroneColumnAddressDist.setSortable(false);
        tableViewOrdersDroneColumnAddressDist.setCellValueFactory(p -> {
            return new SimpleStringProperty(p.getValue().getKey().toString());
        });
        tableViewOrdersDroneColumnEnergyCostDist.setSortable(false);
        tableViewOrdersDroneColumnEnergyCostDist.setCellValueFactory(p -> {
            return new SimpleDoubleProperty(Math.round(p.getValue().getValue())).asObject();
        });

        //Set columns best path scooter distance
        tableViewOrdersScooterColumnAddressDist.setSortable(false);
        tableViewOrdersScooterColumnAddressDist.setCellValueFactory(p -> {
            return new SimpleStringProperty(p.getValue().getKey().toString());
        });
        tableViewOrdersScooterColumnEnergyCostDist.setSortable(false);
        tableViewOrdersScooterColumnEnergyCostDist.setCellValueFactory(p -> {
            return new SimpleDoubleProperty(Math.round(p.getValue().getValue())).asObject();
        });

        //Get selected delivey
        tableViewDeliveries.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearTableViews();
                DeliveryRunDTO object = tableViewDeliveries.getSelectionModel().getSelectedItem();
                EstimatesDTO estimatesScooter = null;
                EstimatesDTO estimatesDrone = null;
                ObservableList<DeliverableDTO> deliverables;
                ObservableList<Map.Entry<AddressDTO, Double>> deliverablesScooter;
                ObservableList<Map.Entry<AddressDTO, Double>> deliverablesDrone;

                if (object != null && selectedPharmacy != null) {

                    try {
                        estimatesScooter = manageDeliveryRunController.estimateScooterDeliveryCosts(selectedPharmacy, object);
                        estimatesDrone = manageDeliveryRunController.estimateDroneDeliveryCosts(selectedPharmacy, object);

                    } catch (SQLException ex) {
                        AlertsUI.createErrorAlert(ex, "Error at loading estimate", "Error");
                    }
                    Set<Map.Entry<AddressDTO, Double>> tmpScooters = estimatesScooter.getEnergyCostMapDTO().entrySet();
                    if (tmpScooters != null && !tmpScooters.isEmpty()) {
                        deliverablesScooter = FXCollections.observableArrayList(tmpScooters);
                        tableViewOrdersScooter.setItems(deliverablesScooter);
                    }
                    Set<Map.Entry<AddressDTO, Double>> tmpDrone = estimatesDrone.getEnergyCostMapDTO().entrySet();
                    if (tmpDrone != null && !tmpDrone.isEmpty()) {
                        deliverablesDrone = FXCollections.observableArrayList(tmpDrone);
                        tableViewOrdersDrone.setItems(deliverablesDrone);
                    }
                    Set<Map.Entry<AddressDTO, Double>> tmpDistScooters = estimatesScooter.getDistanceCostMapDTO().entrySet();
                    if (tmpDistScooters != null && !tmpDistScooters.isEmpty()) {
                        deliverablesScooter = FXCollections.observableArrayList(tmpDistScooters);
                        tableViewOrdersScooterDist.setItems(deliverablesScooter);
                    }
                    Set<Map.Entry<AddressDTO, Double>> tmpDistDrone = estimatesDrone.getDistanceCostMapDTO().entrySet();
                    if (tmpDistDrone != null && !tmpDistDrone.isEmpty()) {
                        deliverablesDrone = FXCollections.observableArrayList(tmpDistDrone);
                        tableViewOrdersDroneDist.setItems(deliverablesDrone);
                    }
                    List<DeliverableDTO> tmpDeliverable = object.getDeliverables();
                    if (tmpDeliverable != null && !tmpDeliverable.isEmpty()) {
                        deliverables = FXCollections.observableArrayList(tmpDeliverable);
                        tableViewOrders.setItems(deliverables);
                    }
                    txtTotalDrone.setText(Math.round(estimatesDrone.getRequiredBatteryToCompletePathDTO() * 1000) + "");
                    txtTotalScooter.setText(Math.round(estimatesScooter.getRequiredBatteryToCompletePathDTO() * 1000) + "");
                    txtTotalDroneDist.setText(Math.round(estimatesDrone.getDistanceTotalLenghDTO()) + "");
                    txtTotalScooterDist.setText(Math.round(estimatesScooter.getDistanceTotalLenghDTO()) + "");
                    timeScooter.setText(estimatesScooter.getTimeDurationDTO() + "");
                    timeDrone.setText(estimatesDrone.getTimeDurationDTO() + "");
                }
            }
        });

        loadPharmacy();
    }

    @FXML
    private void comboBoxPharmaciesActon(ActionEvent event) {
        tableViewDeliveries.getItems().clear();
        tableViewOrdersDrone.getItems().clear();
        tableViewOrdersScooter.getItems().clear();
        txtTotalDrone.clear();
        txtTotalScooter.clear();
        if (!comboBoxPharmacies.getItems().isEmpty()) {
            selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
            if (selectedPharmacy == null) {
                comboBoxPharmacies.getSelectionModel().select(0);
                selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
            }
            loadDeliveriesByPharmacy(comboBoxPharmacies.getSelectionModel().getSelectedItem());
            selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
        }
    }

    public void loadDeliveriesByPharmacy(PharmacyDTO pharmacyDTO) {

        try {
            clearTableViews();
            tableViewDeliveries.getItems().clear();
            List<DeliveryRunDTO> tmp = new LinkedList<>();
            tmp = manageDeliveryRunController.fetchAvailableDeliveryRuns(pharmacyDTO);
            if (tmp != null) {
                ObservableList<DeliveryRunDTO> DeliveriesList = FXCollections.observableArrayList(tmp);
                tableViewDeliveries.setItems(DeliveriesList);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Deliveries ", e.getMessage()).show();
        }
    }

    public void loadPharmacy() {

        try {
            List<PharmacyDTO> tmp = new LinkedList<>();
            tmp = managePharmaciesController.getPharmaciesByAdministrator();
            if (tmp != null) {
                ObservableList<PharmacyDTO> pharmaciesList = FXCollections.observableArrayList(tmp);
                comboBoxPharmacies.setItems(pharmaciesList);
                comboBoxPharmacies.getSelectionModel().select(0);
                comboBoxPharmaciesActon(null);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Pharmacies", e.getMessage()).show();
        }
    }

    public void refreshPharmacies(ObservableList<PharmacyDTO> pharmacies) {
        comboBoxPharmacies.setItems(pharmacies);
        if (!comboBoxPharmacies.getItems().isEmpty()) {
            comboBoxPharmacies.getSelectionModel().select(0);
        }
    }

    public void associateParentUI(MainAppUI mainAppUI) {
        this.mainAppUI = mainAppUI;
    }

    public void clearTableViews() {
        timeScooter.clear();
        timeDrone.clear();
        tableViewOrdersScooter.getItems().clear();
        tableViewOrdersDrone.getItems().clear();
        tableViewOrdersScooterDist.getItems().clear();
        tableViewOrdersDroneDist.getItems().clear();
        tableViewOrders.getItems().clear();
        txtTotalDrone.clear();
        txtTotalScooter.clear();
        txtTotalDroneDist.clear();
        txtTotalScooterDist.clear();
    }
}
