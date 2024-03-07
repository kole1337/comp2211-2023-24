package com.application.dashboard;

import com.application.files.FileChooser;
import com.application.files.FilePathHandler;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;

import org.jfree.chart.ChartFrame;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @TODO
 *   1. Implement CSV input
 *   2. Display CSV info
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
    private FilePathHandler fph = new FilePathHandler();
    public ImageView tutPNG;
    public Button tutorialOFF;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger;

    /*
     * @TODO:
     *   1. Total clicks
     *   2. Average price per click
     *   3. Zero cost click
     *   4. Total Entries
     *   5. Average pages viewed:
     *   6. Age graph
     *   7. Gender graph - fixes
     *   8. Income graph
     *   9. Context origin
     *   10. Conversion graph
     * */


    public void loadCSV(ActionEvent actionEvent) {
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "loadCSV button");
        loadingBar();
        Graphs gg = new Graphs(fph);
        //gg.main();
//        TimeFrameControl tfc = new TimeFrameControl();
//        tfc.createTimeFrame();
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
//        sumImpressionsLabel.setVisible(true);
//        uniqueImpressionLabel.setVisible(true);
//        dataSelection.setVisible(true);
//        filterSelection.setVisible(true);
//        sliderTimeLabel.setVisible(true);
//        timeFrameLabel.setVisible(true);
//        dataChart.setVisible(true);
//        loadCSVbutton.setVisible(false);
    }
    //Logout function for button
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

    public void loadGraph(){
        logger = Logger.getLogger(getClass().getName());
        logger.log(Level.INFO, "Creating data graph");

        //Defining the X axis
        CategoryAxis xAxis = new CategoryAxis();

        //defining the y Axis
        NumberAxis yAxis = new NumberAxis(0, 15, 2.5);
        yAxis.setLabel("Fruit units");

        //Creating the Area chart
        AreaChart<String, Number> areaChart = new AreaChart(xAxis, yAxis);
        areaChart.setTitle("Average fruit consumption during one week");

        //Prepare XYChart.Series objects by setting data
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("John");
        series1.getData().add(new XYChart.Data("Monday", 3));
        series1.getData().add(new XYChart.Data("Tuesday", 4));
        series1.getData().add(new XYChart.Data("Wednesday", 3));
        series1.getData().add(new XYChart.Data("Thursday", 5));
        series1.getData().add(new XYChart.Data("Friday", 4));
        series1.getData().add(new XYChart.Data("Saturday", 10));
        series1.getData().add(new XYChart.Data("Sunday", 12));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Jane");
        series2.getData().add(new XYChart.Data("Monday", 1));
        series2.getData().add(new XYChart.Data("Tuesday", 3));
        series2.getData().add(new XYChart.Data("Wednesday", 4));
        series2.getData().add(new XYChart.Data("Thursday", 3));
        series2.getData().add(new XYChart.Data("Friday", 3));
        series2.getData().add(new XYChart.Data("Saturday", 5));
        series2.getData().add(new XYChart.Data("Sunday", 4));

        //Setting the XYChart.Series objects to area chart
        areaChart.getData().addAll(series1,series2);
        dataChart = areaChart;
        logger.log(Level.INFO, "Finish creating");
    }

    public int countUniques(){
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading Uniques");
        String filePath = fph.getImpressionPath(); // Nikola - PC
        String csvFilePath = filePath;
        int columnIndexToCount = 1; // Change this to the index of the column you want to count (0-based index)
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
            System.out.println("Number of unique entries in column " + columnIndexToCount + ": " + uniqueCount);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return uniqueEntries.size();
    }

    public int countZeroCostClick(){
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading Uniques");
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
            System.out.println("Number of unique entries in column " + columnIndexToCount + ": " + uniqueCount);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return totalZeros;
    }

    public double countAveragePricePerClick(){
        double average = 0;
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading Uniques");
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

            System.out.println("Number of unique entries in column " + columnIndexToCount + ": ");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return totalPrice/countTotalClicks();
    }

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

            System.out.println("Total Entries in Column: " + totalEntries);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return totalEntries;
    }

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

            System.out.println("Total Entries in Column: " + totalEntries);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return totalEntries;
    }
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

            System.out.println("Total Entries in Column: " + totalEntries);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return totalEntries;
    }

    public double countAvgPageViewed(){
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading total entries from ads.");
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
            System.out.println("Number of unique entries in column " + ": " + uniqueCount);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        DecimalFormat f= new DecimalFormat("##.00");
        avgPages = countPages/totalEntries;
        return Math.round(avgPages * 100) / 100;
    }

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

    public void loadGenders(){
        int males = 0;
        int females = 0;
        int unspec = 0;
        String filePath = fph.getImpressionPath();
        //String filePath = "src/main/resources/2_week_campaign_2/impression_log.csv";
        int columnIndex = 2; // Change this to the index of the column you want to read (0-based)

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
                    if(columnValue.equals("Female")) {
                        females ++;
                    } else if(columnValue.equals("Male")) {
                        males ++;
                    }else unspec++;
                } else {
                    System.out.println("Column index out of bounds for line: " + String.join(",", nextLine));
                }
            }

            System.out.println("Females: " + females + "; Males: " + males);
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

    public void loadAgeGraph(){
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

    public void loadIncomeGraph(){
        int lowInc = 0;
        int midInc = 0;
        int highInc = 0;
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

    public void loadTutorial(ActionEvent actionEvent) {
        tutPNG.setVisible(true);
        tutorialOFF.setVisible(true);

    }

    public void disableTutPNG(ActionEvent actionEvent) {
        tutorialOFF.setVisible(false);
        tutPNG.setVisible(false);
    }

    public void openCampaign(ActionEvent actionEvent) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        File selectedFile;
        //System.out.println(selectedFile);
//        FileChooser fc = new FileChooser();
        String [] paths = {"Click Log File", "Impression log file", "Server Log File"};
        for (int i = 0; i < 3; i++) {
            //paths[i] = fc.main();
            fileChooser.setTitle(paths[i]);
            selectedFile = fileChooser.showOpenDialog(stage);
            paths[i] = selectedFile.getAbsolutePath();
            System.out.println(selectedFile);
        }
        fph.setClickPath(paths[0]);
        fph.setImpressionPath(paths[1]);
        fph.setServerPath(paths[2]);
    }
}
