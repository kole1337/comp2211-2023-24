package com.application.database;

import java.sql.*;
public class dbConnection {
    static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/adda","root", "jojo12345");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Statement statement;

    static {
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static PreparedStatement pstmt;
    static ResultSet rs;

    public dbConnection() throws SQLException {
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

    public static void selectAll() throws SQLException {
        rs = statement.executeQuery("select * from users");
        while(rs.next()){
            System.out.println(rs.getString("username") + ", " + rs.getString("password"));
        }
    }

    public static Boolean selectUser( String user, String pass) throws SQLException{
        rs = statement.executeQuery("select username, password from users");

        while(rs.next()){
            if (rs.getString("username").equals(user) && rs.getString("password").equals(pass)) return true;
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
