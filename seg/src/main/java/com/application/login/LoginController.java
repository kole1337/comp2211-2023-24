package com.application.login;

import com.application.database.DbConnection;
import com.application.database.UserManager;
import com.application.logger.LogAction;
import com.application.styles.checkStyle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
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
        //dbConnection.makeConn("root", "jojo12345");

        logger.log(Level.INFO, "You pressed loginButton.");
        //if the login details are wrong, show error
        if (checkUser(usernameField.getText(), passwordField.getText())) {
            try {
                LogAction la = new LogAction(usernameField.getText());
                logger.log(Level.INFO, "Logging in as user. Opening dashboard.");
                root = FXMLLoader.load(getClass().getResource("import-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

                la.logActionToFile("Logging as user. Opening dashboard");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
        }else {
            logger.log(Level.SEVERE, "Wrong credentials entered..");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Problem");
            errorAlert.setContentText("Wrong credentials");
            usernameField.setText("");
            passwordField.setText("");

            errorAlert.showAndWait();
        }
    }

    public Boolean checkUser(String username, String password) throws SQLException {
        logger.log(Level.INFO, "Checking user credentials.");
        return userManager.selectUser(username, password);
    }

    public void adminFunc(ActionEvent event) throws Exception {
        //dbConnection.makeConn("root", "jojo12345");

        logger.log(Level.INFO, "Checking admin credentials");
        if (checkAdmin(usernameField.getText(), passwordField.getText())) {
            try {
                LogAction la = new LogAction(usernameField.getText());
                root = fxmlLoader.load(getClass().getResource("admin-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);

//                AdminController ac = new AdminController();
//                ac.loadUsers();

                stage.setScene(scene);

                stage.setMinHeight(720);
                stage.setMinWidth(1280);
                stage.show();
                la.logActionToFile("Opening admin dashboard");
                logger.log(Level.INFO, "Logging in as admin. Opening dashboard.");
            } catch (IOException e) {
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            logger.log(Level.SEVERE, "Wrong credentials entered..");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Problem");
            errorAlert.setContentText("Wrong credentials");
            usernameField.setText("");
            passwordField.setText("");

            errorAlert.showAndWait();
        }
    }
    public Boolean checkAdmin(String username, String password) throws SQLException {
        logger.log(Level.SEVERE, "Checking admin credentials.");
        return userManager.selectAdmin(username, password);
    }

    public void enableLightTheme() {
        if (!light) {
            light = true;
            dark = false;
            logger.log(Level.INFO,"Light theme displayed");
            panel.getStylesheets().clear();
        }
    }

    public void enableDarkTheme(){
        logger.log(Level.INFO, "Loading dark theme");
        if(!dark){

            String stylesheetPath = getClass().getClassLoader().getResource("loginDarkTheme.css").toExternalForm();;
            System.out.println(stylesheetPath);
            panel.getStylesheets().add(stylesheetPath);
            dark = true;
            light = false;
            logger.log(Level.INFO,"Dark theme displayed");

        }
    }

    public void darkLoader() {
        enableDarkTheme();
    }

    public void openChangePassword(MouseEvent mouseEvent) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("changePassword.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

