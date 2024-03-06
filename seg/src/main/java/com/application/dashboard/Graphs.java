package com.application.dashboard;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Graphs {
    private DatasetCreator dc = new DatasetCreator();
    // Define the date format used in the CSV file
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime startTime = LocalDateTime.parse("2015-01-01 00:00:00", dateFormatter);
    private LocalDateTime endTime = LocalDateTime.parse("2015-01-30 00:00:00", dateFormatter);

    public Graphs() {

    }

    public void createGraph(String graphName, String time) {
        if (graphName.equals("TotalClicks")) {
            createTotalClicksGraph(time, dc.createDataset("TotalClicks", time));
        } else if (graphName.equals("TotalImpressions")) {
            createTotalImpressionsGraph(time, dc.createDataset("TotalImpressions", time));
        } else if (graphName.equals("TotalUniques")) {
            createTotalUniquesGraph(time);
        } else if (graphName.equals("Conversions")) {
            createTotalConversionsGraph(time);
        } else if (graphName.equals("TotalCost")) {
            createTotalCostGraph(time);
        } else if (graphName.equals("CPC")) {
            createCPCGraph(time, dc.createDataset("CPC", time));
        } else if (graphName.equals("CTR")) {
            createCTRGraph(time, dc.createDataset("CTR", time));
        } else if (graphName.equals("CPA")) {
            createCPAGraph(time, dc.createDataset("CPA", time));
        } else if (graphName.equals("CPM")) {
            createCPMGraph(time, dc.createDataset("CPM", time));
        } else if (graphName.equals("Bounce")) {
            createBouncesGraph(time, dc.createDataset("Bounce", time));
        } else if (graphName.equals("BounceRate")) {
            createBounceRateGraph(time, dc.createDataset("BounceRate", time));
        }
    }

    private void createTotalClicksGraph(String time, Map<LocalDateTime, Double> dataset) {
        GraphGenerator gg = new GraphGenerator("Clicks By " + time, "Time", "Total Clicks", dataset);
        gg.generateGraph();
    }

    private void createTotalImpressionsGraph(String time, Map<LocalDateTime, Double> dataset) {
        GraphGenerator gg = new GraphGenerator("Impressions By " + time, "Time", "Total Impressions", dataset);
        gg.generateGraph();
    }

    private void createTotalUniquesGraph(String time) {
        Map<LocalDateTime, Double> dataset = dc.createDataset("TotalUniques", time);
        GraphGenerator gg = new GraphGenerator("Total Uniques By " + time, "Time", "Total Uniques", dataset);
        gg.generateGraph();
    }

    private void createTotalConversionsGraph(String time) {
        Map<LocalDateTime, Double> dataset = dc.createDataset("Conversions", time);
        GraphGenerator gg = new GraphGenerator("Conversions By " + time, "Time", "Total Conversions", dataset);
        gg.generateGraph();
    }

    private void createTotalCostGraph(String time) {
        Map<LocalDateTime, Double> dataset = dc.createDataset("TotalCost", time);
        GraphGenerator gg = new GraphGenerator("Total Cost By " + time, "Time", "Total Cost", dataset);
        gg.generateGraph();
    }

    private void createCPCGraph(String time, Map<LocalDateTime, Double> dataset) {
        GraphGenerator gg = new GraphGenerator("CPC by " + time, "Time", "Average CPC", dataset);
        gg.generateGraph();
    }

    private void createCTRGraph(String time, Map<LocalDateTime, Double> dataset) {
        GraphGenerator gg = new GraphGenerator("CTR by " + time, "Time", "Average CTR", dataset);
        gg.generateGraph();
    }

    private void createCPAGraph(String time, Map<LocalDateTime, Double> dataset) {
        GraphGenerator gg = new GraphGenerator("CPA by " + time, "Time", "Average CPA", dataset);
        gg.generateGraph();
    }

    private void createCPMGraph(String time, Map<LocalDateTime, Double> dataset) {
        GraphGenerator gg = new GraphGenerator("CPM by " + time, "Time", "Average CPM", dataset);
        gg.generateGraph();
    }

    private void createBouncesGraph(String time, Map<LocalDateTime, Double> dataset) {
        GraphGenerator gg = new GraphGenerator("Bounces By " + time, "Time", "Total Bounces", dataset);
        gg.generateGraph();
    }

    private void createBounceRateGraph(String time, Map<LocalDateTime, Double> dataset) {
        GraphGenerator gg = new GraphGenerator("Bounce Rate By " + time, "Time", "Bounce Rate", dataset);
        gg.generateGraph();
    }
    public static void main(String[] args){
        Graphs g = new Graphs();
        g.createGraph("TotalUniques","day");
        g.createGraph("TotalUniques","hour");
        g.createGraph("Bounce","week");
    }
}