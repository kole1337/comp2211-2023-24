package com.application.dashboard;
import com.application.database.*;
import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import com.application.logger.LogAction;
import com.application.styles.checkStyle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
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

        LogAction logAction = new LogAction();

    /**
     * The initialization of the import panel is
     * checking the theme
     * */
    public void initialize(){
            checkStyle obj = new checkStyle();
            String theme = obj.checkStyle();
            fph.innit();
            FileChooserWindow.setfph(fph);
            if(theme.equals("dark")){
                enableDarkTheme();
            }else{
                enableLightTheme();
            }
            logAction.logActionToFile("Launching Import scene.");
    }

    /**
     * Action for the logout button. It logs out the user and opens the login panel.
     * */
    public void logoutSession(ActionEvent event){
        try {
            root = FXMLLoader.load(getClass().getResource("/com/application/login/hello-view.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            logger.log(Level.INFO, "Opening login panel.");
            logAction.logActionToFile("Logging out to Login panel from Import panel.");
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Error opening login panel: " + e);
            a.show();
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
            logAction.logActionToFile("Critical error logging out: " + e);
        }
    }
    String fileFolderPath = "";

    /**
     * Function of the "Select campaign folder" button.
     * It opens a file explorer that only allows the selection of a directory.
     * */
    public void openCampaign(){
        logger.log(Level.INFO, "Opening campaign directory");
        try {
            fileChooser.openFileBox("all");
            if(fph.getClickPath() != null){
                clicksPath.setText(fph.getClickPath().getName());
            }
            if(fph.getImpressionPath() != null){
                impressionPath.setText(fph.getImpressionPath().getName());
            }
            if(fph.getServerPath() != null){
                serverPath.setText(fph.getServerPath().getName());
            }

        }catch(RuntimeException ignored){

        }
        catch(Exception e){
//            Alert a = new Alert(Alert.AlertType.WARNING, "Error opening file explorer: " +e);
//            a.show();
            logger.log(Level.SEVERE, "Error opening file explorer: " + e);
            logAction.logActionToFile("Error opening file explorer: " + e);
        }
    }
    public void opendir(){

    }

    /**
     * Function of the "Go to Dashboard" button.
     * It opens the Dashboard panel
     * */
    public void openDashboard(ActionEvent event) {
        logger.log(Level.INFO,"pressed open-dashboard button");

        if(fph.all_loaded() ){
            start_dash(event);
        }else{
            Alert a1 = new Alert(Alert.AlertType.WARNING,"Are you sure you want to proceed when there are files that are still unloaded?\n Some tables may not load correctly", ButtonType.YES,ButtonType.CANCEL);
            a1.setHeaderText("Not All Files Loaded!");
            Optional<ButtonType> result = a1.showAndWait();
            if(result.isPresent()) {
                if (result.get() == ButtonType.YES) {

                    start_dash(event);
                }
            }
        }

    }

    private void start_dash(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/application/login/dashboard-view.fxml"));
            root = fxmlLoader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            DashboardController dashboardController = fxmlLoader.getController();
            dashboardController.fph = this.fph;
            Alert loading = new Alert(Alert.AlertType.INFORMATION);
            loading.setContentText("Inputting data...");
            loading.show();
            dashboardController.loadSQL();
            loading.close();
            stage.show();

            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.INFO, "Logging in as user. Opening the dashboard.");
            logAction.logActionToFile("Opening dashboard panel.");
        }
        catch (Exception e) {                Alert a = new Alert(Alert.AlertType.WARNING, "Error opening dashboard: " + e);
            a.show();
            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window: ", e);
            logAction.logActionToFile("Critical error: " + e);}
    }

    public void select_directory(){
//        fph.directoy_handler(fileChooser.)
    }

    /**
     * Function that opens the directory from
     * openDashboard
     * It is meant to select the serverlog file.
     *
     * */
    public void selectServerLog() {
        String path = "";
        try {
            logger.log(Level.INFO, "Selecting serverlog");
            path = fileChooser.openSingleFileBox("Server Log").toString();

            fph.setServerPath(path);
            logAction.logActionToFile("Selecting serverlog.");
            serverPath.setText(path);
        }catch(Exception e){
//            Alert a = new Alert(Alert.AlertType.WARNING, "Error selecting server log: " + e);
//            a.show();
            logger = Logger.getLogger(getClass().getName());
            serverPath.clear();
//            logger.log(Level.SEVERE, "Error selecting server log:", e);
            logAction.logActionToFile("Error selecting server log: " + e);
        }
    }

    /**
     * Function that opens the directory from
     * openDashboard
     * It is meant to select the impressionlog file.
     *
     * */
    public void selectImpressionLog() {
        String path = "";
        try {
            logger.log(Level.INFO, "Selecting impressionlog.");
            path = fileChooser.openSingleFileBox("Impression Log").toString();
            fph.setImpressionPath(path);
            logAction.logActionToFile("Selecting impressionlog");
            impressionPath.setText(path);
        }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.WARNING, "Error selecting Impression Log: " + e);
