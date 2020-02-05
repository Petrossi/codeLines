package config;

import service.LinesCountingEligibilityStrategy;
import service.impl.CommentedLinesCountingStrategy;
import service.impl.EmptyLinesCountingStrategy;

import java.util.List;

public class ApplicationConfig {
    public void configureLineCountingStrategies(List<LinesCountingEligibilityStrategy> countingStrategies) {
        countingStrategies.add(new EmptyLinesCountingStrategy());
        countingStrategies.add(new CommentedLinesCountingStrategy());
    }

}
