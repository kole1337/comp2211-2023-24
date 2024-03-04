package com.application.dashboard;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

public class Graphs {

    public Graphs() {

    }

    public void createGraph(String graphName, String time) {
        if (graphName.equals("TotalClicks")) {
            createTotalClicksGraph(time);
        }
    }

    private void createTotalClicksGraph(String time) {
        String csvFilePath = "C:\\Users\\Mel\\Documents\\comp2211\\seg\\src\\main\\java\\com\\application\\dashboard\\2_week_campaign_2\\click_log.csv";
        // Define the date format used in the CSV file
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse("2015-01-01 00:00:00", dateFormatter);
        LocalDateTime endTime = LocalDateTime.parse("2015-01-30 00:00:00", dateFormatter);
        // Define the desired time frame
        Map<String, Integer> clickCountByTime = new TreeMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
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
                        String key = roundedDate.toString();
                        clickCountByTime.put(key, clickCountByTime.getOrDefault(key, 0) + 1);
                    } else if (time.equals("day")) {
                        LocalDateTime roundedDate = date.withHour(0).withMinute(0).withSecond(0); // Round to the nearest day
                        String key = roundedDate.toString();
                        clickCountByTime.put(key, clickCountByTime.getOrDefault(key, 0) + 1);
                    } else if (time.equals("week")) {
                        int week = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                        LocalDateTime roundedDate = date.withHour(0).withMinute(0).withSecond(0); // Round to the nearest day
                        String key = roundedDate.toString() + "-W" + String.format("%02d", week);
                        clickCountByTime.put(key, clickCountByTime.getOrDefault(key, 0) + 1);
                    } else {
                        System.out.println("Enter a valid time period");
                    }
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        GraphGenerator gg = new GraphGenerator("Click By " + time, "Time", "Total Clicks", clickCountByTime);
        gg.generateGraph();
    }
    public static void main(String[] args){
        Graphs g = new Graphs();
        g.createGraph("TotalClicks","day");
    }

}
