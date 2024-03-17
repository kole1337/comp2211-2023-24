package com.application.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DbConnection {
    private static Connection conn;
    private static Logger logger = Logger.getLogger(DbConnection.class.getName());


    /**
     * establishes a connection to the SQL databaase, pushes the connection onto other database modifying classes
     * @param user username for database access
     * @param pass password for database access
     * @throws Exception if the username/password are invalid will throw an SQL exception, if no connection can be established a runtime error will be thrown
     */
    public static void makeConn(String user, String pass)throws Exception{
        try {
            logger.log(Level.INFO, "establishing connection");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adda",user , pass);
            DataManager.getConn();
            UserManager.getConn();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Username or password are not valid");
            e.printStackTrace();
            throw e;
        }
        catch (Exception e){
            logger.log(Level.SEVERE , "Could not establish connection with SQL database");
            throw new RuntimeException();
        }
    }

    /**
     * Will close all connections to the sql server
     * @throws RuntimeException
     */
    public static void closeConnection()throws RuntimeException{
        try {
            logger.log(Level.INFO , "Closing Main connection");
            conn.close();
        }
        catch (Exception e){
            logger.log(Level.SEVERE,"Could not close connection to SQL server" );
            throw new RuntimeException("could not close connection");
        }

        try{
            logger.log(Level.INFO , "Closing DataManager connection");
            DataManager.closeConnection();
            logger.log(Level.INFO , "Closing UserManager connection");
            UserManager.closeConnection();
        }catch (Exception e){
            logger.log(Level.SEVERE, "Failed to close connection to SQL server");
            throw new RuntimeException("could not close connection");
        }
    }


    public static Connection getConn(){
        return conn;
    }

    public static void main(String[] args) throws SQLException {
    }
}
