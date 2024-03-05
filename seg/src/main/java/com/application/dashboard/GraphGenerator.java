package com.application.dashboard;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class GraphGenerator {
    private String title;
    private String xAxisLabel;
    private String yAxisLabel;
    private Map<LocalDateTime, Double> data;
    private ChartFrame frame;
    public GraphGenerator(String title, String xAxisLabel, String yAxisLabel, Map<LocalDateTime, Double> data) {
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.data = data;
    }

    public GraphGenerator() {
        generateGraph();
    }

    public String getTitle() {
        return title;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public Map<LocalDateTime, Double> getData() {
        return data;
    }

    public ChartFrame getFrame() {
        return frame;
    }

//    public void chartFrame(){
//        frame = new ChartFrame(title, chart);
//        frame.pack();
//        frame.setVisible(true);
//    }

    public void generateGraph() {
        // Create a time series collection and add data points to it
        TimeSeries series = new TimeSeries(yAxisLabel);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            Hour hour = new Hour(dateTime.getHour(), dateTime.getDayOfMonth(), dateTime.getMonthValue(), dateTime.getYear());
            series.add(hour, entry.getValue());
        }
        XYDataset dataset = new TimeSeriesCollection(series);

        // Create the chart
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                true,
                true,
                false
        );

        // Customize the plot
        XYPlot plot = chart.getXYPlot();
        DateAxis xAxis = (DateAxis) plot.getDomainAxis();
        xAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // Display the chart in a frame
        frame = new ChartFrame(title, chart);
        frame.pack();
        frame.setVisible(true);
    }
}