package com.application.dashboard;

import java.io.*;
import com.application.dashboard.ReadFile;

/**
 * This is a test file. It should be ignored.
 * */
public class testFile {
    public static void main(String[] args) throws FileNotFoundException {
        // Assuming the project structure is like: src/main/resources/2_week_campaign_2/merge.csv
        String filename = "D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\impression_log.csv";
        final File f = new File(testFile.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        ClassLoader loader = ColumnSwitch.class.getClassLoader();
        System.out.println(f);

        ReadFile rf = new ReadFile();
        System.out.println(rf.readCSV(filename).size());
    }
}
