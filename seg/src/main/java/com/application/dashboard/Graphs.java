package com.application.dashboard;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

public class Graphs {
    String clicksCsv;
    String impressionsCsv;
    String serverCsv;
    // Define the date format used in the CSV file
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime startTime = LocalDateTime.parse("2015-01-01 00:00:00", dateFormatter);
    LocalDateTime endTime = LocalDateTime.parse("2015-01-30 00:00:00", dateFormatter);
    public Graphs() {
        this.clicksCsv = "C:\\Users\\Mel\\Documents\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\click_log.csv";
        this.impressionsCsv = "C:\\Users\\Mel\\Documents\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\impression_log.csv";
        this.serverCsv = "C:\\Users\\Mel\\Documents\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\server_log.csv";
    }

    public void createGraph(String graphName, String time) {
            createDataset(graphName,time);

    }
    public void createDataset(String graphName,String time) {
        if (graphName.equals("TotalClicks")) {
            createTotalClicksGraph(time,createTotalClicksDataset(time));
        }
        if (graphName.equals("TotalImpressions")){
            createTotalImpressionsGraph(time,createTotalImpressionsDataset(time));
        }
    }
    public Map<LocalDateTime, Integer> createTotalClicksDataset(String time) {
        // Define the desired time frame
        Map<LocalDateTime, Integer> clickCountByTime = new TreeMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(clicksCsv))) {
            reader.readNext();
            // Read all records from the CSV file
            List<String[]> records = reader.readAll();

            // Iterate over the records
            for (String[] record : records) {
                String dateString = record[0]; // Assuming the date is in the first column
                LocalDateTime date = LocalDateTime.parse(dateString, dateFormatter);
                if (date.isAfter(startTime) && date.isBefore(endTime)) {
                    if (time.equals("hour")) {
                        LocalDateTime roundedDate = date.withMinute(0).withSecond(0); // Round to the nearest hour
                        clickCountByTime.put(roundedDate, clickCountByTime.getOrDefault(roundedDate, 0) + 1);
                    } else if (time.equals("day")) {
                        LocalDateTime roundedDate = date.withHour(0).withMinute(0).withSecond(0); // Round to the nearest day
                        clickCountByTime.put(roundedDate, clickCountByTime.getOrDefault(roundedDate, 0) + 1);
                    } else if (time.equals("week")) {
                        int week = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                        LocalDateTime roundedDate = date.withHour(0).withMinute(0).withSecond(0); // Round to the nearest day
                        clickCountByTime.put(roundedDate, clickCountByTime.getOrDefault(roundedDate, 0) + 1);
                    } else {
                        System.out.println("Enter a valid time period");
                    }
                }
            }
            return clickCountByTime;
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return clickCountByTime;
    }
    private Map<LocalDateTime, Integer> createTotalImpressionsDataset(String time){
        Map<LocalDateTime, Integer> impressionCountByTime = new TreeMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(impressionsCsv))) {
            reader.readNext();
            // Read all records from the CSV file
            List<String[]> records = reader.readAll();

            // Iterate over the records
            for (String[] record : records) {
                String dateString = record[0]; // Assuming the date is in the first column
                LocalDateTime date = LocalDateTime.parse(dateString, dateFormatter);
                if (date.isAfter(startTime) && date.isBefore(endTime)) {
                    if (time.equals("hour")) {
                        LocalDateTime roundedDate = date.withMinute(0).withSecond(0); // Round to the nearest hour
                        impressionCountByTime.put(roundedDate, impressionCountByTime.getOrDefault(roundedDate, 0) + 1);
                    } else if (time.equals("day")) {
                        LocalDateTime roundedDate = date.withHour(0).withMinute(0).withSecond(0); // Round to the nearest day
                        impressionCountByTime.put(roundedDate, impressionCountByTime.getOrDefault(roundedDate, 0) + 1);
                    } else if (time.equals("week")) {
                        int week = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                        LocalDateTime roundedDate = date.withHour(0).withMinute(0).withSecond(0); // Round to the nearest day
                        impressionCountByTime.put(roundedDate, impressionCountByTime.getOrDefault(roundedDate, 0) + 1);
                    } else {
                        System.out.println("Enter a valid time period");
                    }
                }
            }
            return impressionCountByTime;
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return impressionCountByTime;
    }
    private void createTotalClicksGraph(String time,Map<LocalDateTime, Integer> dataset) {
        GraphGenerator gg = new GraphGenerator("Click By " + time, "Time", "Total Clicks", dataset);
        gg.generateGraph();
    }
    private void createTotalImpressionsGraph(String time,Map<LocalDateTime, Integer> dataset) {
        GraphGenerator gg = new GraphGenerator("Click By " + time, "Time", "Total Clicks", dataset);
        gg.generateGraph();
    }
    public static void main(String[] args){
        Graphs g = new Graphs();
        g.createGraph("TotalImpressions","hour");
    }
}