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
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @TODO
 *   1. Implement CSV input
 *   2. Display CSV info
 * */

public class DashboardController {
    public MenuItem logoutMenuItem;
    public ChartFrame chartCSV;
    public Label test;
    public Label uniqueImpressionLabel;
    public PieChart genderGraph;
    public Slider sliderTimeLabel;
    public Label sumImpressionsLabel;
    public Label timeFrameLabel;
    public AreaChart dataChart;
    public VBox filterSelection;
    public VBox dataSelection;
    public Button loadCSVbutton;
    private FilePathHandler fph = new FilePathHandler();
    public ImageView tutPNG;
    public Button tutorialOFF;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger;


    public void loadCSV(ActionEvent actionEvent) {
        logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "loadCSV button");
        loadingBar();
        Graphs gg = new Graphs();
        //gg.createGraph("TotalClicks","hour");

//        GraphGenerator ggg = new GraphGenerator();
//        ggg.generateGraph();


//        TimeFrameControl tfc = new TimeFrameControl();
//        tfc.createTimeFrame();
        uniqueImpressionLabel.setText("Unique Impressions: " + countUniques());
        sumImpressionsLabel.setText("Total impressions: " + countTotalImpressions());
        loadGenders();

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
            logger = Logger.getLogger(getClass().getName());logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    public void loadGraph(){
        //dataChart.set
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

    public int countTotalImpressions(){
        Logger logger = Logger.getLogger(DashboardController.class.getName());
        logger.log(Level.ALL, "Loading Total Impressions");
        String filePath = fph.getImpressionPath(); // Nikola - PC
        int columnIndex = 2; // Change this to the index of the column you want to read (0-based)
        int totalEntries = 0;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;

            // Read each line from the CSV file
            while ((nextLine = reader.readNext()) != null) {
                // Check if the line has enough columns
//                if (columnIndex < nextLine.length) {
//                    // Get the value of the specified column
//                    String columnValue = nextLine[columnIndex];
//                    //System.out.println("Column Value: " + columnValue);
//
//                    // Increment the total entries
//
//                } else {
//                    System.out.println("Column index out of bounds for line: " + String.join(",", nextLine));
//                }
                totalEntries++;
            }

            System.out.println("Total Entries in Column: " + totalEntries);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return totalEntries;
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
