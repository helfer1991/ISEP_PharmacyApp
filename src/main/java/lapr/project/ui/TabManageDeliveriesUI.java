/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lapr.project.controller.ManageDeliveryRunController;
import lapr.project.model.Scooter;
import lapr.project.ui.dto.*;

/**
 * FXML Controller class
 *
 * @author 1100241
 */
public class TabManageDeliveriesUI implements Initializable {

    @FXML
    private Button btnSetOrderAsDelivered;
    @FXML
    private Button btnPickAScooter;
    @FXML
    private Button btnParkScooter;
    @FXML
    private TableView<DeliveryRunDTO> tableViewDeliveries;
    @FXML
    private TableColumn<DeliveryRunDTO, Integer> tableViewDeliveriesColumnId;
    @FXML
    private TableColumn<DeliveryRunDTO, String> tableViewDeliveriesColumnDate;
    @FXML
    private ComboBox<VehicleDTO> comboBoxScooters;
    @FXML
    private TableView<AddressDTO> tableViewBestPath;
    @FXML
    private TableColumn<AddressDTO, Integer> tableViewBestPathColumnAddressId;
    @FXML
    private TableColumn<AddressDTO, String> tableViewBestPathColumnAddress;
    @FXML
    private TableView<DeliverableDTO> tableViewOrdersToDeliver;
    @FXML
    private TableColumn<DeliverableDTO, Integer> tableViewOrdersToDeliverColumnId;
    @FXML
    private TableColumn<DeliverableDTO, String> tableViewOrdersToDeliverColumDate;
    @FXML
    private TableView<DeliverableDTO> tableViewDeliveredOrders;
    @FXML
    private TableColumn<DeliverableDTO, Integer> tableViewDeliveredOrdersColumnOrderId;
    @FXML
    private TableColumn<DeliverableDTO, String> tableViewDeliveredOrdersColumnDate;
    @FXML
    private CheckBox checkBoxCorrectPark;

    private ManageDeliveryRunController manageDeliveryRunController;

    private MainAppUI mainAppUI;

    private DeliveryRunDTO selectedDeliveryRunDTO;

    private DeliverableDTO selectedOrder;

    private VehicleDTO selectedVehicle;

    private PharmacyDTO selectedPharmacy;

    private EstimatesDTO estimateDrone;

    private EstimatesDTO estimateScooter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //comboBoxParkSlot
        comboBoxScooters.setDisable(true);
        btnParkScooter.setDisable(true);
        checkBoxCorrectPark.setDisable(true);
        btnPickAScooter.setDisable(true);
        btnSetOrderAsDelivered.setDisable(true);

        //Instance controller
        manageDeliveryRunController = new ManageDeliveryRunController();

