package com.application.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class testClickThread {
    // Database credentials
    static final String JDBC_URL = DbConnection.getUrl();
    static final String DB_USER = DbConnection.getUser();
    static final String DB_PASSWORD = DbConnection.getPass();

    private static Logger logger = Logger.getLogger(testClickThread.class.getName());

    // Define the number of threads
    static final int NUM_THREADS = 150; // Adjust according to your requirements


    public static void main(ArrayList<String> path) throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(150);
        config.setConnectionTimeout(300000);
        config.setIdleTimeout(120000);
        config.setLeakDetectionThreshold(300000);

        DataSource dataSource = new HikariDataSource(config);

        ArrayList<String> paths = new ArrayList<>();
        paths = path;

//        String csvPath = "D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\click_log.csv";

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        int count = 0;
        for(String p : paths) {
            try (BufferedReader br = new BufferedReader(new FileReader(p))) {
                String line;

                List<String> batchLines = new ArrayList<>();

                while ((line = br.readLine()) != null) {
                    // Add line to batchLines
                    batchLines.add(line);

                    // If batchLines size reaches a certain threshold, submit task to the thread pool
                    if (batchLines.size() >= 1_000) {
                        executor.submit(new InsertTask(new ArrayList<>(batchLines), dataSource));
                        // Clear batchLines after submitting task
                        batchLines.clear();
                        count++;
                    }

                }
                // Submit the remaining lines as a final task
                if (!batchLines.isEmpty()) {
                    executor.submit(new InsertTask(new ArrayList<>(batchLines), dataSource));
                    count++;
                    System.out.println(batchLines.size());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Shutdown the executor after all tasks are completed
        System.out.println("count: " + count);
        executor.shutdown();

    }


    static class InsertTask implements Runnable {
        private final List<String> batchLines;
        private final DataSource dataSource;

        InsertTask(List<String> batchLines, DataSource dataSource) {
            this.batchLines = batchLines;
            this.dataSource = dataSource;
        }

        @Override
        public void run() {
            try (Connection conn = dataSource.getConnection()) {
                String insertQuery = "INSERT INTO clicklog (date, id, clickcost) VALUES (?, ?, ?)";

                try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
                    System.out.println("Inserting batch of " + batchLines.size() + " lines");

                    for (String dataLine : batchLines) {

                        String[] values = dataLine.split(","); // Assuming CSV data is comma-separated

                        if (values[0].equals("Date")
                                && values[1].equals("ID")
                                && values[2].equals("Click Cost")){

                            System.out.println(values[0]);


                        }else{
                            preparedStatement.setString(1, values[0]);
                            preparedStatement.setString(2, values[1]);
                            preparedStatement.setDouble(3, Double.parseDouble(values[2]));

                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}