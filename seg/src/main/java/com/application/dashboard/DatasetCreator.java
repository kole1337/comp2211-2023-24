package com.application.dashboard;

import com.application.files.FilePathHandler;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.jfree.data.time.Hour;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

public class DatasetCreator {
    private FilePathHandler fph = new FilePathHandler();

    String clicksCsv;
    String impressionsCsv;
    String serverCsv;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime startTime = LocalDateTime.parse("2015-01-01 00:00:00", dateFormatter);
    LocalDateTime endTime = LocalDateTime.parse("2015-01-30 00:00:00", dateFormatter);

    public DatasetCreator() {
//        this.clicksCsv = "seg/src/main/resources/2_week_campaign_2/click_log.csv";
//        this.impressionsCsv = "seg/src/main/resources/2_week_campaign_2/impression_log.csv";
//        this.serverCsv = "seg/src/main/resources/2_week_campaign_2/server_log.csv";
        this.clicksCsv = fph.getClickPath();
        this.impressionsCsv = fph.getImpressionPath();
        this.serverCsv = fph.getServerPath();

    }

    public Map<LocalDateTime, Double> createDataset(String graphName, String time) {
        if (graphName.equals("TotalClicks")) {
            return createCountByTimeDataset(clicksCsv, time);
        } else if (graphName.equals("TotalImpressions")) {
            return createCountByTimeDataset(impressionsCsv, time);
        } else if (graphName.equals("TotalUniques")) {
            return createUniqueClicksDataset(time);
        } else if (graphName.equals("Conversions")) {
            return createConversionsDataset(time);
        } else if (graphName.equals("TotalCost")) {
            return createTotalCostDataset(time);
        } else if (graphName.equals("CPC")){
            return createCPCDataset(time);
        } else if (graphName.equals("CTR")){
            return createCTRDataset(time);
        } else if (graphName.equals("CPA")){
            return createCPADataset(time);
        } else if (graphName.equals("CPM")){
            return createCPMDataset(time);
        } else if (graphName.equals("Bounce")){
            return createBounceDataset(time);
        } else if (graphName.equals("BounceRate")){
            return createBounceRateDataset(time);
        }
        else {
            return null;
        }
    }

    private Map<LocalDateTime, Double> createCountByTimeDataset(String csvFile, String time) {
        Map<LocalDateTime, Double> countByTime = new TreeMap<>();
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
                    countByTime.put(roundedDate, countByTime.getOrDefault(roundedDate, 0.0) + 1);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return countByTime;
    }

