package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.enums.Filter;

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

        int result = data.ingest(pathsTest);

        Assert.assertSame("Test Successful Ingest", 0, result);

    }
    @Test
    public void ingestFolderSucessfulTest(){
        String path = "src/test/java/uk/comp2211/group13/testdata";
        int result2 = data.ingest(path);

        Assert.assertSame("Test Successful Ingest", 0,result2);
    }
    @Test
    public void ingestFolderMissingTest(){
        String path = "src/test/java/uk/comp2211/group13/testdata2";
        int result2 = data.ingest(path);

        Assert.assertSame("Test Missing Ingest", 1,result2);
    }

    /**
     * This will test if ingest will fail if we are missing a log
     */
    @Test
    public void ingestMissingFileTest() {
        ArrayList<String> pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");

        int result = data.ingest(pathsTest);

        Assert.assertSame("Test Successful Ingest", 1, result);
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

        int result = data.ingest(pathsTest);
        Assert.assertSame("Test invalid data file Ingest", 4, result);
    }
    /** This will test if ingest folder will fail if a file contains invalid data
     * */
    @Test
    public void ingestFolderInvalidFormatTest(){
        String paths = "src/test/java/uk/comp2211/group13/testdata2";
        int result = data.ingest(paths);

        Assert.assertSame("Test invalid data file Ingest",4,result);
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

        int result = data.ingest(pathsTest);
        Assert.assertSame("Test wrong data file Ingest", 1, result);
    }
    /** This will test if ingest file if a file not found
     * */
    @Test
    public void ScannerFileNotFoundTest(){
        ArrayList<String> pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log2.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log2.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log2.csv");
       int result = data.ingest(pathsTest);

        Assert.assertSame("Scanner file not found",2,result);
    }
    /**
     * This test is to test log mismatch
     * */
    @Test
    public void MismatchTest(){
        ArrayList<String> pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log2.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");
        int result = data.ingest(pathsTest);
        Assert.assertSame("click and server mismatch",3,result);
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

}

