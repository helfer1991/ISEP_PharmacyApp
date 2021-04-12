/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lapr.project.controller.ManageClientsController;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.ClientDTO;
import lapr.project.utils.Utils;

/**
 * FXML Controller class
 *
 * @author 1100241
 */
public class SignUpUI implements Initializable {

    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtSignUpEmail;
    @FXML
    private PasswordField txtSignUpPassword;
    @FXML
    private TextField txtSignUpSSN;
    @FXML
    private TextField txtSignUpName;
    @FXML
    private TextField txtSignUpLatitude;
    @FXML
    private TextField txtSignUpLongitude;
    @FXML
    private TextField txtSignUpZipCode;
    @FXML
    private TextField txtSignUpCardNumber;
    @FXML
    private TextField txtSignUpCCV;
    @FXML
    private TextField txtSignUpValidThru;
    @FXML
    private ComboBox<AddressDTO> comboBoxAddress;

    private ManageClientsController manageClientsController;

    private LoginUI loginUI;

    private AddressDTO selectedAddress;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        manageClientsController = new ManageClientsController();
        
        List<AddressDTO> tmpAddressDTO = manageClientsController.getAllAddress();
        if (tmpAddressDTO != null) {
            ObservableList<AddressDTO> addressList = FXCollections.observableArrayList(tmpAddressDTO);
            comboBoxAddress.setItems(addressList);
        }
    }

    @FXML
    private void btnSignUpAction(ActionEvent event) {
        try {
            ClientDTO c =  getClientDTOFromTextFields();
            boolean sucess;
            sucess = manageClientsController.insertClient(c);
            
            if(sucess){
                Stage stage = (Stage) btnCancel.getScene().getWindow();
                stage.close();
            }else{
                throw new Exception("Not registered at database");
            }
            
        } catch (Exception e) {
            AlertsUI.createErrorAlert(e, "Error at register client ", e.getMessage()).show();
        }
    }

    @FXML
    private void btnCancelAction(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void comboBoxAddressAction(ActionEvent event) {
        selectedAddress = comboBoxAddress.getSelectionModel().getSelectedItem();
        txtSignUpZipCode.setText(selectedAddress.getZipcode());
        txtSignUpLatitude.setText(selectedAddress.getLatitude() + "");
        txtSignUpLongitude.setText(selectedAddress.getLongitude() + "");
    }

    public void associateParentUI(LoginUI loginUI) {
        this.loginUI = loginUI;
    }

//    private void setTextFieldsTextClient(ClientDTO object) {
//        txtSignUpSSN.setText(object.getNif() + "");
//        txtSignUpName.setText(object.getName());
//        comboBoxAddress.getSelectionModel().select(object.getAddress());
//        txtSignUpCardNumber.setText(object.getCreditCard().getNumber() + "");
//        txtSignUpCCV.setText(object.getCreditCard().getCcv() + "");
//        txtSignUpValidThru.setText(object.getCreditCard().getValidThru());
//        txtSignUpEmail.setText(object.getEmail());
//        txtSignUpPassword.setText(object.getPassword());
//    }

    private ClientDTO getClientDTOFromTextFields() {
        ClientDTO c = new ClientDTO();
        c.setEmail(txtSignUpEmail.getText());
        c.setPassword(txtSignUpPassword.getText());
        c.setNIF(Utils.convertStringToInt(txtSignUpSSN.getText()));
        c.setName(txtSignUpName.getText());
        AddressDTO address = comboBoxAddress.getSelectionModel().getSelectedItem();
        c.setAddress(address);
        c.getCreditCard().setNumber(txtSignUpCardNumber.getText());
        c.getCreditCard().setCcv(Utils.convertStringToInt(txtSignUpCardNumber.getText()));
        c.getCreditCard().setValidThru(txtSignUpValidThru.getText());
        return c;
    }

}
