package com.application.admin;

import com.application.database.DbConnection;
import com.application.database.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class addUserController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private ChoiceBox selectRole;

    @FXML
    private Button cancelButton;

    DbConnection db = new DbConnection();
    UserManager um = new UserManager();



    public void initialize(){
        String[] roles = new String[]{"admin","user"};


        selectRole.getItems().addAll(roles);


    }

    @FXML
    public void addUserToDb() {
        try {
            if( usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                (selectRole.getValue() == null)) {
                // get a handle to the stage
                Alert a = new Alert(Alert.AlertType.WARNING, "Fields cannot be empty!", ButtonType.OK);
                a.show();
            }else{
                um.insertUser(usernameField.getText(),passwordField.getText(),selectRole.getValue().toString());

                Alert a = new Alert(Alert.AlertType.INFORMATION, "User inserted!");
                a.show();

                usernameField.clear();
                passwordField.clear();
                selectRole.setValue(null);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(usernameField.getText()+" "+passwordField.getText()+" "+selectRole.getValue());
    }

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
                    // do what you have to do
                    stage.close();
                }
            }else{
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                // do what you have to do
                stage.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
