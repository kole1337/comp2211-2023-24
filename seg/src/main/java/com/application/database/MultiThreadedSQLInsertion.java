package com.application.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class MultiThreadedSQLInsertion {
    // Database credentials
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/adda";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "jojo12345";

    private static Connection conn;

    // Define the number of threads
    static final int NUM_THREADS = 500; // Adjust according to your requirements

    public static void main(String[] args) throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(100);
        config.setConnectionTimeout(300000);
        config.setIdleTimeout(120000);
        config.setLeakDetectionThreshold(300000);

        DataSource dataSource = new HikariDataSource(config);

        conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
//        String csvFilePath ="D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\impression_log.csv";
        String csvFilePath = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\2month\\impression_log.csv";
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Submit tasks to the thread pool
                executor.submit(new InsertTask(line, dataSource));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Shutdown the executor after all tasks are completed
        executor.shutdown();
    }

    static class InsertTask implements Runnable {
        private final String dataLine;
        private final DataSource dataSource;

        InsertTask(String dataLine, DataSource dataSource) {
            this.dataLine = dataLine;
            this.dataSource = dataSource;
        }

        @Override
        public void run() {
            try(Connection conn = dataSource.getConnection()){
                String insertQuery = "INSERT INTO impressionlog(date, id, gender, age, income, context, impression_cost) VALUES (?,?,?,?,?,?,?)";
//                String insertQuery = "INSERT INTO clicklog (date, id, clickcost) VALUES (?, ?, ?)";
//                String insertQuery = "INSERT INTO clicklog (date, id, clickcost) VALUES (?, ?, ?)";
//
//                try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
//                    // Parse dataLine and set values for the prepared statement
//                    System.out.println("Inserting");
//                    String[] values = dataLine.split(","); // Assuming CSV data is comma-separated
//                    preparedStatement.setString(1, values[0]);
//                    preparedStatement.setString(2, values[1]);
//                    preparedStatement.setDouble(3, Double.parseDouble(values[2]));
//
//                    preparedStatement.executeUpdate();
//                }

                try(PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)){
                    System.out.println("Inserting");
                    String[] values = dataLine.split(","); // Assuming CSV data is comma-separated
                    System.out.println(values[0]);
                    preparedStatement.setString(1, values[0]);
                    preparedStatement.setString(2, values[1]);
                    preparedStatement.setString(3, values[2]);
                    preparedStatement.setString(4, values[3]);
                    preparedStatement.setString(5, values[4]);
                    preparedStatement.setString(6, values[5]);
                    preparedStatement.setDouble(7, Double.parseDouble(values[6]));

                    preparedStatement.executeUpdate();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}