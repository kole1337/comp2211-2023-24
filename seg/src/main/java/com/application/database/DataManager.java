package com.application.database;
import com.application.files.ScriptRunner;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.io.*;
import java.sql.*;

import java.lang.Process.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.application.files.ScriptRunner;

public  class DataManager {


    private static Connection conn;
    private static Connection conn1;
    private static Statement statement;
    private static Statement statement1;
    private static PreparedStatement pstmt;
    private static ResultSet rs;
    private static List<String> rateData = Arrays.asList("CTR","CPA", "CPC", "CPM", "bounceRate");
    public static IntegerProperty history_position = new SimpleIntegerProperty();
    public static String savename;
    public int bouncePages = 1;
    public int bounceTimeMinute = 3;
    private static ResultSet history;
    private static ResultSet saved;
    private static int saved_id = 0;

    static Logger logger = Logger.getLogger(UserManager.class.getName());


    public static void deleteData(String tableName){
        try{
            pstmt = conn.prepareStatement("DELETE FROM " + tableName +";");

        }catch (SQLException e){
            logger.log(Level.SEVERE,"Could not delete data from table: " + tableName);
        }
    }

    public static void getConn(){
        try {
            conn = DbConnection.getConn();
            conn1 = DbConnection.getConn();
            statement = conn.createStatement();
            statement1 = conn1.createStatement();
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
            conn1.close();
        }
        catch (Exception e){
            throw new RuntimeException("could not close connection");
        }
    }

