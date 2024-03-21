package com.application.dashboard;

import com.application.database.DataManager;
import com.application.database.DbConnection;
import com.application.database.Multithread_ImpressionDb;
import com.application.files.FilePathHandler;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

import static javafx.application.Application.launch;

/**
 * This is a test file. It should be ignored.
 * */
public class testFile extends Application {


    @Override
    public void start(Stage stage) throws Exception {
//        DataManager dataman = new DataManager();
//        DbConnection dbconn = new DbConnection();
//        //dbconn.readFromFile("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\user.txt");
//        DashboardController dc = new DashboardController();
//
//        FilePathHandler fph = new FilePathHandler();
//        fph.setClickPath("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\click_log.csv");
//
//
////        Multithread_ClicksDb multiclicks = new Multithread_ClicksDb(fph,dc, Thread.currentThread());
////        multiclicks.run();
////
//        System.out.println(dbconn.checkConn());
        String test =  getClass().getClassLoader().getResource("darktheme.css").toExternalForm();


//        Multithread_ImpressionDb mbsq = new Multithread_ImpressionDb();
        System.out.println(test);


            //function to check file length
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
