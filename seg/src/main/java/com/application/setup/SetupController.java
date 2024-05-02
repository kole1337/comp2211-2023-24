package com.application.setup;

import com.application.database.DataManager;
import com.application.database.DbConnection;
import com.application.database.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class SetupController {

    public void initialize(){
    }

    @FXML
    private Button connectToDbButton;

    @FXML
    private Button createTablesButton;

    @FXML
    private Label dbConnectionStatusLabel;

    @FXML
    private TextField dbPasswordLabel;

    @FXML
    private Label dbUserfilePath;

    @FXML
    private TextField dbUsernameLabel;

    @FXML
    private Button testConnectionToDbButton;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    DbConnection db = new DbConnection();
    DataManager dm = new DataManager();
    UserManager um = new UserManager();
    @FXML
    void connectToDB(ActionEvent event) {

    }

    @FXML
    void createUsers(ActionEvent event) {
        try {
            um.insertUser("user", "1234", "user");
            um.insertUser("admin", "1234", "admin");
        }catch(Exception e){
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.WARNING, "Error creating users: " + e);
            a.show();
        }
    }

    @FXML
    void testConnectionToDB(ActionEvent event) {

    }
}
