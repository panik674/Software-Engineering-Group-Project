package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Path;
import java.util.HashMap;

public class DataTest {
    private Data data;

    @BeforeEach
    public void setupData() {
        data = new Data();
    }

    /**
     * This will test that we can successfully ingest data under the correct conditions.
     */
    @Test
    public void ingestSuccessfulTest() {
        HashMap<Path, String> pathsTest = new HashMap<>();
        pathsTest.put(Path.Click, "src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.put(Path.Impression, "src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.put(Path.Server, "src/test/java/uk/comp2211/group13/testdata/server_log.csv");

        boolean result = data.ingest(pathsTest);

        Assert.assertSame("Test Successful Ingest", true, result);
    }

    /**
     * This will test if ingest will fail if we are missing a log
     */
    @Test
    public void ingestMissingFileTest() {
        HashMap<Path, String> pathsTest = new HashMap<>();
        pathsTest.put(Path.Click, "src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.put(Path.Impression, "src/test/java/uk/comp2211/group13/testdata/impression_log.csv");

        boolean result = data.ingest(pathsTest);

        Assert.assertSame("Test missing path Ingest", false, result);
    }

    /**
     * This will test if ingest will fail if a file contains invalid data
     */
    @Test
    public void ingestInvalidFormatTest() {
        HashMap<Path, String> pathsTest = new HashMap<>();
        pathsTest.put(Path.Click, "src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.put(Path.Impression, "src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.put(Path.Server, "src/test/java/uk/comp2211/group13/testdata/invalid.csv");

        boolean result = data.ingest(pathsTest);

        Assert.assertSame("Test invalid data file Ingest", false, result);
    }

    /**
     * This will test if ingest will fail if a file contains diffrent log data
     */
    @Test
    public void ingestWrongFormatTest() {
        HashMap<Path, String> pathsTest = new HashMap<>();
        pathsTest.put(Path.Click, "src/test/java/uk/comp2211/group13/testdata/server_log.csv");
        pathsTest.put(Path.Impression, "src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.put(Path.Server, "src/test/java/uk/comp2211/group13/testdata/click_log.csv");

        boolean result = data.ingest(pathsTest);

        Assert.assertSame("Test wrong data file Ingest", false, result);
    }

    /**
     * This tests that data will return a logs object on request
     */
    @Test
    public void requestTest() {
        HashMap<Path, String> pathsTest = new HashMap<>();
        pathsTest.put(Path.Click, "src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.put(Path.Impression, "src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.put(Path.Server, "src/test/java/uk/comp2211/group13/testdata/server_log.csv");
        data.ingest(pathsTest);

        HashMap<Filter, String> filter = new HashMap<>();

        Assert.assertNotNull(data.request(filter));
    }

    /**
     * This tests that log type estimations are correct
     */
    @Test
    public void estimateLogTypeTest() {
        Assert.assertEquals(
                Path.Impression,
                data.estimateLogType("src/test/java/uk/comp2211/group13/testdata/impression_log.csv")
        );

        Assert.assertEquals(
                Path.Click,
                data.estimateLogType("src/test/java/uk/comp2211/group13/testdata/click_log.csv")
        );

        Assert.assertEquals(
                Path.Server,
                data.estimateLogType("src/test/java/uk/comp2211/group13/testdata/server_log.csv")
        );
    }
}

