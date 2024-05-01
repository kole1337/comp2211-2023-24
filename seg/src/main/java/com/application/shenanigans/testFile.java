package com.application.shenanigans;

import com.application.database.*;

import static javafx.application.Application.launch;

/**
 * This is a test file. It should be ignored.
 * */
public class testFile{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/adda";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "jojo12345";
    static DbConnection db = new DbConnection();
    static UserManager um = new UserManager();
    static DataManager dm = new DataManager();

    static String currentUser = "test30";
    public static void main(String[] args){
        try {
            db.closeConn();
            db.makeConn();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
