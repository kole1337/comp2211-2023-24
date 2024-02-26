package com.application.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
    public Button loginButton;
    public TextField passwordField;
    public TextField usernameField;
    @FXML
    private Label labelTest;

    @FXML
    protected void loginFunc(){
        labelTest.setText(usernameField.getText() + " & " + passwordField.getText());
    }
}