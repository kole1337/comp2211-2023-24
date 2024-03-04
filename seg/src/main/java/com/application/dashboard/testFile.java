package com.application.dashboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class testFile {
    public static void main(String[] args) {
        // Get the resource file path
        String filePath = "/2_week_campaign_2/click_log.csv"; // Assuming the file is directly in the resources folder

        // Open the file as an InputStream
        InputStream inputStream = testFile.class.getResourceAsStream(filePath);

        if (inputStream != null) {
            // Wrap the InputStream in a BufferedReader for efficient reading
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                // Read and process the contents of the CSV file
                String line;
                while ((line = reader.readLine()) != null) {
                    // Process the CSV line as needed
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // Close the InputStream
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("File not found: " + filePath);
        }
    }
}
