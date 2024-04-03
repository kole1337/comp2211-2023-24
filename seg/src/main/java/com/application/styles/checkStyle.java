package com.application.styles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class checkStyle {
    public String checkStyle(){
        String theme = "";

        try {
            File myObj = new File("theme.txt");
            Scanner myReader = new Scanner(myObj);
            theme = myReader.nextLine();

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return theme;
    }

}
