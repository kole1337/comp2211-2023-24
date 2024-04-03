package com.application.dashboard;

import com.application.database.*;
import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import com.application.styles.checkStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.jfree.chart.ChartFrame;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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


public class DashboardController implements Initializable {
    public TabPane chartPane;
    public Label clicksLoadedLabel;
    public Label impressionLoadedLabel;
    public Label serverLoadedLabel;
    public Label totalBouncesLabel;
    public Label bounceRateLabel;

    public DatePicker fromDate;
    public DatePicker toDate;

    public BarChart histogramClicks;

    public ImageView loadingGIF;
    public ChoiceBox selectGraph;


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
    FilePathHandler fph = new FilePathHandler();
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
    public ComboBox timeBox;
    @FXML
    public VBox timeControlVBox = new VBox ();
    @FXML
    public ComboBox<String> fromHour = new ComboBox<>();
    @FXML
    public ComboBox<String> fromMinute = new ComboBox<>();
    @FXML
    public ComboBox<String> fromSecond = new ComboBox<>();
    @FXML
    public ComboBox<String> toHour = new ComboBox<>();
    @FXML
    public ComboBox<String> toMinute = new ComboBox<>();
    @FXML
    public ComboBox<String> toSecond = new ComboBox<>();
    @FXML
    public TextField timeSpentBounce ;

    @FXML
    public TextField pageViewedBounce;
    public ComboBox genderFilter;
    public ComboBox contextFilter;
    public ComboBox ageFilter;
    public ComboBox incomeFilter;

    CategoryAxis xAxis = new CategoryAxis();

    boolean clicksLoaded = false;
    boolean impressionsLoaded = false;
    boolean serverLoaded = false;

    checkStyle obj = new checkStyle();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String[] bounces = new String[]{"Clicks","Uniques","Bounces","Impressions","Conversion rate", "Total Cost", "Cost per acquisition",
        "Cost per clicks", "Cost per impression", "Cost per thousands","Bounce rate"};

        String theme = obj.checkStyle();

        if(theme.equals("dark")){
            enableDarkTheme();
        }else{
            enableLightTheme();
        }

        selectGraph.getItems().addAll(bounces);

        timeSpentBounce.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Regular expression for digits only
                timeSpentBounce.setText(newValue.replaceAll("[^\\d]", "")); // Replace all non-digits
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Invalid Setting for Time Spent Bounce");
                errorAlert.setContentText("Only accept integers");
                errorAlert.showAndWait();
            } else {
                // Call the dataman.setBounce method with the new value
                try {
                    int timeValue = Integer.parseInt(newValue);
                    dataman.setBounceTimeMinute(timeValue);
                } catch (NumberFormatException e) {
                    // Handle the case when the input is an empty string or invalid number
                    // You can show an error message or take appropriate action
                }
            }
        });
        pageViewedBounce.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Regular expression for digits only
                pageViewedBounce.setText(newValue.replaceAll("[^\\d]", "")); // Replace all non-digits
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Invalid Setting for Time Spent Bounce");
                errorAlert.setContentText("Only accept integers");
                errorAlert.showAndWait();
            } else {
                // Call the dataman.setBounce method with the new value
                try {
                    int pageValue = Integer.parseInt(newValue);
                    dataman.setBouncePages(pageValue);
                } catch (NumberFormatException e) {
                    // Handle the case when the input is an empty string or invalid number
                    // You can show an error message or take appropriate action
                }
            }
        });
        // Assuming you have defined the ComboBoxes in your controller class
        ObservableList<String> hours = FXCollections.observableArrayList();
        ObservableList<String> minutes = FXCollections.observableArrayList();
        ObservableList<String> seconds = FXCollections.observableArrayList();

        for (int i = 0; i <= 23; i++) {
            hours.add(String.format("%02d", i));
        }

        for (int i = 0; i <= 59; i++) {
            minutes.add(String.format("%02d", i));
        }

        for (int i = 0; i <= 59; i++) {
            seconds.add(String.format("%02d", i));
        }


