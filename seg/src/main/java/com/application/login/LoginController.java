package com.application.login;

import com.application.database.DbConnection;
import com.application.database.UserManager;
import com.application.logger.LogAction;
import com.application.setup.styles.checkStyle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * The controller for the Login panel.
 * Checks for credentials and switches scenes.
 * */

public class LoginController {

    /**
     * @UserManager: Connect with the DB
     * */
    public UserManager userManager;
    public DbConnection dbConnection = new DbConnection();
    public Button loginButton;
    public PasswordField passwordField;
    public TextField usernameField;
    public AnchorPane panel;
    //---
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger = Logger.getLogger(getClass().getName());
    private FXMLLoader fxmlLoader;
    private Boolean light = true;
    private Boolean dark = false;

    /**
     * At initialization, the application
     * is checking the theme.
     * */
    public void initialize(){
        checkStyle obj = new checkStyle();
        String theme = obj.checkStyle();

        if(theme.equals("dark")){
            enableDarkTheme();
        }else{
            enableLightTheme();
        }
    }


    /**
     * Login function that checks if the user is
     * an Admin or User.
     */
    @FXML
    public void loginFunc(ActionEvent event) throws Exception {
        logger.log(Level.INFO, "You pressed loginButton.");
        if(usernameField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Please enter a valid username.",ButtonType.OK);
            alert.showAndWait();

        }//if the login details are wrong, show error
        else if (checkUser(usernameField.getText(), passwordField.getText())) {
            try {
                LogAction la = new LogAction(usernameField.getText());
                logger.log(Level.INFO, "Logging in as user. Opening dashboard.");
                la.logActionToFile("Logging as "+usernameField.getText()+". Opening dashboard");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("import-view.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                System.out.println(usernameField.getText());
                stage.setScene(scene);
                stage.show();

               } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
        }else {
            logger.log(Level.SEVERE, "Wrong credentials entered.");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Problem");
            errorAlert.setContentText("Wrong credentials. Try again or change your password using ''Forgot password''.");
            usernameField.setText("");
            passwordField.setText("");

            errorAlert.showAndWait();
        }
    }

    /**
     * Function that passes the user details to the UserManager class.
     * It returns true if the user exists and false if the user doesn't exist, the password
     * is wrong
     * */
    public Boolean checkUser(String username, String password){

        Boolean result = false;
        try {
            //if the user exists and has the correct roles, they will be logged in.
            logger.log(Level.INFO, "Checking user credentials.");
            result = userManager.selectUser(username, password);
            return result;
        }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.WARNING, "Error checking user credentials: " + e);
            a.show();
            logger.log(Level.WARNING, "Error with SQL: " + e);
        }
        return result;
    }

    /**
     * Function for the admin button to open the admin panel.
     * */
    public void adminFunc(ActionEvent event) throws Exception {

        logger.log(Level.INFO, "Checking admin credentials");
        if (checkAdmin(usernameField.getText(), passwordField.getText())) {
            try {
                LogAction la = new LogAction(usernameField.getText());
                la.logActionToFile("Opening admin dashboard");
                logger.log(Level.INFO, "Logging in as admin. Opening dashboard.");

                root = fxmlLoader.load(getClass().getResource("admin-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setMinHeight(720);
                stage.setMinWidth(1280);
                stage.show();

            } catch (IOException e) {
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
        } else {
            logger.log(Level.SEVERE, "Wrong credentials entered.");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Problem");
            errorAlert.setContentText("Wrong credentials. Try again or change your password using ''Forgot password''.");
            usernameField.setText("");
            passwordField.setText("");

            errorAlert.showAndWait();
        }
    }

    /**
     * Function that checks the user credentials.
     * */
    public Boolean checkAdmin(String username, String password){
        Boolean result = false;
        try {
            logger.log(Level.INFO, "Checking admin credentials.");
            result = userManager.selectAdmin(username, password);
            return result;
        }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.WARNING, "Error checking user credentials: " + e);
            a.show();
            logger.log(Level.WARNING, "Error with SQL: " + e);
        }
        return result;
    }

    /**
     * Function to enable light theme.
     * */
    public void enableLightTheme() {
        logger.log(Level.INFO, "Loading light theme");

        try {
            if (!light) {
                light = true;
                dark = false;
                logger.log(Level.INFO, "Light theme displayed.");
                panel.getStylesheets().clear();
            }
        }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.WARNING, "Error displaying light theme. This won't affect the performance");
            a.show();
            logger.log(Level.WARNING, "Error displaying the theme: " + e);
        }
    }

    /**
     * Function to enable dark theme.
     * */
    public void enableDarkTheme(){
        try {
            logger.log(Level.INFO, "Loading dark theme.");
            if (!dark) {

                String stylesheetPath = getClass().getClassLoader().getResource("loginDarkTheme.css").toExternalForm();
                ;
                System.out.println(stylesheetPath);
                panel.getStylesheets().add(stylesheetPath);
                dark = true;
                light = false;
                logger.log(Level.INFO, "Dark theme displayed");

            }
        }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.WARNING, "Error displaying dark theme. This won't affect the performance");
            a.show();
            logger.log(Level.WARNING, "Error displaying the theme: " + e);
        }
    }


    /**
     * Function that opens the window from where the user can change their password.
     * It opens the changePassword.fxml panel
     * */
    public void openChangePassword() {
        logger.log(Level.INFO,"Opening change password panel.");
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("changePassword.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.WARNING, "Error opening changing password panel: " + e);
            a.show();
            logger.log(Level.SEVERE, "Error opening change password panel: " +e);
        }
    }
}