        try {
            selectedPharmacy = manageDeliveryRunController.getPharmacyByCourier();
            if (selectedPharmacy != null) {
                loadDeliveriesByPharmacy(selectedPharmacy);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at get courier pharmacy", e.getMessage()).show();
        }

        //Non editable mode
        tableViewBestPath.setSelectionModel(null);
        tableViewDeliveries.setEditable(false);
        tableViewDeliveredOrders.setEditable(false);
        tableViewOrdersToDeliver.setEditable(false);

        //Set columns Delivery
        tableViewDeliveriesColumnId.setCellValueFactory(new PropertyValueFactory<DeliveryRunDTO, Integer>("id"));
        tableViewDeliveriesColumnId.setSortable(false);
        tableViewDeliveriesColumnDate.setCellValueFactory(new PropertyValueFactory<DeliveryRunDTO, String>("date"));
        tableViewDeliveriesColumnDate.setSortable(false);

        //Set columns best paht
        tableViewBestPathColumnAddressId.setCellValueFactory(new PropertyValueFactory<AddressDTO, Integer>("id"));
        tableViewBestPathColumnAddressId.setSortable(false);
        tableViewBestPathColumnAddress.setCellValueFactory(new PropertyValueFactory<AddressDTO, String>("address"));
        tableViewBestPathColumnAddress.setSortable(false);

        //Set columns orders to Deliver
        tableViewOrdersToDeliverColumnId.setSortable(false);
        tableViewOrdersToDeliverColumnId.setCellValueFactory(p -> {
            return new SimpleIntegerProperty(p.getValue().getId()).asObject();
        });
        tableViewOrdersToDeliverColumDate.setSortable(false);
        tableViewOrdersToDeliverColumDate.setCellValueFactory(p -> {
            return new SimpleStringProperty(p.getValue().getAddress().toString());
        });
        
        //Set columns orders to Deliver
        tableViewDeliveredOrdersColumnOrderId.setSortable(false);
        tableViewDeliveredOrdersColumnOrderId.setCellValueFactory(p -> {
            return new SimpleIntegerProperty(p.getValue().getId()).asObject();
        });
        tableViewDeliveredOrdersColumnDate.setSortable(false);
        tableViewDeliveredOrdersColumnDate.setCellValueFactory(p -> {
            return new SimpleStringProperty(p.getValue().getAddress().toString());
        });

        //Get selected
        tableViewDeliveries.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                DeliveryRunDTO object = tableViewDeliveries.getSelectionModel().getSelectedItem();
                if (object != null) {
                    selectedDeliveryRunDTO = object;

                    //Get estimates
                    try {
                        estimateDrone = manageDeliveryRunController.estimateDroneDeliveryCosts(selectedPharmacy, selectedDeliveryRunDTO);
                        estimateScooter = manageDeliveryRunController.estimateScooterDeliveryCosts(selectedPharmacy, selectedDeliveryRunDTO);

                        //Get scooter
                        List<VehicleDTO> vehicles = manageDeliveryRunController.getAvailableVehiclesForDelivery(selectedPharmacy, estimateDrone);
                        if (vehicles != null && !vehicles.isEmpty()) {
                            ObservableList<VehicleDTO> tmp = FXCollections.observableArrayList(vehicles);
                            comboBoxScooters.setItems(tmp);
                        } else {
                            AlertsUI.createAlert(Alert.AlertType.INFORMATION, "", "", "Not available vehicles for this delivery");
                        }

                        //Enable buttons
                        comboBoxScooters.setDisable(false);
                        btnPickAScooter.setDisable(false);

                    } catch (Exception ex) {
                        comboBoxScooters.setDisable(true);
                        AlertsUI.createErrorAlert(ex, "Error", "Error at load estimate delivery cost").show();
                    }

                }
            }
        });
    }

    @FXML
    private void btnPickAScooterAction(ActionEvent event) {
        try {
            if (selectedDeliveryRunDTO != null) {

                //Go to controller registar no status
                selectedVehicle = comboBoxScooters.getSelectionModel().getSelectedItem();

                //Get Estimate
                if (selectedVehicle != null) {
                    tableViewDeliveries.setSelectionModel(null);
                    btnParkScooter.setDisable(false);
                    checkBoxCorrectPark.setDisable(false);
                    btnPickAScooter.setDisable(true);
                    btnSetOrderAsDelivered.setDisable(false);

                    ObservableList<DeliverableDTO> deliverables;
                    ObservableList<AddressDTO> addresses;
                    if (selectedDeliveryRunDTO != null && selectedPharmacy != null) {
                        try {
                            estimateDrone = manageDeliveryRunController.estimateDroneDeliveryCosts(selectedPharmacy, selectedDeliveryRunDTO);
                            estimateScooter = manageDeliveryRunController.estimateScooterDeliveryCosts(selectedPharmacy, selectedDeliveryRunDTO);
                        } catch (SQLException ex) {
                            AlertsUI.createErrorAlert(ex, "Error at loading estimate", "Error");
                        }
                        LinkedList<DeliverableDTO> tmp;
                        Set<AddressDTO> tmpAddressDTOs;
                        if (selectedVehicle instanceof ScooterDTO) {
                            tmpAddressDTOs = estimateScooter.getEnergyCostMapDTO().keySet();
                        } else {
                            tmpAddressDTOs = estimateDrone.getEnergyCostMapDTO().keySet();
                        }
                        tmp = selectedDeliveryRunDTO.getDeliverables();
                        if (tmp != null && !tmp.isEmpty()) {
                            deliverables = FXCollections.observableArrayList(tmp);
                            tableViewOrdersToDeliver.setItems(deliverables);
                        }
                        if (tmpAddressDTOs != null && !tmpAddressDTOs.isEmpty()) {
                            addresses = FXCollections.observableArrayList(tmpAddressDTOs);
                            tableViewBestPath.setItems(addresses);
                        }
                    }
                    if (selectedVehicle instanceof ScooterDTO) {
                        if (manageDeliveryRunController.startRun(selectedPharmacy, estimateScooter, selectedVehicle)) {
                            AlertsUI.createAlert(Alert.AlertType.INFORMATION, "Start", "Delivery start with success!", "").show();
                        }
                    } else {
                        if (manageDeliveryRunController.startRun(selectedPharmacy, estimateDrone, selectedVehicle)) {
                            AlertsUI.createAlert(Alert.AlertType.INFORMATION, "Start", "Delivery start with success!", "").show();
                        }

                    }
                }
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Pick Vehicle", e.getMessage()).show();
        }
       
    }

    @FXML
    private void btnParkScooterAction(ActionEvent event) {
        try {
            if (selectedOrder != null && selectedVehicle != null) {
                comboBoxScooters.setDisable(true);
                //Go to controller registar no status
                selectedVehicle = comboBoxScooters.getSelectionModel().getSelectedItem();

                if (tableViewOrdersToDeliver.getItems().isEmpty() && !tableViewDeliveredOrders.getItems().isEmpty()) {
                    loadDeliveriesByPharmacy(selectedPharmacy);
                        selectedVehicle.setActualCharge((int) (Math.random()*40) );
                    manageDeliveryRunController.parkVehicle(selectedPharmacy, selectedVehicle, !checkBoxCorrectPark.isSelected());

                }
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Register deliveries and Parking", e.getMessage()).show();
        }
    }

    @FXML
    private void btnSetOrderAsDeliveredAction(ActionEvent event) {

        try {
            selectedOrder = tableViewOrdersToDeliver.getSelectionModel().getSelectedItem();
            if (selectedOrder != null && selectedVehicle != null) {
                if (manageDeliveryRunController.setOrderAsDelivered(selectedOrder)) {
                    tableViewDeliveredOrders.getItems().add(selectedOrder);
                    tableViewDeliveredOrders.setItems(tableViewDeliveredOrders.getItems());
                    tableViewOrdersToDeliver.getItems().remove(selectedOrder);
                    tableViewOrdersToDeliver.setItems(tableViewOrdersToDeliver.getItems());
                }

            } else {
                AlertsUI.createAlert(Alert.AlertType.INFORMATION, "Scooter", "", "Select scooter").show();
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Set Order as Delivered", e.getMessage()).show();
        }

    }

    public void loadDeliveriesByPharmacy(PharmacyDTO pharmacyDTO) {

        try {
            tableViewBestPath.getItems().clear();
            tableViewOrdersToDeliver.getItems().clear();
            tableViewDeliveredOrders.getItems().clear();
            tableViewDeliveries.getItems().clear();
            List<DeliveryRunDTO> tmp = new LinkedList<>();
            tmp = manageDeliveryRunController.fetchAvailableDeliveryRuns(pharmacyDTO);
            if (tmp != null) {
                ObservableList<DeliveryRunDTO> DeliveriesList = FXCollections.observableArrayList(tmp);
                tableViewDeliveries.setItems(DeliveriesList);
            }
            comboBoxScooters.setDisable(true);
            btnParkScooter.setDisable(true);
            checkBoxCorrectPark.setDisable(true);
            btnPickAScooter.setDisable(true);
            btnSetOrderAsDelivered.setDisable(true);
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Deliveries ", e.getMessage()).show();
        }
    }

    public void associateParentUI(MainAppUI mainAppUI) {
        this.mainAppUI = mainAppUI;
    }

}
