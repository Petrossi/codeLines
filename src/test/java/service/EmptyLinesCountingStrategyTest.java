package service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import service.impl.EmptyLinesCountingStrategy;

@RunWith(JUnit4.class)
public class EmptyLinesCountingStrategyTest {
    private EmptyLinesCountingStrategy testedInstance = new EmptyLinesCountingStrategy();

    @Test
    public void shouldReturnTrueForEligibleLine() {
        String line = "normal not empty line";
        boolean result = testedInstance.isLineEligibleForCount(line);
        Assert.assertTrue(result);

    }

    @Test
    public void shouldReturnFalseForWhitespaceLines() {
        String line = "  ";
        boolean result = testedInstance.isLineEligibleForCount(line);
        Assert.assertFalse(result);
    }

    @Test
    public void shouldReturnFalseForEmptyLines() {
        String line = "";
        boolean result = testedInstance.isLineEligibleForCount(line);
        Assert.assertFalse(result);
    }
}
