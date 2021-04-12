/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import lapr.project.controller.ManagePharmaciesController;
import lapr.project.controller.ManageStocksController;
import lapr.project.controller.TransferProductsController;
import lapr.project.ui.dto.*;
import lapr.project.utils.Utils;

/**
 * FXML Controller class
 *
 * @author 1100241
 */
public class TabManageStocksUI implements Initializable {

    @FXML
    private TextField txtProductDescription;
    @FXML
    private TextField txtProductPrice;
    @FXML
    private TextField txtProductWeight;
    @FXML
    private TextField txtProductQuantity;
    @FXML
    private TextField txtOrderId;
    @FXML
    public CheckBox checkBoxTransfer;
    @FXML
    private ComboBox<PharmacyDTO> comboBoxPharmacies;
    @FXML
    private ComboBox<PharmacyDTO> comboBoxTransferPharmacies;
    @FXML
    private TableView<Map.Entry<ProductDTO, Integer>> tableViewProducts;
    @FXML
    private TableColumn<Map.Entry<ProductDTO, Integer>, Integer> tableViewStocksColumnId;
    @FXML
    private TableColumn<Map.Entry<ProductDTO, Integer>, String> tableViewStocksColumnDescription;
    @FXML
    private TableColumn<Map.Entry<ProductDTO, Integer>, Integer> tableViewStocksColumnQuantity;
    @FXML
    private Button btnRemoveProduct;
    @FXML
    private Button btnEditProduct;
    @FXML
    private Button btnAddProduct;
    @FXML
    private Button btnRemoveQuantity;
    @FXML
    private Button btnAddProductQuantity;

    private MainAppUI mainAppUI;

    private ManageStocksController manageStocksController;

    private ManagePharmaciesController managePharmaciesController;

    private TransferProductsController transferProductsController;

    private PharmacyDTO selectedPharmacy;

    private PharmacyDTO selectedTransferPharmacy;

    private ProductDTO selectedProduct;

    private Map.Entry<ProductDTO, Integer> selectedEntry;

    private boolean isTransfer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Disable remove
        btnRemoveProduct.setVisible(false);

        //Instance controller
        manageStocksController = new ManageStocksController();
        managePharmaciesController = new ManagePharmaciesController();
        transferProductsController = new TransferProductsController();
        selectedPharmacy = null;
        selectedProduct = null;

        //Non editable mode
        tableViewProducts.setEditable(false);

        //Set columns
        tableViewStocksColumnId.setCellValueFactory(p -> {
            // this callback returns property for just one cell, you can't use a loop here
            // for first column we use key
            return new SimpleIntegerProperty(p.getValue().getKey().getId()).asObject();
        });
        tableViewStocksColumnDescription.setCellValueFactory(p -> {
            // this callback returns property for just one cell, you can't use a loop here
            // for first column we use key
            return new SimpleStringProperty(p.getValue().getKey().getDescription());
        });
        tableViewStocksColumnQuantity.setCellValueFactory(p -> {
            // this callback returns property for just one cell, you can't use a loop here
            // for first column we use key
            return new SimpleIntegerProperty(p.getValue().getValue()).asObject();
        });

