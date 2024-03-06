package com.application.dashboard;

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
import javafx.scene.shape.Arc;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.jfree.chart.ChartFrame;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    public FilePathHandler filePathHandler = new FilePathHandler();
    public ImageView tutPNG;
    public Button tutorialOFF;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger;

    public void logoutAction(ActionEvent event) {
        //this should be the logout function from the menu item Logout
//        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//        errorAlert.setHeaderText("Problem");
//        errorAlert.setContentText("Wrong credentials");
//        errorAlert.showAndWait();
        logoutButton(new ActionEvent());
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

    public void loadCSV(ActionEvent actionEvent) {
        loadingBar();
        Graphs gg = new Graphs();
        gg.createGraph("TotalClicks","hour");

//        GraphGenerator ggg = new GraphGenerator();
//        chartCSV = ggg.getFrame();
        TimeFrameControl tfc = new TimeFrameControl();
        tfc.createTimeFrame();
        uniqueImpressionLabel.setText("UniqueImpressions: " + countUniques());
        loadGenders();
    }

    public int countUniques(){
//        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
//        errorAlert.setHeaderText("Loading");
//        errorAlert.setContentText("Loading");
//        errorAlert.show();

        String filePath = "D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\impression_log.csv"; // Nikola - PC
        //String filePath = "src/main/resources/2_week_campaign_2/impression_log.csv";
        int columnIndex = 2; // Change this to the index of the column you want to read (0-based)
        int totalEntries = 0;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;


            // Read each line from the CSV file
            while ((nextLine = reader.readNext()) != null) {
                // Check if the line has enough columns
                if (columnIndex < nextLine.length) {
                    // Get the value of the specified column
                    String columnValue = nextLine[columnIndex];
                    //System.out.println("Column Value: " + columnValue);

                    // Increment the total entries
                    totalEntries++;
                } else {
                    System.out.println("Column index out of bounds for line: " + String.join(",", nextLine));
                }
            }

            System.out.println("Total Entries in Column: " + totalEntries);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        //errorAlert.hide();
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
        String filePath = "D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\impression_log.csv"; // Nikola - PC
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
                    System.out.println(columnValue);
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
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        System.out.println(selectedFile);
    }
}
