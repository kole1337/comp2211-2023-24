package com.application.admin;

import com.application.database.DbConnection;
import com.application.database.UserManager;
import com.application.logger.LogAction;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddUserController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private ChoiceBox selectRole;

    @FXML
    private Button cancelButton;

    LogAction logAction = new LogAction();
    DbConnection db = new DbConnection();
    UserManager um = new UserManager();
    private Logger logger = Logger.getLogger(getClass().getName());



    public void initialize(){
        String[] roles = new String[]{"admin","user"};
        selectRole.getItems().addAll(roles);
        logAction.logActionToFile("Opening Add User panel.");
    }

    /**
     * Function to add a user to the database.
     * It must have a username, password and role selected.
     * */
    @FXML
    public void addUserToDb() {
        try {
            if( usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                (selectRole.getValue() == null)) {
                // get a handle to the stage
                Alert a = new Alert(Alert.AlertType.WARNING, "Fields cannot be empty!", ButtonType.OK);
                a.show();
                logger.log(Level.WARNING,"Not all fields were selected.");
            }else{
                um.insertUser(usernameField.getText(),passwordField.getText(),selectRole.getValue().toString());

                Alert a = new Alert(Alert.AlertType.INFORMATION, "User inserted!");
                a.show();

                usernameField.clear();
                passwordField.clear();
                selectRole.setValue(null);
                logger.log(Level.INFO, "User was inserted successfully.");
                logAction.logActionToFile("User was inserted successfully.");

            }
        }catch(Exception e){
            e.printStackTrace();
            logger.log(Level.INFO, "Error adding user to database: " + e);
            logAction.logActionToFile("Error adding user to database: " + e);
            Alert a = new Alert(Alert.AlertType.WARNING, "Error adding user to database: " + e);
            a.show();
        }
    }

    /**
     * A function that closes the add user panel.
     * */
    @FXML
    public void cancelOperation() {
        try {
            if( !usernameField.getText().isEmpty() ||
                !passwordField.getText().isEmpty()) {
                // get a handle to the stage
                Alert a = new Alert(Alert.AlertType.WARNING, "Progress will be lost!", ButtonType.OK, ButtonType.CANCEL);
                Optional<ButtonType> result = a.showAndWait();
                if(result.isPresent() && result.get()==ButtonType.OK) {
                    Stage stage = (Stage) cancelButton.getScene().getWindow();
                    stage.close();
                }
            }else{
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.log(Level.INFO, "Error closing addUserPanel: " + e);
            logAction.logActionToFile("Error closing addUserPanel: " + e);
            Alert a = new Alert(Alert.AlertType.WARNING, "Error closing addUserPanel:" + e);
            a.show();
        }
    }
}
