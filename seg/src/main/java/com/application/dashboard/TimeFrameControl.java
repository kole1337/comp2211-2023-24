package com.application.dashboard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;



public class TimeFrameControl extends DatasetCreator{
    /**
     * This is a VBox that
     */
    private VBox timeControlVBox = new VBox ();
    private DatePicker fromDate = new DatePicker();
    private ComboBox<String> fromHour = new ComboBox<>();
    private ComboBox<String> fromMinute = new ComboBox<>();
    private ComboBox<String> fromSecond = new ComboBox<>();
    private DatePicker toDate = new DatePicker();
    private ComboBox<String> toHour = new ComboBox<>();
    private ComboBox<String> toMinute = new ComboBox<>();
    private ComboBox<String> toSecond = new ComboBox<>();


    public TimeFrameControl() {
        super();
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

    }

    public void createTimeFrame(){


        // DatePicker for "from" date

        setupTimeComboBoxes(fromHour, fromMinute, fromSecond); // Setup method for time ComboBoxes
        HBox fromTimeHBox = new HBox(fromHour,new Label (":"), fromMinute, new Label (":"), fromSecond);


        // Adding components fo HBox h = new HBox(fromHour,fromMinute, fromSecond);r "from" date and time selection
        timeControlVBox.getChildren().addAll(new Label("Start Date and Time:"), fromDate, fromTimeHBox);

        // DatePicker for "to" date
        setupTimeComboBoxes(toHour, toMinute, toSecond); // Setup method for time ComboBoxes
        HBox toTime= new HBox(toHour,new Label (":"), toMinute,new Label (":"), toSecond);

        // Adding components for "to" date and time selection
        timeControlVBox.getChildren().addAll(new Label("End Date and Time:"), toDate, toTime);



        // Add a button that shows chart based on the selected datetime range when clicked
        Button showRangeButton = new Button("Show");
        showRangeButton.setOnAction(e -> {
            LocalDateTime fromDateTime = LocalDateTime.of(fromDate.getValue(), LocalTime.of(Integer.parseInt(fromHour.getValue()), Integer.parseInt(fromMinute.getValue()), Integer.parseInt(fromSecond.getValue())));
            LocalDateTime toDateTime = LocalDateTime.of(toDate.getValue(), LocalTime.of(Integer.parseInt(toHour.getValue()), Integer.parseInt(toMinute.getValue()), Integer.parseInt(toSecond.getValue())));

            // chart display here
            // add method here
        });
        timeControlVBox.getChildren().add(showRangeButton);
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
            }else if (toDateTime.isAfter(endTime) || fromDateTime.isAfter(endTime) || fromDateTime.isBefore(startTime) || toDateTime.isBefore(startTime)){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Invalid Date-Time Selection");
                // errorAlert.setContentText("The to date-time must be before the start date-time.");
                errorAlert.showAndWait();
            }
        }
    }


    // this is an override method
    public Map<LocalDateTime, Integer> createDataset(String graphName, String time, LocalDateTime fromTime, LocalDateTime toTime) {
        if (graphName.equals("TotalClicks")) {
            return createCountByTimeDataset(clicksCsv, time, fromTime, toTime);
        } else if (graphName.equals("TotalImpressions")) {
            return createCountByTimeDataset(impressionsCsv, time, fromTime, toTime);
        } else if (graphName.equals("TotalUniques")) {
            return createUniqueClicksDataset(time, fromTime, toTime);
        } else {
            return null;
        }
    }
    // this is the method that get data within time frame selected by user
    private List<String []> getDataWithinTimeFrame(String csvFile, LocalDateTime fromTime, LocalDateTime toTime){
        try(CSVReader reader = new CSVReader(new FileReader(csvFile))){
            reader.readNext();
            List<String []> records = reader.readAll();
            List<String []> withinTimeFrameRecords = new ArrayList<String []>();
            for(String[] record: records){
                String dateStr = record[0];
                LocalDateTime date = LocalDateTime.parse(dateStr, dateFormatter);
                if(date.isAfter(fromTime) && date.isBefore(toTime)){
                    withinTimeFrameRecords.add(record);
                }
            }
            return withinTimeFrameRecords;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // this is an override method
    private Map<LocalDateTime, Integer> createCountByTimeDataset(String csvFile, String time, LocalDateTime fromTime, LocalDateTime toTime) {
        Map<LocalDateTime, Integer> countByTime = new TreeMap<>();
        List<String []> records = getDataWithinTimeFrame(csvFile, fromTime, toTime);

        for (String[] record : records) {
            String dateString = record[0];
            LocalDateTime date = LocalDateTime.parse(dateString, dateFormatter);

            if (date.isAfter(startTime) && date.isBefore(endTime)) {
                LocalDateTime roundedDate;
                if (time.equals("hour")) {
                    roundedDate = date.withMinute(0).withSecond(0);
                } else if (time.equals("day")) {
                    roundedDate = date.withHour(0).withMinute(0).withSecond(0);
                } else if (time.equals("week")) {
                    int week = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                    roundedDate = date.withHour(0).withMinute(0).withSecond(0);
                } else {
                    System.out.println("Enter a valid time period");
                    return countByTime;
                }
                countByTime.put(roundedDate, countByTime.getOrDefault(roundedDate, 0) + 1);
            }
        }

        return countByTime;
    }

    // this is an override method
    private Map<LocalDateTime, Integer> createUniqueClicksDataset(String time, LocalDateTime fromTime, LocalDateTime toTime) {
        List<String> seen = new ArrayList<>();
        Map<LocalDateTime, Integer> countByTime = new TreeMap<>();
        List<String []> records = getDataWithinTimeFrame(clicksCsv, fromTime, toTime);

        for (String[] record : records) {
            String dateString = record[0];
            String id = record[1];
            if (!seen.contains(id)) {
                seen.add(id);
                LocalDateTime date = LocalDateTime.parse(dateString, dateFormatter);
                if (date.isAfter(startTime) && date.isBefore(endTime)) {
                    LocalDateTime roundedDate;
                    if (time.equals("hour")) {
                        roundedDate = date.withMinute(0).withSecond(0);
                    } else if (time.equals("day")) {
                        roundedDate = date.withHour(0).withMinute(0).withSecond(0);
                    } else if (time.equals("week")) {
                        int week = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                        roundedDate = date.withHour(0).withMinute(0).withSecond(0);
                    } else {
                        System.out.println("Enter a valid time period");
                        return countByTime;
                    }
                    countByTime.put(roundedDate, countByTime.getOrDefault(roundedDate, 0) + 1);
                }
            }
        }
        return countByTime;
    }


}
