package com.application.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserManager {

    private static Connection conn;
    static Statement statement;
    static PreparedStatement pstmt;
    static ResultSet rs;
    static Logger logger = Logger.getLogger(UserManager.class.getName());



    static {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adda", "root", "jojo12345");
            statement = conn.createStatement();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Username or password are not valid");
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



    public UserManager() throws Exception {
    }

    public static void selectAll() throws SQLException {
        rs = statement.executeQuery("select * from users");
        while(rs.next()){
            System.out.println(rs.getString("username") + ", " + rs.getString("password"));
        }
    }

    public static Boolean selectUser( String user, String pass) throws SQLException{
        rs = statement.executeQuery("select username, password, role from users");

        while(rs.next()){
            if (rs.getString("username").equals(user) && rs.getString("password").equals(pass) && rs.getString("role").equals("user")) return true;
        }
        return false;
    }

    public static Boolean selectAdmin( String user, String pass) throws SQLException{
        rs = statement.executeQuery("select username, password, role from users");

        while(rs.next()){
            if (rs.getString("username").equals(user) && rs.getString("password").equals(pass)&& rs.getString("role").equals("admin")) return true;
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