// Set the previously populated hour, minute, and second lists to the ComboBoxes
        fromHour.setItems(hours);
        fromMinute.setItems(minutes);
        fromSecond.setItems(seconds);
        toHour.setItems(hours);
        toMinute.setItems(minutes);
        toSecond.setItems(seconds);
        genderFilter.setItems(dataman.getGenders());
        contextFilter.setItems(dataman.getContext());
        ageFilter.setItems(dataman.getAge());
        incomeFilter.setItems(dataman.getIncome());
        genderFilter.setValue(" ");
        contextFilter.setValue(" ");
        ageFilter.setValue(" ");
        incomeFilter.setValue(" ");
        timeBox.setValue("hour");
    }



    /*
     * @TODO:
     *   1. Context origin
     *   2. Conversion graph
     * */

    public DashboardController() throws Exception {
        logger.log(Level.INFO, "creating dashboard and connecting to database");
        //dbConnection.makeConn("root", "jojo12345");


    }



    public void loadCSV(ActionEvent actionEvent) {

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
        totalBouncesLabel.setText("Total Bounce: " + countTotalBounces());
        bounceRateLabel.setText("Bounce Rate: " + countBounceRate());
    }
    public void loadCSVWithinDates(ActionEvent actionEvent){
        //uniqueImpressionLabel.setText("Unique Impressions: " + countUniqueImpressionsWithinDates());
//        sumImpressionsLabel.setText("Total impressions: " + countTotalImpressionsWithinDates());
//
//        totalClicksLabel.setText("Total clicks: " + countTotalClicksWithinDates());
//        zeroCostClickLabel.setText("Zero cost clicks: " + countZeroCostClickWithinDates());
//        avgClickPriceLabel.setText("Average price per click: " + countAverageProcePerClickWithinDates());
//        totalEntriesLabel.setText("Total entries from ads: " + countTotalEntriesWithinDates());
//        avgPagesViewedLabel.setText("Average pages viewed: " + countAvgPageViewedWithinDates());
    }

    public void loadDataGraphs(ActionEvent actionEvent) {
        dataChart.layout();
        Button clickedButton = (Button) actionEvent.getSource();
        String buttonId = clickedButton.getId();
        String time = (String) timeBox.getValue();

        //to set hour as default time
        if (time == null) {
            time = "hour";
        }
        //to set total clicks as default graph
        if(buttonId.equals("loadCSVbutton3")){
            buttonId = "totalClicks";
        }
        loadingBar();
        dc = new DatasetCreator(fph);
//        TimeFrameControl tfc = new TimeFrameControl();
//        tfc.createTimeFrame();
        loadGraph(buttonId, time);
        dataChart.layout();

    }
    public void loadDataGraphsWithinRange(ActionEvent actionEvent){
        dataChart.layout();
        Button clickedButton = (Button) actionEvent.getSource();
        String buttonId = clickedButton.getId();
        String time = (String) timeBox.getValue();

        //to set hour as default time
        if(time == null){
            time = "hour";
        }
        //to set total clicks as default graph
        if(buttonId.equals("loadCSVbutton3")){
            buttonId = "totalClicks";
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
//        loadHistogramChart();
        //loadHistogramClickCost();
        chartPane.layout();

    }
    // add new load Graphs function handle the date range

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
    public void loadtimeBox(ActionEvent event) {
        ComboBox comboBox = (javafx.scene.control.ComboBox) event.getSource();
        this.timeBox = comboBox;
        System.out.println(timeBox.getValue());
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
            String startDate = getFromDateTime();
            String endDate = getToDateTime();
            dataChart.getData().add(dataman.getData(selectedButton, timeBox.getValue().toString() , startDate,endDate, genderFilter.getValue().toString() , incomeFilter.getValue().toString(), contextFilter.getValue().toString() , ageFilter.getValue().toString()));
            }
            //to set end date way far in the future as dafault, so it reads every data
        if(toDate.getValue() == null){

                int day = dataman.getLastDate("DAY","clicklog");
                int month = dataman.getLastDate("MONTH","clicklog");
                int year = dataman.getLastDate("YEAR","clicklog");


                toDate.setValue(LocalDate.of(year,month,day));
            }
//           if(selectedButton.equals("BounceRate") || selectedButton.equals("TotalBounces")){
//                if(timeSpentBounce.getText()!=null && pageViewedBounce.getText()!=null){
//                    dataChart.getData().add(convertMapToSeries(dc.createDataset(selectedButton, time,fromDate.getValue(),toDate.getValue(), timeSpentBounce.getText(),pageViewedBounce.getText()), selectedButton));
//                }else if(timeSpentBounce.getText()==null && pageViewedBounce.getText()!=null){
//                    dataChart.getData().add(convertMapToSeries(dc.createDataset(selectedButton, time,fromDate.getValue(),toDate.getValue(), "",pageViewedBounce.getText()), selectedButton));
//                }else if(timeSpentBounce.getText()!=null && pageViewedBounce.getText()==null){
//                    dataChart.getData().add(convertMapToSeries(dc.createDataset(selectedButton, time,fromDate.getValue(),toDate.getValue(), timeSpentBounce.getText(),""), selectedButton));
//                }else{
//                    dataChart.getData().add(convertMapToSeries(dc.createDataset(selectedButton, time,fromDate.getValue(),toDate.getValue(), "",""), selectedButton));
//                }
//            }else {
//                dataChart.getData().add(convertMapToSeries(dc.createDataset(selectedButton, time, fromDate.getValue(), toDate.getValue()), selectedButton));
//            }
            // Increase the spacing between tick labels
            xAxis.setTickLabelGap(10);

            // Rotate the tick labels by -45 degrees
            xAxis.setTickLabelRotation(-45);

            // Enable auto-ranging for the x-axis
            dataChart.getXAxis().setAutoRanging(true);

            // Force a layout update

        dataChart.layout();

    }

    public String getToDateTime(){
        if(toDate.getValue() == null){
            int day = dataman.getLastDate("day","clicklog");
            int month = dataman.getLastDate("month","clicklog");
            int year = dataman.getLastDate("year","clicklog");

            toDate.setValue(LocalDate.of(year,month,day));
        }
        if(toHour.getValue() == null){
            int hour = dataman.getLastDate("hour","clicklog");
            toHour.setValue(Integer.toString(hour));
        }
        if(toMinute.getValue() == null){
            int minute = dataman.getLastDate("minute","clicklog");
            toMinute.setValue(Integer.toString(minute));
        }
        if(toSecond.getValue() == null){
            int second = dataman.getLastDate("second","clicklog");
            toSecond.setValue(Integer.toString(second));
        }
        LocalDate selectedDate = toDate.getValue();
        int hour = Integer.parseInt(toHour.getValue());
        int minute = Integer.parseInt(toMinute.getValue());
        int second = Integer.parseInt(toSecond.getValue());

        LocalDateTime dateTime = LocalDateTime.of(selectedDate, LocalTime.of(hour, minute, second));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return dateTime.format(formatter);
    }
    public String getFromDateTime() {
        if (fromDate.getValue() == null) {
            int day = dataman.getFirstDate("day", "clicklog");
            int month = dataman.getFirstDate("month", "clicklog");
            int year = dataman.getFirstDate("year", "clicklog");

            fromDate.setValue(LocalDate.of(year, month, day));
        }
        if (fromHour.getValue() == null) {
            int hour = dataman.getFirstDate("hour", "clicklog");
            fromHour.setValue(Integer.toString(hour));
        }
        if (fromMinute.getValue() == null) {
            int minute = dataman.getFirstDate("minute", "clicklog");
            fromMinute.setValue(Integer.toString(minute));
        }
        if (fromSecond.getValue() == null) {
            int second = dataman.getFirstDate("second", "clicklog");
            fromSecond.setValue(Integer.toString(second));
        }

        LocalDate selectedDate = fromDate.getValue();
        int hour = Integer.parseInt(fromHour.getValue());
        int minute = Integer.parseInt(fromMinute.getValue());
        int second = Integer.parseInt(fromSecond.getValue());

        LocalDateTime dateTime = LocalDateTime.of(selectedDate, LocalTime.of(hour, minute, second));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return dateTime.format(formatter);
    }
    // Customize the graph based on the selected radio button
    //Function that would load the graph data inside the panel.
    //Not implemented.

    public void createTimeFrame(){
        fromDate.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        toDate.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        fromHour.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        fromMinute.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        fromSecond.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        toHour.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        toMinute.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));
        toSecond.valueProperty().addListener((obs, oldVal, newVal) -> validateDateTime(fromDate,fromHour,fromMinute, fromSecond, toDate, toHour, toMinute,toSecond));

       setupTimeComboBoxes(); // Setup method for time ComboBoxes
       setupTimeComboBoxes(); // Setup method for time ComboBoxes

     /*  Button showRangeButton = new Button("Show");
        showRangeButton.setOnAction(e -> {
            LocalDateTime fromDateTime = LocalDateTime.of(startDate.getValue(), LocalTime.of(Integer.parseInt(fromHour.getValue()), Integer.parseInt(fromMinute.getValue()), Integer.parseInt(fromSecond.getValue())));
            LocalDateTime toDateTime = LocalDateTime.of(endDate.getValue(), LocalTime.of(Integer.parseInt(toHour.getValue()), Integer.parseInt(toMinute.getValue()), Integer.parseInt(toSecond.getValue())));
        });*/
    }
    public String getStartDateTimeAsString() {
        LocalDate date = fromDate.getValue();
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
        LocalDate date = toDate.getValue();
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
     *
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


    private void validateDateTime(DatePicker fromDate, ComboBox<String> fromHour, ComboBox<String> fromMinute, ComboBox<String> fromSecond, DatePicker toDate, ComboBox<String> toHour, ComboBox<String> toMinute, ComboBox<String> toSecond) {
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
            System.out.println("date changed");
            fromDate.setValue(fromDate.getValue());
            toDate.setValue(toDate.getValue());
        }
    }


    //Function to count the unique impressions
    public int countUniqueImpressions(){
        logger.log(Level.INFO, "Loading Unique visits from impressions_log");
        return dataman.selectTotalData("impressionlog");
    }
    // function to count the unique impressions within dates
