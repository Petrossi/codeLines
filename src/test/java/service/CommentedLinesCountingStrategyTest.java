package service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import service.impl.CommentedLinesCountingStrategy;

@RunWith(JUnit4.class)
public class CommentedLinesCountingStrategyTest {
    private CommentedLinesCountingStrategy testedInstance = new CommentedLinesCountingStrategy();

    @After
    public void cleanUp() {
        testedInstance.setPreviousLineOpenComment(false);
    }

    @Test
    public void shouldReturnTrueForEligibleLine() {
        String line = "normal";
        boolean result = testedInstance.isLineEligibleForCount(line);
        Assert.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseForLineWithFullyClosedComment() {
        String line = "/*commented*/";
        boolean result = testedInstance.isLineEligibleForCount(line);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalseForLineWithOpenComment() {
        String line = "/*";
        boolean result = testedInstance.isLineEligibleForCount(line);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnTrueForLineWithClosedCommentAndCode() {
        String line = "/*commented part*/ Note commented";
        boolean result = testedInstance.isLineEligibleForCount(line);
        Assert.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseForSequenciallyCommentedLine() {
        String line = "/*fhwjfhljf*//*hdwkdw*/";
        boolean result = testedInstance.isLineEligibleForCount(line);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalseForOpenComments() {
        testedInstance.setPreviousLineOpenComment(true);
        String line = "open comment";
        boolean result = testedInstance.isLineEligibleForCount(line);
        Assert.assertFalse(result);
    }

}
