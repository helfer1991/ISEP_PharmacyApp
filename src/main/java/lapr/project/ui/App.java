package lapr.project.ui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lapr.project.utils.Constants;

/**
 * @author Nuno Bettencourt <nmb@isep.ipp.pt> on 24/05/16.
 */
public class App extends Application{

    /**
     * Logger class.
     */
    private static final Logger LOGGER = Logger.getLogger("MainLog");

    @Override
    public void start(Stage stage) throws Exception {
       if (LOGGER.isLoggable(Level.INFO))
            LOGGER.log(Level.INFO, "");

//        //load database properties
//        try {
//            Properties properties =
//                    new Properties(System.getProperties());
//            InputStream input = new FileInputStream("target/classes/application.properties");
//            properties.load(input);
//            input.close();
//            System.setProperties(properties);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
        
         try {


            //Inciar App
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.MAIN_APP_FILE));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            //não temos ficheiro css para já
            //scene.getStylesheets().add(Constants.CSS_FILE_STYLES);
            stage.setTitle(Constants.MAIN_APP_TITLE);

            stage.setScene(scene);

            stage.setOnCloseRequest(
                    new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event
                ) {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);

                    alerta.setTitle(Constants.MSG_APPLICATION);
                    alerta.setHeaderText(Constants.MSG_CONFIRM);
                    alerta.setContentText(Constants.MSG_ARE_SURE_EXIT);

                    if (alerta.showAndWait().get() == ButtonType.CANCEL) {
                        event.consume();
                        System.exit(0);
                    }
                }
            }
            );
            stage.setResizable(
                    false);
            stage.show();

        } catch (IOException ex) {
            AlertsUI.createErrorAlert(ex, Constants.MSG_APPLICATION, Constants.MSG_ROBLEM_AT_START_APP).show();
        }
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}

