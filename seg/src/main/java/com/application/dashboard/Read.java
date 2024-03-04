package com.application.dashboard;


import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

//public class Read {
//    public static void main(String[] args) {
//        // Reading CSV file
//        try (CSVReader reader = new CSVReaderBuilder(new FileReader("C:\\\\Users\\\\gouri\\\\OneDrive - University of Southampton\\\\Documents\\\\year2\\\\click_log.csv")).withSkipLines(0).build()) {
//            List<String[]> data = reader.readAll();
//            // Process the data
//            for (String[] row : data) {
//                for (String cell : row) {
//                    System.out.print(cell + "\t");
//                }
//                System.out.println();
//            }
//        } catch (IOException | CsvException e) {
//            e.printStackTrace();
//        }
//    }
//}
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

//public class Read {
//    public static void main(String[] args) {
//        // Create a Table object to store the columns
//        Table table = new Table();
//        //String file = Merge.main(new String[0]);
//        try (CSVReader reader = new CSVReader(new FileReader("C:\\Users\\gouri\\OneDrive - University of Southampton\\Documents\\year2\\merge.csv"))) {
//            reader.skip(0); // Skip the header row
//            List<String[]> data = reader.readAll();
//
//            // Populate the table with data from the CSV file
//            for (String[] row : data) {
//                if (row.length >= 12) {
//                    Row newRow = new Row(row[0], row[1], row[2],row[3],row[4],row[5],row[6],row[7],row[8],row[9],row[10],row[11]);
//                    table.addRow(newRow);
//                } else {
//                    // Handle cases where the row doesn't have enough columns
//                    // You may want to log or handle this scenario according to your requirements
//                    System.out.println(String.join(",", row));
//                }
//            }
//
//             //Print the table with column headings
//            //System.out.printf("%-25s %-15s %-15s%n", "Date", "ID", "Click Cost");
//            List<Row> rows = table.getRows();
//            for (Row row : rows) {
//                System.out.printf( row.getDate(), row.getID(), row.getClickCost(),row.getGender(),row.getAge(),row.getSalary(),row.getContext(),
//                        row.getImpCost(),row.getEnDate(),row.getExDate(),row.getPgView(),row.getCon());
//            }
//
//        } catch (IOException | CsvException e) {
//            e.printStackTrace();
//        }
//    }
//}
//        import com.opencsv.CSVReader;
//        import com.opencsv.CSVReaderBuilder;
//        import com.opencsv.exceptions.CsvException;
//
//        import java.io.FileReader;
//        import java.io.IOException;
//        import java.util.List;
//
//        import java.io.FileReader;
//        import java.io.IOException;
//        import java.util.List;
//
//        import com.opencsv.CSVReader;
//        import com.opencsv.exceptions.CsvException;
//
////public class Read {
////    public static void main(String[] args) {
////        // Reading CSV file
////        String file = Merge.main(new String[0]);
////        try (CSVReader reader = new CSVReader(new FileReader(file))) {
////            List<String[]> data = reader.readAll();
////            // Process the data
////            for (String[] row : data) {
////                for (String cell : row) {
////                    System.out.print(cell + "\t");
////                }
////                System.out.println();
////            }
////        } catch (IOException | CsvException e) {
////            e.printStackTrace();
////        }
////    }
////}
//
//
public class Read {

    public static void main(String[] args) throws IOException, CsvException {


        // Open the file as an InputStream


        String fileName = "/2_week_campaign_2/click_log.csv";
        InputStream inputStream = testFile.class.getResourceAsStream(fileName);

        String file = Merge.main(new String[0]);
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> r = reader.readAll();
            r.forEach(x -> System.out.println(Arrays.toString(x)));
        }
        int numberOfUniqueValues = countUniqueValuesSecondColumn(inputStream);
        System.out.println(numberOfUniqueValues);

    }

    public static int countUniqueValuesSecondColumn(InputStream streamPath) {
        Set<String> uniqueValues = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(streamPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 2) {
                    String secondColumnValue = columns[1].trim(); // Assuming second column index is 1
                    uniqueValues.add(secondColumnValue);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uniqueValues.size();
    }
}


// Writing CSV file





