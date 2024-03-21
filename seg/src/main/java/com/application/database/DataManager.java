package com.application.database;
import java.sql.*;
import java.text.SimpleDateFormat;
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

    public void addClickLog(String date, String id, double clickCost) throws SQLException {
        String inClickLog = "INSERT INTO clicklog(date, id, clickCost) VALUES (?,?,?)";

        pstmt = conn.prepareStatement(inClickLog);
        pstmt.setString(1, date);
        pstmt.setString(2, id);
        pstmt.setDouble(3, clickCost);
        pstmt.executeUpdate();
        System.out.println("INSERTED!");
    }
    public void addImpressionLog(String date, String id, String gender,
                                 String age, String income, String context,
                                 Double impressionCost) throws SQLException {
        String inClickLog = "INSERT INTO impressionlog(date, id, gender, age, income, context, impression_cost) VALUES (?,?,?,?,?,?,?)";

        pstmt = conn.prepareStatement(inClickLog);
        pstmt.setString(1, date);
        pstmt.setString(2, id);
        pstmt.setString(3, gender);
        pstmt.setString(4, age);
        pstmt.setString(5, income);
        pstmt.setString(6, context);
        pstmt.setDouble(7, impressionCost);
        pstmt.executeUpdate();
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
}