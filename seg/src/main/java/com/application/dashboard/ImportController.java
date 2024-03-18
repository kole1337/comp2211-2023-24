package com.application.dashboard;

import com.application.files.FileChooserWindow;
import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
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
    public void openCampaign(){
        FileChooserWindow fileChooser = new FileChooserWindow();
        //FilePathHandler pathHandler = new FilePathHandler();

        //pathHandler.fileTypeHandler(fileChooser.openFileBox());
        //String selectedFile;
        //System.out.println(selectedFile);

        //String [] paths = {"Click Log File", "Impression log file", "Server Log File"};
//        for (int i = 0; i < 3; i++) {
//            //paths[i] = fc.main();
//            //fileChooser.setTitle(paths[i]);
//            //fc.main();
//            selectedFile = fc.main();
//            paths[i] = selectedFile;
////            System.out.println(selectedFile);
//        }

        fph.fileTypeHandler(fileChooser.openFileBox());
        System.out.println(fph.getImpressionPath());
        System.out.println(fph.getClickPath());
        System.out.println(fph.getServerPath());
        System.out.println("Ready ^_^!");
        dashboardButton.setDisable(false);
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
