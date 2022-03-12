package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Path;

import java.util.ArrayList;
import java.util.HashMap;

public class DataTest {
    private Data data;

    @Before
    public void setupData() {
        data = new Data();
    }

    /**
     * This will test that we can successfully ingest data under the correct conditions.
     */
    @Test
    public void ingestSuccessfulTest() {
        ArrayList<String> pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");

        boolean result = data.ingest(pathsTest);

        Assert.assertSame("Test Successful Ingest", true, result);
    }

    /**
     * This will test if ingest will fail if we are missing a log
     */
    @Test
    public void ingestMissingFileTest() {
        ArrayList<String> pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");

        boolean result = data.ingest(pathsTest);

        Assert.assertSame("Test missing path Ingest", false, result);
    }

    /**
     * This will test if ingest will fail if a file contains invalid data
     */
    @Test
    public void ingestInvalidFormatTest() {
        ArrayList<String> pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/invalid.csv");

        boolean result = data.ingest(pathsTest);

        Assert.assertSame("Test invalid data file Ingest", false, result);
    }

    /**
     * This will test if ingest will fail if a file contains diffrent log data
     */
    @Test
    public void ingestDupeLogTest() {
        ArrayList<String> pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");

        boolean result = data.ingest(pathsTest);

        Assert.assertSame("Test wrong data file Ingest", false, result);
    }

    /**
     * This tests that data will return a logs object on request
     */
    @Test
    public void requestTest() {
        ArrayList<String> pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");
        data.ingest(pathsTest);

        HashMap<Filter, String[]> filter = new HashMap<>();
        Assert.assertNotNull(data.request(data.getMinDate(), data.getMaxDate(), filter));
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

