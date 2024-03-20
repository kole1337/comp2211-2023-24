package com.application.database;
import javafx.scene.chart.XYChart;
import org.jfree.data.xy.XYSeries;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public  class DataManager {


    private static Connection conn;
    private static Statement statement;
    private static Statement statement2;
    private static PreparedStatement pstmt;
    private static ResultSet rs;
    private static List<String> rateData = Arrays.asList("CPA", "CPC", "CPM", "bounceRate");

    static Logger logger = Logger.getLogger(UserManager.class.getName());

//    public static void main(String[] args) throws SQLException {
//    }


    public static void getConn(){
        try {
            conn = DbConnection.getConn();
            statement = conn.createStatement();
            statement2 = conn.createStatement();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Could not create statement");
            e.printStackTrace();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not establish connection with SQL database");
            throw new RuntimeException();
        }
    }

    public static void closeConnection()throws RuntimeException{
        try {
            conn.close();
        }
        catch (Exception e){
            throw new RuntimeException("could not close connection");
        }
    }


    private static ResultSet runQuerry(String state, String table) throws SQLException {
        try {
            return statement.executeQuery(state);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not carry out querry ");
            throw e;
        }
    }

    public void addClickLog(ArrayList<String[]> clickLogs) throws SQLException {
        String inClickLog = "INSERT INTO clicklog(date, id, clickCost) VALUES (?,?,?)";
        int i = 0;
        while(i<clickLogs.size()){
            String[] clickLog = clickLogs.get(i);
            pstmt = conn.prepareStatement(inClickLog);
            pstmt.setString(1, clickLog[0]);
            pstmt.setString(2, clickLog[1]);
            pstmt.setDouble(3, Double.parseDouble(clickLog[2]));
            pstmt.addBatch();
            System.out.println("INSERTED!");
            i++;
        }
        pstmt.executeBatch();
        System.out.println("Clicks INSERTED");
    }
    public void addImpressionLog(ArrayList<String[]> impressionLogs) throws SQLException {
        String inClickLog = "INSERT INTO impressionlog(date, id, gender, age, income, context, impression_cost) VALUES (?,?,?,?,?,?,?)";
        int i = 0;
        while(i<impressionLogs.size()){
            String[] impressionLog = impressionLogs.get(i);
            pstmt = conn.prepareStatement(inClickLog);
            pstmt.setString(1, impressionLog[0]);
            pstmt.setString(2, impressionLog[1]);
            pstmt.setString(3, impressionLog[2]);
            pstmt.setString(4, impressionLog[3]);
            pstmt.setString(5, impressionLog[4]);
            pstmt.setString(6, impressionLog[5]);
            pstmt.setDouble(7, Double.parseDouble(impressionLog[6]));
            pstmt.addBatch();
            System.out.println("INSERTED!");
        }
        pstmt.executeBatch();
        System.out.println("INSERTED!");
    }
    public void addServerLog(String entryDate, String id, String exitDate, int pages, String conversion) throws SQLException {

        String inClickLog = "INSERT INTO serverlog(entryDate, id, exitDate, pagesViewed, conversion) VALUES (?,?,?,?,?)";

        pstmt = conn.prepareStatement(inClickLog);
        pstmt.setString(1, entryDate);
        pstmt.setString(2, id);
        pstmt.setString(3, exitDate);
        pstmt.setInt(4, pages);
        pstmt.setString(5, conversion);
        pstmt.executeUpdate();
        System.out.println("INSERTED!");
    }

    public int selectTotalData(String table){
        int totals = 0;
        try {
            rs = statement.executeQuery("select count(*) from " + table);
            if(rs.next()) {
                totals = rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return totals;
    }

    public int selectZeroClickCost(){
        int totals = 0;
        try {
            rs = statement.executeQuery("select count(*) from clicklog where clickCost = 0");
            if(rs.next()) {
                totals = rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return totals;
    }

    public int selectAvgData(String column, String table){
        int totals = 0;
        try {
            rs = statement.executeQuery("SELECT AVG("+ column+ ") FROM "+table);
            if(rs.next()) {
                totals = rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return totals;
    }

    public int[] getUniqueAppearanceInt(String column, String table){
        int uniqueVals = uniqueValues(column, table);
        int totals[] = new int[uniqueVals];

        try {
            rs = statement.executeQuery("SELECT "+column+", COUNT(*) AS count FROM "+table+" GROUP BY "+column);
            int i = 0;
            while(rs.next()){

                totals[i] = rs.getInt("count");
                ++i;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return totals;
    }

    public String[] getUniqueAppearanceString(String column, String table){
        int uniqueVals = uniqueValues(column, table);
        String totals[] = new String[uniqueVals];

        try {
            rs = statement.executeQuery("SELECT "+column+", COUNT(*) AS count FROM "+table+" GROUP BY "+column);
            int i = 0;
            while(rs.next()){

                totals[i] = rs.getString(column);
                ++i;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return totals;
    }

    public void loadCSVintoDB(){

        try {
            statement.executeUpdate("LOAD DATA INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\2weeks\\click_log.csv' INTO TABLE clicklog FIELDS TERMINATED BY ',' IGNORE 1 LINES (date, id, clickCost)");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int uniqueValues(String column, String table){
        int totals = 0;
        try {
            rs = statement.executeQuery("SELECT COUNT(DISTINCT "+ column+ ") FROM "+table);
            if(rs.next()) {
                totals = rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return totals;
    }

    public int getFirstDate(String period, String table) {
        int totals = 0;

        try {
            rs = statement.executeQuery("SELECT MIN("+period+"(date)) FROM " +table);
            int i = 0;
            while(rs.next()){

                totals = rs.getInt(1);
                ++i;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return totals;
    }

    public int getLastDate(String period, String table) {
        int totals = 0;

        try {
            rs = statement.executeQuery("SELECT MAX("+period+"(date)) FROM " +table);
            int i = 0;
            while(rs.next()){

                totals = rs.getInt(1);
                ++i;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return totals;
    }

    public XYChart.Series<String, Number> getData(String dataName, String startDate, String endDate, String gender, String income, String context, String age ) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        if(rateData.contains(dataName)){
            return getRateData(dataName, startDate, endDate,gender,income,context,age);
        }
        else{
            String query = queryGenerator(dataName, startDate, endDate,gender,income,context,age);
            try {
                System.out.println(dataName);
                rs = statement.executeQuery(query);
                System.out.println(rs);
                while (rs.next()) {
                    String xValue = rs.getString("date"); // Get value from column1
                    Number yValue = rs.getInt("data"); // Get value from column2
                    // Discard column3 by not using it
                    series.getData().add(new XYChart.Data<>(xValue, yValue));
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return series;
        }
    }

    public XYChart.Series<String,Number> getRateData(String dataName, String startDate, String endDate, String gender, String income, String context, String age) {
        XYChart.Series<String, Number> series = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String query1 = "";
        String query2 = "";
        if (dataName.equals("CTR")) {
            XYChart.Series<String, Number> clicks = getData("totalClicks", startDate, endDate, gender, income, context, age);
            XYChart.Series<String, Number> impressions = getData("totalImpressions", startDate, endDate, gender, income, context, age);
            XYChart.Series<String, Number> clicksPerImpression = new XYChart.Series<>();
            Iterator<XYChart.Data<String, Number>> clicksIterator = clicks.getData().iterator();
            Iterator<XYChart.Data<String, Number>> impressionsIterator = impressions.getData().iterator();
            while (clicksIterator.hasNext() && impressionsIterator.hasNext()) {
                XYChart.Data<String, Number> clickData = clicksIterator.next();
                XYChart.Data<String, Number> impressionData = impressionsIterator.next();

                String xValue1 = clickData.getXValue();
                String xValue2 = impressionData.getXValue();
                Number clickValue = clickData.getYValue();
                Number impressionValue = impressionData.getYValue();

                if (xValue1 == xValue2 && impressionValue.doubleValue() != 0) {
                    double ratio = clickValue.doubleValue() / impressionValue.doubleValue();
                    clicksPerImpression.getData().add(new XYChart.Data<>(xValue1, ratio));
                } else {
                    // Handle division by zero case if needed
                }
            }
        }
            if (dataName.equals("CPA")) {
                query1 = queryGenerator("totalCost", startDate, endDate, gender, income, context, age);
                query2 = queryGenerator("totalConversions", startDate, endDate, gender, income, context, age);
                try {
                    rs1 = statement.executeQuery(query1);
                    rs2 = statement2.executeQuery(query2);
                    while (rs1.next() && rs2.next()) {
                        String xValue = rs1.getString("date");
                        Number yValue = rs1.getInt("data") / rs2.getInt("data");
                        series.getData().add(new XYChart.Data<>(xValue, yValue));
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
            if (dataName.equals("CPM")) {
                query1 = queryGenerator("totalClicks", startDate, endDate, gender, income, context, age);
                query2 = queryGenerator("totalImpressions", startDate, endDate, gender, income, context, age);
                try {
                    rs1 = statement.executeQuery(query1);
                    rs2 = statement2.executeQuery(query2);
                    while (rs1.next() && rs2.next()) {
                        String xValue = rs1.getString("date");
                        Number yValue = rs1.getInt("data") / rs2.getInt("data");
                        series.getData().add(new XYChart.Data<>(xValue, yValue));
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (dataName.equals("CPC")) {
                query1 = queryGenerator("totalCost", startDate, endDate, gender, income, context, age);
                query2 = queryGenerator("totalClicks", startDate, endDate, gender, income, context, age);
                try {
                    rs1 = statement.executeQuery(query1);
                    rs2 = statement2.executeQuery(query2);
                    while (rs1.next() && rs2.next()) {
                        String xValue = rs1.getString("date");
                        Number yValue = rs1.getInt("data") / rs2.getInt("data");
                        series.getData().add(new XYChart.Data<>(xValue, yValue));
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
            if (dataName.equals("bounceRate")) {
                query1 = queryGenerator("totalBounces", startDate, endDate, gender, income, context, age);
                query2 = queryGenerator("totalClicks", startDate, endDate, gender, income, context, age);
                try {
                    rs1 = statement.executeQuery(query1);
                    rs2 = statement2.executeQuery(query2);
                    while (rs1.next() && rs2.next()) {
                        System.out.println("EEEEEEEEEE");
                        String xValue = rs1.getString("date");
                        Number yValue = rs1.getInt("data") / rs2.getInt("data");
                        series.getData().add(new XYChart.Data<>(xValue, yValue));
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
            return series;
        }


    private String queryGenerator(String dataName, String startDate, String endDate, String gender, String income, String context, String age){
        String query = "";
        String filterQuery = filterQueryHelper(gender,age,income,context);
        if(dataName.equals("totalClicks")){
            query = "SELECT DATE_FORMAT(click.Date, '%Y-%m-%d %H:00:00') AS date, COUNT(*) AS data " +
                    "FROM clicklog as click " +  "JOIN impressionlog AS impression ON click.id = impression.id "+ filterQuery + " AND click.date BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY date";
        }
        if(dataName.equals("totalImpressions")){
            query = "SELECT DATE_FORMAT(impression.Date, '%Y-%m-%d %H:00:00') AS date, COUNT(*) AS data " +
                    "FROM impressionlog as impression " + filterQuery + " AND impression.date BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY date";
        }
        if(dataName.equals("totalUniques")){
            query = "SELECT DATE_FORMAT(click.Date, '%Y-%m-%d %H:00:00') AS date, COUNT(DISTINCT click.id) AS data " +
                    "FROM clicklog as click " +
                    "JOIN impressionlog AS impression ON click.id = impression.id "  + filterQuery + " AND click.date BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY date";
        }
        if(dataName.equals("totalBounces")){
            query = "SELECT DATE_FORMAT(click.Date, '%Y-%m-%d %H:00:00') AS date, COUNT(*) AS data " +
                    "FROM clicklog as click " +
                    "JOIN impressionlog AS impression ON click.id = impression.id JOIN serverlog AS server ON click.id = server.id "
                    + filterQuery + " AND click.date BETWEEN '" + startDate + "' AND '" + endDate + "' AND (TIMESTAMPDIFF(MINUTE, server.entrydate, server.exitdate) > 3 OR server.pagesviewed > 1) GROUP BY date";
        }
        if(dataName.equals("totalConversions")){
            query = "SELECT DATE_FORMAT(server.Date, '%Y-%m-%d %H:00:00') AS date, COUNT(*) AS data WHERE server.conversion = 'Yes' " +
                    "FROM serverlog AS server " +  "JOIN impressionlog AS impression ON server.id = impression.id " + filterQuery + " AND server.date BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY date";
        }
        if(dataName.equals("totalCost")){
            query = "SELECT DATE_FORMAT(click.Date, '%Y-%m-%d %H:00:00') AS date, SUM(click.ClickCost) AS data " +
                    "FROM clicklog AS click " +  "JOIN impressionlog AS impression ON click.id = impression.id " + filterQuery + " AND click.date BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY date";
        }
        if(dataName.equals("CTR")){
            query = "SELECT DATE_FORMAT(click.Date, '%Y-%m-%d %H:00:00') AS date, " +
                    "SUM(CASE WHEN click.id IS NOT NULL THEN 1 ELSE 0 END) AS clicks, " +
                    "SUM(CASE WHEN impression.id IS NOT NULL THEN 1 ELSE 0 END) AS impressions, " +
                    "SUM(CASE WHEN click.id IS NOT NULL THEN 1 ELSE 0 END) /SUM(CASE WHEN impression.id IS NOT NULL THEN 1 ELSE 0 END) AS data " +
                    "FROM clicklog AS click " +
                    "RIGHT JOIN impressionlog AS impression ON click.id = impression.id " + filterQuery + " AND click.date BETWEEN '" +startDate + "' AND '" + endDate + "' GROUP BY date";
        }
        return query;
    }

    private String filterQueryHelper(String gender, String age, String income, String context){
        if(gender.equals("")){
            gender = "IS NOT NULL";
        }
        else{
            gender = "= '" + gender + "'";
        }
        if(age.equals("")){
            age = "IS NOT NULL";
        }
        else{
            age = "= '" + age + "'";
        }
        if(income.equals("")){
            income = "IS NOT NULL";
        }
        else{
            income = "= '" + income + "'";
        }
        if(context.equals("")){
            context = "IS NOT NULL";
        }
        else{
            context = "= '" + context + "'";
        }
        String query = "WHERE impression.Gender " + gender + " AND impression.Age " + age + " AND impression.Income " + income + " AND impression.Context " + context;
        return query;
    }

}