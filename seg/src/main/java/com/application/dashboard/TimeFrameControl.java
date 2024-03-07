package com.application.dashboard;

import com.application.files.FilePathHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;



public class TimeFrameControl extends DatasetCreator {

    /**
     * This is the constructor that create the listener of those date and time control.
     * @param fph the file path handler
     */

    public TimeFrameControl(FilePathHandler fph) {
        super(fph);

    }

    /**
     * this is an override method
     * @param graphName
     * @param time
     * @param fromTime
     * @param toTime
     * @return
     */
    public Map<LocalDateTime, Double> createDataset(String graphName, String time, LocalDateTime fromTime, LocalDateTime toTime) {
        if (graphName.equals("TotalClicks")) {
            return createCountByTimeDataset(clicksCsv, time, fromTime, toTime);
        } else if (graphName.equals("TotalImpressions")) {
            return createCountByTimeDataset(impressionsCsv, time, fromTime, toTime);
        } else if (graphName.equals("TotalUniques")) {
            return createUniqueClicksDataset(time, fromTime, toTime);
        } else if (graphName.equals("TotalConversions")) {
            return createConversionsDataset(time, fromTime, toTime);
        } else if (graphName.equals("TotalCost")) {
            return createTotalCostDataset(time, fromTime, toTime);
        } else if (graphName.equals("CPC")){
            return createCPCDataset(time, fromTime, toTime);
        } else if (graphName.equals("CTR")){
            return createCTRDataset(time, fromTime, toTime);
        } else if (graphName.equals("CPA")){
            return createCPADataset(time, fromTime, toTime);
        } else if (graphName.equals("CPM")){
            return createCPMDataset(time, fromTime, toTime);
        } else if (graphName.equals("TotalBounces")){
            return createBounceDataset(time, fromTime, toTime);
        } else if (graphName.equals("BounceRate")){
            return createBounceRateDataset(time, fromTime, toTime);
        }
        else {
            return null;
        }
    }

