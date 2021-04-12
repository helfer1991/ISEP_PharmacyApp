/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import lapr.project.controller.BackOrderController;
import lapr.project.controller.CommonController;
import lapr.project.controller.ManageClientsController;
import lapr.project.controller.ManagePharmaciesController;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.ClientDTO;
import lapr.project.ui.dto.CreditCardDTO;
import lapr.project.ui.dto.OrderDTO;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.ui.dto.ProductDTO;
import lapr.project.ui.dto.ShoppingCartDTO;
import lapr.project.ui.dto.StorageDTO;
import lapr.project.utils.Utils;

/**
 * FXML Controller class
 *
 * @author 1100241
 */
public class TabManageOrdersUI implements Initializable {

    @FXML
    private TextField txtCreditCard;
    @FXML
    public Button btnBuyAction;
    @FXML
    private TextField txtShipmentAddressLongitude;
    @FXML
    private TextField txtShipmentAddressLatitude;
    @FXML
    private TextField txtShipmentAddressZipCode;
    @FXML
    private TextField txtCreditCardValidThru;
    @FXML
    private TextField txtCreditCardValidCCV;
    @FXML
    private TableView<Map.Entry<ProductDTO, Integer>> tableViewAvailableProducts;
    @FXML
    private TableColumn<Map.Entry<ProductDTO, Integer>, String> tableViewProductsColumnDescription;
    @FXML
    private TableColumn<Map.Entry<ProductDTO, Integer>, Float> tableViewProductsColumnUPrice;
    @FXML
    private TableView<Map.Entry<ProductDTO, Integer>> tableViewShoppingCart;
    @FXML
    private TableColumn<Map.Entry<ProductDTO, Integer>, String> tableViewShoppingCartColumnDescription;
    @FXML
    private TableColumn<Map.Entry<ProductDTO, Integer>, Integer> tableViewShoppingCartColumnQt;
    @FXML
    private TableColumn<Map.Entry<ProductDTO, Integer>, Float> tableViewShoppingCartColumnTotalPrice;
    @FXML
    private TextField txtQuantity;
    @FXML
    private Button btnAddQuantity;
    @FXML
    private ComboBox<PharmacyDTO> comboBoxPharmaciesList;
    @FXML
    public Button btnAddProduct;
    @FXML
    public Button btnRemoveProduct;
    @FXML
    private ComboBox<AddressDTO> comboBoxAddress;
    @FXML
    private CheckBox checkBoxDiscountPoints;
    @FXML
    private TextField txtTotalEarnedPoints;

    private BackOrderController backOrderController;
    
    private ManagePharmaciesController managePharmaciesController;
    
    private CommonController commonController;

    private ManageClientsController manageClientController;

    private Map.Entry<ProductDTO, Integer> selectedEntry;

    private PharmacyDTO selectedPharmacy;

    private ClientDTO selectedClient;
    
    private AddressDTO selectedAddress;

    private MainAppUI mainAppUI;
    @FXML
    private TextField txtTotal;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Disable buttons
        btnAddProduct.setDisable(true);
        btnRemoveProduct.setDisable(true);
        comboBoxPharmaciesList.setDisable(true);
        
        //initialize controllers
        backOrderController = new BackOrderController();
        manageClientController = new ManageClientsController();
        commonController = new CommonController();

