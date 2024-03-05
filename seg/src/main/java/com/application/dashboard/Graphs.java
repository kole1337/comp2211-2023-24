package com.application.dashboard;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

import static javafx.application.Application.launch;

public class Graphs {
    com.application.dashboard.DatasetCreator dc = new com.application.dashboard.DatasetCreator();
    // Define the date format used in the CSV file
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime startTime = LocalDateTime.parse("2015-01-01 00:00:00", dateFormatter);
    LocalDateTime endTime = LocalDateTime.parse("2015-01-30 00:00:00", dateFormatter);

    public Graphs() {

    }

    public void createGraph(String graphName, String time) {
        if(graphName == "CPC"){
            createCPCGraph(time,dc.createDataset("CPC",time));
        }
        createTotalClicksGraph(time, dc.createDataset(graphName, time));
    }

    private void createTotalClicksGraph(String time, Map<LocalDateTime, Double> dataset) {
        com.application.dashboard.GraphGenerator gg = new com.application.dashboard.GraphGenerator("Clicks By " + time, "Time", "Total Clicks", dataset);
        gg.generateGraph();
    }

    private void createTotalImpressionsGraph(String time, Map<LocalDateTime, Double> dataset) {
        com.application.dashboard.GraphGenerator gg = new com.application.dashboard.GraphGenerator("Impressions By " + time, "Time", "Total Clicks", dataset);
        gg.generateGraph();
    }
    private void createCPCGraph(String time, Map<LocalDateTime, Double> dataset){
        GraphGenerator gg = new GraphGenerator("CPC by" + time, "Time","Average CPC",dataset);
    }

    public void start() {
        Graphs g = new Graphs();
        g.createGraph("Conversions","hour");
        g.createGraph("TotalClicks", "hour");
        g.createGraph("TotalClicks", "hour");
    }

   public static void main(String[] args) {
       Graphs g = new Graphs();
       g.createGraph("CPC","hour");

   }
}