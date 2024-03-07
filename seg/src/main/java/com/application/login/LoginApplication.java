package com.application.login;

import com.application.files.FilePathHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Ad dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        FilePathHandler fph = new FilePathHandler();
        //hardcoded paths to use when debugging VVV
//        fph.setClickPath("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\click_log.csv");
//        fph.setImpressionPath("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\impression_log.csv");
//        fph.setServerPath("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\server_log.csv");
        Logger logger = Logger.getLogger(LoginApplication.class.getName());
        logger.log(Level.INFO, "Launching application.");

        launch();
    }
}