package com.application.dashboard;

import java.io.*;
import com.application.dashboard.ReadFile;
import com.application.database.DataManager;
import com.application.database.DbConnection;
import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

/**
 * This is a test file. It should be ignored.
 * */
public class testFile extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        DataManager dataman = new DataManager();
        DbConnection dbconn = new DbConnection();
        //dbconn.readFromFile("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\user.txt");

        //dbconn.makeConn("root", "jojo12345");
        //dbconn.createPassFile();


//        InputStream is = new BufferedInputStream(new FileInputStream(fph.getImpressionPath()));
//        int count = 0;
//        int readChars = 0;
//        try {
//                byte[] c = new byte[1024];
//
//                boolean empty = true;
//                while ((readChars = is.read(c)) != -1) {
//                    empty = false;
//                    for (int i = 0; i < readChars; ++i) {
//                        if (c[i] == '\n') {
//                            ++count;
//                        }
//                    }
//                }
//
//            } finally {
//                is.close();
//            }
//        System.out.println(count);

        //dataman.loadCSVintoDB();
        }


    public static void main(String[] args) {
        launch(args);
    }
}
