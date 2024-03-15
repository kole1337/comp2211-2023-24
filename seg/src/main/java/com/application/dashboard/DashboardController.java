package com.application.dashboard;

import com.application.files.FileChooser;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;

import org.jfree.chart.ChartFrame;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
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
    private Logger logger;
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


    public void loadCSV(ActionEvent actionEvent) {
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "loadCSV button");
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
        uniqueImpressionLabel.setText("Unique Impressions: " + countUniques());
        sumImpressionsLabel.setText("Total impressions: " + countTotalImpressions());
        loadGenders();
        loadAgeGraph();
        loadIncomeGraph();
        //loadGraph();
        totalClicksLabel.setText("Total clicks: " + countTotalClicks());
        zeroCostClickLabel.setText("Zero cost clicks: " + countZeroCostClick());
        avgClickPriceLabel.setText("Average price per click: " + countAveragePricePerClick());
        totalEntriesLabel.setText("Total entries from ads: " + countTotalEntries());
        avgPagesViewedLabel.setText("Average pages viewed: " + countAvgPageViewed());
        //load: graph, data to view, time slider, filters, number data
        sumImpressionsLabel.setVisible(true);
        uniqueImpressionLabel.setVisible(true);
        totalClicksLabel.setVisible(true);
        zeroCostClickLabel.setVisible(true);
        avgClickPriceLabel.setVisible(true);
        totalEntriesLabel.setVisible(true);
        avgPagesViewedLabel.setVisible(true);
        dataSelection.setVisible(true);
        filterSelection.setVisible(true);
        sliderTimeLabel.setVisible(true);
        timeFrameLabel.setVisible(true);
        dataChart.setVisible(true);
        loadCSVbutton.setVisible(false);
    }

    //Logout function for button.
    public void logoutButton(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/application/login/hello-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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
        xAxis.setTickLabelGap(10); // Set the spacing between major tick marks
        xAxis.setTickLabelRotation(-45);
        if (selectedButton != null) {
            //to set start date way back in the past as default, so it reads every data
            if(startDate.getValue() == null){
                startDate.setValue(LocalDate.of(1000,1,1));
            }
            //to set end date way far in the future as dafault, so it reads every data
            if(endDate.getValue() == null){
                endDate.setValue(LocalDate.of(3000,1,1));
            }
            dataChart.getData().add(convertMapToSeries(dc.createDataset(selectedButton, time,startDate.getValue(),endDate.getValue()), selectedButton));
            // Increase the spacing between tick labels
            xAxis.setTickLabelGap(10);

            // Rotate the tick labels by -45 degrees
            xAxis.setTickLabelRotation(-45);

            // Enable auto-ranging for the x-axis
            dataChart.getXAxis().setAutoRanging(true);

            // Force a layout update
            dataChart.layout();
        }
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

      //  setupTimeComboBoxes(fromHour, fromMinute, fromSecond); // Setup method for time ComboBoxes
       // setupTimeComboBoxes(toHour, toMinute, toSecond); // Setup method for time ComboBoxes

     /*  Button showRangeButton = new Button("Show");
        showRangeButton.setOnAction(e -> {
            LocalDateTime fromDateTime = LocalDateTime.of(startDate.getValue(), LocalTime.of(Integer.parseInt(fromHour.getValue()), Integer.parseInt(fromMinute.getValue()), Integer.parseInt(fromSecond.getValue())));
            LocalDateTime toDateTime = LocalDateTime.of(endDate.getValue(), LocalTime.of(Integer.parseInt(toHour.getValue()), Integer.parseInt(toMinute.getValue()), Integer.parseInt(toSecond.getValue())));
        });*/
    }
    /**
     * this is a method to create appropriate comboboxes for user to select hour/minute/second
     * @param hour
     * @param minute
     * @param second
     */

    private void setupTimeComboBoxes(ComboBox<String> hour, ComboBox<String> minute, ComboBox<String> second) {
        hour.getItems().addAll(generateTimeOptions(0, 23)); // Hours 0-23
        minute.getItems().addAll(generateTimeOptions(0, 59)); // Minutes 0-59
        second.getItems().addAll(generateTimeOptions(0, 59)); // Seconds 0-59
        hour.getSelectionModel().select("00"); // Default value
        minute.getSelectionModel().select("00"); // Default value
        second.getSelectionModel().select("00"); // Default value
    }

    /**
     * this is a method to generate time options
     * @param start
     * @param end
     * @return
     */

    private java.util.List<String> generateTimeOptions(int start, int end) {
        java.util.List<String> options = new java.util.ArrayList<>();
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
    public int countUniques(){
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading Unique visits from impressions_log");
        String filePath = fph.getImpressionPath(); // Nikola - PC
        String csvFilePath = filePath;
        int columnIndexToCount = 1; // Index of the column for data
        Set<String> uniqueEntries = new HashSet<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length > columnIndexToCount) {
                    String columnValue = line[columnIndexToCount].trim();
                    uniqueEntries.add(columnValue);
                }
            }

            int uniqueCount = uniqueEntries.size();
            //System.out.println("Number of unique entries in column " + columnIndexToCount + ": " + uniqueCount);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return uniqueEntries.size();
    }

    //Function to count the zero cost clicks
    public int countZeroCostClick(){
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading Zero Cost Clicks");
        String filePath = fph.getClickPath(); // Nikola - PC
        String csvFilePath = filePath;
        int columnIndexToCount = 2; // Change this to the index of the column you want to count (0-based index)
        Set<String> uniqueEntries = new HashSet<>();
        int totalZeros = 0;
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {


            String[] line;
            line = reader.readNext(); //skip header
            while ((line = reader.readNext()) != null) {
                if (Double.parseDouble(line[2]) == 0) {
                    totalZeros++;
                }
            }

            int uniqueCount = uniqueEntries.size();
//            System.out.println("Number of unique entries in column " + columnIndexToCount + ": " + uniqueCount);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return totalZeros;
    }

    //Function to find the average price per click
    public double countAveragePricePerClick(){
        double average = 0;
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading Average Price per Click");
        String filePath = fph.getClickPath(); // Nikola - PC
        String csvFilePath = filePath;
        int columnIndexToCount = 2; // Change this to the index of the column you want to count (0-based index)
        int totalPrice= 0;
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {


            String[] line;
            line = reader.readNext(); //skip header
            while ((line = reader.readNext()) != null) {
                totalPrice += Double.parseDouble(line[2]);
            }

//            System.out.println("Number of unique entries in column " + columnIndexToCount + ": ");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return totalPrice/countTotalClicks();
    }

    //Function to find the total impressions
    public int countTotalImpressions(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading Total Impressions");
        String filePath = fph.getImpressionPath(); // Nikola - PC
        int totalEntries = 0;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            nextLine = reader.readNext(); // skip header
            // Read each line from the CSV file
            while ((nextLine = reader.readNext()) != null) {
                totalEntries++;
            }

            //System.out.println("Total Entries in Column: " + totalEntries);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return totalEntries;
    }

    //Function to find the total clicks for the campaign
    public int countTotalClicks(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading Total clicks");
        String filePath = fph.getClickPath();
        int totalEntries = 0;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;

            // Read each line from the CSV file
            while ((nextLine = reader.readNext()) != null) {
                totalEntries++;
            }

            //System.out.println("Total Entries in Column: " + totalEntries);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return totalEntries;
    }

    //Function to find the total entries from adds - needs better explanation
    public int countTotalEntries(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading total entries from ads.");
        String filePath = fph.getServerPath();
        int totalEntries = 0;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            nextLine = reader.readNext();
            // Read each line from the CSV file
            while ((nextLine = reader.readNext()) != null) {
                totalEntries++;
            }

            //System.out.println("Total Entries in Column: " + totalEntries);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return totalEntries;
    }

    //Function to find the average number of pages
    public double countAvgPageViewed(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading average pages viewed.");
        String filePath = fph.getServerPath();
        double avgPages = 0;
        Set<String> uniqueEntries = new HashSet<>();
        double countPages = 0;
        int totalEntries = 0;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {


            String[] line;
            line = reader.readNext(); //skip header
            while ((line = reader.readNext()) != null) {
                countPages+=Double.parseDouble(line[3]);
                totalEntries++;
            }

            int uniqueCount = uniqueEntries.size();
            //System.out.println("Number of unique entries in column " + ": " + uniqueCount);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        DecimalFormat f= new DecimalFormat("##.00");
        avgPages = countPages/totalEntries;
        return Math.round(avgPages * 100) / 100;
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


    //Function to find the gender separation.
    public void loadGenders(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading genders.");
        int males = 0;
        int females = 0;
        int unspec = 0;
        String filePath = fph.getImpressionPath();
        int columnIndex = 2; // Change this to the index of the column you want to read (0-based)

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            // Read each line from the CSV file
            while ((nextLine = reader.readNext()) != null) {
                // Check if the line has enough columns
                if (columnIndex < nextLine.length) {
                    // Get the value of the specified column
                    String columnValue = nextLine[columnIndex];
                    // Increment the total entries
                    if(columnValue.equals("Female")) {
                        females ++;
                    } else if(columnValue.equals("Male")) {
                        males ++;
                    }else unspec++;
                } else {
//                    System.out.println("Column index out of bounds for line: " + String.join(",", nextLine));
                }
            }

//            System.out.println("Females: " + females + "; Males: " + males);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        //errorAlert.hide();

        String maleLabel = "Males: " + males;
        String femaleLabel = "Females: " + females;
        String unsepcLabel = "Unspecified: " + unspec;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data(femaleLabel, females),
                new PieChart.Data(maleLabel, males),
                new PieChart.Data(unsepcLabel, unspec));
        genderGraph.setTitle("Gender Graph");
        genderGraph.setLabelLineLength(20);
        genderGraph.setLabelsVisible(true);
        genderGraph.setData(pieChartData);
    }

    //function to load the age graph.
    public void loadAgeGraph(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading age graph.");
        int below25 = 0;
        int between25to34 = 0;
        int between35to44 = 0;
        int between45to54 = 0;
        int over54 = 0;
        int unspec = 0;
        String filePath = fph.getImpressionPath();
        //String filePath = "src/main/resources/2_week_campaign_2/impression_log.csv";
        int columnIndex = 3; // Change this to the index of the column you want to read (0-based)

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;


            // Read each line from the CSV file
            while ((nextLine = reader.readNext()) != null) {
                // Check if the line has enough columns
                if (columnIndex < nextLine.length) {
                    // Get the value of the specified column
                    String columnValue = nextLine[columnIndex];
                    //System.out.println("Column Value: " + columnValue);
                    //System.out.println(columnValue);
                    // Increment the total entries
                    if(columnValue.equals("<25")) {
                        below25 ++;
                    } else if(columnValue.equals("25-34")) {
                        between25to34 ++;
                    }else if(columnValue.equals("35-44")){
                        between35to44++;
                    } else if(columnValue.equals("45-54")) {
                        between45to54++;
                    }else if(columnValue.equals(">54")){
                        over54++;
                    } else unspec++;
                } else {
                    System.out.println("Column index out of bounds for line: " + String.join(",", nextLine));
                }
            }

            //System.out.println("Females: " + females + "; Males: " + males);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        //errorAlert.hide();
        String under25 = "< 25: " + below25;
        String bet25to34 = "25 - 34: " +between25to34;
        String bet35to44 = "35 - 44: " + between35to44;
        String bet45to54 = "45 - 54: "+ between45to54;
        String ov54 = "> 54: "+over54;
        String unspc = "unspecified: "+unspec;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data(under25, below25),
                new PieChart.Data(bet25to34, between25to34),
                new PieChart.Data(bet35to44, between35to44),
                new PieChart.Data(bet45to54, between45to54),
                new PieChart.Data(ov54, over54),
                new PieChart.Data(unspc, unspec));
        //genderGraph.setTitle("Gender Graph");
        ageGraph.setLabelLineLength(20);
        ageGraph.setLabelsVisible(true);
        ageGraph.setData(pieChartData);
    }

    //Load income graph
    public void loadIncomeGraph(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading income graph.");
        int lowInc = 0;
        int midInc = 0;
        int highInc = 0;
        int unspec = 0;
        String filePath = fph.getImpressionPath();
        //String filePath = "src/main/resources/2_week_campaign_2/impression_log.csv";
        int columnIndex = 4; // Change this to the index of the column you want to read (0-based)

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;


            // Read each line from the CSV file
            while ((nextLine = reader.readNext()) != null) {
                // Check if the line has enough columns
                if (columnIndex < nextLine.length) {
                    // Get the value of the specified column
                    String columnValue = nextLine[columnIndex];
                    //System.out.println("Column Value: " + columnValue);
                    //System.out.println(columnValue);
                    // Increment the total entries
                    if(columnValue.equals("Low")) {
                        lowInc ++;
                    } else if(columnValue.equals("Medium")) {
                        midInc ++;
                    }else if(columnValue.equals("High")){
                        highInc++;
                    } else unspec++;
                } else {
                    System.out.println("Column index out of bounds for line: " + String.join(",", nextLine));
                }
            }

            //System.out.println("Females: " + females + "; Males: " + males);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        //errorAlert.hide();
        String lowIncome = "Low: " + lowInc;
        String mediumIncome = "Medium: " +midInc;
        String highIncome = "High: " + highInc;
        String unspc = "unspecified: "+unspec;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data(lowIncome, lowInc),
                new PieChart.Data(mediumIncome, midInc),
                new PieChart.Data(highIncome, highInc),
                new PieChart.Data(unspc, unspec));
        incomeGraph.setLabelLineLength(20);
        incomeGraph.setLabelsVisible(true);
        incomeGraph.setData(pieChartData);
    }

    //Display tutorial overlay
    public void loadTutorial(ActionEvent actionEvent) {
        tutPNG.setVisible(true);
        tutorialOFF.setVisible(true);
    }

    //Disable tutorial overlay
    public void disableTutPNG(ActionEvent actionEvent) {
        tutorialOFF.setVisible(false);
        uploadPNG.setVisible(false);
        tutPNG.setVisible(false);
    }

    //Open dialogue box for opening files
    public void openCampaign(ActionEvent actionEvent) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        String selectedFile;
        //System.out.println(selectedFile);
        FileChooser fc = new FileChooser();
        String [] paths = {"Click Log File", "Impression log file", "Server Log File"};
        for (int i = 0; i < 3; i++) {
            //paths[i] = fc.main();
            //fileChooser.setTitle(paths[i]);
            //fc.main();
            selectedFile = fc.main();
            paths[i] = selectedFile;
//            System.out.println(selectedFile);
        }
        System.out.println(paths[0]);
        fph.setClickPath(paths[0]);
        fph.setImpressionPath(paths[1]);
        fph.setServerPath(paths[2]);
    }

    public void openOnlineDocumentation(ActionEvent actionEvent) throws IOException {
        java.awt.Desktop.getDesktop().browse(URI.create("https://nikolaparushev2003.wixsite.com/ecs-adda/documentation"));
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
            String currentDirectory = System.getProperty("user.dir");

            // Define the relative path to your stylesheet
            String stylesheetPath = "file:///" + currentDirectory.replace("\\", "/") + "/comp2211/seg/src/main/java/com/application/dashboard/lighttheme.css";
            System.out.println(currentDirectory);

            background.getStylesheets().setAll(stylesheetPath);
            light = true;
            dark = false;
            System.out.println("light theme");
        }
    }

    public void enableDarkTheme(ActionEvent actionEvent) throws MalformedURLException {
        if(dark == false){
            String currentDirectory = System.getProperty("user.dir");

            // Define the relative path to your stylesheet
            String stylesheetPath = "file:///" + currentDirectory.replace("\\", "/") + "/comp2211/seg/src/main/java/com/application/dashboard/darktheme.css";
            System.out.println(currentDirectory);

            background.getStylesheets().setAll(stylesheetPath);
            dark = true;
            light = false;
            System.out.println("dark theme");

        }
    }
}
