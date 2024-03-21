package com.application.database;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DbConnection {
    private static Connection conn;
    private static Logger logger = Logger.getLogger(DbConnection.class.getName());
    private static String user;
    private static String pass;

    private static  String url = "jdbc:mysql://localhost:3306/adda";



    public DbConnection(){

        readFromFile("user.txt");

        try {
            makeConn();
        }catch(Exception e){
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("DATABASE ERROR");
            a.setTitle("ERROR!");
            a.setContentText("THE SERVER IS NOT RUNNING OR IS NOT CONNECTED! PLEASE, CONTACT YOUR ADMINISTRATOR!");
            a.show();
        }
    }

    /**
     * establishes a connection to the SQL databaase, pushes the connection onto other database modifying classes
     * @param user username for database access
     * @param pass password for database access
     * @throws Exception if the username/password are invalid will throw an SQL exception, if no connection can be established a runtime error will be thrown
     */
    public static void makeConn()throws Exception{

        try {
            logger.log(Level.INFO, "establishing connection");

            conn = DriverManager.getConnection(url,getUser() , getPass());
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

    public static void readFromFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File does not exist.");
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            user = reader.readLine();
            pass = reader.readLine();
            reader.close();
            System.out.println("Username: " + user);
            System.out.println("Password: " + pass);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    public static void createPassFile(){
        try {
            File myObj = new File("seg\\comp2211\\seg\\src\\main\\resources\\user.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                System.out.println(myObj.getAbsolutePath());

            } else {
                System.out.println("File already exists.");
                System.out.println(myObj.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String getUser() {
        return user;
    }

    public static String getPass() {
        return pass;
    }

    public static String getUrl(){
        return url;
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

    public Boolean checkConn() throws SQLException {
        try {
            Statement statement = conn.createStatement();
            statement.execute("SELECT 1");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static Connection getConn(){
        return conn;
    }

    public static void main(String[] args) throws SQLException {
    }


}
