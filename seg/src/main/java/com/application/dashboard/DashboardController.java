package com.application.dashboard;

import com.application.database.DataManager;
import com.application.database.DbConnection;
import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import com.application.login.LoginController;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;

import javafx.util.Callback;
import org.jfree.chart.ChartFrame;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A grand controller for the dashboard
 * of the application.
 * It is a bit messy, maybe some functions can be transferred
 * to separate packages/classes.
 * Every function contains explanation to what
 * it is doing and why
 * */

public class DashboardController {
    public TabPane chartPane;
    public Label clicksLoadedLabel;
    public Label impressionLoadedLabel;
    public Label serverLoadedLabel;
    public Label totalBouncesLabel;
    public Label bounceRateLabel;
    public DatePicker fromDate;
    public DatePicker toDate;
    DataManager dataman = new DataManager();

    public ChartFrame chartCSV;
    public Label test;
    public Label uniqueImpressionLabel;
    public PieChart genderGraph;
    public Slider sliderTimeLabel;
    public Label sumImpressionsLabel;
    public Label timeFrameLabel;
    public AreaChart<String, Number> dataChart;
    public VBox filterSelection;
    public VBox dataSelection;
    public Button loadCSVbutton;
    public ScrollPane scrollDataPane;
    public StackedBarChart genderBarChart;
    public Label avgPagesViewedLabel;
    public Label totalEntriesLabel;
    public PieChart conversionGraph;
    public PieChart contextOriginGraph;
    public PieChart incomeGraph;
    public PieChart ageGraph;
    public Label zeroCostClickLabel;
    public Label totalClicksLabel;
    public Label avgClickPriceLabel;
    public ImageView uploadPNG;
    public AnchorPane background;
    private FilePathHandler fph = new FilePathHandler();
    public ImageView tutPNG;
    public Button tutorialOFF;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger = Logger.getLogger(DashboardController.class.getName());
    private DatasetCreator dc;
    @FXML
    public Button TotalClicks;
    @FXML
    public Button TotalImpressions;
    @FXML
    public Button TotalCost;
    @FXML
    public Button TotalUniques;
    @FXML
    public Button TotalBounces;
    @FXML
    public Button TotalConversions;
    @FXML
    public Button CPA;
    @FXML
    public Button CPC;
    @FXML
    public Button CTR;
    @FXML
    public Button CPM;
    @FXML
    public Button BounceRate;
    public ComboBox ComboBox;
    @FXML
    public VBox timeControlVBox = new VBox ();
    @FXML
    public DatePicker startDate = new DatePicker();
    @FXML
    public ComboBox<String> fromHour = new ComboBox<>();
    @FXML
    public ComboBox<String> fromMinute = new ComboBox<>();
    @FXML
    public ComboBox<String> fromSecond = new ComboBox<>();
    @FXML
    public DatePicker endDate = new DatePicker();
    @FXML
    public ComboBox<String> toHour = new ComboBox<>();
    @FXML
    public ComboBox<String> toMinute = new ComboBox<>();
    @FXML
    public ComboBox<String> toSecond = new ComboBox<>();
    CategoryAxis xAxis = new CategoryAxis();

    boolean clicksLoaded = false;
    boolean impressionsLoaded = false;
    boolean serverLoaded = false;

    private XYChart.Series<String, Number> convertMapToSeries(Map<LocalDateTime, Double> dataset, String seriesName) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);
        // Iterate over the entries of the map and add them to the series
        for (Map.Entry<LocalDateTime, Double> entry : dataset.entrySet()) {
            String xValue = entry.getKey().toString(); // Assuming LocalDateTime.toString() provides the desired format
            Number yValue = entry.getValue();
            series.getData().add(new XYChart.Data<>(xValue, yValue));
        }

        return series;
    }

    /*
     * @TODO:
     *   1. Context origin
     *   2. Conversion graph
     * */

    DbConnection dbConnection = new DbConnection();
    public DashboardController() throws Exception {
        logger.log(Level.INFO, "creating dashboard and connecting to database");
        //dbConnection.makeConn("root", "jojo12345");

    }


    public void loadCSV(ActionEvent actionEvent) {
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "loadCSV button");
//        Button clickedButton = (Button) actionEvent.getSource();
//        String buttonId = clickedButton.getId();
//        String time = (String) ComboBox.getValue();
//        //to set hour as default time
//        if(time == null){
//            time = "hour";
//        }
//        //to set total clicks as default graph
//        if(buttonId.equals("loadCSVbutton3")){
//            buttonId = "TotalClicks";
//        }
//        loadingBar();
//        dc = new DatasetCreator(fph);
////        TimeFrameControl tfc = new TimeFrameControl();
////        tfc.createTimeFrame();
//        loadGraph(buttonId,time);
        uniqueImpressionLabel.setText("Unique Impressions: " + countUniqueImpressions());
        sumImpressionsLabel.setText("Total impressions: " + countTotalImpressions());