    public static void dumpData() throws SQLException{
        try{

            pstmt = conn.prepareStatement("DELETE FROM impressionlog;");
            pstmt.executeUpdate();

            // Delete data from serverlog table
            pstmt = conn.prepareStatement("DELETE FROM serverlog;");
            pstmt.executeUpdate();

            // Delete data from clickslog table
            pstmt = conn.prepareStatement("DELETE FROM clicklog;");
            pstmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
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

    public void addClickLog(String date, String id, double clickCost) throws SQLException {
        String inClickLog = "INSERT INTO clicklog(date, id, clickCost) VALUES (?,?,?)";

        pstmt = conn.prepareStatement(inClickLog);
        pstmt.setString(1, date);
        pstmt.setString(2, id);
        pstmt.setDouble(3, clickCost);
        pstmt.executeUpdate();
        System.out.println("INSERTED!");
    }
    public void addImpressionLog(ArrayList<String[]> impressionLogs) throws SQLException {
        try {
            String inClickLog = "INSERT INTO impressionlog(date, id, gender, age, income, context, impression_cost) VALUES (?,?,?,?,?,?,?)";
            int i = 0;
            while (i < impressionLogs.size()) {
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
        }catch(Exception e){
            throw e;
        }
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

    public int selectTotalData(String table,String gender, String age, String income, String context, String startDate, String endDate){
        int totals = 0;
        String filterQuery = filterQueryHelper(gender, age, income, context);
        try {
            if(table.equals("impressionlog")){
                rs = statement.executeQuery("select count(1) from " + table + " AS impression " + filterQuery + " AND impression.Date BETWEEN '" + startDate + "' AND '" + endDate +"'");

            }
            else if(table.equals("serverlog") || table.equals("Serverlog")){

                rs = statement.executeQuery("select count(1) from " + table + " JOIN impressionLog AS impression ON impression.id = " + table + ".id " + filterQuery + " AND " + table + ".entryDate BETWEEN '" + startDate + "' AND '" + endDate +"'");

            }
            else {
                rs = statement.executeQuery("select count(1) from " + table + " JOIN impressionLog AS impression ON impression.id = " + table + ".id " + filterQuery + " AND " + table + ".date BETWEEN '" + startDate + "' AND '" + endDate + "'");
            }
            if(rs.next()) {
                totals = rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return totals;
    }
    public int selectUniqueImpressionData(String gender, String age, String income, String context, String startDate, String endDate){
        int totals = 0;
        String filterQuery = filterQueryHelper(gender,age,income,context);
        try{
            rs = statement.executeQuery("select count(DISTINCT impression.id) from impressionLog AS impression " + filterQuery + " AND impression.Date BETWEEN '" + startDate + "' AND '" + endDate +"'");
        if(rs.next()){
            totals = rs.getInt(1);
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return totals;
    }

    /**
     * This is for selecting data from serverlog to get the total bounces
     * @param timeSpentBounce this is the String that user specifying which timespent will be counted as bounce
     * @param pageViewedBounce this is the String that user specifying which pageviewed will be counted as bounce
     * @return total bounces which data type is int
     */
    public int selectTotalBounces(String gender, String age, String income, String context, String startDate, String endDate){
        int totals = 0;
        String filterQuery = filterQueryHelper(gender,age,income, context);
        String timeBounce = getBounceTimeMinute();
        String pageBounce = getBouncePages();
        try {
            String query = "SELECT COUNT(1) FROM serverlog AS server JOIN impressionLog AS impression ON server.id = impression.id " + filterQuery + " AND server.entryDate BETWEEN '" + startDate + "' AND '" + endDate +"'" +
                    " AND (TIMESTAMPDIFF(SECOND, server.entryDate, server.exitDate) >= " + timeBounce +
                    " OR server.pagesViewed >= " + pageBounce + ")" ;

            rs = statement.executeQuery(query);
            if (rs.next()) {
                totals = rs.getInt(1); // Retrieve the count from the result set
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return totals;
    }
    /**
     * This is for selecting data from serverlog to get the bounce rate
     * @param timeSpentBounce this is the String that user specifying which timespent will be counted bounce
     * @param pageViewedBounce this is the String that user specifying which pageviewed will be counted bounce
     * @return bounce rate which data type is double
     */
    public double selectBounceRate(String gender, String age, String income, String context, String startDate, String endDate) {
        double bounceRate = 0.0;
        int totalBounces = selectTotalBounces(gender, age, income, context, startDate, endDate);
        int totalSessions = 0;
        String filterQuery = filterQueryHelper(gender,age,income,context);
        try {
            // Query to count the total number of sessions
            String queryTotalSessions = "SELECT COUNT(1) FROM serverlog AS server JOIN impressionLog AS impression ON impression.id = server.id " + filterQuery + " AND server.entryDate BETWEEN '" + startDate + "' AND '" + endDate +"'" ;

            rs = statement.executeQuery(queryTotalSessions);
            if (rs.next()) {
                totalSessions = rs.getInt(1); // Retrieve the count from the result set
            }

            // Calculating the bounce rate as a percentage
            if (totalSessions > 0) { // Avoid division by zero
                bounceRate = (double) totalBounces / totalSessions * 100;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return bounceRate;
    }
    public int selectZeroClickCost(String gender, String age, String income, String context, String startDate, String endDate){
        int totals = 0;
        String filterQuery = filterQueryHelper(gender,age, income, context);
        try {
            rs = statement.executeQuery("SELECT count(1) FROM clicklog AS clicks JOIN impressionLog AS impression ON impression.id = clicks.id " + filterQuery + " AND clicks.Date BETWEEN '" + startDate + "' AND '" + endDate +"'" + " AND clickCost = 0");
            if(rs.next()) {
                totals = rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return totals;
    }

    public int selectAvgData(String column, String table, String gender, String age, String income, String context, String startDate, String endDate){
        int totals = 0;
        String filterQuery = filterQueryHelper(gender, age, income, context);
        try {
            if(table.equals("serverlog") || table.equals("Serverlog")){
                rs = statement.executeQuery("SELECT AVG("+ column+ ") FROM "+table + " JOIN impressionLog AS impression ON impression.id = " + table + ".id " + filterQuery + " AND " + table + ".entryDate BETWEEN '" + startDate + "' AND '" + endDate +"'" );

            }
            else{rs = statement.executeQuery("SELECT AVG("+ column+ ") FROM "+table + " JOIN impressionLog AS impression ON impression.id = " + table + ".id " + filterQuery + " AND " + table + ".Date BETWEEN '" + startDate + "' AND '" + endDate +"'" );}

            if(rs.next()) {
                totals = rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return totals;
    }

    public int[] getUniqueAppearanceInt(String column, String table, String fromDate, String toDate){
        int uniqueVals = uniqueValues(column, table);
        int totals[] = new int[uniqueVals];

        try {
            rs = statement.executeQuery("SELECT "+column+", COUNT(1) AS count FROM "+table+" WHERE date BETWEEN '"+fromDate +"' " + " AND '" +toDate +"' GROUP BY "+column);
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

    public String getMaxDateFromTable(String table){
        String endDate = "";
        try {
            rs = statement.executeQuery("SELECT MAX(date) FROM "+table);
            while(rs.next()) {
                endDate = rs.getString(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return endDate;
    }

    public String getMinDateFromTable(String table){
        String endDate = "";
        try {
            rs = statement.executeQuery("SELECT MIN(date) FROM "+table);
            while(rs.next()) {
                endDate = rs.getString(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return endDate;
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
    public XYChart.Series<String, Number> getData(String dataName, String timePeriod, String startDate, String endDate, String gender, String income, String context, String age) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        if (rateData.contains(dataName)) {
            return getRateData(dataName, timePeriod, startDate, endDate, gender, income, context, age);
        } else {
            String query = queryGenerator(dataName, timePeriod, startDate, endDate, gender, income, context, age);

            try {
                rs = statement.executeQuery(query);
                int pageSize = 1000; // Adjust this value based on your requirements
                boolean hasMore = rs.next();

                while (hasMore) {
                    for (int i = 0; i < pageSize && hasMore; i++) {
                        String xValue = rs.getString("date");
                        Number yValue = rs.getInt("data");
                        series.getData().add(new XYChart.Data<>(xValue, yValue));
                        hasMore = rs.next();
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return series;
        }
    }

    public XYChart.Series<String,Number> getRateData(String dataName, String timePeriod,  String startDate, String endDate, String gender, String income, String context, String age) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String query1 = "";
        String query2 = "";
        if (dataName.equals("costPerImpres")) {
            query1 = queryGenerator("totalClicks", timePeriod, startDate, endDate, gender, income, context, age);
            query2 = queryGenerator("totalImpressions", timePeriod, startDate, endDate, gender, income, context, age);
            try {
                rs1 = statement.executeQuery(query1);
                rs2 = statement1.executeQuery(query2);
                while (rs1.next() && rs2.next()) {
                    String xValue = rs1.getString("date");
                    Number yValue = rs1.getInt("data") / rs2.getInt("data");
                    series.getData().add(new XYChart.Data<>(xValue, yValue));
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (dataName.equals("costPerAcq")) {
                query1 = queryGenerator("totalCost", timePeriod,  startDate, endDate, gender, income, context, age);
                query2 = queryGenerator("totalConversions", timePeriod,  startDate, endDate, gender, income, context, age);

                try {
                    rs1 = statement.executeQuery(query1);
                    rs2 = statement1.executeQuery(query2);
                    while (rs1.next() && rs2.next()) {
                        String xValue = rs1.getString("date");
                        Number yValue = rs1.getInt("data") / rs2.getInt("data");
//                        System.out.println(xValue);
//                        System.out.println(yValue);
                        series.getData().add(new XYChart.Data<>(xValue, yValue));
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        if (dataName.equals("costPerThousandImpres")) {
                query1 = queryGenerator("totalClicks", timePeriod, startDate, endDate, gender, income, context, age);
                query2 = queryGenerator("totalImpressions",timePeriod, startDate, endDate, gender, income, context, age);
                try {
                    rs1 = statement.executeQuery(query1);
                    rs2 = statement1.executeQuery(query2);
                    while (rs1.next() && rs2.next()) {
                        String xValue = rs1.getString("date");
                        Number yValue = rs1.getInt("data") / (rs2.getInt("data")*1000);
                        series.getData().add(new XYChart.Data<>(xValue, yValue));
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        if (dataName.equals("costPerClicks")) {
                query1 = queryGenerator("totalCost", timePeriod,  startDate, endDate, gender, income, context, age);
                query2 = queryGenerator("totalClicks", timePeriod,  startDate, endDate, gender, income, context, age);
                try {
                    rs1 = statement.executeQuery(query1);
                    rs2 = statement1.executeQuery(query2);
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
                query1 = queryGenerator("totalBounces", timePeriod,  startDate, endDate, gender, income, context, age);
                query2 = queryGenerator("totalClicks", timePeriod, startDate, endDate, gender, income, context, age);
                try {
                    rs1 = statement.executeQuery(query1);
                    rs2 = statement1.executeQuery(query2);
                    while (rs1.next() && rs2.next()) {
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


    private String queryGenerator(String dataName, String timePeriod, String startDate, String endDate, String gender, String income, String context, String age){
        String query = "";
        String filterQuery = filterQueryHelper(gender,age,income,context);
        if(timePeriod.equals("hour")){
            timePeriod = "'%Y-%m-%d %H:00:00'";
        }
        else if(timePeriod.equals("day")) {
            timePeriod = "'%Y-%m-%d 00:00:00'";
        }
        else if(timePeriod.equals("week")){
            timePeriod = "'%Y-%V'";
        }

        if(dataName.equals("totalClicks")){
            query = "SELECT DATE_FORMAT(click.Date, " + timePeriod + " ) AS date, COUNT(*) AS data " +
                    "FROM clicklog as click " +  "JOIN impressionlog AS impression ON click.id = impression.id "+ filterQuery + " AND click.date BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY date";
        }
        if(dataName.equals("totalImpressions")){
            query = "SELECT DATE_FORMAT(date, " + timePeriod + " ) AS date, COUNT(*) AS data " +
                    "FROM impressionlog as impression " + filterQuery + " AND impression.date BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY DATE_FORMAT(impression.Date, '%Y-%m-%d %H:00:00')";
        }
        if(dataName.equals("totalUniques")){
            query = "SELECT DATE_FORMAT(click.Date, " + timePeriod + " ) AS date, COUNT(*) AS data " +
                    "FROM clicklog as click " +
                    "JOIN impressionlog AS impression ON click.id = impression.id "  + filterQuery + " AND click.date BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY date";
        }
        if(dataName.equals("totalBounces")){
            query = "SELECT DATE_FORMAT(click.Date, " + timePeriod + " ) AS date, COUNT(*) AS data " +
                    "FROM clicklog as click " +
                    "JOIN impressionlog AS impression ON click.id = impression.id JOIN serverlog AS server ON click.id = server.id "
                    + filterQuery + " AND click.date BETWEEN '" + startDate + "' AND '" + endDate + "' AND (TIMESTAMPDIFF(SECOND, server.entryDate, server.exitDate) >= " + getBounceTimeMinute() + " OR server.pagesviewed >= " + getBouncePages() + ") GROUP BY date";
        }
        if(dataName.equals("totalConversions")){
            query = "SELECT DATE_FORMAT(server.entryDate, " + timePeriod + " ) AS date, COUNT(*) AS data " +
                    "FROM serverlog AS server " +  "JOIN impressionlog AS impression ON server.id = impression.id " + filterQuery + " AND server.conversion = 'Yes' AND server.entryDate BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY entryDate";
        }
        if(dataName.equals("totalCost")){
            query = "SELECT DATE_FORMAT(click.Date, " + timePeriod + " ) AS date, SUM(click.ClickCost) AS data " +
                    "FROM clicklog AS click " +  "JOIN impressionlog AS impression ON click.id = impression.id " + filterQuery + " AND click.date BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY date";
        }
        if(dataName.equals("genderGraph")){
            query = "SELECT DATE_FORMAT(click.Date, " + timePeriod + " ) AS date, SUM(click.ClickCost) AS data " +
                    "FROM clicklog AS click " +  "JOIN impressionlog AS impression ON click.id = impression.id " + filterQuery + " AND click.date BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY date";
        }
        return query;
    }

    private String filterQueryHelper(String gender, String age, String income, String context){
        if(gender.equals("Any")){
            gender = "IS NOT NULL";
        }
        else{
            gender = "= '" + gender + "'";
        }
        if(age.equals("Any")){
            age = "IS NOT NULL";
        }
        else{
            age = "= '" + age + "'";
        }
        if(income.equals("Any")){
            income = "IS NOT NULL";
        }
        else{
            income = "= '" + income + "'";
        }
        if(context.equals("Any")){
            context = "IS NOT NULL";
        }
        else{
            context = "= '" + context + "'";
        }
        String query = "WHERE impression.Gender " + gender + " AND impression.Age " + age + " AND impression.Income " + income + " AND impression.Context " + context;
        return query;
    }
    public ObservableList<String> getGenders(){
        ObservableList<String> genders = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT Gender FROM impressionLog AS impression " ;
        try{
            rs = statement.executeQuery(query);
            while(rs.next()){
                genders.add(rs.getString("gender"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        genders.add("Any");
        return genders;
    }
    public ObservableList<String> getContext(){
        ObservableList<String> context = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT Context FROM impressionlog";
        try{
            rs = statement.executeQuery(query);
            while(rs.next()){
                context.add(rs.getString("context"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        context.add("Any");
        return context;
    }
    public ObservableList<String> getAge(){
        ObservableList<String> age = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT Age FROM impressionlog";
        try{
            rs = statement.executeQuery(query);
            while(rs.next()){
                age.add(rs.getString("age"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        age.add("Any");
        return age;
    }
    public ObservableList<String> getIncome(){
        ObservableList<String> income= FXCollections.observableArrayList();
        String query = "SELECT DISTINCT Income FROM impressionlog";
        try{
            rs = statement.executeQuery(query);
            while(rs.next()){
                income.add(rs.getString("income"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        income.add("Any");
        return income;
    }

    private static void innit_history(File structure) throws Exception{
        logger.log(Level.INFO,"Inserting history");
        InputStreamReader br;
        try {
            br = new InputStreamReader(new FileInputStream(structure));
            ScriptRunner runner = new ScriptRunner(DataManager.conn,false, false);
            runner.runScript(br);
            br.close();

            logger.log(Level.INFO,"Successfully inserted history");
            history = statement.executeQuery("SELECT history_data, history_date FROM history");
            ResultSet count =  statement.executeQuery("SELECT COUNT (*) AS count FROM history");
            history.last();
            history_position.setValue(count.getInt("count"));
            update();
        }catch (FileNotFoundException e){
            logger.log(Level.WARNING,"History file not found, Loading blank history");
            DataManager.innit_history();
        }

    }

    private static void innit_history() throws Exception{
        logger.log(Level.INFO, "clearing history");
        statement.executeQuery("Alter TABLE history Drop COLUMN *");
        history_position.setValue(0);
        update();
    }

    //update the selected save
    public static void move_saved(int pos){
        logger.log(Level.INFO, "moving saved");
        try {
            if (!saved.absolute(pos)) {
                logger.log(Level.WARNING, "cursor cannot be moved out of bounds!");
            }
        }catch (SQLException e){logger.log(Level.SEVERE, "error with sql server");}

    }

    public static void update(){
        try {
            history = statement.executeQuery("SELECT history_data, history_date FROM history");
            saved = statement.executeQuery("SELECT saved_data, saved_date FROM history");
        }catch(SQLException e){logger.log(Level.SEVERE, "error with sql server");}
    }

    //
    public static void move_history(int pos){
        try {
            logger.log(Level.INFO, "moving history");
            if (!history.absolute(pos)) {
                logger.log(Level.WARNING, "cursor cannot be moved out of bounds!");
            }
            else{
                history_position.setValue(pos);
            }
        }catch (SQLException e){logger.log(Level.SEVERE, "error with sql server");}

    }

    public static void add_save()throws RuntimeException{
            String data;
            String date;
            try {
                data = history.getString("history_data");
                date = history.getString("history_date");
                pstmt = conn.prepareStatement("INSERT INTO save(idsaved, saved_data, saved_date) VALUES (?,?,?)");
                pstmt.setInt(1, saved_id);
                saved_id++;
                pstmt.setString(2, data);
                pstmt.setString(3, date);
                pstmt.addBatch();
                update();

            }catch (SQLException e){
                logger.log(Level.SEVERE,"Error with the save table" );
            }
    }

    public static void delete_saved(){

    }

    public static void add_history(String data,String date)throws RuntimeException{
        try{
            ResultSet count = statement.executeQuery("SELECT COUNT (*) AS count FROM history");
            if(history.isLast()) {

                try {

                    pstmt = conn.prepareStatement("INSERT INTO history(history_data, history_date) VALUES (?,?)");
                    pstmt.setString(1, data);
                    pstmt.setString(2, date);
                    pstmt.addBatch();
                    update();
                    history.next();
                    history_position.setValue(history_position.getValue() + 1);
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Error with the save table");
                }
            }
            else{
                //this drops the remaining rows of history
                statement.executeQuery("WITH ToDelete AS (SELECT *,ROW_NUMBER() OVER (ORDER BY idhistory) AS rn FROM history) DELETE FROM history USING history JOIN ToDelete ON history.idhistory = ToDelete.idhistory WHERE ToDelete.rn > " +  count.getInt("count") + ";"  );
                history_position.setValue(count.getInt("count"));
                }
            update();

        }catch (SQLException e){
            logger.log(Level.SEVERE,"Error with the history table" );
        }

    }


    public XYChart.Series<String, Number> historyUp() throws SQLException{
        try{
            if (history.next()){
                history_position.setValue(history_position.getValue() + 1);
                if(history.getString("history_rate_data") == null){
                    return hist_disp(history.getString("history_data"));
                }else{
                    return hist_disp(history.getString("history_data"),history.getString("history_date"));
                }
            }
            return null;
        }catch (SQLException e){
            logger.log(Level.SEVERE,"Error with the history table" );
            throw new SQLException();
        }
    }

    public XYChart.Series<String, Number> historyDown() throws SQLException{
        try{
            if (history.previous()){
                history_position.setValue(history_position.getValue() -1);
                if(history.getString("history_rate_data") == null){
                    return hist_disp(history.getString("history_data"));
                }else{
                    return hist_disp(history.getString("history_data"),history.getString("history_date"));
                }
            }
            return null;
        }catch (SQLException e){
            logger.log(Level.SEVERE,"Error with the history table" );
            throw new SQLException();
        }
    }

    private XYChart.Series<String, Number> hist_disp(String query1, String query2 )throws RuntimeException {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        try {
            ResultSet rs1 = statement.executeQuery(query1);
            ResultSet rs2 = statement1.executeQuery(query2);
            while (rs1.next() && rs2.next()) {
                String xValue = rs1.getString("date");
                Number yValue = rs1.getInt("data") / (rs2.getInt("data") * 1000);
                series.getData().add(new XYChart.Data<>(xValue, yValue));
            }
            return series;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

        private XYChart.Series<String, Number> hist_disp(String query )throws RuntimeException{
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            try {
                ResultSet rs = statement.executeQuery(query);
                int pageSize = 1000;
                boolean hasMore = rs.next();

                while (hasMore) {
                    for (int i = 0; i < pageSize && hasMore; i++) {
                        String xValue = rs.getString("date");
                        Number yValue = rs.getInt("data");
                        series.getData().add(new XYChart.Data<>(xValue, yValue));
                        hasMore = rs.next();
                    }
                }
                return series;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
    }


    public void setBounceTimeMinute(int newTime){
        bounceTimeMinute = newTime;
    }
    public void setBouncePages(int pages){
        bouncePages = pages;
    }
    public String getBounceTimeMinute(){
        return Integer.toString(bounceTimeMinute);
    }
    public String getBouncePages(){
        return Integer.toString(bouncePages);
    }

    public int selectTotalDataWithinRange(String table, String startDateTime, String endDateTime){
        int totals = 0;
        try{
            String query = "SELECT COUNT(*) FROM " + table +
                    " WHERE date_column >= '" + startDateTime +
                    "' AND date_column <= '" + endDateTime + "'";

            rs = statement.executeQuery(query);
            if (rs.next()) {
                totals = rs.getInt(1); // Retrieve the count from the result set
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return totals;
    }
    public Map<String, Double> getDateAndClickCost(java.lang.String table) {
        java.util.Map<java.lang.String, java.lang.Double> dateAndClickCost = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order

        try {
            java.lang.String query = "SELECT DATE(date) AS Date, clickCost FROM " + table + " ORDER BY Date";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                java.lang.String date = resultSet.getString("Date");
                double clickCost = resultSet.getDouble("clickCost");
                if (clickCost != 0.0) {
                    dateAndClickCost.put(date, clickCost);
                }
                System.out.println("Date: " + date + ", Click Cost: " + clickCost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dateAndClickCost;
    }
}