        //Table Products
        tableViewAvailableProducts.setEditable(false);
        tableViewProductsColumnDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<ProductDTO, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<ProductDTO, Integer>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey().getDescription());
            }
        });
        tableViewProductsColumnUPrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<ProductDTO, Integer>, Float>, ObservableValue<Float>>() {
            @Override
            public ObservableValue<Float> call(TableColumn.CellDataFeatures<Map.Entry<ProductDTO, Integer>, Float> p) {
                return new SimpleFloatProperty(p.getValue().getKey().getPrice()).asObject();
            }
        });

        //Table ShoppingCart
        tableViewShoppingCart.setEditable(false);
        tableViewShoppingCartColumnDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<ProductDTO, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<ProductDTO, Integer>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey().getDescription());
            }
        });
        tableViewShoppingCartColumnQt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<ProductDTO, Integer>, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<ProductDTO, Integer>, Integer> p) {
                return new SimpleIntegerProperty(p.getValue().getValue()).asObject();
            }
        });
        tableViewShoppingCartColumnTotalPrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<ProductDTO, Integer>, Float>, ObservableValue<Float>>() {
            @Override
            public ObservableValue<Float> call(TableColumn.CellDataFeatures<Map.Entry<ProductDTO, Integer>, Float> p) {
                return new SimpleFloatProperty(p.getValue().getValue() * p.getValue().getKey().getPrice()).asObject();
            }
        });

        //Load Items
        loadPharmacy();
        loadProducts();
    }

    @FXML
    private void buyAction(ActionEvent event) {
        try {
            selectedPharmacy = comboBoxPharmaciesList.getSelectionModel().getSelectedItem();
            OrderDTO result = null;
            if (selectedPharmacy != null && selectedClient != null && tableViewShoppingCart.getItems().size() > 0) {
                ShoppingCartDTO tmp = new ShoppingCartDTO();
                for (Map.Entry<ProductDTO, Integer> entry : tableViewShoppingCart.getItems()) {
                    tmp.addProductToShoppingCart(entry.getKey(), entry.getValue());
                }
                ClientDTO c = getClientDTOFromTextFields();
                OrderDTO d = new OrderDTO();
                d.setShopCart(tmp);
                result = backOrderController.registerOrderRequest(selectedPharmacy,d,c);
                txtTotalEarnedPoints.setText(Utils.convertStringToInt(txtTotalEarnedPoints.getText()) + result.getCredits()+"");
            }
            AlertsUI.createAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Order request", 
                    "The request for home Delivery was submitted with success. \n The invoice will be sent to your email shortly.").show();
            
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Buy Error ", e.getMessage()).show();
        } finally{
            loadProducts();
        }
    }
    
    @FXML
    private void btnAddQuantityAction(ActionEvent event) {
        selectedEntry = tableViewShoppingCart.getSelectionModel().getSelectedItem();

        if (selectedEntry != null) {
            if (txtQuantity.getText() != null) {
                int qt = Utils.convertStringToInt(txtQuantity.getText());
                if (qt > 0) {
                    selectedEntry.setValue(qt);
                    tableViewShoppingCart.getItems().set(tableViewShoppingCart.getItems().indexOf(selectedEntry), selectedEntry);
                    updateTotalCost();
                }
            }
        }
    }

    @FXML
    private void btnAddProductAction(ActionEvent event) {

        selectedEntry = tableViewAvailableProducts.getSelectionModel().getSelectedItem();

        if (selectedEntry != null) {
            selectedEntry.setValue(1);
            tableViewShoppingCart.getItems().add(selectedEntry);
            tableViewAvailableProducts.getItems().remove(selectedEntry);
            updateTotalCost();
        }
    }

    @FXML
    private void btnRemoveProductAction(ActionEvent event) {
        selectedEntry = tableViewShoppingCart.getSelectionModel().getSelectedItem();

        if (selectedEntry != null) {
            selectedEntry.setValue(0);
            tableViewAvailableProducts.getItems().add(selectedEntry);
            tableViewShoppingCart.getItems().remove(selectedEntry);
            updateTotalCost();
        }
    }
    
    @FXML
    private void comboBoxAddressAction(ActionEvent event) {
        selectedAddress = comboBoxAddress.getSelectionModel().getSelectedItem();
        txtShipmentAddressZipCode.setText(selectedAddress.getZipcode());
        txtShipmentAddressLatitude.setText(selectedAddress.getLatitude() + "");
        txtShipmentAddressLongitude.setText(selectedAddress.getLongitude() + "");
    }

    private void loadProducts() {
        try {
            StorageDTO tmp = new StorageDTO();
            tmp = backOrderController.getProducts(selectedPharmacy);
            if (tmp != null && tmp.getProductMap().size() != 0) {
                ObservableList<Map.Entry<ProductDTO, Integer>> productsList = FXCollections.observableArrayList(tmp.getProductMap().entrySet());
                tableViewAvailableProducts.setItems(productsList);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Products", e.getMessage()).show();
        }
    }

    public void loadPharmacy() {

        try {
            List<PharmacyDTO> tmp = new LinkedList<>();
            PharmacyDTO p = backOrderController.getNearestPharmacy();
            if (p != null) {
                tmp.add(p);
                ObservableList<PharmacyDTO> pharmaciesList = FXCollections.observableArrayList(tmp);
                comboBoxPharmaciesList.setItems(pharmaciesList);
                comboBoxPharmaciesList.getSelectionModel().select(0);
                selectedPharmacy = p;
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Pharmacies", e.getMessage()).show();
        }
    }

    public void loadClient() {
        try {
            selectedClient = manageClientController.getClientByUserSession();
            setTextFields(selectedClient);
            List<AddressDTO> tmpAddressDTO = commonController.getAllAddress();
            if (tmpAddressDTO != null) {
                ObservableList<AddressDTO> addressList = FXCollections.observableArrayList(tmpAddressDTO);
                comboBoxAddress.setItems(addressList);
            }
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at Load Client Info", e.getMessage()).show();
        }
    }

    private void setTextFields(ClientDTO client) {
        if (client != null) {
            comboBoxAddress.getSelectionModel().select(client.getAddress());
             comboBoxAddressAction(null);
            txtCreditCard.setText(client.getCreditCard().getNumber() + "");
            txtCreditCardValidThru.setText(client.getCreditCard().getValidThru() + "");
            txtCreditCardValidCCV.setText(client.getCreditCard().getCcv() + "");
            txtTotalEarnedPoints.setText(client.getCredits()+"");
        }
    }

    private ClientDTO getClientDTOFromTextFields() {
        AddressDTO address = comboBoxAddress.getSelectionModel().getSelectedItem();
        String numero = txtCreditCard.getText();
        String date = txtCreditCardValidThru.getText();
        int ccv = Utils.convertStringToInt(txtCreditCardValidCCV.getText());
        ClientDTO client = new ClientDTO(new AddressDTO(selectedAddress.getId(), address.getLatitude(), address.getLongitude(), address.getAddress(), address.getZipcode(), 100), new CreditCardDTO(numero, ccv, date));
        client.setNIF(selectedClient.getNIF());
        client.setName(selectedClient.getName());
        client.setEmail(selectedClient.getEmail());
        client.setPassword(selectedClient.getPassword());
        if(checkBoxDiscountPoints.isSelected()){
            client.setCredits(Utils.convertStringToInt(txtTotalEarnedPoints.getText())*-1);
        }else{
            client.setCredits(Utils.convertStringToInt(txtTotalEarnedPoints.getText()));
        }
        return client;
    }

    public void associateParentUI(MainAppUI mainAppUI) {
        this.mainAppUI = mainAppUI;
    }
    
    
    private void updateTotalCost(){
        
        float totalCost = 0;
        
        for (Map.Entry<ProductDTO, Integer> entry : tableViewShoppingCart.getItems()) {
            totalCost = totalCost + (entry.getKey().getPrice()*entry.getValue());
                }
        txtTotal.setText(totalCost+"");
    }

}