//    public int countUniqueImpressionsWithinDates(){
//        Logger logger = Logger.getLogger(DashboardController.class.getName());
//        logger.log(Level.INFO, "Loading Unique visits within start and end time from impressions_log");
//        return dataman.selectTotalDataWithinRange("impressionlog", getStartDateTimeAsString(),getEndDateTimeAsString());
//    }

    //Function to count the zero cost clicks
    public int countZeroCostClick(){
        logger.log(Level.INFO, "Loading Zero Cost Clicks");
        return dataman.selectZeroClickCost();
    }
//    public int countZeroCostClickWithinDates(){
//        Logger logger = Logger.getLogger(DashboardController.class.getName());
//        logger.log(Level.INFO,"Loading Zero Cost within start and end time Clicks");
//        return dataman.selectZeroClickCostWithinRange(getStartDateTimeAsString(),getEndDateTimeAsString());
//    }

    //Function to find the average price per click
    public double countAveragePricePerClick(){
        logger.log(Level.INFO, "Loading Average Price per Click");
        return dataman.selectAvgData("clickCost", "clicklog");
    }
//    public double countAverageProcePerClickWithinDates(){
//        Logger logger = Logger.getLogger(DashboardController.class.getName());
//        logger.log(Level.INFO, "Loading Average Price per Click");
//        return dataman.selectAvgDataWithinRange("clickCost", "clicklog", getStartDateTimeAsString(),getEndDateTimeAsString());
//    }


    //Function to find the total impressions
    public int countTotalImpressions() {

        return dataman.selectTotalData("impressionlog");
    }
