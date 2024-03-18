package com.application.dashboard;

import com.application.files.FileChooser;
import com.application.files.FilePathHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportController {
    FilePathHandler fph = new FilePathHandler();
    private Parent root;
    private Scene scene;
    private Stage stage;
    private Logger logger = Logger.getLogger(getClass().getName());
    @FXML
    public Button importButton;
    @FXML
    public Button dashboardButton;
    public void initialize(){
        dashboardButton.setDisable(true);
    }
    public void openCampaign(ActionEvent actionEvent) {
        logger.log(Level.INFO,"pressed open campaign button");
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        String selectedFile;
        //System.out.println(selectedFile);
        FileChooser fc = new FileChooser();
        String [] paths = {"Click Log File", "Impression log file", "Server Log File"};
        paths = fc.main();
        System.out.println(paths[0]);
        fph.setClickPath(paths[0]);
        fph.setImpressionPath(paths[1]);
        fph.setServerPath(paths[2]);
        if(paths.length == 3){
            dashboardButton.setDisable(false);
        }
    }
    public void openDashboard(ActionEvent event) {
        logger.log(Level.INFO,"pressed open-dashboard button");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/application/login/dashboard-view.fxml"));
            root = fxmlLoader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            DashboardController dashboardController = fxmlLoader.getController();
            dashboardController.fph = this.fph;
            stage.show();
            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.INFO, "Logging in as user. Opening the dashboard.");
        } catch (IOException e) {
            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
}
