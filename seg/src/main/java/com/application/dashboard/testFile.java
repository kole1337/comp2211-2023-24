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
//        FileChooserWindow fc = new FileChooserWindow();
//        FilePathHandler obj = new FilePathHandler();
//        //File selected = ;
//        for (int i = 0; i < 1; i++) {
//            //fc.openFileBox();
//            obj.fileTypeHandler(fc.openFileBox());
//        }
//
//        System.out.println(obj.getClickPath());
//        System.out.println(obj.getImpressionPath());
//
//        MergeCSV obj2 = new MergeCSV();
//        obj2.main(obj.getClickPath(), obj.getImpressionPath());

        DataManager dataman = new DataManager();
        DbConnection dbconn = new DbConnection();
        dbconn.makeConn("root", "jojo12345");
        //dataman.addClickLog("2015-01-01 12:12:47", "12454225425", 11.311353);
//        dataman.addImpressionLog("2015-01-01 12:00:02",
//                "1243234513",
//                "Male",
//                "25-34",
//                "High",
//                "News",
//                0.001713);
//        dataman.addServerLog("2015-01-01 12:01:21",
//                "1234412341", "2015-01-01 12:05:32",
//                7, "No");

            int[] vals = dataman.getUniqueAppearanceInt("context", "impressionlog");
            String[] names = dataman.getUniqueAppearanceString("context", "impressionlog");
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
        }

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
        }


    public static void main(String[] args) {
        launch(args);
    }
}
