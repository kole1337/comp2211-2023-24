package com.application.admin;

import com.application.database.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger;

    UserManager userManager = new UserManager();

    @FXML
    private TableColumn<User, String> fName;

    @FXML
    private TableColumn<User, String> password;

    @FXML
    private TableView<User> showUsers = new TableView<>();

    @FXML
    private TableColumn<User, String> surName;

    @FXML
    private TableColumn<User, String> userName;

    public AdminController() throws Exception {
    }

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

    public void loadUsers(){
        userName.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        password.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        populateTableView();
    }

    private void populateTableView() {
        System.out.println("Entering");

        try {

            ResultSet rs = userManager.selectAll();
            System.out.println("Populations");
            while (rs.next()) {
                String fName = rs.getString("username");
                String surName = rs.getString("password");
                showUsers.getItems().add(new User(fName, surName));
                System.out.println(fName + ", " + surName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNewUser() {
        TextInputDialog tiDialog = new TextInputDialog();
        tiDialog.setTitle("New user");
        tiDialog.setHeaderText("Input the username and password");
        tiDialog.setContentText("Username: ");
//        tiDialog.setContentText("Password: ");

        tiDialog.showAndWait();
        String result = tiDialog.getResult().toString();
        if(result.isEmpty()){
            Alert a = new Alert(Alert.AlertType.WARNING, "Insert name!" ,ButtonType.OK, ButtonType.CANCEL);
            a.showAndWait();
            if(a.getResult() == ButtonType.OK){
                createNewUser();
            }
        }
    }
}
