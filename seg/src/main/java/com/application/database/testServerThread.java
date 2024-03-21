package com.application.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class testServerThread {
    // Database credentials
    static final String JDBC_URL = DbConnection.getUrl();
    static final String DB_USER = DbConnection.getUser();
    static final String DB_PASSWORD = DbConnection.getPass();

    private static Logger logger = Logger.getLogger(Multithread_ImpressionDb.class.getName());
    private static Connection conn;

    // Define the number of threads
    static final int NUM_THREADS = 250; // Adjust according to your requirements

    public static void main(String path) throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(2500);
        config.setConnectionTimeout(300000);
        config.setIdleTimeout(120000);
        config.setLeakDetectionThreshold(300000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        DataSource dataSource = new HikariDataSource(config);
        conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
        String csvFilePath = path;
//        String csvFilePath = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\2month\\impression_log.csv";
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
//                String insertQuery = "INSERT INTO impressionlog(date, id, gender, age, income, context, impression_cost) VALUES (?,?,?,?,?,?,?)";
//                String insertQuery = "INSERT INTO clicklog (date, id, clickcost) VALUES (?, ?, ?)";
                String insertQuery = "INSERT INTO serverlog(entryDate, id, exitDate, pagesViewed, conversion) VALUES (?,?,?,?,?)";



                try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
                    // Parse dataLine and set values for the prepared statement
                    System.out.println("Inserting Server");
                    String[] values = dataLine.split(","); // Assuming CSV data is comma-separated
                    if("n/a".equals(values[0].toLowerCase())) {
                        preparedStatement.setString(1, null);
                        preparedStatement.setString(2, values[1]);
                        preparedStatement.setString(3, values[2]);
                        preparedStatement.setInt(4, Integer.parseInt(values[3]));
                        preparedStatement.setString(5, values[4]);
                        System.out.println("INSERTED SERVER!");



                    }else if("n/a".equals(values[2].toLowerCase())) {
                        preparedStatement.setString(1, values[0]);
                        preparedStatement.setString(2, values[1]);
                        preparedStatement.setString(3, null);
                        preparedStatement.setInt(4, Integer.parseInt(values[3]));
                        preparedStatement.setString(5, values[4]);
                        System.out.println("INSERTED SERVER!");


                    }else{
                        preparedStatement.setString(1, values[0]);
                        preparedStatement.setString(2, values[1]);
                        preparedStatement.setString(3, values[2]);
                        preparedStatement.setInt(4, Integer.parseInt(values[3]));
                        preparedStatement.setString(5, values[4]);
                        System.out.println("INSERTED SERVER!");


                    }
                    preparedStatement.executeUpdate();
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}