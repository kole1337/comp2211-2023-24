package com.application.database;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FileSplit {
    public static ArrayList<String> splitFile(File file, int sizeOfFileInMB) throws IOException {
        int counter = 1;
        ArrayList<String> files = new ArrayList<String>();
        int sizeOfChunk = 1024 * 1024 * sizeOfFileInMB;
        String eof = System.lineSeparator();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String name = file.getName();
            String line = br.readLine();
            while (line != null) {
                File newFile = new File(file.getParent(), name + "."
                        + String.format("%03d", counter++));
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile))) {
                    int fileSize = 0;
                    while (line != null) {
                        byte[] bytes = (line + "\n").getBytes(Charset.defaultCharset());
                        if (fileSize + bytes.length > sizeOfChunk)
                            break;

                        out.write(bytes);
                        fileSize += bytes.length;
                        line = br.readLine();
                    }
                }
                files.add(newFile.getAbsolutePath());
            }
        }

        return files;

    }

//    public static void main(String[] args) throws IOException {
//        splitFile(new File("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\impression_log.csv"),10);
////        splitFile(new File("D:\\year2\\seg\\docs\\2_month_campaign\\impression_log.csv"),15);
//
//        //D:\year2\seg\comp2211\seg\src\main\resources\2_week_campaign_2\impression_log.csv
//        //
//    }
}