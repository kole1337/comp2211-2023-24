package com.application.shenanigans;

import com.application.database.*;
import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import com.application.logger.LogAction;
import com.application.styles.checkStyle;
import eu.hansolo.tilesfx.Tile;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javafx.application.Application.launch;

/**
 * This is a test file. It should be ignored.
 * */
public class testFile{

    public static void main(String[] args){

        Encryption enc = new Encryption();
        String test = enc.encrypt("gesh");
        String test2 = "gesh";
        System.out.println(test.equals(enc.encrypt(test2)));

    }

}
