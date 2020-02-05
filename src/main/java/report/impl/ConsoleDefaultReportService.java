package report.impl;

import report.ReportService;
import service.LinesCountingService;
import service.impl.JavaFilesLinesCountingService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleDefaultReportService implements ReportService {
    public static final String INDENTATION_UNIT = "    ";
    private LinesCountingService countingService = new JavaFilesLinesCountingService();

    public void printReport(File file) {
        printNestedFiles(file, 0);
    }

    private void printNestedFiles(File file, int indentation) {
        if (file.isFile()) {
            printReportForSingleFile(file, indentation);
        } else {
            printDirectory(file, indentation);
        }
    }

    private void printDirectory(File file, int indentation) {
        printRootAggregatedValue(file, indentation);
        indentation++;
        printRootContent(file, indentation);
    }

    private void printReportForSingleFile(File file, int indentation) {

        int linesNumber = countingService.countLinesNumber(file);

        printIndentation(indentation);
        System.out.println(file.getName() + " : " + linesNumber);
    }

    private void printRootAggregatedValue(File file, int indentationRelation) {
        printIndentation(indentationRelation);
        int linesNumber = countingService.countLinesNumber(file);
        System.out.println(file.getName() + " : " + linesNumber);
    }

    private void printRootContent(File file, int indentation) {
        List<File> dirs = Arrays.stream(file.listFiles()).filter(f -> f.isDirectory()).collect(Collectors.toList());
        printDirs(dirs, indentation);
        List<File> files = Arrays.stream(file.listFiles()).filter(f -> f.isFile()).collect(Collectors.toList());
        printFiles(files, indentation);
    }

    private void printDirs(List<File> dirs, int indentation){
        for(File file : dirs){
            printDirectory(file, indentation);
        }
    }

    private void printFiles(List<File> singleFiles, int indentation){
        for(File file : singleFiles){
            printReportForSingleFile(file, indentation);
        }
    }

    private void printIndentation(int indentationRelation) {
        for (int i = 0; i < indentationRelation; i++) {
            System.out.print(INDENTATION_UNIT);
        }
    }

}
