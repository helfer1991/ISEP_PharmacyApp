/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertsUI {

    public static Alert createErrorAlert(Exception ex, String title, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(ex.getMessage());

        return alert;
    }

    public static Alert createAlert(Alert.AlertType alterType, String title, String header, String message) {
        Alert alert = new Alert(alterType);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        return alert;
    }

    public static boolean confirmAlertShow(String title, String header, String context) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);

        if (alert.showAndWait().get() == ButtonType.CANCEL) {
            alert.close();
            return false;
        }else{
            alert.close();
            return true;
        }
    }
}