//

        totalClicksLabel.setText("Total clicks: " + countTotalClicks());
        zeroCostClickLabel.setText("Zero cost clicks: " + countZeroCostClick());
        avgClickPriceLabel.setText("Average price per click: " + countAveragePricePerClick());
        totalEntriesLabel.setText("Total entries from ads: " + countTotalEntries());
        avgPagesViewedLabel.setText("Average pages viewed: " + countAvgPageViewed());
    }
    public void loadCSVWithinDates(ActionEvent actionEvent){
        uniqueImpressionLabel.setText("Unique Impressions: " + countUniqueImpressionsWithinDates());
        sumImpressionsLabel.setText("Total impressions: " + countTotalImpressionsWithinDates());

        totalClicksLabel.setText("Total clicks: " + countTotalClicksWithinDates());
        zeroCostClickLabel.setText("Zero cost clicks: " + countZeroCostClickWithinDates());
        avgClickPriceLabel.setText("Average price per click: " + countAverageProcePerClickWithinDates());
        totalEntriesLabel.setText("Total entries from ads: " + countTotalEntriesWithinDates());
        avgPagesViewedLabel.setText("Average pages viewed: " + countAvgPageViewedWithinDates());
    }

    public void loadDataGraphs(ActionEvent actionEvent){
        dataChart.layout();
        Button clickedButton = (Button) actionEvent.getSource();
        String buttonId = clickedButton.getId();
        String time = (String) ComboBox.getValue();

        //to set hour as default time
        if(time == null){
            time = "hour";
        }
        //to set total clicks as default graph
        if(buttonId.equals("loadCSVbutton3")){
            buttonId = "TotalClicks";
        }
        loadingBar();
        dc = new DatasetCreator(fph);
//        TimeFrameControl tfc = new TimeFrameControl();
//        tfc.createTimeFrame();
        loadGraph(buttonId,time);
        dataChart.layout();

    }

    public void loadGraphs(ActionEvent actionEvent) {
        chartPane.layout();
        loadGenders();
        loadAgeGraph();
        loadIncomeGraph();
        loadContextOriginChart();
        loadConversionChart();
        chartPane.layout();

    }

    //Logout function for button.
    public void logoutButton(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.WARNING, "Signing out! All information will be lost!", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = a.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK) {
            try {
                root = FXMLLoader.load(getClass().getResource("/com/application/login/hello-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.INFO, "Opening hello view.");
            } catch (IOException e) {
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
        }
    }
    //get time variable from combo box
    public void loadComboBox(ActionEvent event){
        ComboBox comboBox = (javafx.scene.control.ComboBox) event.getSource();
        this.ComboBox = comboBox;
        System.out.println(ComboBox.getValue());
    }

    //load the graph
    public void loadGraph(String selectedButton, String time) {
        logger = Logger.getLogger(getClass().getName());
        logger.log(Level.INFO, "Creating data graph");
        dataChart.getData().clear();

        dataChart.layout();

        xAxis.setTickLabelGap(10); // Set the spacing between major tick marks
        xAxis.setTickLabelRotation(-45);



        if (selectedButton != null) {
            //to set start date way back in the past as default, so it reads every data
            if(fromDate.getValue() == null){
                int day = dataman.getFirstDate("day","clicklog");
                int month = dataman.getFirstDate("month","clicklog");
                int year = dataman.getFirstDate("year","clicklog");

                fromDate.setValue(LocalDate.of(year,month,day));
            }
            //to set end date way far in the future as dafault, so it reads every data
            if(toDate.getValue() == null){

                int day = dataman.getLastDate("DAY","clicklog");
                int month = dataman.getLastDate("MONTH","clicklog");
                int year = dataman.getLastDate("YEAR","clicklog");


                toDate.setValue(LocalDate.of(year,month,day));
            }


            dataChart.getData().add(convertMapToSeries(dc.createDataset(selectedButton, time,fromDate.getValue(),toDate.getValue()), selectedButton));
            // Increase the spacing between tick labels
            xAxis.setTickLabelGap(10);

            // Rotate the tick labels by -45 degrees
            xAxis.setTickLabelRotation(-45);

            // Enable auto-ranging for the x-axis
            dataChart.getXAxis().setAutoRanging(true);

            // Force a layout update

        }
        dataChart.layout();

    }
        // Customize the graph based on the selected radio button
    //Function that would load the graph data inside the panel.
    //Not implemented.


    public void createTimeFrame(){
        startDate.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(startDate,fromHour,fromMinute, fromSecond, endDate, toHour, toMinute,toSecond));
        endDate.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(startDate,fromHour,fromMinute, fromSecond, endDate, toHour, toMinute,toSecond));
        fromHour.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(startDate,fromHour,fromMinute, fromSecond, endDate, toHour, toMinute,toSecond));
        fromMinute.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(startDate,fromHour,fromMinute, fromSecond, endDate, toHour, toMinute,toSecond));
        fromSecond.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(startDate,fromHour,fromMinute, fromSecond, endDate, toHour, toMinute,toSecond));
        toHour.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(startDate,fromHour,fromMinute, fromSecond, endDate, toHour, toMinute,toSecond));
        toMinute.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(startDate,fromHour,fromMinute, fromSecond, endDate, toHour, toMinute,toSecond));
        toSecond.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(startDate,fromHour,fromMinute, fromSecond, endDate, toHour, toMinute,toSecond));

       setupTimeComboBoxes(); // Setup method for time ComboBoxes
       setupTimeComboBoxes(); // Setup method for time ComboBoxes

     /*  Button showRangeButton = new Button("Show");
        showRangeButton.setOnAction(e -> {
            LocalDateTime fromDateTime = LocalDateTime.of(startDate.getValue(), LocalTime.of(Integer.parseInt(fromHour.getValue()), Integer.parseInt(fromMinute.getValue()), Integer.parseInt(fromSecond.getValue())));
            LocalDateTime toDateTime = LocalDateTime.of(endDate.getValue(), LocalTime.of(Integer.parseInt(toHour.getValue()), Integer.parseInt(toMinute.getValue()), Integer.parseInt(toSecond.getValue())));
        });*/
    }
    public String getStartDateTimeAsString() {
        LocalDate date = startDate.getValue();
        String hour = fromHour.getValue();
        String minute = fromMinute.getValue();
        String second = fromSecond.getValue();

        if (date != null && hour != null && minute != null && second != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = date.format(formatter);
            // Assuming hour, minute, and second are already in 'HH', 'mm', and 'ss' format
            return dateString + " " + hour + ":" + minute + ":" + second;
        } else {
            // Handle case where some values are not selected
            return null; // or some default value or throw an exception as per your requirement
        }
    }
    public String getEndDateTimeAsString() {
        LocalDate date = endDate.getValue();
        String hour = toHour.getValue();
        String minute = toMinute.getValue();
        String second = toSecond.getValue();

        if (date != null && hour != null && minute != null && second != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = date.format(formatter);
            // Assuming hour, minute, and second are already in 'HH', 'mm', and 'ss' format
            return dateString + " " + hour + ":" + minute + ":" + second;
        } else {
            // Handle case where some values are not selected
            return null; // or some default value or throw an exception as per your requirement
        }
    }
    /**
     * this is a method to create appropriate comboboxes for user to select hour/minute/second
     */

    private void setupTimeComboBoxes() {
        fromHour.getItems().addAll(generateTimeOptions(0, 23)); // Hours 0-23
        fromMinute.getItems().addAll(generateTimeOptions(0, 59)); // Minutes 0-59
        fromSecond.getItems().addAll(generateTimeOptions(0, 59)); // Seconds 0-59
        toHour.getSelectionModel().select("00"); // Default value
        toMinute.getSelectionModel().select("00"); // Default value
        toSecond.getSelectionModel().select("00"); // Default value
    }

    /**
     * this is a method to generate time options
     * @param start
     * @param end
     * @return
     */

    private List<String> generateTimeOptions(int start, int end) {
        List<String> options = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            options.add(String.format("%02d", i));
        }
        return options;
    }




    private void validateDateTime(DatePicker fromDate, ComboBox<String> fromHour,  ComboBox<String> fromMinute,  ComboBox<String> fromSecond, DatePicker toDate,  ComboBox<String> toHour,  ComboBox<String> toMinute,  ComboBox<String> toSecond) {
        if (fromDate.getValue() != null && toDate.getValue() != null &&
                fromHour.getValue() != null && fromMinute.getValue() != null && fromSecond.getValue() != null &&
                toHour.getValue() != null && toMinute.getValue() != null && toSecond.getValue() != null) {

            LocalDateTime fromDateTime = LocalDateTime.of(fromDate.getValue(),
                    LocalTime.of(Integer.parseInt(fromHour.getValue()), Integer.parseInt(fromMinute.getValue()),
                            Integer.parseInt(fromSecond.getValue())));
            LocalDateTime toDateTime = LocalDateTime.of(toDate.getValue(),
                    LocalTime.of(Integer.parseInt(toHour.getValue()), Integer.parseInt(toMinute.getValue()),
                            Integer.parseInt(toSecond.getValue())));

            // Check if "to" datetime is before "from" datetime
            if (toDateTime.isBefore(fromDateTime)) {
                // Show error message
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Invalid Date-Time Selection");
                errorAlert.setContentText("The end date-time must be after the start date-time.");
                errorAlert.showAndWait();

                // Optionally, reset the "to" date-time selection to match the "from" date-time or to a valid state
                toDate.setValue(fromDate.getValue());
                toHour.setValue(fromHour.getValue());
                toMinute.setValue(fromMinute.getValue());
                toSecond.setValue(fromSecond.getValue());
            }
        }
    }



    //Function to count the unique impressions
    public int countUniqueImpressions(){
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO, "Loading Unique visits from impressions_log");
        return dataman.selectTotalData("impressionlog");
    }
    // function to count the unique impressions within dates
    public int countUniqueImpressionsWithinDates(){
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO, "Loading Unique visits within start and end time from impressions_log");
        return dataman.selectTotalDataWithinRange("impressionlog", getStartDateTimeAsString(),getEndDateTimeAsString());
    }

    //Function to count the zero cost clicks
    public int countZeroCostClick(){
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO, "Loading Zero Cost Clicks");
        return dataman.selectZeroClickCost();
    }
    public int countZeroCostClickWithinDates(){
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO,"Loading Zero Cost within start and end time Clicks");
        return dataman.selectZeroClickCostWithinRange(getStartDateTimeAsString(),getEndDateTimeAsString());
    }

    //Function to find the average price per click
    public double countAveragePricePerClick(){
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO, "Loading Average Price per Click");
        return dataman.selectAvgData("clickCost", "clicklog");
    }
    public double countAverageProcePerClickWithinDates(){
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO, "Loading Average Price per Click");
        return dataman.selectAvgDataWithinRange("clickCost", "clicklog", getStartDateTimeAsString(),getEndDateTimeAsString());
    }


    //Function to find the total impressions
    public int countTotalImpressions(){

        return dataman.selectTotalData("impressionlog");
    }
    public int countTotalImpressionsWithinDates(){
        return dataman.selectTotalDataWithinRange("impressionlog", getStartDateTimeAsString(),getEndDateTimeAsString());
    }

    //Function to find the total clicks for the campaign
    public int countTotalClicks(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO, "Loading Total clicks");

        return dataman.selectTotalData("clicklog");
    }
    public int countTotalClicksWithinDates(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO, "Loading Total clicks within start and end time");
        return dataman.selectTotalDataWithinRange("clicklog", getStartDateTimeAsString(),getEndDateTimeAsString());
    }

    public int totalBounces(){


        return 1;
    }

    //Function to find the total entries from adds - needs better explanation
    public int countTotalEntries(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading total entries from ads.");
        return dataman.selectTotalData("serverlog");
    }
    public int countTotalEntriesWithinDates(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading total entries from ads within start and end time.");
        return dataman.selectTotalDataWithinRange("severlog", getStartDateTimeAsString(),getEndDateTimeAsString());
    }
    //Function to find the average number of pages
    public double countAvgPageViewed(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO, "Loading average pages viewed.");
        return Math.round(dataman.selectAvgData("pagesViewed", "serverlog") * 100) / 100;
    }
    public double countAvgPageViewedWithinDates(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO, "Loading average pages viewed.");
        return Math.round(dataman.selectAvgDataWithinRange("pagesViewed", "serverlog", getStartDateTimeAsString(),getEndDateTimeAsString()) * 100) / 100;
    }

    //loading bar function
    public void loadingBar(){
        ButtonType cancelButtonType = new ButtonType("Cancel");

        Dialog<ButtonType> progressDialog = new Dialog<>();
        progressDialog.setTitle("Progress Dialog");
        progressDialog.setHeaderText("Please wait...");

        ProgressBar progressBar = new ProgressBar();
        StackPane stackPane = new StackPane(progressBar);
        progressDialog.getDialogPane().setContent(stackPane);

        progressDialog.getDialogPane().getButtonTypes().addAll(cancelButtonType);

        // Handle cancel button action
        progressDialog.setOnCloseRequest(dialogEvent -> {
            // Handle cancellation logic if needed
            System.out.println("Progress canceled");
        });

        // Create a task for simulating a time-consuming operation
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    updateProgress(i, 100);
                    Thread.sleep(50); // Simulate some work being done
                }
                return null;
            }
        };

        // Bind the ProgressBar's progress property to the Task's progress property
        progressBar.progressProperty().bind(task.progressProperty());

        // Set up a timeline to close the dialog after the task is completed
        task.setOnSucceeded(event -> progressDialog.setResult(ButtonType.OK));

        // Show the dialog
        progressDialog.show();

        // Start the task in a new thread
        new Thread(task).start();
    }

    public void loadHistogramClickCost(){

    }


    //Function to find the gender separation.
    public void loadGenders(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading genders.");
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading income graph.");
        int[] vals = dataman.getUniqueAppearanceInt("gender", "impressionlog");
        String[] names = dataman.getUniqueAppearanceString("gender", "impressionlog");
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < vals.length; i++) {
            names[i] = names[i] + ": " + vals[i];
            pieChartData.add(new PieChart.Data(names[i], vals[i]));
        }
        genderGraph.setTitle("Gender Graph");
        genderGraph.setLabelLineLength(20);
        genderGraph.setLabelsVisible(true);
        genderGraph.setData(pieChartData);
    }

    //function to load the age graph.
    public void loadAgeGraph(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading age graph.");
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading income graph.");
        int[] vals = dataman.getUniqueAppearanceInt("age", "impressionlog");
        String[] names = dataman.getUniqueAppearanceString("age", "impressionlog");
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < vals.length; i++) {
            names[i] = names[i] + ": " + vals[i];
            pieChartData.add(new PieChart.Data(names[i], vals[i]));
        }
        ageGraph.setLabelLineLength(20);
        ageGraph.setLabelsVisible(true);
        ageGraph.setData(pieChartData);
    }

    //Load income graph
    public void loadIncomeGraph(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading income graph.");
        int[] vals = dataman.getUniqueAppearanceInt("income", "impressionlog");
        String[] names = dataman.getUniqueAppearanceString("income", "impressionlog");
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < vals.length; i++) {
            names[i] = names[i] + ": " + vals[i];
            pieChartData.add(new PieChart.Data(names[i], vals[i]));
        }
        incomeGraph.setLabelLineLength(20);
        incomeGraph.setLabelsVisible(true);
        incomeGraph.setData(pieChartData);
    }

    public void loadContextOriginChart(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading income graph.");

        int[] vals = dataman.getUniqueAppearanceInt("context", "impressionlog");
        String[] names = dataman.getUniqueAppearanceString("context", "impressionlog");
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < vals.length; i++) {
            names[i] = names[i] + ": " + vals[i];
            pieChartData.add(new PieChart.Data(names[i], vals[i]));
        }

        contextOriginGraph.setLabelLineLength(20);
        contextOriginGraph.setLabelsVisible(true);
        contextOriginGraph.setData(pieChartData);
    }

    public void loadConversionChart(){

            int[] vals = dataman.getUniqueAppearanceInt("conversion", "serverlog");
            String[] names = dataman.getUniqueAppearanceString("conversion", "serverlog");
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            for (int i = 0; i < vals.length; i++) {
                names[i] = names[i] + ": " + vals[i];
                pieChartData.add(new PieChart.Data(names[i], vals[i]));
                //System.out.println(vals[i]);
            }

            conversionGraph.setLabelLineLength(20);
            conversionGraph.setLabelsVisible(true);
            conversionGraph.setData(pieChartData);

    }


    //Display tutorial overlay
    public void loadTutorial() {
        tutPNG.setVisible(true);
        tutorialOFF.setVisible(true);
    }

    //Disable tutorial overlay
    public void disableTutPNG() {
        tutorialOFF.setVisible(false);
        uploadPNG.setVisible(false);
        tutPNG.setVisible(false);
    }

    //Open dialogue box for opening files
    public void openCampaign(){
        FileChooserWindow fileChooser = new FileChooserWindow();

        fph.fileTypeHandler(fileChooser.openFileBox());
        System.out.println(fph.getImpressionPath());
        System.out.println(fph.getClickPath());
        System.out.println(fph.getServerPath());
        loadingBar();
        System.out.println("Ready ^_^!");
    }

    public void loadSQL(){
        try {

            if(fph.getClickPath() != null) {
                writeClicksDB();
                clicksLoadedLabel.setText("clicks_log.csv: loaded");
                clicksLoaded = true;
            }
            if(fph.getImpressionPath()!= null) {
                writeImpressionsDB();
                impressionLoadedLabel.setText("impression_log.csv: loaded");
                impressionsLoaded = true;
            }
            if(fph.getServerPath()!= null) {
                writeServerDB();
                serverLoadedLabel.setText("server_log.csv: loaded");
                serverLoaded = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Ready ^_^!");
    }

    void writeClicksDB() throws Exception {

        FileReader clickReader = new FileReader(fph.getClickPath());
        CSVReader clickCSVReader = new CSVReader(clickReader);
        System.out.println("readng");
        String[] nextRecord;

        nextRecord = clickCSVReader.readNext();



        while((nextRecord = clickCSVReader.readNext()) != null ) {
//            nextRecord = clickCSVReader.readNext();
            dataman.addClickLog(nextRecord[0], nextRecord[1], Double.parseDouble(nextRecord[2]));

        }

    }
    void writeImpressionsDB() throws Exception {

        FileReader impressionReader = new FileReader(fph.getImpressionPath());
        CSVReader clickCSVReader = new CSVReader(impressionReader);
        System.out.println("readng");
        String[] nextRecord;

        nextRecord = clickCSVReader.readNext();


        while((nextRecord = clickCSVReader.readNext()) != null ) {
                //nextRecord = clickCSVReader.readNext();
                dataman.addImpressionLog(nextRecord[0], nextRecord[1],
                        nextRecord[2], nextRecord[3], nextRecord[4],
                        nextRecord[5], Double.parseDouble(nextRecord[6]));
            }


    }
    void writeServerDB() throws Exception {

        FileReader clickReader = new FileReader(fph.getServerPath());
        CSVReader clickCSVReader = new CSVReader(clickReader);
        System.out.println("readng");
        String[] nextRecord;

        nextRecord = clickCSVReader.readNext();


        while ((nextRecord = clickCSVReader.readNext()) != null) {
            //nextRecord = clickCSVReader.readNext();
            if("n/a".equals(nextRecord[2])) {
                dataman.addServerLog(nextRecord[0], nextRecord[1], null,
                        Integer.parseInt(nextRecord[3]), nextRecord[4]);
            }else{
                dataman.addServerLog(nextRecord[0], nextRecord[1], nextRecord[2],
                        Integer.parseInt(nextRecord[3]), nextRecord[4]);
            }
        }


    }

    public void openOnlineDocumentation(ActionEvent actionEvent) throws IOException {
        Desktop.getDesktop().browse(URI.create("https://nikolaparushev2003.wixsite.com/ecs-adda/documentation"));
    }

    public void loadTutorial2(ActionEvent actionEvent) {
        uploadPNG.setVisible(true);
        tutorialOFF.setVisible(true);
    }

    public void loadTheme(ActionEvent actionEvent) {
    }


    boolean light = true;
    boolean dark = false;
    public void enableLightTheme(ActionEvent actionEvent) {
        if (light == false) {
//            String currentDirectory = System.getProperty("user.dir");
//
//            // Define the relative path to your stylesheet
//            String stylesheetPath = "file:///" + currentDirectory.replace("\\", "/") + "/comp2211/seg/src/main/java/com/application/dashboard/lighttheme.css";
//            System.out.println(currentDirectory);
//
//            background.getStylesheets().setAll(stylesheetPath);
            light = true;
            dark = false;
            System.out.println("light theme");
            background.getStylesheets().clear();
        }
    }

    public void enableDarkTheme(ActionEvent actionEvent) throws MalformedURLException {
        if(dark == false){
            String currentDirectory = System.getProperty("user.dir");

            // Define the relative path to your stylesheet
            String stylesheetPath = "file:///" + currentDirectory.replace("\\", "/") + "/comp2211/seg/src/main/java/com/application/dashboard/darktheme.css";
            //System.out.println(currentDirectory);

            background.getStylesheets().setAll(stylesheetPath);
            dark = true;
            light = false;
            System.out.println("dark theme");

        }
    }


    public void selectClickGraph(ActionEvent actionEvent) {
    }
}
