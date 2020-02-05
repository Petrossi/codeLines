package ui.impl;

import report.ReportService;
import report.impl.ConsoleDefaultReportService;
import ui.UserInterfaceRunner;

import java.io.File;
import java.util.Scanner;

public class ConsoleUserInterfaceRunner implements UserInterfaceRunner {
    ReportService reportService = new ConsoleDefaultReportService();

    @Override
    public void runApplication() {

        boolean isRunning = true;
        while (isRunning) {
            System.out.println("Please enter the file name and press Enter. If you want to exit type X and press Enter:");
            Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter("\\n");
            String userInput = scanner.next();
            if (userInput.equalsIgnoreCase("X")) {
                isRunning = false;
                continue;
            }

            File file = new File(userInput);
            if (file.exists()) {
                reportService.printReport(file);
            }
            else {
                System.out.println("Oops, seems this file doesn't really exist. Try again.");
            }

        }
        System.out.println("Thank you! Bye!");
    }
}
