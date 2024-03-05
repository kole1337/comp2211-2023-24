package com.application.dashboard;

import java.io.*;

public class testFile {
    public static void main(String[] args) throws FileNotFoundException {
        // Assuming the project structure is like: src/main/resources/2_week_campaign_2/merge.csv
        String filename = "src/main/resources/2_week_campaign_2/merge.csv";

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename))))) {
            writer.print("gesh");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
