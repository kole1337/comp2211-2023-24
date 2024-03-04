package com.application.dashboard;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Merge {
    public static String main(String[] args) {
        String file1 = "/2_week_campaign_2/merged.csv"; // Path to the first CSV file
        InputStream inputStream = testFile.class.getResourceAsStream(file1);

        ColumnSwitch.switchColumns(); // Path to the second CSV file

        String file2 = "/2_week_campaign_2/output.csv";
        InputStream inputStream2 = testFile.class.getResourceAsStream(file2);

        String outputFile = "/2_week_campaign_2/merge.csv"; // Output file path for merged CSV


        // Read data from the first CSV file
        Map<String, String[]> data1 = readCSV(inputStream);
        //System.out.println(data1);

        // Read data from the second CSV file
        Map<String, String[]> data2 = readCSV2(inputStream2);

        // Merge the data based on the common ID column
        List<String[]> mergedData = mergeData(data1, data2);

        // Get the headings from the first row of the merged data
        //String[] headings = mergedData.isEmpty() ? new String[0] : mergedData.get(0);

        // Write the merged data into a new CSV file with headings
        writeCSV(outputFile, mergedData);
        return outputFile;
    }

    // Read data from a CSV file and store it in a map with the ID column as key
    private static Map<String, String[]> readCSV(InputStream streamPath) {
        Map<String, String[]> data = new HashMap<>();
        //        try (BufferedReader reader = new BufferedReader(new InputStreamReader(streamPath))) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(streamPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Assuming CSV format is comma-separated
                String id = parts[1]; // Assuming ID is in the second column
                data.put(id, parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static Map<String, String[]> readCSV2(InputStream streamPath) {
        Map<String, String[]> data = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(streamPath))) {
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
            // Write headings
            // writer.println(String.join(",", headings));
            // Write data rows
            for (String[] row : data) {
                writer.println(String.join(",", row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
