package com.application.admin;

import com.application.database.DbConnection;
import com.application.database.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class editUserController {

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
    public void openEditPanel(String username, String password, int userId){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editUser.fxml"));
            Parent parent = loader.load();
            editUserController controller = loader.getController(); // Get the controller instance
            controller.setFields(username, password, userId); // Call setFields method

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);

            stage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setFields(String username, String password, int userId){
        usernameField.setText(username);
        userIdField.setText(String.valueOf(userId));
        passwordField.setText(password);

        oldPassword = password;
        oldUsername = username;
        oldUserId = userId;
    }

    public void updateUser(){

        if( !oldUsername.equals(usernameField.getText()) ){
            System.out.println("updating username");
            um.updateUsername(oldUsername,usernameField.getText());
            oldUsername = usernameField.getText();
        }

        if( !oldPassword.equals(passwordField.getText())){
            System.out.println("updating password via username");
            
            um.updatePasswordWithUsername(passwordField.getText(), oldUsername);
            oldPassword = passwordField.getText();
        }
        
        if( oldUserId != Integer.parseInt(userIdField.getText()) ){
            System.out.println("updating user id");

            um.updateUserId(oldUserId, Integer.parseInt(userIdField.getText()));
            oldUserId = Integer.parseInt(userIdField.getText());
        }
    }

}
