package service.impl;

import config.ApplicationConfig;
import service.LinesCountingEligibilityStrategy;
import service.LinesCountingService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JavaFilesLinesCountingService implements LinesCountingService {
    private static final int RESULT_DEFAULT_VALUE = 0;
    public static final String JAVA_FORMAT = ".java";
    private List<LinesCountingEligibilityStrategy> countingStrategies = new ArrayList<>();

    {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.configureLineCountingStrategies(countingStrategies);
    }

    @Override
    public int countLinesNumber(File file) {
        int aggregatedCount = 0;

        aggregatedCount = countAll(file, aggregatedCount);

        return aggregatedCount;
    }

    private int countAll(File file, int aggregatedCount) {
        if (file.isFile()) {
            if(file.getName().endsWith(JAVA_FORMAT)) {
                aggregatedCount += countSingleFileLines(file);
            }else{
                System.out.println(file.getName() + " is not in .java format.");
                return 0;
            }
        } else {
            aggregatedCount += countAllFiles(file, aggregatedCount);
        }
        return aggregatedCount;
    }

    private int countSingleFileLines(File file) {
        int result = RESULT_DEFAULT_VALUE;
        List<String> fileLines = null;
        try {
            fileLines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : fileLines) {
            if (isLineEligibleForCount(line)) {
                result++;
            }
        }
        cleanUpStrategy();
        return result;
    }

    private int countAllFiles(File file, int aggregatedValue) {
        List<File> singleFiles = Arrays.stream(file.listFiles()).filter(f -> f.isFile()).collect(Collectors.toList());
        aggregatedValue += countSingleFilesAggregated(singleFiles);
        List<File> dirs = Arrays.stream(file.listFiles()).filter(f -> f.isDirectory()).collect(Collectors.toList());

        aggregatedValue += countDirectoriesAggregated(dirs);

        return aggregatedValue;
    }

    private int countDirectoriesAggregated(List<File> dirs) {
        int aggregatedValue = 0;
        for (File dir : dirs) {
            int tempAggr = 0;
            aggregatedValue += countAll(dir, tempAggr);
        }
        return aggregatedValue;
    }

    private int countSingleFilesAggregated(List<File> files) {
        int aggregatedValue = 0;
        for (File file : files) {
            aggregatedValue += countSingleFileLines(file);
        }
        return aggregatedValue;
    }

    private boolean isLineEligibleForCount(String line) {
        for (LinesCountingEligibilityStrategy strategy : countingStrategies) {
            if (!strategy.isLineEligibleForCount(line)) {
                return false;
            }
        }
        return true;
    }

    private void cleanUpStrategy() {
        countingStrategies.stream()
                .map(CommentedLinesCountingStrategy.class::cast)
                .peek(s -> s.setPreviousLineOpenComment(false));
    }

    public List<LinesCountingEligibilityStrategy> getCountingStrategies() {
        return countingStrategies;
    }

    public void setCountingStrategies(List<LinesCountingEligibilityStrategy> countingStrategies) {
        this.countingStrategies = countingStrategies;
    }

}
