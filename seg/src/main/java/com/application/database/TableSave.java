package com.application.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableSave {

    private static Statement statement;
    private static ResultSet rs;
    private static final String os = System.getProperty("os.name").toLowerCase();

    static Logger logger = Logger.getLogger(UserManager.class.getName());


    public static void save_default(){
        try{
            ResultSet info  = statement.executeQuery("SELECT  COLUMN_NAME FROM INFORMATION_SCHEME.COLUMNS\n" +
                    " WHERE TABLE_NAME = 'save'");
            Array p = info.getArray(1);
            try{info.next();
                savetable("save","savetable_backup");
            }
            catch (Exception e ){
                logger.log(Level.INFO,"Nothing to save!" );
            }
        }
        catch (Exception ignored){}
    }

    public static void save_default(String filename){
        try{
            ResultSet info  = statement.executeQuery("SELECT  COLUMN_NAME FROM INFORMATION_SCHEME.COLUMNS\n" +
                    " WHERE TABLE_NAME = 'save'");
            Array p = info.getArray(1);
            try{info.next();
                savetable("save",filename);
            }
            catch (Exception e ){
                logger.log(Level.INFO,"Nothing to save!" );
            }
        }
        catch (Exception ignored){}
    }


    public static void savetable (String tablename,String filename){
        File batFile;
        if (os.contains("windows")){
             batFile = new File("src/main/resources/saved/temp.bat");
        }else{
             batFile = new File("src/main/resources/saved/temp.sh");
        }
        try{
            //TODO: make this dynamic I guess, maybe ask for the user to input where they wanna save it ui people job

            BufferedWriter bw = new BufferedWriter(new FileWriter(batFile));
                bw.write("cd " + batFile.getPath() + "\n");

            bw.write("mysqldump -u " + DbConnection.getUser() + " -p"+ DbConnection.getPass() + " adda " + tablename + " > " + filename + ".sql");
            ProcessBuilder prepare = new ProcessBuilder(batFile.getName());
            Process p = prepare.start();
        }catch (Exception e ){
            logger.log(Level.SEVERE,"Error with the mysqldump table" );
        }
        batFile.delete();

    }
}
