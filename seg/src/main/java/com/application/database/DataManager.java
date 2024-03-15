package com.application.database;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public  class DataManager {


    private static Connection conn;
    private static Statement statement;
    private static PreparedStatement pstmt;
    private static ResultSet rs;

    static Logger logger = Logger.getLogger(UserManager.class.getName());

    public static void main(String[] args) throws SQLException {
    }


    public static void getconn(){
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
}