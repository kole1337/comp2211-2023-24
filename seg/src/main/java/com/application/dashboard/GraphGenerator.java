package com.application.dashboard;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

        // Adjust the tick marks and intervals

        xAxis.setAutoTickUnitSelection(true);
        xAxis.setVerticalTickLabels(true); // Rotate tick labels vertically if needed

        // Create a panel to hold the chart
        ChartPanel chartPanel = new ChartPanel(chart);

        // Create a scroll pane and add the chart panel to it
        JScrollPane scrollPane = new JScrollPane(chartPanel);
        scrollPane.setPreferredSize(new Dimension(800, 600)); // Adjust the preferred size as needed
        // Display the scroll pane in a frame
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }


}
