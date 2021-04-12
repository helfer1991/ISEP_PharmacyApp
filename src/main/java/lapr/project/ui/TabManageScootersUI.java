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
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.ui.dto.ScooterDTO;
import lapr.project.utils.Utils;

/**
 * FXML Controller class
 *
 * @author 1100241
 */
public class TabManageScootersUI implements Initializable {

    @FXML
    private ComboBox<PharmacyDTO> comboBoxPharmacies;
    @FXML
    private TableColumn<ScooterDTO, Integer> tableViewStocksColumnId;
    @FXML
    private TableColumn<ScooterDTO, Integer> tableViewStocksColumnDescription;
    @FXML
    private TableColumn<ScooterDTO, Boolean> tableViewStocksColumnQuantity;
    @FXML
    private TextField txtScooterWeight;
    @FXML
    private Button btnRemoveScooter;
    @FXML
    private Button btnEditScooter;
    @FXML
    private Button btnAddScooter;
    @FXML
    private TextField txtScooterBatteryCapacity;
    @FXML
    private ImageView imageViewQRCode;
    @FXML
    private TableView<ScooterDTO> tableViewScooters;

    private MainAppUI mainAppUI;
    private ManageVehiclesController manageVehiclesController;
    private ScooterDTO selectedScooter;
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
        tableViewScooters.setEditable(false);

        //Set columns
        tableViewStocksColumnId.setCellValueFactory(new PropertyValueFactory<ScooterDTO, Integer>("id"));
        tableViewStocksColumnDescription.setCellValueFactory(new PropertyValueFactory<ScooterDTO, Integer>("weight"));
        tableViewStocksColumnQuantity.setCellValueFactory(new PropertyValueFactory<ScooterDTO, Boolean>("isAvailable"));

        //Get selected scooter
        tableViewScooters.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                ScooterDTO object = tableViewScooters.getSelectionModel().getSelectedItem();
                if (object != null) {
                    selectedScooter = object;
                    setTextFieldsTextScooter(object);
                }
            }
        });
        loadPharmacy();
    }

    public void associateParentUI(MainAppUI mainAppUI) {
        this.mainAppUI = mainAppUI;
    }

    @FXML
    private void comboBoxPharmaciesActon(ActionEvent event) {
        tableViewScooters.getItems().clear();
        if (!comboBoxPharmacies.getItems().isEmpty()) {

            selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
            if (selectedPharmacy == null) {
                comboBoxPharmacies.getSelectionModel().select(0);
                selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
            }
            loadScootersByPharmacy(comboBoxPharmacies.getSelectionModel().getSelectedItem());
            selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
        }
    }

    @FXML
    private void btnRemoveScooterAction(ActionEvent event) {
        try {
            boolean sucess;
            sucess = manageVehiclesController.removeVehicle(selectedScooter, selectedPharmacy);
            if (sucess) {
                tableViewScooters.getItems().remove(selectedScooter);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at remove scooter", e.getMessage()).show();
        }
    }

    @FXML
    private void btnEditScooterAction(ActionEvent event) {
        try {
            ScooterDTO s = getScooterDTOFromTextFields();
            s.setId(selectedScooter.getId());
            s = (ScooterDTO) manageVehiclesController.updateVehicle(s, selectedPharmacy);
            if (s != null) {
                tableViewScooters.getItems().set(tableViewScooters.getItems().indexOf(selectedScooter), s);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at edit scooter", e.getMessage()).show();
        }
    }

    @FXML
    private void btnAddScooterAction(ActionEvent event) {
        try {
//            ScooterDTO s =  getScooterDTOFromTextFields();
            ScooterDTO s = (ScooterDTO) manageVehiclesController.insertVehicle(getScooterDTOFromTextFields(), selectedPharmacy);

            if (s != null) {
                tableViewScooters.getItems().add(s);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at adding scooter", e.getMessage()).show();
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

    public void loadScootersByPharmacy(PharmacyDTO pharmacyDTO) {
        try {
            List<ScooterDTO> tmp = new LinkedList<>();
            tmp = manageVehiclesController.getScooters(pharmacyDTO);
            if (tmp != null) {
                ObservableList<ScooterDTO> ScootersList = FXCollections.observableArrayList(tmp);
                tableViewScooters.setItems(ScootersList);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Scooters", e.getMessage()).show();
        }
    }

    private void setTextFieldsTextScooter(ScooterDTO object) {
        txtScooterBatteryCapacity.setText(object.getBattery().getCapacity() + "");
        txtScooterWeight.setText(object.getWeight() + "");

        try {
            byte[] byteArray = QRCode.getQRCodeImage(object.getId() + "", 150, 150);
            Image img = new Image(new ByteArrayInputStream(byteArray, 0, byteArray.length));
            imageViewQRCode.setImage(img);
        } catch (Exception e) {
        }

    }

    private ScooterDTO getScooterDTOFromTextFields() {
        ScooterDTO tmp = new ScooterDTO();
        tmp.setWeight(Utils.convertStringToInt(txtScooterWeight.getText()));
        tmp.getBattery().setCapacity(Utils.convertStringToInt(txtScooterBatteryCapacity.getText()));
        return tmp;
    }

    public void refreshPharmacies(ObservableList<PharmacyDTO> pharmacies) {
        comboBoxPharmacies.setItems(pharmacies);
        if (!comboBoxPharmacies.getItems().isEmpty()) {
            comboBoxPharmacies.getSelectionModel().select(0);
        }
    }
}
