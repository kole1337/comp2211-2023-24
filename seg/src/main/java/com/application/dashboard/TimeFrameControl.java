package com.application.dashboard;

import com.application.files.FilePathHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;



public class TimeFrameControl extends DatasetCreator {
    /**
     * This is a VBox that holds for the time control DatePicker and ComboBox
     */
    private VBox timeControlVBox = new VBox ();
    /**
     * This is a DatePicker for users to select the start date they want to view
     */
    private DatePicker fromDate = new DatePicker();
    /**
     * This is a ComboBox for users to select the start hour they want to view
     */
    private ComboBox<String> fromHour = new ComboBox<>();
    /**
     * This is a ComboBox for users to select the start minute they want to view
     */
    private ComboBox<String> fromMinute = new ComboBox<>();
    /**
     * This is a ComboBox for users to select the start second they want to view
     */
    private ComboBox<String> fromSecond = new ComboBox<>();
    /**
     * This is a DatePicker for users to select the end date
     */
    private DatePicker toDate = new DatePicker();
    /**
     * This is a ComboBox for users to select the end hour they want to view
     */
    private ComboBox<String> toHour = new ComboBox<>();
    /**
     * This is a ComboBox for users to select the end minute they want to view
     */
    private ComboBox<String> toMinute = new ComboBox<>();
    /**
     * This is a ComboBox for users to select the end second they want to view
     */
    private ComboBox<String> toSecond = new ComboBox<>();

    /**
     * This is the constructor that create the listener of those date and time control.
     * @param fph the file path handler
     */

    public TimeFrameControl(FilePathHandler fph) {
        super(fph);
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

    /**
     * this is the method to create the time frame for user to control
     */

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

    /**
     * this is a method to create appropriate comboboxes for user to select hour/minute/second
     * @param hour
     * @param minute
     * @param second
     */

    private void setupTimeComboBoxes(ComboBox<String> hour, ComboBox<String> minute, ComboBox<String> second) {
        hour.getItems().addAll(generateTimeOptions(0, 23)); // Hours 0-23
        minute.getItems().addAll(generateTimeOptions(0, 59)); // Minutes 0-59
        second.getItems().addAll(generateTimeOptions(0, 59)); // Seconds 0-59
        hour.getSelectionModel().select("00"); // Default value
        minute.getSelectionModel().select("00"); // Default value
        second.getSelectionModel().select("00"); // Default value
    }

    /**
     * this is a method to generate time options
     * @param start
     * @param end
     * @return
     */
    private java.util.List<String> generateTimeOptions(int start, int end) {
        java.util.List<String> options = new java.util.ArrayList<>();
        for (int i = start; i <= end; i++) {
            options.add(String.format("%02d", i));
        }
        return options;
    }

    /**
     * This is the method to check whether the user select the valid from date and time and to date and time
     * @param fromDate the start date
     * @param fromHour the start hour
     * @param fromMinute the start minute
     * @param fromSecond the start second
     * @param toDate the end date
     * @param toHour the end hour
     * @param toMinute the end minute
     * @param toSecond the end second
     */
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


    /**
     * this is an override method
     * @param graphName
     * @param time
     * @param fromTime
     * @param toTime
     * @return
     */
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

    /**
     * This is the method for user to get the data from csv within selected time frame
     * @param csvFile the csvfile
     * @param fromTime the start date and time
     * @param toTime the end date and time
     * @return the ArrayList<String[]> that holds the date and time within that range (start->end)
     */
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

    /**
     * this is an override method
     * @param csvFile
     * @param time
     * @param fromTime
     * @param toTime
     * @return
     */
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

    /**
     * This is an override method
     * @param time
     * @param fromTime
     * @param toTime
     * @return
     */
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
