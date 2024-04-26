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

    private static Encryption encryption = new Encryption();


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

            if (rs.getString("username").equals(user) && rs.getString("password").equals(encryption.encrypt(pass)) && rs.getString("role").equals("user")){
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
            if (rs.getString("username").equals(user) && rs.getString("password").equals(encryption.encrypt(pass)) && rs.getString("role").equals("admin")){
                logger.log(Level.INFO, "Admin exists.");
                return true;
            }
        }


        return false;
    }

    public static void insertUser(String username, String pass, String role){

        String inUser = "INSERT INTO users(username, password, role) VALUES (?,?,?)";
        try {
            pstmt = conn.prepareStatement(inUser);
            pstmt.setString(1, username);
            pstmt.setString(2, encryption.encrypt(pass));
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            System.out.println("INSERTED!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateUserPasswordUsername(String username, String newPassword){
        logger.log(Level.INFO, "Updating password.");
        try {
            String updatePass = "UPDATE users SET password = ? WHERE username = ?";
            System.out.println(username + ":" + newPassword);
            pstmt = conn.prepareStatement(updatePass);
            pstmt.setString(1, encryption.encrypt(newPassword));
            pstmt.setString(2, username);


            pstmt.execute();
            logger.log(Level.INFO, "Updated!");

        }catch (Exception e){
            e.printStackTrace();
            logger.log(Level.INFO, "Ooops! Error: " + e.getMessage());

        }
    }

    public static void updateUserPasswordID(String id, String newPassword){
        logger.log(Level.INFO, "Updating password.");
        try {
            String updatePass = "UPDATE users SET password = ? WHERE user_id = ?";
            System.out.println(id + ":" + newPassword);

            pstmt = conn.prepareStatement(updatePass);
            pstmt.setString(1, encryption.encrypt(newPassword));
            pstmt.setInt(2, Integer.parseInt(id));

            pstmt.execute();
            logger.log(Level.INFO, "Updated!");

        }catch (Exception e){
            e.printStackTrace();
            logger.log(Level.INFO, "Ooops! Error: " + e.getMessage());

        }
    }

    public void updateUsername(String oldUsername, String newUsername){
        logger.log(Level.INFO,"Update username");
        try{
            String updateUsername = "UPDATE users SET username = ? WHERE username =?";
            pstmt = conn.prepareStatement(updateUsername);

            pstmt.setString(1, newUsername);
            pstmt.setString(2, oldUsername);

            pstmt.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updatePasswordWithUsername(String newPassword, String username){
        logger.log(Level.INFO,"Update password with username");
        try{
            String updateUsername = "UPDATE users SET password = ? WHERE username =?";
            pstmt = conn.prepareStatement(updateUsername);

            pstmt.setString(1, encryption.encrypt(newPassword));
            pstmt.setString(2, username);

            pstmt.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateUserId(int oldId, int newId){
        logger.log(Level.INFO,"Update user id");
        try{
            String updateUsername = "UPDATE users SET user_id = ? WHERE user_id =?";
            pstmt = conn.prepareStatement(updateUsername);

            pstmt.setInt(1, newId);
            pstmt.setInt(2, oldId);

            pstmt.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updatePasswordWithId(String newPassword, int id){
        logger.log(Level.INFO,"Update password with id");
        try{
            String updateUsername = "UPDATE users SET password = ? WHERE user_id =?";
            pstmt = conn.prepareStatement(updateUsername);

            pstmt.setString(1, encryption.encrypt(newPassword));
            pstmt.setInt(2, id);

            pstmt.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
