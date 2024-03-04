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
        Label prompt = new Label("Select the date and time from - to -"); // Updated label to include time
        r.getChildren().add(prompt);

        // DatePicker for "from" date
        DatePicker fromDate = new DatePicker();
        ComboBox<String> fromHour = new ComboBox<>();
        ComboBox<String> fromMinute = new ComboBox<>();
        ComboBox<String> fromSecond = new ComboBox<>();
        setupTimeComboBoxes(fromHour, fromMinute, fromSecond); // Setup method for time ComboBoxes
        HBox fromTime = new HBox(fromHour,fromMinute, fromSecond);

        // Adding components fo HBox h = new HBox(fromHour,fromMinute, fromSecond);r "from" date and time selection
        r.getChildren().addAll(new Label("Start Date and Time:"), fromDate, fromTime);

        // DatePicker for "to" date
        DatePicker toDate = new DatePicker();
        ComboBox<String> toHour = new ComboBox<>();
        ComboBox<String> toMinute = new ComboBox<>();
        ComboBox<String> toSecond = new ComboBox<>();
        setupTimeComboBoxes(toHour, toMinute, toSecond); // Setup method for time ComboBoxes
        HBox toTime= new HBox(toHour, toMinute, toSecond);

        // Adding components for "to" date and time selection
        r.getChildren().addAll(new Label("End Date and Time:"), toDate, toTime);

        // Logic to ensure "to" datetime is not before "from" datetime
        // Note: You need to combine both date and time for validation
        fromDate.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        toDate.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        fromHour.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        fromMinute.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        fromSecond.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        toHour.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        toMinute.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        toSecond.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));


        // Add a button that shows the selected datetime range when clicked
        Button showRangeButton = new Button("Show");
        showRangeButton.setOnAction(e -> {
            LocalDateTime fromDateTime = LocalDateTime.of(fromDate.getValue(), LocalTime.of(Integer.parseInt(fromHour.getValue()), Integer.parseInt(fromMinute.getValue()), Integer.parseInt(fromSecond.getValue())));
            LocalDateTime toDateTime = LocalDateTime.of(toDate.getValue(), LocalTime.of(Integer.parseInt(toHour.getValue()), Integer.parseInt(toMinute.getValue()), Integer.parseInt(toSecond.getValue())));

            System.out.println("From: " + fromDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                    ", To: " + toDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            filterData(fromDate, fromHour, fromMinute, fromSecond, toDate, toHour, toMinute, toSecond);

        });
        r.getChildren().add(showRangeButton); // Add the button to your TilePane
    }

    private void setupTimeComboBoxes(ComboBox<String> hour, ComboBox<String> minute, ComboBox<String> second) {
        hour.getItems().addAll(generateTimeOptions(0, 23)); // Hours 0-23
        minute.getItems().addAll(generateTimeOptions(0, 59)); // Minutes 0-59
        second.getItems().addAll(generateTimeOptions(0, 59)); // Seconds 0-59

        hour.getSelectionModel().select("00"); // Default value
        minute.getSelectionModel().select("00"); // Default value
        second.getSelectionModel().select("00"); // Default value
    }

    private java.util.List<String> generateTimeOptions(int start, int end) {
        java.util.List<String> options = new java.util.ArrayList<>();
        for (int i = start; i <= end; i++) {
            options.add(String.format("%02d", i));
        }
        return options;
    }
    private void validateDateTime(DatePicker fromDate, ComboBox<String> fromHour,  ComboBox<String> fromMinute,  ComboBox<String> fromSecond, DatePicker toDate,  ComboBox<String> toHour,  ComboBox<String> toMinute,  ComboBox<String> toSecond) {
        if (fromDate.getValue() != null && toDate.getValue() != null &&
                fromHour.getValue() != null && fromMinute.getValue() != null && fromSecond.getValue() != null &&
                toHour.getValue() != null && toMinute.getValue() != null && toSecond.getValue() != null) {

            LocalDateTime fromDateTime = LocalDateTime.of(fromDate.getValue(),
                    LocalTime.of(Integer.parseInt(fromHour.getValue()), Integer.parseInt(fromMinute.getValue()),
                            Integer.parseInt(fromSecond.getValue())));
            LocalDateTime toDateTime = LocalDateTime.of(toDate.getValue(),
                    LocalTime.of(Integer.parseInt(toHour.getValue()), Integer.parseInt(toMinute.getValue()),
                            Integer.parseInt(toSecond.getValue())));

            // Check if "to" datetime is before "from" datetime
            if (toDateTime.isBefore(fromDateTime)) {
                // Show error message
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Invalid Date-Time Selection");
                errorAlert.setContentText("The end date-time must be after the start date-time.");
                errorAlert.showAndWait();

                // Optionally, reset the "to" date-time selection to match the "from" date-time or to a valid state
                toDate.setValue(fromDate.getValue());
                toHour.setValue(fromHour.getValue());
                toMinute.setValue(fromMinute.getValue());
                toSecond.setValue(fromSecond.getValue());
            }
        }
    }
    public void filterData(DatePicker fromDate, ComboBox<String> fromHour,  ComboBox<String> fromMinute,  ComboBox<String> fromSecond, DatePicker toDate,  ComboBox<String> toHour,  ComboBox<String> toMinute,  ComboBox<String> toSecond) {

    }

}