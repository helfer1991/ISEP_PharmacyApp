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
import lapr.project.controller.ManageCouriesController;
import lapr.project.controller.ManagePharmaciesController;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.ui.dto.CourierDTO;
import lapr.project.utils.Utils;

/**
 * FXML Controller class
 *
 * @author 1100241
 */
public class TabManageCouriersUI implements Initializable {

    @FXML
    private TextField txtCourierName;
    @FXML
    private TextField txtCourierWeight;
    @FXML
    private TextField txtCourierEmail;
    @FXML
    private Button btnRemoveCourier;
    @FXML
    private Button btnEditCourier;
    @FXML
    private Button btnAddCourier;
    @FXML
    private TextField txtCourierPassword;
    @FXML
    private TextField txtCourierNIF;
    @FXML
    private ComboBox<PharmacyDTO> comboBoxPharmacies;
    @FXML
    private TableView<CourierDTO> tableViewProducts;
    @FXML
    private TableColumn<CourierDTO, Integer> tableViewStocksColumnId;
    @FXML
    private TableColumn<CourierDTO, String> tableViewStocksColumnDescription;
    @FXML
    private TableColumn<CourierDTO, String> tableViewStocksColumnQuantity;

    private MainAppUI mainAppUI;

    private ManageCouriesController manageCouriesController;

    private CourierDTO selectedCourier;

    private ManagePharmaciesController managePharmaciesController;

    private PharmacyDTO selectedPharmacy;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Instance controller
        managePharmaciesController = new ManagePharmaciesController();
        manageCouriesController = new ManageCouriesController();
        selectedPharmacy = null;
        selectedPharmacy = null;

        //Non editable mode
        tableViewProducts.setEditable(false);

        //Set columns
        tableViewStocksColumnId.setCellValueFactory(new PropertyValueFactory<CourierDTO, Integer>("NIF"));
        tableViewStocksColumnDescription.setCellValueFactory(new PropertyValueFactory<CourierDTO, String>("name"));
        tableViewStocksColumnQuantity.setCellValueFactory(new PropertyValueFactory<CourierDTO, String>("isWorking"));

        //Get selected courier
        tableViewProducts.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                CourierDTO object = tableViewProducts.getSelectionModel().getSelectedItem();
                if (object != null) {
                    selectedCourier = object;
                    setTextFieldsTextCourier(object);
                }
            }
        });

        loadPharmacy();

    }

    @FXML
    private void btnRemoveCourierAction(ActionEvent event) {
        try {

            boolean sucess;
            sucess = manageCouriesController.removeCourier(selectedCourier, selectedPharmacy);

            if (sucess) {
                tableViewProducts.getItems().remove(selectedCourier);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at remove courier", e.getMessage()).show();
        }

    }

    @FXML
    private void btnEditCourierAction(ActionEvent event) {
        try {
            CourierDTO c = getCourierDTOFromTextFields();
            boolean sucess;
            sucess = manageCouriesController.updateCourier(c, selectedPharmacy);
            if (sucess) {
                tableViewProducts.getItems().remove(selectedCourier);
                tableViewProducts.getItems().add(c);
            }

        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at edit courier", e.getMessage()).show();
        }
    }

    @FXML
    private void btnAddCourierAction(ActionEvent event) {
        try {
            CourierDTO c = getCourierDTOFromTextFields();
            boolean sucess;
            sucess = manageCouriesController.insertCourier(c, selectedPharmacy);

            if (sucess) {
                tableViewProducts.getItems().add(c);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at register courier", e.getMessage()).show();
        }

    }

    @FXML
    private void comboBoxPharmaciesActon(ActionEvent event) {
        tableViewProducts.getItems().clear();
        if (!comboBoxPharmacies.getItems().isEmpty()) {
            selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
            if (selectedPharmacy == null) {
                comboBoxPharmacies.getSelectionModel().select(0);
                selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
            }
            loadCouriersByPharmacy(comboBoxPharmacies.getSelectionModel().getSelectedItem());
            selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
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

    public void loadCouriersByPharmacy(PharmacyDTO pharmacyDTO) {

        try {
            List<CourierDTO> tmp = new LinkedList<>();
            tmp = manageCouriesController.getCouries(pharmacyDTO);
            if (tmp != null) {
                ObservableList<CourierDTO> CouriesList = FXCollections.observableArrayList(tmp);
                tableViewProducts.setItems(CouriesList);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Couries", e.getMessage()).show();
        }
    }

    public void associateParentUI(MainAppUI mainAppUI) {
        this.mainAppUI = mainAppUI;
    }

    private void setTextFieldsTextCourier(CourierDTO object) {
        txtCourierNIF.setText(object.getNIF() + "");
        txtCourierName.setText(object.getName());
        txtCourierEmail.setText(object.getEmail());
        txtCourierWeight.setText(object.getWeight() + "");
        txtCourierPassword.setText(object.getPassword());

    }

    private CourierDTO getCourierDTOFromTextFields() {
        Double weight = Utils.convertStringToDouble(txtCourierWeight.getText());
        CourierDTO courier = new CourierDTO(weight, true);
        courier.setNIF(Utils.convertStringToInt(txtCourierNIF.getText()));
        courier.setEmail(txtCourierEmail.getText());
        courier.setName(txtCourierName.getText());
        courier.setPassword(txtCourierPassword.getText());
        return courier;

    }

    public void refreshPharmacies(ObservableList<PharmacyDTO> pharmacies) {
        comboBoxPharmacies.setItems(pharmacies);
        if (!comboBoxPharmacies.getItems().isEmpty()) {
            comboBoxPharmacies.getSelectionModel().select(0);
        }
    }

}
