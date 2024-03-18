package com.application.login;

import com.application.admin.AdminController;
import com.application.dashboard.DashboardController;
import com.application.database.DbConnection;
import com.application.database.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
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

    //---
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger = Logger.getLogger(getClass().getName());
    ;



        /**
     * Login function that checks if the user is
     * an Admin or User.
     * Hardcoded for now, will implement
     * User System later.
     */
    @FXML
    public void loginFunc(ActionEvent event) throws Exception {
        //dbConnection.makeConn("root", "jojo12345");

        logger.log(Level.INFO, "You pressed loginButton.");


        //if the login details are wrong, show error
        if (checkUser(usernameField.getText(), passwordField.getText())) {
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
                root = FXMLLoader.load(getClass().getResource("admin-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);

//                AdminController ac = new AdminController();
//                ac.loadUsers();

                stage.setScene(scene);

                stage.setMinHeight(720);
                stage.setMinWidth(1280);
                stage.show();
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

}

