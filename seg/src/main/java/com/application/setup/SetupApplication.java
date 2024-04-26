package com.application.setup;

import com.application.database.DbConnection;
import com.application.login.LoginApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SetupApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(SetupApplication.class.getResource("setup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Ad dashboard");

        stage.setScene(scene);
        stage.setMinHeight(scene.getRoot().minHeight(-1));
        stage.setMinWidth(scene.getRoot().minWidth(-1));
        stage.show();
        DbConnection db = new DbConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}