//    public int countTotalImpressionsWithinDates(){
//        return dataman.selectTotalDataWithinRange("impressionlog", getStartDateTimeAsString(),getEndDateTimeAsString());
//    }

    //Function to find the total clicks for the campaign

    public int countTotalClicks(){

        logger.log(Level.INFO, "Loading Total clicks");

        return dataman.selectTotalData("clicklog");
    }
//    public int countTotalClicksWithinDates(){
//        logger = Logger.getLogger(DashboardController.class.getName());
//        logger.log(Level.INFO, "Loading Total clicks within start and end time");
//        return dataman.selectTotalDataWithinRange("clicklog", getStartDateTimeAsString(),getEndDateTimeAsString());
//    }
public int countTotalBounces(){
    logger = Logger.getLogger(DashboardController.class.getName());
    logger.log(Level.INFO, "Loading Total Bounces");

    return dataman.selectTotalBounces(timeSpentBounce.getText(), pageViewedBounce.getText());
}
    public double countBounceRate(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO, "Loading Bounce Rate");
        return dataman.selectBounceRate(timeSpentBounce.getText(), pageViewedBounce.getText());
    }

    //Function to find the total entries from adds - needs better explanation
    public int countTotalEntries(){
        logger.log(Level.ALL, "Loading total entries from ads.");
        return dataman.selectTotalData("serverlog");
    }
