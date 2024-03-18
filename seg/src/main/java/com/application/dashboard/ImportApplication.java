package com.application.dashboard;

import com.application.login.LoginApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ImportApplication.class.getResource("import-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setScene(scene);
        stage.setMinHeight(scene.getRoot().minHeight(-1));
        stage.setMinWidth(scene.getRoot().minWidth(-1));
        stage.show();
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(ImportApplication.class.getName());
        logger.log(Level.INFO, "Launching Import.");
        launch();
    }
}