//            a.show();
            logger = Logger.getLogger(getClass().getName());
            impressionPath.clear();
//            logger.log(Level.SEVERE, "Error selecting Impression Log:", e);
            logAction.logActionToFile("Error selecting Impression Log: " + e);
        }
    }

    /**
     * Function that opens the directory from
     * openDashboard
     * It is meant to select the clicklog file.
     *
     * */
    public void selectClickLog() {
        String path = "";
        try {
            logger.log(Level.INFO, "Selecting clicklog.");

            path = fileChooser.openSingleFileBox("Click Log").toString();
            fph.setClickPath(path);
            logAction.logActionToFile("Selecting clickLog");
            clicksPath.setText(path);


        }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.WARNING, "Error selecting clicklog: " + e);
//            a.show();
            logger = Logger.getLogger(getClass().getName());
            clicksPath.clear();
//            logger.log(Level.SEVERE, "Error selecting clicklog: ", e);
            logAction.logActionToFile("Error selecting clicklog: " + e);
        }

    }

    /**
     * Function that disables the dark theme (essentially enabling "light theme")
     * */
    public void enableLightTheme() {
        try {
            if (!light) {
                light = true;
                dark = false;
                logger.log(Level.INFO, "Light theme displayed");
                logAction.logActionToFile("Light theme displayed.");

                background.getStylesheets().clear();
            }
        }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.WARNING, "Error enabling light theme: " + e);
            a.show();
            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error enabling light theme: ", e);
            logAction.logActionToFile("Error enabling light theme: " + e);
        }
    }

    /**
     * Function that loads the darkTheme stylesheet
     * */
    public void enableDarkTheme(){
        logger.log(Level.INFO, "Loading dark theme");
        try {
            if (!dark) {
                logger.log(Level.INFO, "Dark theme displayed.");
                logAction.logActionToFile("Dark theme displayed.");

                String stylesheetPath = getClass().getClassLoader().getResource("uploadDarkTheme.css").toExternalForm();
                System.out.println(stylesheetPath);
                background.getStylesheets().add(stylesheetPath);
                dark = true;
                light = false;

            }
        }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.WARNING, "Error enabling dark theme: " + e);
            a.show();
            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error enabling dark theme: ", e);
            logAction.logActionToFile("Error enabling dark theme: " + e);
        }
    }

    DataManager dm = new DataManager();
    DbConnection db = new DbConnection();
    public void loadSQL(){
        try {
            dm.dumpData();
            Multithread_ImpressionDb multiImpress = new Multithread_ImpressionDb();
            testClickThread tct = new testClickThread();
            testServerThread tst = new testServerThread();
            FileSplit splitFiles = new FileSplit();
//        fph.setClickPath("asdffa");
//        fph.setImpressionPath("asfdsdfa");
//        fph.setServerPath("dasdadas");


            try {
                if (fph.getClickPath() != null) {
                    File file1 = fph.getClickPath();
//                File file1 = new File("C:\\Users\\gouri\\OneDrive - University of Southampton\\Documents\\year2\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\click_log.csv");
                    ArrayList<String> tempClicks = new ArrayList<>(splitFiles.splitFile(file1, 10));
                    tct.main(tempClicks);
                    System.out.println();
                    System.out.println("Importing");
                }
                if (fph.getImpressionPath() != null) {
                    File file1 = fph.getImpressionPath();
//                File file1 = new File("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\impression_log.csv");

                    ArrayList<String> tempClicks = new ArrayList<>(splitFiles.splitFile(file1, 10));
                    multiImpress.main(tempClicks);

                }
                if (fph.getServerPath() != null) {
                    File file1 = fph.getServerPath();
//                File file1 = new File("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\server_log.csv");

                    ArrayList<String> tempClicks = new ArrayList<>(splitFiles.splitFile(file1, 10));
                    tst.main(tempClicks);

                }

                db.closeConn();

                db.makeConn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Ready ^_^!");
    }
}
