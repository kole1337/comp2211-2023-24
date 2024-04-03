package com.application.logger;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.Logger;

public class LogAction {

    /**
     * Separate logger class that would be
     * responsible for handling logging
     * */

    private static String filename = "";

    /**
     *
     * */
    public LogAction(String user){
        try {
            java.util.Date currentTime = new java.util.Date();
            String formatedDate = currentTime.toString().replace(' ','-');
            formatedDate = formatedDate.toString().replace(':','-');

            String sessionName = user+ "-" + formatedDate +".txt";
            File myObj = new File(sessionName);

            filename = myObj.getName();

            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                logActionToFile("Starting session: " + user + " at " +currentTime);
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void logActionToFile(String message){
        try{
            FileWriter myWriter = new FileWriter(filename,true);

            myWriter.write(message+"\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
