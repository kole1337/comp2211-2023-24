package com.application.database;

import java.sql.SQLException;

public class UserManager {

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

        pstmt = DbConnection.runquerry(inUser);
        pstmt.setString(1, username);
        pstmt.setString(2, pass);
        pstmt.setString(3, role);
        pstmt.executeUpdate();
        System.out.println("INSERTED!");
    }

}
