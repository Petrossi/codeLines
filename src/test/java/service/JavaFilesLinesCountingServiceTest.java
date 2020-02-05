package service;

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import service.impl.CommentedLinesCountingStrategy;
import service.impl.EmptyLinesCountingStrategy;
import service.impl.JavaFilesLinesCountingService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class JavaFilesLinesCountingServiceTest {
    private JavaFilesLinesCountingService testedInstance = new JavaFilesLinesCountingService();
    private List<LinesCountingEligibilityStrategy> countingStrategies = new ArrayList<>();
    private CommentedLinesCountingStrategy commentedLinesCountingStrategy;
    private EmptyLinesCountingStrategy emptyLinesCountingStrategy;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        commentedLinesCountingStrategy = Mockito.mock(CommentedLinesCountingStrategy.class);
        emptyLinesCountingStrategy = Mockito.mock(EmptyLinesCountingStrategy.class);
        when(commentedLinesCountingStrategy.getPreviousLineOpenComment()).thenReturn(false);
        countingStrategies.add(commentedLinesCountingStrategy);
        countingStrategies.add(emptyLinesCountingStrategy);
        testedInstance.setCountingStrategies(countingStrategies);
    }

    @Test
    public void shouldReturnZeroForEmptyList()  throws IOException {
        File file = temporaryFolder.newFile("file.java");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write("");
        bufferedWriter.close();
        int result = testedInstance.countLinesNumber(file);
        Assert.assertEquals("The counted number of lines is wrong", 0, result);
    }

    @Test
    public void shouldReturnOneForOneLine() throws IOException {
        File file = temporaryFolder.newFile("file.java");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write("oneline");
        bufferedWriter.close();
        when(commentedLinesCountingStrategy.isLineEligibleForCount(anyString())).thenReturn(true);
        testedInstance.countLinesNumber(file);
        verify(emptyLinesCountingStrategy, times(1)).isLineEligibleForCount(anyString());
        verify(commentedLinesCountingStrategy, times(1)).isLineEligibleForCount(anyString());
    }

}
