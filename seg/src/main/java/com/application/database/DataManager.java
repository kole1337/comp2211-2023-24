package com.application.database;
import java.sql.*;
public class DataManager {

     static Connection conn;
    public static void main (String[] args) throws SQLException {


        try {
            conn = DriverManager.getConnection ("jdbc:mysql://localhost:3306/db");

            Statement Stmt = conn.createStatement ("");
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (Exception exception) {
            System.err.println ("SQLException : " + exception.toString () + "\n stack trace: ");
            exception.printStackTrace();
        } finally {
            conn.close ();
        }
    }

    public static void deleteTable(String tablename){

    }
}
