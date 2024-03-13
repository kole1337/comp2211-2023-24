package com.application.login;

import com.application.dashboard.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* @TODO
*   1. Implement SQL
* */

/*
* The controller for the Login panel.
* Checks for credentials and switches scenes.
* */

public class LoginController {
    public Button loginButton;
    public PasswordField passwordField;
    public TextField usernameField;
    public Label labelTest;

    //---
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger;

    /**
     * Login function that checks if the user is
     * an Admin or User.
     * Hardcoded for now, will implement
     * User System later.
     * */
    @FXML
    public void loginFunc(ActionEvent event){
        logger = Logger.getLogger(getClass().getName());
        logger.log(Level.INFO, "You pressed loginButton.");

        //if login details are right, switch to dashboard
        if(checkAdmin(usernameField.getText(), passwordField.getText())){
            try {
                root = FXMLLoader.load(getClass().getResource("admin-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.INFO, "Logging in as admin. Opening dashboard.");
            } catch (IOException e) {
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
            //if the login details are wrong, show error
        }else if (checkUser(usernameField.getText(), passwordField.getText())){
            try {
                root = FXMLLoader.load(getClass().getResource("dashboard-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.INFO, "Logging in as user. Opening dashboard.");
            } catch (IOException e) {
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
        }
        else {
            logger.log(Level.SEVERE, "Wrong credentials entered..");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Problem");
            errorAlert.setContentText("Wrong credentials");
            usernameField.setText("");
            passwordField.setText("");

            errorAlert.showAndWait();
        }
    }

    public Boolean checkUser(String username, String password) {
        logger.log(Level.SEVERE, "Checking user credentials.");
        return username.equals("user") && password.equals("0000");
    }

    public Boolean checkAdmin(String username, String password) {
        logger.log(Level.SEVERE, "Checking admin credentials.");
        return username.equals("admin") && password.equals("0000");
    }
}

