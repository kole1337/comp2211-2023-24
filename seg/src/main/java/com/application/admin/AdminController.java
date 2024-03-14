package com.application.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger;
    public void logoutFunc(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/application/login/hello-view.fxml"));
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();


            scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.INFO, "Opening hello view.");
        } catch (IOException e) {
            logger = Logger.getLogger(getClass().getName());logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
}
