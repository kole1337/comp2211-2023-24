package com.application.dashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* @TODO
*   1. Implement CSV input
*   2. Display CSV info
* */

public class DashboardController {
    public MenuItem logoutMenuItem;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger;

    public void logoutAction(ActionEvent event) {
        //this should be the logout function from the menu item Logout
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Problem");
        errorAlert.setContentText("Wrong credentials");
        errorAlert.showAndWait();
    }

    //Logout function for button
    public void logoutButton(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/application/login/hello-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.INFO, "Opening hello view.");
        } catch (IOException e) {
            logger = Logger.getLogger(getClass().getName());logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }}
}
