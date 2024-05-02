package com.application.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    static final int NUM_THREADS = 50; // Adjust according to your requirements

    public static void main(ArrayList<String> path) throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);

        config.setMaximumPoolSize(1500);
        config.setConnectionTimeout(300000);
        config.setIdleTimeout(120000);
        config.setLeakDetectionThreshold(300000);
        config.setMaxLifetime(60000);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("userServerPrepStmts",true);

        HikariDataSource dataSource = new HikariDataSource(config);
//        String csvFilePath = path;
//        String csvFilePath = path;
        ArrayList<String> paths = new ArrayList<>();
        paths = path;
        //8_828_248
//        String csvFilePath = "D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\server_log.csv";
        //486_104
        CountDownLatch latch = new CountDownLatch(paths.size() * NUM_THREADS);

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
                    if (batchLines.size() >= 100000) {
                        executor.submit(new InsertTask(new ArrayList<>(batchLines), dataSource, latch));
                        // Clear batchLines after submitting task
                        batchLines.clear();

                        count++;
                    }

                }
                // Submit the remaining lines as a final task
                if (!batchLines.isEmpty()) {
                    executor.submit(new InsertTask(new ArrayList<>(batchLines), dataSource, latch));
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
//        dataSource.close();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("END");
        dataSource.close();
    }


    static class InsertTask implements Runnable {
        private final List<String> batchLines;
        private final HikariDataSource dataSource;
        private final CountDownLatch latch;

        InsertTask(List<String> batchLines, HikariDataSource dataSource, CountDownLatch latch) {
            this.batchLines = batchLines;
            this.dataSource = dataSource;
            this.latch = latch;

        }

        @Override
        public void run() {
            try (Connection conn = dataSource.getConnection()) {

                String insertQuery = "INSERT INTO serverlog(entryDate, id, exitDate, pagesViewed, conversion) VALUES (?,?,?,?,?)";


                try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
//                    System.out.println("Inserting batch of " + batchLines.size() + " lines");

                    for (String dataLine : batchLines) {
                        String[] values = dataLine.split(",");
//                        System.out.println(values[0]);
                        if(      ((values[0].equals("Entry Date")
                                && values[1].equals("ID")
                                && values[2].equals("Exit Date")
                                && values[3].equals("Pages Viewed"))
                                && values[4].equals("Conversion")) ) {
//                            System.out.println(values[0] + "<<<");

                        }else{
                            if ("n/a".equals(values[0].toLowerCase())) {
                                preparedStatement.setNull(1, Types.NULL);
                                preparedStatement.setString(2, values[1]);
                                preparedStatement.setString(3, values[2]);
                                preparedStatement.setInt(4, Integer.parseInt(values[3]));
                                preparedStatement.setString(5, values[4]);
                                System.out.println("inserting null at 1!");


                            } else if ("n/a".equals(values[2].toLowerCase())) {
                                preparedStatement.setString(1, values[0]);
                                preparedStatement.setString(2, values[1]);
                                preparedStatement.setNull(3, Types.NULL);
                                preparedStatement.setInt(4, Integer.parseInt(values[3]));
                                preparedStatement.setString(5, values[4]);
                                System.out.println("inserting null at 2");


                            } else {
                                preparedStatement.setString(1, values[0]);
                                preparedStatement.setString(2, values[1]);
                                preparedStatement.setString(3, values[2]);
                                preparedStatement.setInt(4, Integer.parseInt(values[3]));
                                preparedStatement.setString(5, values[4]);
//                                System.out.println("INSERTED SERVER!");

                            }
                            preparedStatement.addBatch();

                        }
                    }
                    preparedStatement.executeBatch();
//                    conn.close();

                }
                latch.countDown();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}