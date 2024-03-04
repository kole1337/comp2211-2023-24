package com.application.dashboard;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

public class DatasetCreator {
    String clicksCsv;
    String impressionsCsv;
    String serverCsv;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime startTime = LocalDateTime.parse("2015-01-01 00:00:00", dateFormatter);
    LocalDateTime endTime = LocalDateTime.parse("2015-01-30 00:00:00", dateFormatter);

    public DatasetCreator() {
        this.clicksCsv = "seg/src/main/resources/2_week_campaign_2/click_log.csv";
        this.impressionsCsv = "seg/src/main/resources/2_week_campaign_2/impression_log.csv";
        this.serverCsv = "seg/src/main/resources/2_week_campaign_2/server_log.csv";

    }

    public Map<LocalDateTime, Integer> createDataset(String graphName, String time) {
        if (graphName.equals("TotalClicks")) {
            return createCountByTimeDataset(clicksCsv, time);
        } else if (graphName.equals("TotalImpressions")) {
            return createCountByTimeDataset(impressionsCsv, time);
        } else if (graphName.equals("TotalUniques")) {
            return createUniqueClicksDataset(time);
        } else {
            return null;
        }
    }

    private Map<LocalDateTime, Integer> createCountByTimeDataset(String csvFile, String time) {
        Map<LocalDateTime, Integer> countByTime = new TreeMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            reader.readNext();
            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                String dateString = record[0];
                LocalDateTime date = LocalDateTime.parse(dateString, dateFormatter);
                if (date.isAfter(startTime) && date.isBefore(endTime)) {
                    LocalDateTime roundedDate;
                    if (time.equals("hour")) {
                        roundedDate = date.withMinute(0).withSecond(0);
                    } else if (time.equals("day")) {
                        roundedDate = date.withHour(0).withMinute(0).withSecond(0);
                    } else if (time.equals("week")) {
                        int week = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                        roundedDate = date.withHour(0).withMinute(0).withSecond(0);
                    } else {
                        System.out.println("Enter a valid time period");
                        return countByTime;
                    }
                    countByTime.put(roundedDate, countByTime.getOrDefault(roundedDate, 0) + 1);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return countByTime;
    }

    private Map<LocalDateTime, Integer> createUniqueClicksDataset(String time) {
        List<String> seen = new ArrayList<>();
        Map<LocalDateTime, Integer> countByTime = new TreeMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(clicksCsv))) {
            reader.readNext();
            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                String dateString = record[0];
                String id = record[1];
                if (!seen.contains(id)) {
                    seen.add(id);
                    LocalDateTime date = LocalDateTime.parse(dateString, dateFormatter);
                    if (date.isAfter(startTime) && date.isBefore(endTime)) {
                        LocalDateTime roundedDate;
                        if (time.equals("hour")) {
                            roundedDate = date.withMinute(0).withSecond(0);
                        } else if (time.equals("day")) {
                            roundedDate = date.withHour(0).withMinute(0).withSecond(0);
                        } else if (time.equals("week")) {
                            int week = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                            roundedDate = date.withHour(0).withMinute(0).withSecond(0);
                        } else {
                            System.out.println("Enter a valid time period");
                            return countByTime;
                        }
                        countByTime.put(roundedDate, countByTime.getOrDefault(roundedDate, 0) + 1);
                    }
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return countByTime;
    }
}
