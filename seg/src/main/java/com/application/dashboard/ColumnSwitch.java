package com.application.dashboard;


import java.io.*;
import java.util.*;

public class ColumnSwitch {
    public static void switchColumns() {
        String inputFile = "/2_week_campaign_2/server_log.csv"; // Input file path
        String outputFile = "/2_week_campaign_2/output.csv"; // Output file path

        InputStream inputStream = ColumnSwitch.class.getResourceAsStream(inputFile);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Split the line into columns
                if (parts.length >= 2) {
                    // Swap the 1st and 2nd columns
                    String temp = parts[0];
                    parts[0] = parts[1];
                    parts[1] = temp;
                }
                // Write the modified line to the output file
                bw.write(String.join(",", parts));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the path to the output file
    }
}


