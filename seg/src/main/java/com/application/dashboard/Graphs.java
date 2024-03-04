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
    DatasetCreator dc = new DatasetCreator();
    // Define the date format used in the CSV file
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime startTime = LocalDateTime.parse("2015-01-01 00:00:00", dateFormatter);
    LocalDateTime endTime = LocalDateTime.parse("2015-01-30 00:00:00", dateFormatter);
    public Graphs() {

    }

    public void createGraph(String graphName, String time) {
            createTotalClicksGraph(time,dc.createDataset(graphName,time));
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
        g.createGraph("Conversions","hour");
    }
}