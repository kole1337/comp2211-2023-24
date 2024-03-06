package com.application.dashboard;

import com.application.files.FilePathHandler;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Graphs {
    private DatasetCreator dc;
    private FilePathHandler fph = new FilePathHandler();
    // Define the date format used in the CSV file
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime startTime = LocalDateTime.parse("2015-01-01 00:00:00", dateFormatter);
    private LocalDateTime endTime = LocalDateTime.parse("2015-01-30 00:00:00", dateFormatter);

    public Graphs(FilePathHandler fph) {
        this.fph = fph;
        this.dc = new DatasetCreator(fph);
    }

    public void createGraph(String graphName, String time) {
        switch (graphName){
            case "TotalClicks":
                createTotalClicksGraph(time, dc.createDataset("TotalClicks", time));
                break;
            case "TotalImpressions":
                createTotalImpressionsGraph(time, dc.createDataset("TotalImpressions", time));
                break;
            case "TotalUniques":
                createTotalUniquesGraph(time);
                break;
            case "Conversions":
                createTotalConversionsGraph(time);
                break;
            case "TotalCost":
                createTotalCostGraph(time);
                break;
            case "CPC" :
                createCPCGraph(time, dc.createDataset("CPC", time));
                break;
            case "CTR":
                createCTRGraph(time, dc.createDataset("CTR", time));
                break;
            case "CPA" :
                createCPAGraph(time, dc.createDataset("CPA", time));
                break;
            case "CPM" :
                createCPMGraph(time, dc.createDataset("CPM", time));
                break;
            case "Bounce" :
                createBouncesGraph(time, dc.createDataset("Bounce", time));
                break;
            case "BounceRate" :
                createBounceRateGraph(time, dc.createDataset("BounceRate", time));
                break;
            default:
                break;
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
    public void main(){
        Graphs g = new Graphs(fph);
        g.createGraph("TotalUniques","week");
        g.createGraph("TotalUniques","week");
        g.createGraph("CPA","week");
    }
}