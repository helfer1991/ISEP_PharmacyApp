/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lapr.project.controller.ManagePharmaciesController;
import lapr.project.controller.ManageVehiclesController;
import lapr.project.ui.dto.DroneDTO;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.utils.Utils;

/**
 * FXML Controller class
 *
 * @author 1100241
 */
public class TabManageDronesUI implements Initializable {

    @FXML
    private TextField txtDroneWeight;
    @FXML
    private Button btnRemoveDrone;
    @FXML
    private Button btnEditDrone;
    @FXML
    private Button btnAddDrone;
    @FXML
    private TextField txtDroneBatteryCapacity;
    @FXML
    private ImageView imageViewQRCode;
    @FXML
    private ComboBox<PharmacyDTO> comboBoxPharmacies;
    @FXML
    private TableView<DroneDTO> tableViewDrones;
    @FXML
    private TableColumn<DroneDTO, Integer> tableViewStocksColumnId;

    private MainAppUI mainAppUI;
    @FXML
    private TableColumn<DroneDTO, Integer> tableViewPayload;
    @FXML
    private TableColumn<DroneDTO, Boolean> tableViewActiveStatus;

    private ManageVehiclesController manageVehiclesController;
    private DroneDTO selectedDrone;
    private ManagePharmaciesController managePharmaciesController;
    private PharmacyDTO selectedPharmacy;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Instance controller
        managePharmaciesController = new ManagePharmaciesController();
        manageVehiclesController = new ManageVehiclesController();
        selectedPharmacy = null;

        //Non editable mode
        tableViewDrones.setEditable(false);

        //Set columns
        tableViewStocksColumnId.setCellValueFactory(new PropertyValueFactory<DroneDTO, Integer>("id"));
        tableViewPayload.setCellValueFactory(new PropertyValueFactory<DroneDTO, Integer>("weight"));
        tableViewActiveStatus.setCellValueFactory(new PropertyValueFactory<DroneDTO, Boolean>("isAvailable"));

        //Get selected scooter
        tableViewDrones.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                DroneDTO object = tableViewDrones.getSelectionModel().getSelectedItem();
                if (object != null) {
                    selectedDrone = object;
                    setTextFieldsTextDrone(object);
                }
            }
        });
        loadPharmacy();
    }

    @FXML
    private void btnRemoveDroneAction(ActionEvent event) {
        try {
            boolean sucess;
            sucess = manageVehiclesController.removeVehicle(selectedDrone, selectedPharmacy);

            if (sucess) {
                tableViewDrones.getItems().remove(selectedDrone);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at remove drone", e.getMessage()).show();
        }
    }

    @FXML
    private void btnEditDroneAction(ActionEvent event) {
        try {
            DroneDTO d = getDroneDTOFromTextFields();
            d.setId(selectedDrone.getId());
            d = (DroneDTO) manageVehiclesController.updateVehicle(d, selectedPharmacy);
            if (d != null) {
                tableViewDrones.getItems().set(tableViewDrones.getItems().indexOf(selectedDrone), d);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at edit drone", e.getMessage()).show();
        }
    }

    @FXML
    private void btnAddDroneAction(ActionEvent event) {
        try {
            DroneDTO d = (DroneDTO) manageVehiclesController.insertVehicle(getDroneDTOFromTextFields(), selectedPharmacy);

            if (d != null) {
                tableViewDrones.getItems().add(d);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at adding a drone", e.getMessage()).show();
        }
    }

    @FXML
    private void comboBoxPharmaciesActon(ActionEvent event) {
        tableViewDrones.getItems().clear();
        if (!comboBoxPharmacies.getItems().isEmpty()) {
            
            selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
                        if (selectedPharmacy==null){
                comboBoxPharmacies.getSelectionModel().select(0);
                selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
            }
            loadDronesByPharmacy(comboBoxPharmacies.getSelectionModel().getSelectedItem());
            selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
        }

    }

    public void associateParentUI(MainAppUI mainAppUI) {
        this.mainAppUI = mainAppUI;
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

    public void loadDronesByPharmacy(PharmacyDTO pharmacyDTO) {
        try {
            List<DroneDTO> tmp = new LinkedList<>();
            tmp = manageVehiclesController.getDrones(pharmacyDTO);
            if (tmp != null) {
                ObservableList<DroneDTO> DronesList = FXCollections.observableArrayList(tmp);
                tableViewDrones.setItems(DronesList);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Drones", e.getMessage()).show();
        }
    }

    private void setTextFieldsTextDrone(DroneDTO object) {
        txtDroneBatteryCapacity.setText(object.getBattery().getCapacity() + "");
        txtDroneWeight.setText(object.getWeight() + "");
        try {
            byte[] byteArray = QRCode.getQRCodeImage(object.getId() + "", 150, 150);
            Image img = new Image(new ByteArrayInputStream(byteArray, 0, byteArray.length));
            imageViewQRCode.setImage(img);
        } catch (Exception e) {
        }
    }

    private DroneDTO getDroneDTOFromTextFields() {
        DroneDTO tmp = new DroneDTO();
        tmp.setWeight(Utils.convertStringToInt(txtDroneWeight.getText()));
        tmp.getBattery().setCapacity(Utils.convertStringToInt(txtDroneBatteryCapacity.getText()));

        return tmp;
    }

    public void refreshPharmacies(ObservableList<PharmacyDTO> pharmacies) {
        comboBoxPharmacies.setItems(pharmacies);
        if (!comboBoxPharmacies.getItems().isEmpty()) {
            comboBoxPharmacies.getSelectionModel().select(0);
        }
    }

}
