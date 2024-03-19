package com.application.database;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public  class DataManager {


    private static Connection conn;
    private static Statement statement;
    private static PreparedStatement pstmt;
    private static ResultSet rs;

    static Logger logger = Logger.getLogger(UserManager.class.getName());

//    public static void main(String[] args) throws SQLException {
//    }


    public static void getConn(){
        try {
            conn = DbConnection.getConn();
            statement = conn.createStatement();
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
}