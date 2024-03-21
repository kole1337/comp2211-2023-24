package com.application.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserManager {

    private static Connection conn;
    private static Statement statement;
    private static PreparedStatement pstmt;
    private static ResultSet rs;
    private static final Logger logger = Logger.getLogger(UserManager.class.getName());



    public static void getConn(){
        try {
            conn = DbConnection.getConn();
            statement = conn.createStatement();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Could not create statement");
            e.printStackTrace();
        }
    }

    public static void closeConnection()throws RuntimeException{
        try {
            conn.close();
        }
        catch (Exception e){
            logger.log(Level.SEVERE,"Could not close conncetion to sql server" );
            throw new RuntimeException("could not close connection");
        }
    }

    private static ResultSet runQuerry(String state , String table) throws SQLException {
        try{
            return statement.executeQuery(state);

        }
        catch(SQLException e){
            logger.log(Level.SEVERE, "Could not carry out querry ");
            throw e;
        }
    }





    public static ResultSet selectAll() throws SQLException {
        rs = statement.executeQuery("select * from users");
//        while(rs.next()){
//            System.out.println(rs.getString("username") + ", " + rs.getString("password"));
//        }
        return rs;
    }

    public static Boolean selectUser( String user, String pass) throws SQLException{
        rs = statement.executeQuery("select username, password, role from users");

        while(rs.next()){

            if (rs.getString("username").equals(user) && rs.getString("password").equals(pass) && rs.getString("role").equals("user")){
                logger.log(Level.INFO, "User exists.");
                return true;
            }
        }
        return false;
    }

    public static Boolean selectAdmin( String user, String pass) throws SQLException{
        //getconn();
        rs = statement.executeQuery("select username, password, role from users");

        while(rs.next()){
            if (rs.getString("username").equals(user) && rs.getString("password").equals(pass)&& rs.getString("role").equals("admin")){
                logger.log(Level.INFO, "Admin exists.");
                return true;
            }
        }


        return false;
    }

    public static void insertUser(String username, String pass, String role) throws SQLException{

        String inUser = "INSERT INTO users(username, password, role) VALUES (?,?,?)";

        pstmt = conn.prepareStatement(inUser);
        pstmt.setString(1, username);
        pstmt.setString(2, pass);
        pstmt.setString(3, role);
        pstmt.executeUpdate();
        System.out.println("INSERTED!");
    }

}