    /**
     * This is the method for user to get the data from csv within selected time frame
     * @param csvFile the csvfile
     * @param fromTime the start date and time
     * @param toTime the end date and time
     * @return the ArrayList<String[]> that holds the date and time within that range (start->end)
     */
    private List<String []> getDataWithinTimeFrame(String csvFile, LocalDateTime fromTime, LocalDateTime toTime){
        try(CSVReader reader = new CSVReader(new FileReader(csvFile))){
            reader.readNext();
            List<String []> records = reader.readAll();
            List<String []> withinTimeFrameRecords = new ArrayList<String []>();
            for(String[] record: records){
                String dateStr = record[0];
                LocalDateTime date = LocalDateTime.parse(dateStr, dateFormatter);
                if(date.isAfter(fromTime) && date.isBefore(toTime)){
                    withinTimeFrameRecords.add(record);
                }
            }
            return withinTimeFrameRecords;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * this is an override method
     * @param csvFile
     * @param time
     * @param fromTime
     * @param toTime
     * @return
     */
    private Map<LocalDateTime, Double> createCountByTimeDataset(String csvFile, String time, LocalDateTime fromTime, LocalDateTime toTime) {
        Map<LocalDateTime, Double> countByTime = new TreeMap<>();
        List<String []> records = getDataWithinTimeFrame(csvFile, fromTime, toTime);

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

        return countByTime;
    }

    /**
     * This is an override method
     * @param time
     * @param fromTime
     * @param toTime
     * @return
     */
    private Map<LocalDateTime, Double>createUniqueClicksDataset(String time, LocalDateTime fromTime, LocalDateTime toTime) {
        List<String> seen = new ArrayList<>();
        Map<LocalDateTime, Double> countByTime = new TreeMap<>();
        List<String []> records = getDataWithinTimeFrame(clicksCsv, fromTime, toTime);

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
        return countByTime;
    }
    private Map<LocalDateTime, Double> createConversionsDataset(String time, LocalDateTime fromTime, LocalDateTime toTime) {
        Map<LocalDateTime, Double> countByTime = new TreeMap<>();
        List<String []> records = getDataWithinTimeFrame(serverCsv, fromTime, toTime);
        LocalDateTime startDayForWeek = null;
        for (String[] record : records) {
            String dateString = record[0];
            String conversion = record[4];
            LocalDateTime date = LocalDateTime.parse(dateString, dateFormatter);
            if (conversion.equals("Yes")) {
                if (date.isAfter(startTime) && date.isBefore(endTime)) {
                    if(startDayForWeek == null){
                        startDayForWeek = date;
                    }
                    if(Duration.between(startDayForWeek.withHour(0).withMinute(0).withSecond(0),date.withHour(0).withMinute(0).withSecond(0)).toDays() > 7){
                        startDayForWeek = date;
                    }
                    LocalDateTime roundedDate;
                    if (time.equals("hour")) {
                        roundedDate = date.withMinute(0).withSecond(0);
                    } else if (time.equals("day")) {
                        roundedDate = date.withHour(0).withMinute(0).withSecond(0);
                    } else if (time.equals("week")) {
                        roundedDate = startDayForWeek.withHour(0).withMinute(0).withSecond(0);
                    } else {
                        System.out.println("Enter a valid time period");
                        return countByTime;
                    }
                    countByTime.put(roundedDate, countByTime.getOrDefault(roundedDate, 0.0) + 1);
                }
            }
        }

        return countByTime;
    }

    private Map<LocalDateTime, Double> createTotalCostDataset(String time, LocalDateTime fromTime, LocalDateTime toTime) {

        Map<LocalDateTime, Double> countByTime = new TreeMap<>();
        List<String []> records = getDataWithinTimeFrame(clicksCsv, fromTime, toTime);
        LocalDateTime startDayForWeek = null;
        for (String[] record : records) {
            String dateString = record[0];
            String cost = record[2];
            LocalDateTime date = LocalDateTime.parse(dateString, dateFormatter);
            if(startDayForWeek == null){
                startDayForWeek = date;
            }
            if(Duration.between(startDayForWeek.withHour(0).withMinute(0).withSecond(0),date.withHour(0).withMinute(0).withSecond(0)).toDays() > 7){
                startDayForWeek = date;
            }
            if (date.isAfter(startTime) && date.isBefore(endTime)) {
                LocalDateTime roundedDate;
                if (time.equals("hour")) {
                    roundedDate = date.withMinute(0).withSecond(0);
                } else if (time.equals("day")) {
                    roundedDate = date.withHour(0).withMinute(0).withSecond(0);
                } else if (time.equals("week")) {
                    roundedDate = startDayForWeek.withHour(0).withMinute(0).withSecond(0);
                } else {
                    System.out.println("Enter a valid time period");
                    return countByTime;
                }
                countByTime.put(roundedDate, countByTime.getOrDefault(roundedDate, 0.0) + Double.valueOf(cost));
            }
        }

        return countByTime;
    }

    private Map<LocalDateTime, Double> createCPCDataset(String time, LocalDateTime fromTime, LocalDateTime toTime) {
        Map<LocalDateTime, Double> totalClicks = createCountByTimeDataset(clicksCsv, time, fromTime, toTime);
        Map<LocalDateTime, Double> totalCost = createTotalCostDataset(time, fromTime, toTime);
        Map<LocalDateTime, Double> averageCPC = new TreeMap<>();
        for (Map.Entry<LocalDateTime, Double> entry : totalClicks.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            Double CPCvalue = totalCost.get(dateTime)/ totalClicks.get(dateTime);
            averageCPC.put(dateTime,averageCPC.getOrDefault(dateTime,0.0) + CPCvalue);
        }
        return averageCPC;
    }
    private Map<LocalDateTime, Double> createCTRDataset(String time, LocalDateTime fromTime, LocalDateTime toTime) {
        Map<LocalDateTime, Double> totalClicks = createCountByTimeDataset(clicksCsv, time, fromTime, toTime);
        Map<LocalDateTime, Double> totalImpressions = createCountByTimeDataset(impressionsCsv, time, fromTime, toTime);
        Map<LocalDateTime, Double> averageCTR = new TreeMap<>();
        for (Map.Entry<LocalDateTime, Double> entry : totalClicks.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            Double CPCvalue = totalClicks.get(dateTime)/ totalImpressions.get(dateTime);
            averageCTR.put(dateTime,averageCTR.getOrDefault(dateTime,0.0) + CPCvalue);
        }
        return averageCTR;
    }
    private Map<LocalDateTime, Double> createCPADataset(String time, LocalDateTime fromTime, LocalDateTime toTime) {
        Map<LocalDateTime, Double> totalCost = createTotalCostDataset(time, fromTime, toTime);
        Map<LocalDateTime, Double> totalAcquisitions = createConversionsDataset(time, fromTime, toTime);
        Map<LocalDateTime, Double> averageCPA = new TreeMap<>();
        for (Map.Entry<LocalDateTime, Double> entry : totalCost.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            if(totalCost.get(dateTime) != null && totalAcquisitions.get(dateTime) != null){
                Double CPAvalue = totalCost.get(dateTime)/ totalAcquisitions.get(dateTime);
                averageCPA.put(dateTime,averageCPA.getOrDefault(dateTime,0.0) + CPAvalue);
            }
        }
        return averageCPA;
    }
    private Map<LocalDateTime, Double> createCPMDataset(String time, LocalDateTime fromTime, LocalDateTime toTime) {
        Map<LocalDateTime, Double> totalCost = createTotalCostDataset(time, fromTime, toTime);
        Map<LocalDateTime, Double> totalAcquisitions = createConversionsDataset(time, fromTime, toTime);
        Map<LocalDateTime, Double> averageCPA = new TreeMap<>();
        for (Map.Entry<LocalDateTime, Double> entry : totalCost.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            if(totalCost.get(dateTime) != null && totalAcquisitions.get(dateTime) != null){
                Double CPAvalue = totalCost.get(dateTime)/ totalAcquisitions.get(dateTime);
                averageCPA.put(dateTime,averageCPA.getOrDefault(dateTime,0.0) + CPAvalue);
            }
        }
        return averageCPA;
    }
    private Map<LocalDateTime, Double> createBounceDataset(String time, LocalDateTime fromTime, LocalDateTime toTime) {
        Map<LocalDateTime, Double> bounceCount = new TreeMap<>();
        List<String []> records = getDataWithinTimeFrame(serverCsv, fromTime, toTime);
        LocalDateTime startDayForWeek = null;
        for (String[] record : records) {
            String enter = record[0];
            LocalDateTime enterTime = LocalDateTime.parse(enter, dateFormatter);
            if(startDayForWeek == null){
                startDayForWeek = enterTime;
            }
            if(Duration.between(startDayForWeek.withHour(0).withMinute(0).withSecond(0),enterTime.withHour(0).withMinute(0).withSecond(0)).toDays() > 7){
                startDayForWeek = enterTime;
            }
            if (enterTime.isAfter(startTime) && enterTime.isBefore(endTime)) {
                String leave = record[2];
                String pages = record[3];
                if(!leave.equals("n/a")){
                    LocalDateTime leaveTime = LocalDateTime.parse(leave, dateFormatter);
                    if(Duration.between(enterTime,leaveTime).getSeconds() > 10 && Integer.parseInt(pages) > 1){
                        LocalDateTime roundedDate;
                        if (time.equals("hour")) {
                            roundedDate = enterTime.withMinute(0).withSecond(0);
                        } else if (time.equals("day")) {
                            roundedDate = enterTime.withHour(0).withMinute(0).withSecond(0);
                        } else if (time.equals("week")) {
                            roundedDate = startDayForWeek.withHour(0).withMinute(0).withSecond(0);
                        } else {
                            System.out.println("Enter a valid time period");
                            return bounceCount;
                        }
                        bounceCount.put(roundedDate, bounceCount.getOrDefault(roundedDate, 0.0) + 1);

                    }
                }

            }
        }

        return bounceCount;
    }
    private Map<LocalDateTime, Double> createBounceRateDataset(String time, LocalDateTime fromTime, LocalDateTime toTime) {
        Map<LocalDateTime, Double> totalClicks = createCountByTimeDataset(clicksCsv,time, fromTime, toTime);
        Map<LocalDateTime, Double> totalBounces = createBounceDataset(time, fromTime, toTime);
        Map<LocalDateTime, Double> bounceRate = new TreeMap<>();
        for (Map.Entry<LocalDateTime, Double> entry : totalClicks.entrySet()) {
            LocalDateTime dateTime = entry.getKey();
            if(totalBounces.get(dateTime)!=null && totalClicks.get(dateTime)!=null){
                Double CPCvalue = totalBounces.get(dateTime)/ totalClicks.get(dateTime);
                bounceRate.put(dateTime,bounceRate.getOrDefault(dateTime,0.0) + CPCvalue);
            }
        }
        return bounceRate;
    }



}
