package com.application.login;

import com.application.admin.User;
import com.application.database.DbConnection;
import com.application.database.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class updatePassword {
    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField idField;

    @FXML
    private TextField usernameField;

    DbConnection db = new DbConnection();
    UserManager um = new UserManager();

    @FXML
    void changePassword() {
        Alert a = new Alert(Alert.AlertType.NONE);

        try {
            if(usernameField.getText().isEmpty() && idField.getText().isEmpty()){
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("You must enter a username or User ID.");

                a.show();
            }else if(passwordField.getText().isEmpty()){
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("You must enter a password.");

                a.show();
            }
            else {
                if (usernameField.getText().isEmpty()) {
                    System.out.println("Change with ID");
                    um.updateUserPasswordID(idField.getText(), passwordField.getText());
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("Password updated");
                    a.show();
                } else {
                    System.out.println("Change with username");
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("Password updated");
                    a.show();
                    um.updateUserPasswordUsername(usernameField.getText(), passwordField.getText());
                }
            }
        }catch(Exception e){
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Problems arose");

            a.show();
        }
    }

    @FXML
    void closeButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

}