        //Get selected
        tableViewProducts.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedEntry = tableViewProducts.getSelectionModel().getSelectedItem();
                if (selectedEntry != null) {
                    selectedProduct = selectedEntry.getKey();
                    setTextFieldsText(selectedEntry);
                }
            }
        });

        //Load Items
        loadItems();
    }

    @FXML
    private void comboBoxPharmaciesActon(ActionEvent event) {
        tableViewProducts.getItems().clear();
        if (!comboBoxPharmacies.getItems().isEmpty()) {
            getProductsByPharmacy(comboBoxPharmacies.getSelectionModel().getSelectedItem());
            selectedPharmacy = comboBoxPharmacies.getSelectionModel().getSelectedItem();
        }
    }

    private void getProductsByPharmacy(PharmacyDTO pharmacyDTO) {
        try {
            StorageDTO tmp = new StorageDTO();
            tmp = manageStocksController.getProductsByPharmacy(pharmacyDTO);
            if (tmp != null && tmp.getProductMap().size() != 0) {
                ObservableList<Map.Entry<ProductDTO, Integer>> productsList = FXCollections.observableArrayList(tmp.getProductMap().entrySet());
                tableViewProducts.setItems(productsList);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Products", e.getMessage()).show();
        }
    }

    public void loadItems() {

        try {
            List<PharmacyDTO> tmpPharmacyDTOs = new LinkedList<>();
            tmpPharmacyDTOs = managePharmaciesController.getPharmaciesByAdministrator();
            if (tmpPharmacyDTOs != null) {
                ObservableList<PharmacyDTO> pharmaciesList = FXCollections.observableArrayList(tmpPharmacyDTOs);
                comboBoxPharmacies.setItems(pharmaciesList);
                comboBoxPharmacies.getSelectionModel().select(0);
                comboBoxPharmaciesActon(null);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Pharmacies", e.getMessage()).show();
        }

        checkBoxTransfer.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                    isTransfer = new_val;
                    if (isTransfer) {
                        txtOrderId.setDisable(false);
                        comboBoxTransferPharmacies.setDisable(false);
                    }
                    if (!isTransfer) {
                        txtOrderId.setDisable(true);
                        comboBoxTransferPharmacies.setDisable(true);
                    }
                });
    }

    public void associateParentUI(MainAppUI mainAppUI) {
        this.mainAppUI = mainAppUI;
    }

    @FXML
    private void btnRemoveProductAction(ActionEvent event) {
    }

    @FXML
    private void btnEditProductAction(ActionEvent event) {
        try {
            Map.Entry<ProductDTO, Integer> entry = getEntryFromTextFields();
            entry.getKey().setId(selectedProduct.getId());
            ProductDTO p = manageStocksController.updateProduct(selectedPharmacy, entry.getKey());
            if (p != null) {
                entry.setValue(selectedEntry.getValue());
                tableViewProducts.getItems().set(tableViewProducts.getItems().indexOf(selectedEntry), entry);
            }

        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at edit product", e.getMessage()).show();
        }
    }

    @FXML
    private void btnAddProductAction(ActionEvent event) {
        try {
            Map.Entry<ProductDTO, Integer> entry = getEntryFromTextFields();
            ProductDTO p = manageStocksController.insertProduct(entry.getKey());
            if (p != null) {
                tableViewProducts.getItems().add(new HashMap.SimpleEntry<ProductDTO, Integer>(p, 0));
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at add product", e.getMessage()).show();
        }
    }

    @FXML
    private void btnRemoveQuantityAction(ActionEvent event) {

        try {
            if (selectedProduct != null) {
                Map.Entry<ProductDTO, Integer> entry = getEntryFromTextFields();
                entry.getKey().setId(selectedProduct.getId());
                entry = manageStocksController.removeProductQuantity(selectedPharmacy, entry);
                if (entry != null) {
                    //new quantity
                    entry.setValue(selectedEntry.getValue() - entry.getValue());
                    tableViewProducts.getItems().set(tableViewProducts.getItems().indexOf(selectedEntry), entry);
                }
            }

        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at edit pharmacy", e.getMessage()).show();
        }
    }

    @FXML
    private void btnAddProductQuantityAction(ActionEvent event) {
        try {
            if (selectedProduct != null) {
                Map.Entry<ProductDTO, Integer> entry;
                if (isTransfer) {
                    TransferDTO transferDTO = getTransferInformation();
                    entry = transferProductsController.receiveTransfer(transferDTO);
                } else {
                    entry = getEntryFromTextFields();
                    entry.getKey().setId(selectedProduct.getId());
                    entry = manageStocksController.insertProductQuantity(selectedPharmacy, entry);
                }
                if (entry != null) {
                    //new quantity
                    entry.setValue(selectedEntry.getValue() + entry.getValue());
                    tableViewProducts.getItems().set(tableViewProducts.getItems().indexOf(selectedEntry), entry);
                }

            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at edit pharmacy", e.getMessage()).show();
        }
    }

    @FXML
    public void comboBoxTransferPharmaciesAction(ActionEvent actionEvent) {
        selectedTransferPharmacy = comboBoxTransferPharmacies.getSelectionModel().getSelectedItem();
    }

    public void CheckBoxTransferAction(ActionEvent actionEvent) {

        try {
            List<PharmacyDTO> tmpPharmacyDTOs;
            tmpPharmacyDTOs = managePharmaciesController.getAllPharmacies();
            if (tmpPharmacyDTOs != null) {
                tmpPharmacyDTOs.remove(selectedPharmacy);
                ObservableList<PharmacyDTO> pharmaciesList = FXCollections.observableArrayList(tmpPharmacyDTOs);
                comboBoxTransferPharmacies.setItems(pharmaciesList);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Pharmacies", e.getMessage()).show();
        }
    }

    private TransferDTO getTransferInformation() {
        Map.Entry<ProductDTO, Integer> entry = getEntryFromTextFields();
        int orderId = Utils.convertStringToInt(txtOrderId.getText());
        return new TransferDTO(selectedPharmacy, selectedTransferPharmacy, entry.getKey(), entry.getValue(), orderId);
    }

    private void setTextFieldsText(Map.Entry<ProductDTO, Integer> object) {
        txtProductDescription.setText(object.getKey().getDescription());
        txtProductPrice.setText(object.getKey().getPrice() + "");
        txtProductWeight.setText(object.getKey().getWeight() + "");
        txtProductQuantity.setText("0");
    }

    private Map.Entry<ProductDTO, Integer> getEntryFromTextFields() {
        String desc = txtProductDescription.getText();
        Float price = Utils.convertStringToFloat(txtProductPrice.getText());
        Float weight = Utils.convertStringToFloat(txtProductWeight.getText());
        Integer qt = Utils.convertStringToInt(txtProductQuantity.getText());
        int id = 0;
        if (selectedProduct != null) {
            id = selectedProduct.getId();
        }
        ProductDTO product = new ProductDTO(id, desc, price, weight);
        return new HashMap.SimpleEntry<ProductDTO, Integer>(product, qt);
    }

    public void refreshPharmacies(ObservableList<PharmacyDTO> pharmacies) {
        comboBoxPharmacies.setItems(pharmacies);
        if (!comboBoxPharmacies.getItems().isEmpty()) {
            comboBoxPharmacies.getSelectionModel().select(0);
        }
    }
}
