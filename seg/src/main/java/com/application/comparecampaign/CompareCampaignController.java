package com.application.comparecampaign;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import com.application.database.DataManager;
public class CompareCampaignController {
    @FXML
    private BarChart<String, Number> barChart1;
    @FXML
    private BarChart<String, Number> barChart2;
    private DataManager dataman = new DataManager();

    public void initialize() {
        // Populate the first bar chart
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Data 1");
        series1.getData().add(new XYChart.Data<>("A", 10));
        series1.getData().add(new XYChart.Data<>("B", 20));
        series1.getData().add(new XYChart.Data<>("C", 15));
        barChart1.getData().add(series1);

        // Populate the second bar chart
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Data 2");
        series2.getData().add(new XYChart.Data<>("A", 8));
        series2.getData().add(new XYChart.Data<>("B", 25));
        series2.getData().add(new XYChart.Data<>("C", 12));
        barChart2.getData().add(series2);
    }
}