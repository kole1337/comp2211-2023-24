package com.application.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DbConnection {
    private static Connection conn;
    private static Statement statement;
    static PreparedStatement pstmt;
    static ResultSet rs;
    static Logger logger = Logger.getLogger(DbConnection.class.getName());
    
    

    public static void getConnection(String user, String pass)throws Exception{
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adda",user , pass);
            statement = conn.createStatement();
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

    public static void closeConnection()throws RuntimeException{
        try {
            conn.close();
        }
        catch (Exception e){
            logger.log(Level.SEVERE,"Could not close conncetion to sql server" );
            throw new RuntimeException("could not close connection");
        }
    }

    private static ResultSet runQuerry(String state , String table){
        try{
            return statement.executeQuery(state);

        }
        catch(SQLException e){
            logger.log(Level.SEVERE, "Could not carry out querry ");
            throw e;
        }
    }



    public DbConnection() throws SQLException {
    }

    public static void main(String[] args) throws SQLException {


            //connect to the db

            //statemts

            //exec querry

            //process the results
//            while(rs.next()){
//                System.out.println(rs.getString("username") + ", " + rs.getString("password"));
//            }

//            String insertGesh = " INSERT INTO `adda`.`users`" +
//                    "(`username`,`password`)" +
//                    "VALUES" +
//                    "(\"gesh5\",\"1234\");";
//            statement.executeUpdate(insertGesh);

//        if(selectUser("gesh6", "1234")) {
//            System.out.println("User exists");
//        }else System.out.println("nuh uh");
        insertUser("gesh69", "1234", "user");
    }



}
