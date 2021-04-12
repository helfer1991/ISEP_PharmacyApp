/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

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
import javafx.scene.input.MouseEvent;
import lapr.project.controller.ManagePharmaciesController;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.ParkDTO;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.utils.Utils;

/**
 * FXML Controller class
 *
 * @author 1100241
 */
public class TabManagePharmaciesUI implements Initializable {

    @FXML
    private TextField txtPharmacyName;
    @FXML
    private TextField txtPharmacyMaxPayload;
    @FXML
    private TextField txtPharmacyMinLoad;
    @FXML
    private TextField txtPharmacyScooterParkCapacity;
    @FXML
    private TextField txtPharmacyDroneParkCapacity;
    @FXML
    private TextField txtPharmacyDroneChargerCapacity;
    @FXML
    private Button btnEditPharmacy;
    @FXML
    private Button btnAddPharmacy;
    @FXML
    private ComboBox<AddressDTO> comboBoxAddress;
    @FXML
    private TextField txtPharmacyScooterChargerCapacity;
    @FXML
    private TextField txtPharmacyZipCode;
    @FXML
    private TextField txtPharmacyLatitude;
    @FXML
    private TextField txtPharmacyLongitude;
    @FXML
    private TableView<PharmacyDTO> tableViewPharmacies;
    @FXML
    private TableColumn<PharmacyDTO, Integer> tableViewPharmaciesColumnId;
    @FXML
    private TableColumn<PharmacyDTO, String> tableViewPharmaciesName;
    @FXML
    private Button btnRemovePharmacy;

    private MainAppUI mainAppUI;

    private ManagePharmaciesController managePharmaciesController;

    private PharmacyDTO selectedPharmacy;
    
    private AddressDTO selectedAddress;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Instance controller
        managePharmaciesController = new ManagePharmaciesController();
        selectedPharmacy = null;

        //Non editable mode
        tableViewPharmacies.setEditable(false);
      
        //Set columns
        tableViewPharmaciesColumnId.setCellValueFactory(new PropertyValueFactory<PharmacyDTO, Integer>("id"));
        tableViewPharmaciesColumnId.setSortable(false);
        tableViewPharmaciesName.setCellValueFactory(new PropertyValueFactory<PharmacyDTO, String>("name"));
        tableViewPharmaciesName.setSortable(false);

        //Get selected
        tableViewPharmacies.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                PharmacyDTO object = tableViewPharmacies.getSelectionModel().getSelectedItem();
                if (object != null) {
                    selectedPharmacy = object;
                    setTextFieldsText(object);
                }
            }
        });

        //LoadItems
        loadItems();
    }

    @FXML
    private void btnEditPharmacyAction(ActionEvent event) {
        try {
            PharmacyDTO p = getPharmacyDTOFromTextFields();
            p.setId(selectedPharmacy.getId());
            p = managePharmaciesController.updatePharmacy(p);
            if (p != null) {
                tableViewPharmacies.getItems().set(tableViewPharmacies.getItems().indexOf(selectedPharmacy), p);
                syncPharmaciesWithOtherViews();
            }

        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at edit pharmacy", e.getMessage()).show();
        }
    }

    @FXML
    private void btnAddPharmacyAction(ActionEvent event) {
        try {
            PharmacyDTO p = managePharmaciesController.insertPharmacy(getPharmacyDTOFromTextFields());
            if (p != null) {
                tableViewPharmacies.getItems().add(p);
                syncPharmaciesWithOtherViews();
            }

        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at add pharmacy", e.getMessage()).show();
        }

    }

    @FXML
    private void btnRemovePharmacyAction(ActionEvent event) {
        try {
            PharmacyDTO p = getPharmacyDTOFromTextFields();
            p.setId(selectedPharmacy.getId());
            if (managePharmaciesController.removePharmacy(p)) {
                tableViewPharmacies.getItems().remove(selectedPharmacy);
                syncPharmaciesWithOtherViews();
            }

        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at edit pharmacy", e.getMessage()).show();
        }
    }

    @FXML
    private void comboBoxAddressAction(ActionEvent event) {
        selectedAddress = comboBoxAddress.getSelectionModel().getSelectedItem();
        txtPharmacyZipCode.setText(selectedAddress.getZipcode());
        txtPharmacyLatitude.setText(selectedAddress.getLatitude() + "");
        txtPharmacyLongitude.setText(selectedAddress.getLongitude() + "");
    }

    public void loadItems() {

        try {
            List<PharmacyDTO> tmpPharmacyDTOs = new LinkedList<>();
            tmpPharmacyDTOs = managePharmaciesController.getPharmaciesByAdministrator();
            if (tmpPharmacyDTOs != null) {
                ObservableList<PharmacyDTO> pharmaciesList = FXCollections.observableArrayList(tmpPharmacyDTOs);
                tableViewPharmacies.setItems(pharmaciesList);
            }
            List<AddressDTO> tmpAddressDTO = managePharmaciesController.getAllAddress();
            if (tmpAddressDTO != null) {
                ObservableList<AddressDTO> addressList = FXCollections.observableArrayList(tmpAddressDTO);
                comboBoxAddress.setItems(addressList);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Pharmacies", e.getMessage()).show();
        }

    }
    
    private void syncPharmaciesWithOtherViews(){
        mainAppUI.syncPharmaciesWithOtherViews(tableViewPharmacies.getItems());
    }

    public void associateParentUI(MainAppUI mainAppUI) {
        this.mainAppUI = mainAppUI;
    }

    private void setTextFieldsText(PharmacyDTO object) {
        txtPharmacyName.setText(object.getName());
        comboBoxAddress.getSelectionModel().select(object.getAddress());
        comboBoxAddressAction(null);
        txtPharmacyMaxPayload.setText(object.getMaximumPayloadCourier() + "");
        txtPharmacyMinLoad.setText(object.getMinimumLoadCourier() + "");
        txtPharmacyScooterParkCapacity.setText(object.getPark().getScooterChargersNumber() + "");
        txtPharmacyScooterChargerCapacity.setText(object.getPark().getScooterChargerCapacity() + "");
        txtPharmacyDroneParkCapacity.setText(object.getPark().getDroneChargersNumber() + "");
        txtPharmacyDroneChargerCapacity.setText(object.getPark().getDroneChargerCapacity() + "");
        
    }

    private PharmacyDTO getPharmacyDTOFromTextFields() {
        String name = txtPharmacyName.getText();
        AddressDTO address = comboBoxAddress.getSelectionModel().getSelectedItem();
        Float maxload = Utils.convertStringToFloat(txtPharmacyMaxPayload.getText());
        Float minLoad = Utils.convertStringToFloat(txtPharmacyMinLoad.getText());
        int scooterParkCapacity = Utils.convertStringToInt(txtPharmacyScooterParkCapacity.getText());
        float scooterChargerCapacity = Utils.convertStringToFloat(txtPharmacyScooterChargerCapacity.getText());
        int droneParkCapacity = Utils.convertStringToInt(txtPharmacyDroneParkCapacity.getText());
        float droneChargerCapacity = Utils.convertStringToFloat(txtPharmacyDroneChargerCapacity.getText());
        return new PharmacyDTO(name, 0, new ParkDTO(0, scooterParkCapacity, droneParkCapacity, scooterChargerCapacity, droneChargerCapacity), address, minLoad, maxload);
    }

}
