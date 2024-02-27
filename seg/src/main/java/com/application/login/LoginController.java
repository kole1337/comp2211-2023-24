package com.application.login;

import com.application.dashboard.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* @TODO
*   1. Implement SQL
* */

public class LoginController {
    public Button loginButton;
    public TextField passwordField;
    public TextField usernameField;
    public Label labelTest;

    //---
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger;

    @FXML
    public void loginFunc(ActionEvent event){
        //if login details are right, switch to dashboard
        if(usernameField.getText().equals("admin") && passwordField.getText().equals("0000")){
            try {
                root = FXMLLoader.load(getClass().getResource("dashboard-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.INFO, "Logging in. Opening dashboard.");
            } catch (IOException e) {
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
            //if the login details are wrong, show error
        }else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Problem");
            errorAlert.setContentText("Wrong credentials");
            usernameField.setText("");
            passwordField.setText("");

            errorAlert.showAndWait();
        }
    }
}