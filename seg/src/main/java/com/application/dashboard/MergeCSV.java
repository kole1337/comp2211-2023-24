package com.application.dashboard;

import java.io.*;
import java.util.*;

public class MergeCSV {
    public static String main(String file1,String file2) {
        //String file1 = "C:\\Users\\gouri\\OneDrive - University of Southampton\\Documents\\click_log.csv"; // Path to the first CSV file
        //String file2 = "C:\\Users\\gouri\\OneDrive - University of Southampton\\Documents\\year2\\impression_log.csv"; // Path to the second CSV file
        String outputFile = "C:\\Users\\gouri\\OneDrive - University of Southampton\\Documents\\year2\\merged.csv"; // Output file path for merged CSV





                // Read data from the first CSV file
                Map<String, String[]> data1 = readCSV(file1);
                //System.out.println(data1);

                // Read data from the second CSV file
                Map<String, String[]> data2 = readCSV(file2);

                // Merge the data based on the common ID column
                List<String[]> mergedData = mergeData(data1, data2);

                // Get the headings from the first row of the merged data
                //String[] headings = mergedData.isEmpty() ? new String[0] : mergedData.get(0);

                // Write the merged data into a new CSV file with headings
                writeCSV(outputFile, mergedData);
                return outputFile;
            }

            // Read data from a CSV file and store it in a map with the ID column as key
            private static Map<String, String[]> readCSV(String filename) {
                Map<String, String[]> data = new HashMap<>();
                try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
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

            // Merge data from two maps based on a common key (ID)
            private static List<String[]> mergeData(Map<String, String[]> data1, Map<String, String[]> data2) {
                List<String[]> mergedData = new ArrayList<>();
                for (String id : data1.keySet()) {
                    if (data2.containsKey(id)) {
                        String[] row1 = data1.get(id);
                        String[] row2 = data2.get(id);
                        // Calculate the length of the merged row
                        int mergedRowLength = row1.length + row2.length - 2; // -1 to skip duplicate ID
                        String[] mergedRow = new String[mergedRowLength];
                        // Copy elements from row1 to mergedRow
                        System.arraycopy(row1, 0, mergedRow, 0, row1.length);
                        // Copy elements from row2 to mergedRow, skipping the ID column
                        System.arraycopy(row2, 2, mergedRow, row1.length, row2.length - 2); // Skip the ID column
                        mergedData.add(mergedRow);
                    }
                }
                return mergedData;
            }


    // Write data to a CSV file with headings
            private static void writeCSV(String filename, List<String[]> data) {
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
