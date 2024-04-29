package com.application.dashboard;
import com.application.database.UserManager;
import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import com.application.styles.checkStyle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportController {
    public TextField folderPath;
    public TextField serverPath;
    public TextField impressionPath;
    public TextField clicksPath;
    public AnchorPane background;
    FilePathHandler fph = new FilePathHandler();
    private Parent root;
    private Scene scene;
    private Stage stage;
    private Logger logger = Logger.getLogger(getClass().getName());
    @FXML
    public Button importButton;
    @FXML
    public Button dashboardButton;

    FileChooserWindow fileChooser = new FileChooserWindow();

    private Boolean light = true;
    private Boolean dark = false;
    String currentUser=  "";



    public void initialize(){
            checkStyle obj = new checkStyle();
            String theme = obj.checkStyle();

            if(theme.equals("dark")){
                enableDarkTheme();
            }else{
                enableLightTheme();
            }
//        Timer timer = new Timer();
//
//        timer.schedule(new UserExistenceTask(),0,3000);
    }


    public void setCurrentUser(String username){
        currentUser = username;
    }

    private class UserExistenceTask extends TimerTask {
        UserManager um = new UserManager();
        @Override
        public void run() {
            Platform.runLater(() -> {
                if(um.checkUserExistence(currentUser)){
                    System.out.println("Exists!");

                }else{
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Invalid Date-Time Selection");
                    errorAlert.setContentText("The end date-time must be after the start date-time.");
                    errorAlert.show();
                    System.out.println("Gooner!");
                    logoutSession(new ActionEvent());
                }
            });
        }
    }

    public void logoutSession(ActionEvent event){
        try {
            root = FXMLLoader.load(getClass().getResource("/com/application/login/hello-view.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.INFO, "Opening hello view.");
        } catch (IOException e) {
            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
    String fileFolderPath = "";
    public void openCampaign(){
        fileFolderPath = fileChooser.selectFolderPath();

        folderPath.setText(fileFolderPath);
//        fph.fileTypeHandler(fileChooser.openFileBox());
//        System.out.println(fph.getImpressionPath());
//        System.out.println(fph.getClickPath());
//        System.out.println(fph.getServerPath());
//        System.out.println("Ready ^_^!");
        //dashboardButton.setDisable(false);
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

    public void selectServerLog(ActionEvent actionEvent) {
        String path = "";
        path = fileChooser.openFileBox("Server Log").toString();
        fph.setServerPath(path);
        serverPath.setText(path);
    }

    public void selectImpressionLog(ActionEvent actionEvent) {
        String path = "";
        path = fileChooser.openFileBox("Impression Log").toString();
        fph.setImpressionPath(path);
        impressionPath.setText(path);
    }

    public void selectClickLog(ActionEvent actionEvent) {
        String path = "";
        path = fileChooser.openFileBox("Click Log").toString();
        fph.setClickPath(path);
        clicksPath.setText(path);
    }

    public void enableLightTheme() {
        if (!light) {
            light = true;
            dark = false;
            logger.log(Level.INFO,"Light theme displayed");
            background.getStylesheets().clear();
        }
    }

    public void enableDarkTheme(){
        logger.log(Level.INFO, "Loading dark theme");
        if(!dark){

            String stylesheetPath = getClass().getClassLoader().getResource("uploadDarkTheme.css").toExternalForm();;
            System.out.println(stylesheetPath);
            background.getStylesheets().add(stylesheetPath);
            dark = true;
            light = false;
            logger.log(Level.INFO,"Dark theme displayed");

        }
    }
}
