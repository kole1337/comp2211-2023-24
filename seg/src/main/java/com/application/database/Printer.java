package com.application.database;


    //package com.application.dashboard;



import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.stage.Window;


    public class Printer {

        /*    Button printButton = new Button("Print");
        printButton.setOnAction(event -> {
                boolean result = PrinterUtil.print(yourNodeToPrint, yourStage);
                if (result) {
                    System.out.println("Print job successfully sent to printer.");
                } else {
                    System.out.println("Print job failed or was cancelled by the user.");
                }
            });
        */
        public static boolean print(Node node, Window ownerWindow) {
            // Create a printer job for the default printer
            PrinterJob job = PrinterJob.createPrinterJob();

            if (job != null) {
                // Show the print setup dialog
                boolean proceed = job.showPrintDialog(ownerWindow);

                if (proceed) {
                    // Print the node and check for success
                    boolean printed = job.printPage(node);
                    if (printed) {
                        // End the printer job successfully
                        job.endJob();
                    } else {
                        // Cancel the printer job
                        job.cancelJob();
                    }

                    return printed;
                } else {
                    // The user canceled the print job
                    return false;
                }
            } else {
                // No printer job can be created, possibly no printers are installed.
                return false;
            }
        }
    }

