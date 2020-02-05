package service.impl;

import service.LinesCountingEligibilityStrategy;

public class EmptyLinesCountingStrategy implements LinesCountingEligibilityStrategy {
    @Override
    public boolean isLineEligibleForCount(String line) {
        return !line.matches("\\s+|\\t+|\\r+|\\v+") && !line.isEmpty();
    }
}
