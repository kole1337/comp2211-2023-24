package com.application.database;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataManager {


    private static Connection conn;
    static Statement statement;
    static PreparedStatement pstmt;
    static ResultSet rs;

    private static String user = "";
    private static String password = "";
    static Logger logger = Logger.getLogger(UserManager.class.getName());

    public static void main(String[] args) throws SQLException {
    }


    static{
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adda", user, password);
            statement = conn.createStatement();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Username or password are not valid");
            e.printStackTrace();
            ;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not establish connection with SQL database");
            throw new RuntimeException();
        }
    }

    public static void closeConnection() throws RuntimeException {
        try {
            conn.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not close conncetion to sql server");
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