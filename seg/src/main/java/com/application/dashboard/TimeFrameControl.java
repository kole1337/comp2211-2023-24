package com.application.dashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Timeframe {

    public void createTimeFrame(){
        Label prompt = new Label ("Select the date from - to -") // this is based on the csv start date and end date
        DatePicker from = new DatePicker();
        Label fromLabel = new Label("Start Date:");
        r.getChildren().add(fromLabel); // Add the label before the DatePicker
        r.getChildren().add(from);

        // Create a date picker for "to" date
        DatePicker to = new DatePicker();
        Label toLabel = new Label("End Date:");
        r.getChildren().add(toLabel); // Add the label before the DatePicker
        r.getChildren().add(to);
        startDate = from.toString();

        endDate = to.toString();

        // Add a button that shows the selected date range when clicked
        Button showRangeButton = new Button("Show");
        showRangeButton.setOnAction(e -> {
            System.out.println("From: " + from.getValue() + ", To: " + to.getValue());
            try {
                createChart(fram, to);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        r.getChildren().add(showRangeButton); // Add the button to your TilePane

        // Logic to ensure "to" date is not before "from" date
        from.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.isAfter(to.getValue())) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("ERROR");
                errorAlert.setContentText("the start date must be before the end date");
                errorAlert.showAndWait();
            }

        });

        to.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.isBefore(from.getValue())) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("ERROR");
                errorAlert.setContentText("the end date must be after the start date");
                errorAlert.showAndWait();
            }
        });

    }
    public void createChart(DatePicker f, DatePicker t) {

    }

}