    private Map<LocalDateTime, Double> createUniqueClicksDataset(String time) {
        List<String> seen = new ArrayList<>();
        Map<LocalDateTime, Double> countByTime = new TreeMap<>();
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
                        countByTime.put(roundedDate, countByTime.getOrDefault(roundedDate, 0.0) + 1);
                    }
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return countByTime;
    }

    private Map<LocalDateTime, Double> createConversionsDataset(String time) {
        Map<LocalDateTime, Double> countByTime = new TreeMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(serverCsv))) {
            reader.readNext();
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                String dateString = record[0];
                String conversion = record[4];
                if (conversion.equals("Yes")) {
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
                        countByTime.put(roundedDate, countByTime.getOrDefault(roundedDate, 0.0) + 1);
                    }
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return countByTime;
    }

    private Map<LocalDateTime, Double> createTotalCostDataset(String time) {
        Map<LocalDateTime, Double> countByTime = new TreeMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(clicksCsv))) {
            reader.readNext();
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                String dateString = record[0];
                String cost = record[2];
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
                    countByTime.put(roundedDate, countByTime.getOrDefault(roundedDate, 0.0) + Double.valueOf(cost));
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return countByTime;
    }

    private Map<LocalDateTime, Double> createCPCDataset(String time) {
        Map<LocalDateTime, Double> totalClicks = createCountByTimeDataset(clicksCsv, time);
        Map<LocalDateTime, Double> totalCost = createTotalCostDataset(time);
        Map<LocalDateTime, Double> averageCPC = new TreeMap<>();
        for (Map.Entry<LocalDateTime, Double> entry : totalClicks.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            Double CPCvalue = totalCost.get(dateTime)/ totalClicks.get(dateTime);
            averageCPC.put(dateTime,averageCPC.getOrDefault(dateTime,0.0) + CPCvalue);
        }
        return averageCPC;
    }
    private Map<LocalDateTime, Double> createCTRDataset(String time) {
        Map<LocalDateTime, Double> totalClicks = createCountByTimeDataset(clicksCsv, time);
        Map<LocalDateTime, Double> totalImpressions = createCountByTimeDataset(impressionsCsv, time);
        Map<LocalDateTime, Double> averageCTR = new TreeMap<>();
        for (Map.Entry<LocalDateTime, Double> entry : totalClicks.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            Double CPCvalue = totalClicks.get(dateTime)/ totalImpressions.get(dateTime);
            averageCTR.put(dateTime,averageCTR.getOrDefault(dateTime,0.0) + CPCvalue);
        }
        return averageCTR;
    }
    private Map<LocalDateTime, Double> createCPADataset(String time) {
        Map<LocalDateTime, Double> totalCost = createTotalCostDataset(time);
        Map<LocalDateTime, Double> totalAcquisitions = createConversionsDataset(time);
        Map<LocalDateTime, Double> averageCPA = new TreeMap<>();
        for (Map.Entry<LocalDateTime, Double> entry : totalCost.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            Double CPCvalue = totalCost.get(dateTime)/ totalAcquisitions.get(dateTime);
            averageCPA.put(dateTime,averageCPA.getOrDefault(dateTime,0.0) + CPCvalue);
        }
        return averageCPA;
    }
    private Map<LocalDateTime, Double> createCPMDataset(String time) {
        Map<LocalDateTime, Double> totalCost = createTotalCostDataset(time);
        Map<LocalDateTime, Double> totalAcquisitions = createConversionsDataset(time);
        Map<LocalDateTime, Double> averageCPM = new TreeMap<>();
        for (Map.Entry<LocalDateTime, Double> entry : totalCost.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            Double CPCvalue = totalCost.get(dateTime)/ totalAcquisitions.get(dateTime);
            averageCPM.put(dateTime,averageCPM.getOrDefault(dateTime,0.0) + CPCvalue);
        }
        return averageCPM;
    }
    private Map<LocalDateTime, Double> createBounceDataset(String time) {
        Map<LocalDateTime, Double> bounceCount = new TreeMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(serverCsv))) {
            reader.readNext();
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                String enter = record[0];
                LocalDateTime enterTime = LocalDateTime.parse(enter, dateFormatter);
                if (enterTime.isAfter(startTime) && enterTime.isBefore(endTime)) {
                    String leave = record[2];
                    String pages = record[4];
                    if(!leave.equals("n/a")){
                        LocalDateTime leaveTime = LocalDateTime.parse(leave, dateFormatter);
                        if(Duration.between(enterTime,leaveTime).getSeconds() > 10 && Integer.parseInt(pages) > 1){
                            LocalDateTime roundedDate;
                            if (time.equals("hour")) {
                                roundedDate = enterTime.withMinute(0).withSecond(0);
                            } else if (time.equals("day")) {
                                roundedDate = enterTime.withHour(0).withMinute(0).withSecond(0);
                            } else if (time.equals("week")) {
                                int week = enterTime.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                                roundedDate = enterTime.withHour(0).withMinute(0).withSecond(0);
                            } else {
                                System.out.println("Enter a valid time period");
                                return bounceCount;
                            }
                            bounceCount.put(roundedDate, bounceCount.getOrDefault(roundedDate, 0.0) + 1);

                        }
                    }

                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return bounceCount;
    }
    private Map<LocalDateTime, Double> createBounceRateDataset(String time) {
        Map<LocalDateTime, Double> totalClicks = createCountByTimeDataset(clicksCsv,time);
        Map<LocalDateTime, Double> totalBounces = createBounceDataset(time);
        Map<LocalDateTime, Double> bounceRate = new TreeMap<>();
        for (Map.Entry<LocalDateTime, Double> entry : totalClicks.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            Double CPCvalue = totalBounces.get(dateTime)/ totalClicks.get(dateTime);
            bounceRate.put(dateTime,bounceRate.getOrDefault(dateTime,0.0) + CPCvalue);
        }
        return bounceRate;
    }



}
