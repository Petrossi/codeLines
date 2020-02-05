package service.impl;

import service.LinesCountingEligibilityStrategy;

public class CommentedLinesCountingStrategy implements LinesCountingEligibilityStrategy {
    private Boolean isPreviousLineOpenComment = false;

    @Override
    public boolean isLineEligibleForCount(String line) {
        boolean shouldLineCount;

        boolean isCurrentLineOpen = isCommentOpen(line);
        shouldLineCount = shouldCountLine(line, isCurrentLineOpen);
        isPreviousLineOpenComment = isCurrentLineOpen;

        return shouldLineCount;
    }

    private boolean shouldCountLine(String line, boolean isCurrentLineOpen) {
        boolean result;
        if (isPreviousLineOpenComment) {
            if (isCurrentLineOpen) {
                result = processOpenMultiComments(line);
            } else {
                result = processClosedMultiComments(line);
            }
        } else {
            if (isCurrentLineOpen) {
                String subStrBeforeOpenSign = line.substring(0, line.indexOf("/*")).trim();
                if (subStrBeforeOpenSign.isEmpty()) {
                    result = processOpenMultiComments(line);
                } else {
                    result = true;
                }
            } else {
                if (line.contains("/*")) {
                    String subStrBeforeOpenSign = line.substring(0, line.indexOf("/*")).trim();
                    if (subStrBeforeOpenSign.isEmpty()) {
                        result = processClosedMultiComments(line);
                    } else {
                        result = true;
                    }
                } else {
                    result = true;
                }
            }
        }

        return result;
    }

    private boolean processClosedMultiComments(String line) {
        String substrAfterClosingSign = line.substring(line.indexOf("*/") + 2);
        if (substrAfterClosingSign.isEmpty()) {
            return false;
        } else {
            if (substrAfterClosingSign.startsWith("/*")) {
                return processClosedMultiComments(substrAfterClosingSign);
            } else {
                return true;
            }
        }
    }

    private boolean processOpenMultiComments(String line) {
        boolean result;
        if (line.contains("*/")) {
            String subString = line.substring(line.indexOf("*/") + 2);
            if (subString.startsWith("/*")) {
                result = processOpenMultiComments(subString);
            } else {
                result = true;
            }
        } else {
            result = false;
        }
        return result;
    }

    private boolean isCommentOpen(String line) {
        if (isPreviousLineOpenComment) {
            return checkClosingSignForMultiComments(line);
        } else {
            return checkOpenSignForMultiComments(line);
        }
    }

    private boolean checkOpenSignForMultiComments(String line) {
        if (line.contains("/*")) {
            return checkClosingSignForMultiComments(line);
        } else {
            return false;
        }
    }

    private boolean checkClosingSignForMultiComments(String line) {
        if (line.contains("*/")) {
            String substr = line.substring(line.indexOf("*/")+2);
            return checkOpenSignForMultiComments(substr);
        } else {
            return true;
        }
    }

    public void setPreviousLineOpenComment(Boolean previousLineOpenComment) {
        isPreviousLineOpenComment = previousLineOpenComment;
    }

    public Boolean getPreviousLineOpenComment() {
        return isPreviousLineOpenComment;
    }
}
