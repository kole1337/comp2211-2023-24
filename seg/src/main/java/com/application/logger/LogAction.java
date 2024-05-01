package com.application.logger;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.Logger;

public class LogAction {

    /**
     * Separate logger class that is
     * responsible for handling logging actions
     * to a file.
     * @author Nikola Parushev
     * */

    private static String filename = "";

    /**
     * When the function is called in
     * {@link com.application.login.LoginController}
     * it is creating a new session file in the format
     * username-currentTimeAndDate.txt
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
    public LogAction(){

    }

    /**
     A function that writes
     @param message to
     a log file of the current session.
     */
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
