package com.application.menu;

import com.application.database.DbConnection;
import com.application.login.LoginApplication;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController extends Application {

    public BorderPane mainMenu;
    private Pane view;

    public void openFile(ActionEvent actionEvent) {
        Pane fill = getPage("import-view");
        mainMenu.setCenter(fill);
    }
    public void openDashboard(ActionEvent actionEvent) {
        Pane fill = getPage("dashboard-view");
        mainMenu.setCenter(fill);
    }

    public Pane getPage(String fileName){
        try{
            URL file = MenuController.class.getResource(fileName +".fxml");
            view = new FXMLLoader().load(file);
        }catch(Exception e){
            e.printStackTrace();
        }

        return view;
    }

    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("mainmenu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Ad dashboard");

        stage.setScene(scene);
        stage.setMinHeight(800);
        stage.setMinWidth(1300);
        stage.show();
        DbConnection db = new DbConnection();

    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(LoginApplication.class.getName());
        logger.log(Level.INFO, "Launching application.");
        launch();
    }


}
