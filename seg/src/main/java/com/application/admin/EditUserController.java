package com.application.admin;

import com.application.database.DbConnection;
import com.application.database.UserManager;
import com.application.logger.LogAction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EditUserController {

    @FXML
    public TextField passwordField;
    @FXML
    public TextField userIdField;
    @FXML
    public TextField usernameField;

    private String oldUsername;
    private String oldPassword;
    private int oldUserId;

    DbConnection dbConnection = new DbConnection();
    UserManager um = new UserManager();
    LogAction logAction = new LogAction();

    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Function that opens the editUser panel.
     * @param username - the username of the user to be edited
     * @param password - the current password of the user, encrypted
     * @param userId   - the id of the user to be edited
     * */
    public void openEditPanel(String username, String password, int userId){
        try {
            logger.log(Level.INFO,"Opening editUser panel.");
            logAction.logActionToFile("Opening editUser panel.");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("editUser.fxml"));
            Parent parent = loader.load();
            EditUserController controller = loader.getController(); // Get the controller instance
            controller.setFields(username, password, userId); // Call setFields method

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);

            stage.show();
        } catch(Exception e){
            logger.log(Level.INFO,"Error opening editUser panel:" + e);
            logAction.logActionToFile("Error opening editUser panel:" + e);
            Alert a = new Alert(Alert.AlertType.WARNING, "Error opening editUser panel:"+ e);
            a.show();

            e.printStackTrace();
        }
    }


    /**
     * This function updates the TextFields and the local variables
     * used to compare the old data to the new data.
     * */
    public void setFields(String username, String password, int userId){
        usernameField.setText(username);
        userIdField.setText(String.valueOf(userId));
        passwordField.setText(password);

        oldPassword = password;
        oldUsername = username;
        oldUserId = userId;
    }

    /**
     * Function that checks what field is changed, and then updates them.
     * */
    public void updateUser(){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        String fieldsUpdated = "";
        try {
            if (!usernameField.getText().isEmpty() || !passwordField.getText().isEmpty() || !userIdField.getText().isEmpty()) {
                if (!oldUsername.equals(usernameField.getText())) {

                    logAction.logActionToFile("Updating username.");
                    logger.log(Level.INFO, "Updating username.");

                    um.updateUsername(oldUsername, usernameField.getText());
                    oldUsername = usernameField.getText();
                    fieldsUpdated.concat(" Username");

                }

                if (!oldPassword.equals(passwordField.getText())) {

                    logAction.logActionToFile("Updating password.");
                    logger.log(Level.INFO, "Updating password.");

                    um.updatePasswordWithUsername(passwordField.getText(), oldUsername);
                    oldPassword = passwordField.getText();
                    fieldsUpdated.concat(" Password");
                }

                if (oldUserId != Integer.parseInt(userIdField.getText())) {
                        logAction.logActionToFile("Updating User ID.");
                        logger.log(Level.INFO, "Updating User ID.");

                        um.updateUserId(oldUserId, Integer.parseInt(userIdField.getText()));
                        oldUserId = Integer.parseInt(userIdField.getText());
                        fieldsUpdated.concat(" User ID");

                }



                    fieldsUpdated.replace(' ', ';');
                    a.setContentText("Fields updated: " + fieldsUpdated);
                    a.show();


            } else {
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("Fields cannot be empty!");
                a.show();
            }
        }catch(Exception e){
            logger.log(Level.INFO,"Error updating user details: " + e);
            logAction.logActionToFile("Error updating user details: " + e);
            Alert a1 = new Alert(Alert.AlertType.WARNING, "Error updating user details: "+ e);
            a1.show();
            e.printStackTrace();
        }
    }

}
