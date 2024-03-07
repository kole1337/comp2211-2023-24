package com.application.dashboard;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class GraphGenerator {
    private String title;
    private String xAxisLabel;
    private String yAxisLabel;
    private Map<LocalDateTime, Double> data;
    private JFrame frame;

    public GraphGenerator(String title, String xAxisLabel, String yAxisLabel, Map<LocalDateTime, Double> data) {
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.data = data;
    }

    public void generateGraph() {
        // Create a time series collection and add data points to it
        TimeSeries series = new TimeSeries(yAxisLabel);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            Hour hour = new Hour(dateTime.getHour(), dateTime.getDayOfMonth(), dateTime.getMonthValue(), dateTime.getYear());
            series.add(hour, entry.getValue());
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

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

        // Create the chart panel
        ChartPanel chartPanel = new ChartPanel(chart);

        // Create the frame to hold the chart panel
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose the frame, not exit the application
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    // Additional methods if needed
}
