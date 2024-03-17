package com.application.dashboard;


import com.application.files.FileChooserWindow;
import com.application.files.FileChooserExample;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadFile extends Application {
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //String file1 = "/2_week_campaign_2/click_log.csv"; // Path to the first CSV file
        Stage stage = new Stage();
        FileChooserExample fileChooserExample = new FileChooserExample();
        fileChooserExample.start(null);
        //  FileChooserExample fileChooserExample = new FileChooserExample();
        //fileChooserExample.start(null); // Start the JavaFX application without a stage

        String file1 = fileChooserExample.getSelectedFile();
        fileChooserExample.start(null);
        String file2 = fileChooserExample.getSelectedFile();
        String file3 = MergeCSV.main(file1, file2);
        fileChooserExample.start(null);
        String file5 = fileChooserExample.getSelectedFile();
        // ColumnSwitch.switchColumns(file5);

        String file4 = ColumnSwitch.switchColumns(file5);
        String outputFile = "seg/src/main/resources/2_week_campaign_2/merge.csv";

        // Read data from the first CSV file
        Map<String, String[]> data1 = readCSV(file3);

        // Read data from the second CSV file
        Map<String, String[]> data2 = readCSV2(file4);

        // Merge the data based on the common ID column
        List<String[]> mergedData = mergeData(data1, data2);

        // Write the merged data into a new CSV file with headings
        writeCSV(outputFile, mergedData);
    }



    // Read data from a CSV file and store it in a map with the ID column as key
    static Map<String, String[]> readCSV(String streamPath) {
        Map<String, String[]> data = new HashMap<>();
        int gesh = 0;
        //        try (BufferedReader reader = new BufferedReader(new InputStreamReader(streamPath))) {
        try (BufferedReader br = new BufferedReader(new FileReader(streamPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Assuming CSV format is comma-separated
                String id = parts[1]; // Assuming ID is in the second column
                data.put(id, parts);
                gesh++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(gesh);
        return data;
    }

    private static Map<String, String[]> readCSV2(String streamPath) {
        Map<String, String[]> data = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(streamPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Assuming CSV format is comma-separated
                String id = parts[0]; // Assuming ID is in the second column
                data.put(id, parts);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Merge data from two maps based on a common key (ID)
    private static List<String[]> mergeData(Map<String, String[]> data1, Map<String, String[]> data2) {
        List<String[]> mergedData = new ArrayList<>();
        for (String id : data1.keySet()) {
            if (data2.containsKey(id)) {
                String[] row1 = data1.get(id);
                String[] row2 = data2.get(id);
                // Calculate the length of the merged row
                int mergedRowLength = row1.length + row2.length - 1; // -1 to skip duplicate ID
                String[] mergedRow = new String[mergedRowLength];
                // Copy elements from row1 to mergedRow
                System.arraycopy(row1, 0, mergedRow, 0, row1.length);
                // Copy elements from row2 to mergedRow, skipping the ID column
                System.arraycopy(row2, 1, mergedRow, row1.length, row2.length - 1); // Skip the ID column
                mergedData.add(mergedRow);
            }
        }
        return mergedData;
    }

    private static List<List<String>> transposeData(List<String[]> data) {
        List<List<String>> columns = new ArrayList<>();
        int numRows = data.size();
        int numCols = data.get(0).length;
        for (int col = 0; col < numCols; col++) {
            List<String> column = new ArrayList<>();
            for (int row = 0; row < numRows; row++) {
                column.add(data.get(row)[col]);
            }
            columns.add(column);
        }
        return columns;
    }


    // Write data to a CSV file with headings
    private static void writeCSV(String filename, List<String[]> data) {
        List<List<String>> columns = transposeData(data);


        // Write each column to a separate ArrayList
        List<ArrayList<String>> columnLists = new ArrayList<>();
        for (List<String> column : columns) {
            columnLists.add(new ArrayList<>(column));
        }
        System.out.println(columnLists);
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String[] row : data) {
                writer.println(String.join(",", row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    }
