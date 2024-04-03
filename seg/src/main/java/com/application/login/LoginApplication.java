package com.application.login;

import com.application.database.DbConnection;
import com.application.database.UserManager;
import com.application.files.FilePathHandler;
import com.application.logger.LogAction;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginApplication extends Application {

    private static Logger logger = Logger.getLogger(LoginApplication.class.getName());


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Ad dashboard");

        stage.setScene(scene);
        stage.setMinHeight(800);
        stage.setMinWidth(1300);
        stage.show();
//        DbConnection db = new DbConnection();

    }

    public static void main(String[] args) {
        logger.log(Level.INFO, "Launching application.");
        launch();
    }
}