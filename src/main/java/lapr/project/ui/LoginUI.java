/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lapr.project.controller.LoginController;
import javafx.stage.Stage;
import lapr.project.utils.Constants;

public class LoginUI implements Initializable {

    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtLoginEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnCancel1;

    private MainAppUI mainAppUI;

    private LoginController loginController;

    private SignUpUI signUpUI;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginController = new LoginController();
    }

    @FXML
    private void btnLoginAction(ActionEvent event) {

        String sId = txtLoginEmail.getText();

        String sPwd = txtPassword.getText();

        boolean sucesso = loginController.doLogin(sId, sPwd);
        if (!sucesso) {
            AlertsUI.createErrorAlert(new Exception(""), "Login", "Password or email invalid!").show();
        } else {

            try {
                //Fechar Login Stage
                Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                currentStage.close();
                mainAppUI.loadTabsByUserRole(loginController.getUserRole().getRole());
                mainAppUI.enableLogout(loginController.getUserEmail());

            } catch (Exception ex) {
                AlertsUI.createErrorAlert(ex, "Login", "Problem at Login").show();
            }

        }
    }

    @FXML
    private void btnCancelAction(ActionEvent event) {

        //Fechar Login Stage
        Stage currentStage = (Stage) btnCancel1.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void btnSignUpAction(ActionEvent event) {
        try {

            //Inciar stage Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.SIGNUP_FILE));
            Parent root = loader.load();
            signUpUI = loader.getController();
            signUpUI.associateParentUI(this);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(Constants.CSS_FILE_STYLES);
            stage.setTitle(Constants.SIGNUP_TITLE);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException ex) {
            AlertsUI.createErrorAlert(ex, "Sign Up", "Error at Sign Up").show();
        }

    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void associateParentUI(MainAppUI mainAppUI) {
        this.mainAppUI = mainAppUI;
    }
    

}
