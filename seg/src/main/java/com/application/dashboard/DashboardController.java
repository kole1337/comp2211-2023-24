package com.application.dashboard;

import com.application.database.*;
import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import com.application.logger.LogAction;
import com.application.setup.styles.checkStyle;
import javafx.application.Platform;
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
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.jfree.chart.ChartFrame;

import java.awt.*;
import java.io.*;
import java.net.URI;
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
    public VBox background;
    FilePathHandler fph = new FilePathHandler();
    public ImageView tutPNG;
    public Button tutorialOFF;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger = Logger.getLogger(DashboardController.class.getName());
    private DatasetCreator dc;
    @FXML
    public Button totalClicks;
    @FXML
    public Button totalImpressions;
    @FXML
    public Button totalCost;
    @FXML
    public Button totalUniques;
    @FXML
    public Button totalBounces;
    @FXML
    public Button totalConversions;
    @FXML
    public Button costPerAcq;
    @FXML
    public Button costPerClicks;
    @FXML
    public Button costPerImpres;
    @FXML
    public Button costPerThousandImpres;
    @FXML
    public Button bounceRate;
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
    @FXML public CheckBox chkSelectAll;
    @FXML public CheckBox chkConversion;
    @FXML public CheckBox chkContextOriginal;
    @FXML public CheckBox chkIncome;
    @FXML public CheckBox chkAge;
    @FXML public CheckBox chkGender;

    CategoryAxis xAxis = new CategoryAxis();

    boolean clicksLoaded = false;
    boolean impressionsLoaded = false;
    boolean serverLoaded = false;

    private checkStyle obj = new checkStyle();

    private LogAction logAction = new LogAction();

    DbConnection dbConnection = new DbConnection();
    List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();
    List<String> labelTextList = new ArrayList<>();
    List<Tooltip> toolTipList = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String theme = obj.checkStyle();
        if(theme.equals("dark")){
            enableDarkTheme();
        }else{
            enableLightTheme();
        }
        logAction.logActionToFile("Open dashboard panel.");
        startUP();
    }




    public void startUP(){

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
        genderFilter.setValue("Any");
        contextFilter.setValue("Any");
        ageFilter.setValue("Any");
        incomeFilter.setValue("Any");
        timeBox.setValue("hour");

        dataChart.setMaxHeight(Double.MAX_VALUE);
        dataChart.setMaxWidth(Double.MAX_VALUE);
    }

    public DashboardController(){
        logger.log(Level.INFO, "Creating dashboard and connecting to database");
    }

    /**
     * The button Load data displays
     * */
    public void loadData() {

        logger.log(Level.ALL, "loadData button");

        String genderFilt = genderFilter.getValue().toString();
        String ageFilt = ageFilter.getValue().toString();
        String incomeFilt = incomeFilter.getValue().toString();
        String contextFilt = contextFilter.getValue().toString();

        uniqueImpressionLabel.setText("Unique Impressions: " +   countUniqueImpressions(genderFilt, ageFilt, incomeFilt, contextFilt));
        sumImpressionsLabel.setText("Total impressions: " +      countTotalImpressions(genderFilt, ageFilt, incomeFilt, contextFilt));
        totalClicksLabel.setText("Total clicks: " +              countTotalClicks(genderFilt, ageFilt, incomeFilt, contextFilt));
//
        zeroCostClickLabel.setText("Zero cost clicks: " +        countZeroCostClick(genderFilt, ageFilt, incomeFilt, contextFilt));
        avgClickPriceLabel.setText("Average price per click: " + countAveragePricePerClick(genderFilt, ageFilt, incomeFilt, contextFilt));
        totalEntriesLabel.setText("Total entries from ads: " +   countTotalEntries(genderFilt, ageFilt, incomeFilt, contextFilt));
        avgPagesViewedLabel.setText("Average pages viewed: " +   countAvgPageViewed(genderFilt, ageFilt, incomeFilt, contextFilt));
        totalBouncesLabel.setText("Total Bounce: " +             countTotalBounces(genderFilt, ageFilt, incomeFilt, contextFilt));
        bounceRateLabel.setText("Bounce Rate: " +                countBounceRate(genderFilt, ageFilt, incomeFilt, contextFilt));
    }
    public void loadCSVWithinDates(ActionEvent actionEvent){
//        uniqueImpressionLabel.setText("Unique Impressions: " + countUniqueImpressionsWithinDates());
//        sumImpressionsLabel.setText("Total impressions: " + countTotalImpressionsWithinDates());
//
//        totalClicksLabel.setText("Total clicks: " + countTotalClicksWithinDates());
//        zeroCostClickLabel.setText("Zero cost clicks: " + countZeroCostClickWithinDates());
//        avgClickPriceLabel.setText("Average price per click: " + countAverageProcePerClickWithinDates());
//        totalEntriesLabel.setText("Total entries from ads: " + countTotalEntriesWithinDates());
//        avgPagesViewedLabel.setText("Average pages viewed: " + countAvgPageViewedWithinDates());
    }
    public void clearGraphs(){
        dataChart.getData().clear();
        labelTextList.clear();
        toolTipList.clear();
        uniqueImpressionLabel.setText("Unique Impressions:");
        sumImpressionsLabel.setText("Total impressions: ");
        totalClicksLabel.setText("Total clicks: ");
        zeroCostClickLabel.setText("Zero cost clicks: ");
        avgClickPriceLabel.setText("Average price per click: ");
        totalEntriesLabel.setText("Total entries from ads: ");
        avgPagesViewedLabel.setText("Average pages viewed: ");
        totalBouncesLabel.setText("Total Bounce: ");
        bounceRateLabel.setText("Bounce Rate: ");
        genderGraph.getData().clear();
        ageGraph.getData().clear();
        incomeGraph.getData().clear();
        contextOriginGraph.getData().clear();
        conversionGraph.getData().clear();
        histogramClicks.getData().clear();
        genderFilter.setValue("Any");
        incomeFilter.setValue("Any");
        ageFilter.setValue("Any");
        contextFilter.setValue("Any");
    }

    public void loadDataGraphs(ActionEvent event) {
        String time = (String) timeBox.getValue();
        Button clicked = (Button) event.getSource();
        String graphName = clicked.getId();
        //to set hour as default time
        if (time == null) {
            time = "hour";
        }
        loadingBar();
//        dc = new DatasetCreator(fph);
//        TimeFrameControl tfc = new TimeFrameControl();
//        tfc.createTimeFrame();
        loadGraph(graphName, time);
        System.out.println(graphName);
        dataChart.requestLayout();

    }
  /*  public void loadDataGraphsWithinRange(ActionEvent actionEvent){
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
    } */
    public void exportChartToPDF(){
        PDFExporter pdfExporter = new PDFExporter();
        pdfExporter.exportChartToPDF(dataChart);
    }
    public void saveChartAsImage(){

        ImageExporter imageExporter = new ImageExporter();
        imageExporter.exportChartToImage(dataChart);
    }

    public void exportSinglePieChartToPDF(ActionEvent e){
        Button clickedButton = (Button) e.getSource();
        String buttonId = clickedButton.getId();
        PDFExporter pdfExporter = new PDFExporter();
        if(buttonId.equals("exportAgeGraphToPDF")){
           pdfExporter.exportChartToPDF(ageGraph);
        }
        if(buttonId.equals("exportGenderGraphToPDF")){
            pdfExporter.exportChartToPDF(genderGraph);
        }
        if(buttonId.equals("exportIncomeGraphToPDF")){
            pdfExporter.exportChartToPDF(incomeGraph);
        }
        if(buttonId.equals("exportCOGraphToPDF")){
            pdfExporter.exportChartToPDF(contextOriginGraph);
        }
        if(buttonId.equals("exportConversionGraphToPDF")){
            pdfExporter.exportChartToPDF(conversionGraph);
        }

    }
    public void exportSinglePieChartToImage(ActionEvent e){
        Button clickedButton = (Button) e.getSource();
        String buttonId = clickedButton.getId();
        ImageExporter imageExporter = new ImageExporter();
        if(buttonId.equals("exportAgeGraphToImage")){
            imageExporter.exportChartToImage(ageGraph);
        }
        if(buttonId.equals("exportGenderGraphToImage")){
            imageExporter.exportChartToImage(genderGraph);
        }
        if(buttonId.equals("exportIncomeGraphToImage")){
            imageExporter.exportChartToImage(incomeGraph);
        }
        if(buttonId.equals("exportCOGraphToImage")){
            imageExporter.exportChartToImage(contextOriginGraph);
        }
        if(buttonId.equals("exportConversionGraphToImage")){
            imageExporter.exportChartToImage(conversionGraph);
        }
    }



    /**
     * print the pie chart
     * @param chart the pie chart
     */
    public void printPieChart(PieChart chart){
        SnapshotParameters parameters = new SnapshotParameters();
        WritableImage image = chart.snapshot(parameters, null);
        ImageView imageView = new ImageView(image);
        PrinterUtil printerUtil = new PrinterUtil();
        printerUtil.print(imageView, this.stage);
    }

    public void printSelectedPieCharts(){

        if (conversionGraph != null && chkConversion.isSelected()) {
            printPieChart(conversionGraph);
        }
        if (contextOriginGraph != null && chkContextOriginal.isSelected()) {
            printPieChart(contextOriginGraph);
        }
        if (incomeGraph != null && chkIncome.isSelected()) {
            printPieChart(incomeGraph);
        }
        if (ageGraph != null && chkAge.isSelected()) {
            printPieChart(ageGraph);
        }
        if(genderGraph != null && chkGender.isSelected()){
            printPieChart(genderGraph);
        }
    }

    /**
     * print the data chart (main chart)
     */
    public void printChart(){
        PrinterUtil printerUtil = new PrinterUtil();
        SnapshotParameters parameters = new SnapshotParameters();

        // Create a writable image based on the chart dimensions
        WritableImage writableImage = dataChart.snapshot(parameters, null);
        ImageView imageView = new ImageView(writableImage);
        printerUtil.print(imageView, this.stage);
    }

    /**
     * check whether the CheckBox is slected
     */
    public void handleSelectAllAction(){
        boolean selected = chkSelectAll.isSelected();
        chkConversion.setSelected(selected);
        chkContextOriginal.setSelected(selected);
        chkIncome.setSelected(selected);
        chkAge.setSelected(selected);
        chkGender.setSelected(selected);
    }

    /**
     * download selected pie charts in the selected directory
     */
    public void downloadSelectedCharts() {

        ImageExporter imageExporter = new ImageExporter();
        if (conversionGraph != null && chkConversion.isSelected()) {
            imageExporter.exportChartToImage(conversionGraph);
        }
        if (contextOriginGraph != null && chkContextOriginal.isSelected()) {
            imageExporter.exportChartToImage(conversionGraph);
        }
        if (incomeGraph != null && chkIncome.isSelected()) {
            imageExporter.exportChartToImage(incomeGraph);
        }
        if (ageGraph != null && chkAge.isSelected()) {
            imageExporter.exportChartToImage(ageGraph);
        }
        if(genderGraph != null && chkGender.isSelected()){
            imageExporter.exportChartToImage(genderGraph);
        }
    }
    public void downloadSelectedPDFs(){
        PDFExporter pdfExporter = new PDFExporter();
        if (conversionGraph != null && chkConversion.isSelected()) {
            pdfExporter.exportChartToPDF(conversionGraph);
        }
        if (contextOriginGraph != null && chkContextOriginal.isSelected()) {
            pdfExporter.exportChartToPDF(conversionGraph);
        }
        if (incomeGraph != null && chkIncome.isSelected()) {
            pdfExporter.exportChartToPDF(incomeGraph);
        }
        if (ageGraph != null && chkAge.isSelected()) {
            pdfExporter.exportChartToPDF(ageGraph);
        }
        if(genderGraph != null && chkGender.isSelected()){
            pdfExporter.exportChartToPDF(genderGraph);
        }
    }

    public void loadGraphs() {
        loadGenders();
        loadAgeGraph();
        loadIncomeGraph();
        loadContextOriginChart();
        loadConversionChart();
        loadHistogramClickCost();
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
        dataChart.layout();
        xAxis.setTickLabelGap(10); // Set the spacing between major tick marks
        xAxis.setTickLabelRotation(-45);
        dataChart.setAnimated(false);
        if (selectedButton != null) {
            //to set start date way back in the past as default, so it reads every data
            String startDate = getFromDateTime();
            String endDate = getToDateTime();
            XYChart.Series<String, Number> data = new XYChart.Series<>();
            data = dataman.getData(selectedButton, timeBox.getValue().toString(), startDate, endDate, genderFilter.getValue().toString(), incomeFilter.getValue().toString(), contextFilter.getValue().toString(), ageFilter.getValue().toString());
            data.setName(selectedButton + " with " + "gender: " + genderFilter.getValue().toString() + " income: " + incomeFilter.getValue().toString() + " context: " + contextFilter.getValue().toString() + " age: " + ageFilter.getValue().toString() + " from " + startDate + " to " + endDate);
            seriesList.add(data);
            dataChart.getData().add(data);
            Set<Node> nodes = dataChart.lookupAll(".chart-legend-item");
            List<Node> nodesList = nodes.stream().toList();
            labelTextList.add(selectedButton);
            toolTipList.add(new Tooltip(selectedButton + " with " + "gender: " + genderFilter.getValue().toString() + " income: " + incomeFilter.getValue().toString() + " context: " + contextFilter.getValue().toString() + " age: " + ageFilter.getValue().toString() + " from " + startDate + " to " + endDate));
            System.out.println(data.getName());
            for (Node legend : nodesList) {
                int index = nodesList.indexOf(legend);
                Label label = (Label) legend;
                label.setText(labelTextList.get(index));
                label.setTooltip(toolTipList.get(index));
                label.setOnMouseClicked(e -> {
                    System.out.println("WHATTTT");
                    boolean isVisible =!(label.getOpacity() == 1.0); // Check if the label is currently visible
                    label.setStyle("-fx-opacity: " + (isVisible ? 1.0 : 0.5) + ";"); // Set the opacity based on the visibility

                    for (XYChart.Series<String, Number> i : seriesList) {
                        System.out.println(i.getName());
                        System.out.println(label.getTooltip().getText());
                        if (i.getName().equals(label.getTooltip().getText()) && index == seriesList.indexOf(i)) {
                            System.out.println("Ok");
                            Node seriesNode = i.getNode();
                            seriesNode.setVisible(isVisible);

                            // Iterate over data points and set their visibility
                            for (XYChart.Data<String, Number> idata : i.getData()) {
                                Node dataNode = idata.getNode();
                                if (dataNode != null) {
                                    dataNode.setVisible(isVisible);
                                }
                            }
                        }
                    }
                });
            }//to set end date way far in the future as dafault, so it reads every data
            if (toDate.getValue() == null) {

                int day = dataman.getLastDate("DAY", "clicklog");
                int month = dataman.getLastDate("MONTH", "clicklog");
                int year = dataman.getLastDate("YEAR", "clicklog");


                toDate.setValue(LocalDate.of(year, month, day));
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

            // Force a layout update

            // Trigger a layout update after a short delay
            Platform.runLater(() -> {
                try {
                    Thread.sleep(100); // Adjust the delay as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dataChart.requestLayout();
            });

        }
    };


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
        if (fromHour.getValue() == null){
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
        int hour = dataman.getFirstDate("hour", "clicklog");
        fromHour.setValue(Integer.toString(hour));
        int minute = dataman.getFirstDate("minute", "clicklog");
        fromMinute.setValue(Integer.toString(minute));
        int second = dataman.getFirstDate("second", "clicklog");
        fromSecond.setValue(Integer.toString(second));
        int hourto = dataman.getLastDate("hour", "clicklog");
        toHour.setValue(Integer.toString(hourto));
        int minuteto = dataman.getLastDate("minute", "clicklog");
        toMinute.setValue(Integer.toString(minuteto));
        int secondto = dataman.getLastDate("second", "clicklog");
        toSecond.setValue(Integer.toString(secondto));
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
    public int countUniqueImpressions(String gender, String age, String income, String context){
        logger.log(Level.INFO, "Loading Unique visits from impressions_log");
        return dataman.selectUniqueImpressionData(gender, age, income, context, getFromDateTime(), getToDateTime());
    }
    // function to count the unique impressions within dates
//    public int countUniqueImpressionsWithinDates(){
//        Logger logger = Logger.getLogger(DashboardController.class.getName());
//        logger.log(Level.INFO, "Loading Unique visits within start and end time from impressions_log");
//        return dataman.selectTotalDataWithinRange("impressionlog", getStartDateTimeAsString(),getEndDateTimeAsString());
//    }

    //Function to count the zero cost clicks
    public int countZeroCostClick(String gender, String age, String income, String context){
        logger.log(Level.INFO, "Loading Zero Cost Clicks");
        return dataman.selectZeroClickCost( gender, age, income, context, getFromDateTime(), getToDateTime());
    }
//    public int countZeroCostClickWithinDates(){
//        Logger logger = Logger.getLogger(DashboardController.class.getName());
//        logger.log(Level.INFO,"Loading Zero Cost within start and end time Clicks");
//        return dataman.selectZeroClickCostWithinRange(getStartDateTimeAsString(),getEndDateTimeAsString());
//    }

    //Function to find the average price per click
    public double countAveragePricePerClick(String gender, String age, String income, String context){
        logger.log(Level.INFO, "Loading Average Price per Click");
        return dataman.selectAvgData("clickCost", "clicklog",  gender, age, income, context, getFromDateTime(), getToDateTime());
    }
//    public double countAverageProcePerClickWithinDates(){
//        Logger logger = Logger.getLogger(DashboardController.class.getName());
//        logger.log(Level.INFO, "Loading Average Price per Click");
//        return dataman.selectAvgDataWithinRange("clickCost", "clicklog", getStartDateTimeAsString(),getEndDateTimeAsString());
//    }


    //Function to find the total impressions
    public int countTotalImpressions(String gender, String age, String income, String context) {

        return dataman.selectTotalData("impressionlog",  gender, age, income, context, getFromDateTime(), getToDateTime());
    }


    //Function to find the total clicks for the campaign
    public int countTotalClicks(String gender, String age, String income, String context){

        logger.log(Level.INFO, "Loading Total clicks");

        return dataman.selectTotalData("clicklog", gender, age, income, context, getFromDateTime(), getToDateTime());
    }

    public int countTotalBounces(String gender, String age, String income, String context){
    logger = Logger.getLogger(DashboardController.class.getName());
    logger.log(Level.INFO, "Loading Total Bounces");

    return dataman.selectTotalBounces(gender, age, income, context, getFromDateTime(), getToDateTime());
    }
    public double countBounceRate(String gender, String age, String income, String context){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.INFO, "Loading Bounce Rate");
        return dataman.selectBounceRate(gender, age, income, context, getFromDateTime(), getToDateTime());
    }

    //Function to find the total entries from adds - needs better explanation
    public int countTotalEntries(String gender, String age, String income, String context){
        logger.log(Level.ALL, "Loading total entries from ads.");
        return dataman.selectTotalData("serverlog", gender, age, income, context, getFromDateTime(), getToDateTime());
    }
//    public int countTotalEntriesWithinDates(){
//        logger = Logger.getLogger(DashboardController.class.getName());
//        logger.log(Level.ALL, "Loading total entries from ads within start and end time.");
//        return dataman.selectTotalDataWithinRange("severlog", getStartDateTimeAsString(),getEndDateTimeAsString());
//    }
    //Function to find the average number of pages
    public double countAvgPageViewed(String gender, String age, String income, String context){
        logger.log(Level.INFO, "Loading average pages viewed.");
        return Math.round(dataman.selectAvgData("pagesViewed", "serverlog", gender, age, income, context, getFromDateTime(), getToDateTime()) * 100) / 100;
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
public void loadHistogramClickCost() {
    Map<String, Double> dateAndClickCost = dataman.getDateAndClickCost("clicklog");
//
//     Create data series for the histogram chart
    XYChart.Series<String, Number> series = new XYChart.Series<>();
//
//     Populate data series with dates and corresponding click costs
    for (Map.Entry<String, Double> entry : dateAndClickCost.entrySet()) {
        series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
    }
//
//     Add data series to the histogram chart
    histogramClicks.getData().add(series);
    histogramClicks.setTitle("Histogram");
//
//     Adjust the width of the bars
    double barWidth = 100; // Adjust this value as needed
    for (XYChart.Data<String, Number> data : series.getData()) {
        Node bar = data.getNode();
        if (bar != null) {
            bar.setStyle("-fx-bar-width: " + barWidth + ";");
        }
    }
}


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
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if(fromDate.getValue() == null && toDate.getValue() == null){
            int[] vals = dataman.getUniqueAppearanceInt("gender", "impressionlog", dataman.getMinDateFromTable("impressionlog"), dataman.getMaxDateFromTable("impressionlog"));
            String[] names = dataman.getUniqueAppearanceString("gender", "impressionlog");
            for (int i = 0; i < vals.length; i++) {
                names[i] = names[i] + ": " + vals[i];
                pieChartData.add(new PieChart.Data(names[i], vals[i]));
            }
        }

        if(fromDate.getValue() != null && toDate.getValue() != null){
            int[] vals = dataman.getUniqueAppearanceInt("gender", "impressionlog",fromDate.getValue().toString(), toDate.getValue().toString());
            String[] names = dataman.getUniqueAppearanceString("gender", "impressionlog");
            for (int i = 0; i < vals.length; i++) {
                names[i] = names[i] + ": " + vals[i];
                pieChartData.add(new PieChart.Data(names[i], vals[i]));
            }
        }

        if(fromDate.getValue() != null && toDate.getValue() == null) {

            int[] vals = dataman.getUniqueAppearanceInt("gender", "impressionlog", fromDate.getValue().toString(), dataman.getMaxDateFromTable("impressionlog"));
            String[] names = dataman.getUniqueAppearanceString("gender", "impressionlog");
            for (int i = 0; i < vals.length; i++) {
                names[i] = names[i] + ": " + vals[i];
                pieChartData.add(new PieChart.Data(names[i], vals[i]));
            }
        }

        if(fromDate.getValue() == null && toDate.getValue() != null) {

            int[] vals = dataman.getUniqueAppearanceInt("gender", "impressionlog", dataman.getMinDateFromTable("impressionlog"), toDate.getValue().toString());
            String[] names = dataman.getUniqueAppearanceString("gender", "impressionlog");
            for (int i = 0; i < vals.length; i++) {
                names[i] = names[i] + ": " + vals[i];
                pieChartData.add(new PieChart.Data(names[i], vals[i]));
            }
        }



        genderGraph.setTitle("Gender Graph (totals)");
        genderGraph.setLabelLineLength(20);
        genderGraph.setLabelsVisible(true);
        genderGraph.setData(pieChartData);
    }


    //function to load the age graph.
    public void loadAgeGraph(){

        logger.log(Level.ALL, "Loading age graph.");

        logger.log(Level.ALL, "Loading income graph.");
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if(fromDate.getValue() != null) {
            if(toDate.getValue() != null) {
                int[] vals = dataman.getUniqueAppearanceInt("age", "impressionlog", fromDate.getValue().toString(), toDate.getValue().toString());
                String[] names = dataman.getUniqueAppearanceString("age", "impressionlog");
                for (int i = 0; i < vals.length; i++) {
                    names[i] = names[i] + ": " + vals[i];
                    pieChartData.add(new PieChart.Data(names[i], vals[i]));
                }
            }else{
                int[] vals = dataman.getUniqueAppearanceInt("age", "impressionlog", fromDate.getValue().toString(), dataman.getMaxDateFromTable("impressionlog"));
                String[] names = dataman.getUniqueAppearanceString("age", "impressionlog");
                for (int i = 0; i < vals.length; i++) {
                    names[i] = names[i] + ": " + vals[i];
                    pieChartData.add(new PieChart.Data(names[i], vals[i]));
                }
            }
        }else{
            int[] vals = dataman.getUniqueAppearanceInt("age", "impressionlog", dataman.getMinDateFromTable("impressionlog"), dataman.getMaxDateFromTable("impressionlog"));
            String[] names = dataman.getUniqueAppearanceString("age", "impressionlog");
            for (int i = 0; i < vals.length; i++) {
                names[i] = names[i] + ": " + vals[i];
                pieChartData.add(new PieChart.Data(names[i], vals[i]));
            }
        }
        ageGraph.setLabelLineLength(20);
        ageGraph.setLabelsVisible(true);
        ageGraph.setData(pieChartData);
    }

    //Load income graph
    public void loadIncomeGraph(){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if(fromDate.getValue() != null) {
            if(toDate.getValue() != null) {
                int[] vals = dataman.getUniqueAppearanceInt("income", "impressionlog", fromDate.getValue().toString(), toDate.getValue().toString());
                String[] names = dataman.getUniqueAppearanceString("income", "impressionlog");
                for (int i = 0; i < vals.length; i++) {
                    names[i] = names[i] + ": " + vals[i];
                    pieChartData.add(new PieChart.Data(names[i], vals[i]));
                }
            }else{
                int[] vals = dataman.getUniqueAppearanceInt("income", "impressionlog", fromDate.getValue().toString(), dataman.getMaxDateFromTable("impressionlog"));
                String[] names = dataman.getUniqueAppearanceString("income", "impressionlog");
                for (int i = 0; i < vals.length; i++) {
                    names[i] = names[i] + ": " + vals[i];
                    pieChartData.add(new PieChart.Data(names[i], vals[i]));
                }
            }
        }else{
            int[] vals = dataman.getUniqueAppearanceInt("income", "impressionlog", dataman.getMinDateFromTable("impressionlog"), dataman.getMaxDateFromTable("impressionlog"));
            String[] names = dataman.getUniqueAppearanceString("income", "impressionlog");
            for (int i = 0; i < vals.length; i++) {
                names[i] = names[i] + ": " + vals[i];
                pieChartData.add(new PieChart.Data(names[i], vals[i]));
            }
        }
        incomeGraph.setLabelLineLength(20);
        incomeGraph.setLabelsVisible(true);
        incomeGraph.setData(pieChartData);
    }

    public void loadContextOriginChart(){

        logger.log(Level.ALL, "Loading income graph.");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if(fromDate.getValue() != null) {
            if(toDate.getValue() != null) {
                int[] vals = dataman.getUniqueAppearanceInt("context", "impressionlog", fromDate.getValue().toString(), toDate.getValue().toString());
                String[] names = dataman.getUniqueAppearanceString("context", "impressionlog");
                for (int i = 0; i < vals.length; i++) {
                    names[i] = names[i] + ": " + vals[i];
                    pieChartData.add(new PieChart.Data(names[i], vals[i]));
                }
            }else{
                int[] vals = dataman.getUniqueAppearanceInt("context", "impressionlog", fromDate.getValue().toString(), dataman.getMaxDateFromTable("impressionlog"));
                String[] names = dataman.getUniqueAppearanceString("context", "impressionlog");
                for (int i = 0; i < vals.length; i++) {
                    names[i] = names[i] + ": " + vals[i];
                    pieChartData.add(new PieChart.Data(names[i], vals[i]));
                }
            }
        }else{
            int[] vals = dataman.getUniqueAppearanceInt("context", "impressionlog", dataman.getMinDateFromTable("impressionlog"), dataman.getMaxDateFromTable("impressionlog"));
            String[] names = dataman.getUniqueAppearanceString("context", "impressionlog");
            for (int i = 0; i < vals.length; i++) {
                names[i] = names[i] + ": " + vals[i];
                pieChartData.add(new PieChart.Data(names[i], vals[i]));
            }
        }

        contextOriginGraph.setLabelLineLength(20);
        contextOriginGraph.setLabelsVisible(true);
        contextOriginGraph.setData(pieChartData);
    }

    public void loadConversionChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if(fromDate.getValue() != null) {
            if(toDate.getValue() != null) {
                int[] vals = dataman.getUniqueAppearanceInt("context", "impressionlog", fromDate.getValue().toString(), toDate.getValue().toString());
                String[] names = dataman.getUniqueAppearanceString("context", "impressionlog");
                for (int i = 0; i < vals.length; i++) {
                    names[i] = names[i] + ": " + vals[i];
                    pieChartData.add(new PieChart.Data(names[i], vals[i]));
                }
            }else{
                int[] vals = dataman.getUniqueAppearanceInt("context", "impressionlog", fromDate.getValue().toString(), dataman.getMaxDateFromTable("impressionlog"));
                String[] names = dataman.getUniqueAppearanceString("contextn", "impressionlog");
                for (int i = 0; i < vals.length; i++) {
                    names[i] = names[i] + ": " + vals[i];
                    pieChartData.add(new PieChart.Data(names[i], vals[i]));
                }
            }
        }else{
            int[] vals = dataman.getUniqueAppearanceInt("context", "impressionlog", dataman.getMinDateFromTable("impressionlog"), dataman.getMaxDateFromTable("impressionlog"));
            String[] names = dataman.getUniqueAppearanceString("context", "impressionlog");
            for (int i = 0; i < vals.length; i++) {
                names[i] = names[i] + ": " + vals[i];
                pieChartData.add(new PieChart.Data(names[i], vals[i]));
            }
        }

        conversionGraph.setLabelLineLength(20);
        conversionGraph.setLabelsVisible(true);
        conversionGraph.setData(pieChartData);

    }

    //Display tutorial overlay
    public void loadTutorial() {
//        tutPNG.setVisible(true);
//        tutorialOFF.setVisible(true);

    }
    public void openCompareCampaign(ActionEvent event){
        logger.log(Level.INFO,"pressed compare-campaign button");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/application/login/compare-campaign-view.fxml"));
            root = fxmlLoader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            DashboardController dashboardController = fxmlLoader.getController();
            dashboardController.fph = this.fph;
            stage.show();
            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.INFO, "Logging in as user. Opening the dashboard.");
        } catch (IOException e) {
            logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
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
        loadSQL();
        a.hide();

        a.setContentText("Ready.");
        a.show();
        //a.hide();
        System.out.println("Ready ^_^!");
//        dataman.closeConnection();
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
        try {
            dataman.dumpData();

            Multithread_ImpressionDb multiImpress = new Multithread_ImpressionDb();
            testClickThread tct = new testClickThread();
            testServerThread tst = new testServerThread();
            FileSplit splitFiles = new FileSplit();


            try {
                if (fph.getClickPath() != null) {
                    File file1 = fph.getClickPath();
//                File file1 = new File("C:\\Users\\gouri\\OneDrive - University of Southampton\\Documents\\year2\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\click_log.csv");
                    ArrayList<String> tempClicks = new ArrayList<>(splitFiles.splitFile(file1, 10));
                    tct.main(tempClicks);
                    System.out.println();
                    System.out.println("Importing");
                    clicksLoaded = true;
                    clicksLoadedLabel.setText("clicks_log.csv: loaded");
                }
                if (fph.getImpressionPath() != null) {
                    File file1 = fph.getImpressionPath();
//                File file1 = new File("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\impression_log.csv");

                    ArrayList<String> tempClicks = new ArrayList<>(splitFiles.splitFile(file1, 10));
                    multiImpress.main(tempClicks);
                    impressionsLoaded = true;
                    impressionLoadedLabel.setText("impression_log.csv: loaded");
                }
                if (fph.getServerPath() != null) {
                    File file1 = fph.getServerPath();
//                File file1 = new File("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\server_log.csv");

                    ArrayList<String> tempClicks = new ArrayList<>(splitFiles.splitFile(file1, 10));
                    tst.main(tempClicks);
                    serverLoaded = true;
                    serverLoadedLabel.setText("server_log.csv: loaded");
                }

                dbConnection.closeConn();

                dbConnection.makeConn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch(Exception e){
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
}