//    public int countTotalEntriesWithinDates(){
//        logger = Logger.getLogger(DashboardController.class.getName());
//        logger.log(Level.ALL, "Loading total entries from ads within start and end time.");
//        return dataman.selectTotalDataWithinRange("severlog", getStartDateTimeAsString(),getEndDateTimeAsString());
//    }
    //Function to find the average number of pages
    public double countAvgPageViewed(){
        logger.log(Level.INFO, "Loading average pages viewed.");
        return Math.round(dataman.selectAvgData("pagesViewed", "serverlog") * 100) / 100;
    }
//    public double countAvgPageViewedWithinDates(){
//        logger = Logger.getLogger(DashboardController.class.getName());
//        logger.log(Level.INFO, "Loading average pages viewed.");
//        return Math.round(dataman.selectAvgDataWithinRange("pagesViewed", "serverlog", getStartDateTimeAsString(),getEndDateTimeAsString()) * 100) / 100;
//    }

    //loading bar function
    public void loadingBar() {
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

//    public void loadHistogramClickCost() {
//        Map<String, Double> dateAndClickCost = dataman.getAverageClickCostPerDay("clicklog");
//
//        // Create a sorted TreeMap to ensure dates are in order
//        TreeMap<String, Double> sortedDateAndClickCost = new TreeMap<>(dateAndClickCost);
//
//        // Create data series for the histogram chart
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//
//        // Populate data series with dates and corresponding click costs
//        for (Map.Entry<String, Double> entry : sortedDateAndClickCost.entrySet()) {
//            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
//        }
//
//        // Add data series to the histogram chart
//        histogramClicks.getData().add(series);
//        histogramClicks.setTitle("Histogram");
//
//    }
//public void loadHistogramClickCost() {
   // Map<String, Double> dateAndClickCost = dataman.getDateAndClickCost("clicklog");

    // Create data series for the histogram chart
  //  XYChart.Series<String, Number> series = new XYChart.Series<>();

    // Populate data series with dates and corresponding click costs
   // for (Map.Entry<String, Double> entry : dateAndClickCost.entrySet()) {
   //     series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
   // }

    // Add data series to the histogram chart
   // histogramClicks.getData().add(series);
  //  histogramClicks.setTitle("Histogram");

    // Adjust the width of the bars
//    double barWidth = 100; // Adjust this value as needed
//    for (XYChart.Data<String, Number> data : series.getData()) {
//        Node bar = data.getNode();
//        if (bar != null) {
//            bar.setStyle("-fx-bar-width: " + barWidth + ";");
//        }
//    }
//}


//    public void loadHistogramClickCost() {
//        Map<String, Double> dateAndClickCost = dataman.getDateAndClickCost("clicklog");
//
//        // Create a new map to store aggregated click costs by day
//        Map<String, Double> aggregatedClickCostsByDay = new HashMap<>();
//
//        // Aggregate click costs by day
//        for (Map.Entry<String, Double> entry : dateAndClickCost.entrySet()) {
//            String date = LocalDate.parse(entry.getKey(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
//            double clickCost = entry.getValue();
//            aggregatedClickCostsByDay.put(date, aggregatedClickCostsByDay.getOrDefault(date, 0.0) + clickCost);
//        }
//
//        // Create data series for the histogram chart
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//
//        // Populate data series with dates and corresponding aggregated click costs by day
//        for (Map.Entry<String, Double> entry : aggregatedClickCostsByDay.entrySet()) {
//            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
//        }
//
//        // Add data series to the histogram chart
//        histogramClicks.getData().add(series);
//        histogramClicks.setTitle("Histogram");
//    }



    //Function to find the gender separation.
    public void loadGenders(){
        logger.log(Level.ALL, "Loading genders.");

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

        logger.log(Level.ALL, "Loading age graph.");

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

    public void loadConversionChart() {
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
//    public void loadHistogramChart() {
//        Map<String, Double> dateAndClickCost = dataman.getDateAndClickCost("serverlog");
//
//        // Create a category axis for the X-axis
//        CategoryAxis xAxis = new CategoryAxis();
//        xAxis.setLabel("Date");
//
//        // Create a number axis for the Y-axis
//        NumberAxis yAxis = new NumberAxis();
//        yAxis.setLabel("Click Cost");
//
//        // Create a histogram chart
//        LineChart<String, Number> histogramChart = new LineChart<>(xAxis, yAxis);
//
//        // Set chart title
//        histogramChart.setTitle("Click Costs Over Time");
//
//        // Create data series for the histogram chart
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//
//        // Populate data series with dates and corresponding click costs
//        for (Map.Entry<String, Double> entry : dateAndClickCost.entrySet()) {
//            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
//        }
//
//        // Add data series to the histogram chart
//        histogramChart.getData().add(series);
//
//        // Add the histogram chart to your layout
//        // Replace 'yourPane' with the actual Pane or other layout container you want to add the chart to
//        //histogramClicks.getChildren().add(histogramChart);
//        histogramClicks.setLabelLineLength(20);
//        histogramClicks.setLabelsVisible(true);
//        histogramClicks.setData(histogramChart);
//
//    }





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
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        FileChooserWindow fileChooser = new FileChooserWindow();

        fph.fileTypeHandler(fileChooser.openFileBox("all"));
        System.out.println(fph.getImpressionPath());
        System.out.println(fph.getClickPath());
        System.out.println(fph.getServerPath());
        a.setContentText("Inputting data...");
        a.show();
        //loadingBar();
        //loadSQL();
        a.hide();

        a.setContentText("Ready.");
        a.show();
        //a.hide();
        System.out.println("Ready ^_^!");
    }
    public void setClicksLoaded(Boolean bool){
        clicksLoaded = bool;
    }
    public void setimpressionsLoaded(Boolean bool){
        impressionsLoaded = bool;
    }
    public void setserverLoaded(Boolean bool){
        serverLoaded = bool;
    }
    public void loadSQL(){

//        Runnable servrun = new Multithread_ServerDB(fph,this,Thread.currentThread());
//        Thread servThread = new Thread(servrun);
        //Runnable imprun = new Multithread_ImpressionsDB(fph,this,Thread.currentThread());
        //Thread impresThread = new Thread(imprun);

        Multithread_ImpressionDb multiImpress = new Multithread_ImpressionDb();
        testClickThread tct = new testClickThread();
        testServerThread tst = new testServerThread();

//        Runnable clickrun = new Multithread_ClicksDb(fph,this,Thread.currentThread());
//        Thread clickTread = new Thread(clickrun);
//        Iterator<Map.Entry<Integer, Boolean>> it;
//        Map.Entry<Integer, Boolean> entry;
//        Map<Integer,Boolean> finished;

        try {

            //finished = new HashMap<>();
            if(fph.getClickPath() != null) {
                //finished.put(1,false);
                //clickTread.start();
                tct.main(fph.getClickPath());
            }
            if(fph.getImpressionPath()!= null) {
                //finished.put(2,false);
                //impresThread.start();
                multiImpress.main(fph.getImpressionPath());
            }
            if(fph.getServerPath()!= null) {
                //finished.put(3,false);
                //servThread.start();
                tst.main(fph.getServerPath());
            }

//                while(!finished.isEmpty()) {
//
//                    it = finished.entrySet().iterator();
//                    while (it.hasNext()){
//                        entry = it.next();
//                        if(entry.getKey() == 1 && entry.getValue()){
//                            finished.remove(1);
//                            logger.log(Level.INFO,"notifying clicks thread");
//                            clickrun.notify();
//                        }
//                        else if(entry.getKey() == 2 && entry.getValue()){
//                            finished.remove(2);
//                            logger.log(Level.INFO,"notifying impressions thread");
//                            //impresThread.notify();
//                        }
//                        else if(entry.getKey() == 3 && entry.getValue()){
//                            finished.remove(3);
//                            logger.log(Level.INFO,"notifying server thread");
//                            servThread.notify();
//                        }
//                    }
//                    wait();
//                }


        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Ready ^_^!");
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

    public void enableLightTheme() {
        if (!light) {
            light = true;
            dark = false;
            logger.log(Level.INFO,"Light theme displayed");
            background.getStylesheets().clear();
            obj.writeTheme("light");

        }
    }

    public void enableDarkTheme(){
        logger.log(Level.INFO, "Loading dark theme");
        if(!dark){

            String stylesheetPath = getClass().getClassLoader().getResource("dashboardDarkTheme.css").toExternalForm();;
            background.getStylesheets().add(stylesheetPath);
            dark = true;
            light = false;
            logger.log(Level.INFO,"Dark theme displayed");
            obj.writeTheme("dark");
        }
    }
    public void selectClickGraph(ActionEvent actionEvent) {}

}
