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
  public void ingestFileSuccessfulTest() {
    ArrayList<String> pathsTest = new ArrayList<>();
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");

    int result = data.ingest(pathsTest);

    Assert.assertSame("Test Successful File Ingest", 0, result);

  }

  @Test
  public void ingestFolderSuccessfulTest() {
    String path = "src/test/java/uk/comp2211/group13/testdata";
    int result2 = data.ingest(path);

    Assert.assertSame("Test Successful Folder Ingest", 0, result2);
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

    Assert.assertSame("Test Missing File Ingest", 1, result);
  }

  /**
   * This will test if ingest will fail if a folder does not exist
   */
  @Test
  public void ingestMissingFolderTest() {
    String path = "src/test/java/uk/comp2211/group13/testdata2";
    int result2 = data.ingest(path);

    Assert.assertSame("Test Missing Folder Ingest", 1, result2);
  }

  /**
   * This will test if ingest will fail if a file contains invalid data
   */
  @Test
  public void ingestInvalidFormatTest() {
    ArrayList<String> pathsTest = new ArrayList<>();
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/invaliddata/invalid.csv");

    int result = data.ingest(pathsTest);
    Assert.assertSame("Test invalid data file Ingest", 1, result);
  }

  /**
   * This will test if ingest folder will fail if a file contains invalid data
   */
  @Test
  public void ingestFolderInvalidFormatTest() {
    String path = "src/test/java/uk/comp2211/group13/invaliddata/invalid.csv";
    int result = data.ingest(path);

    Assert.assertSame("Test invalid folder Ingest", 1, result);
  }

  /**
   * This will test if ingest will fail if a file contains different log data
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

  /**
   * This will test the ingest file if a file not found
   */
  @Test
  public void FileNotFoundTest() {
    ArrayList<String> pathsTest = new ArrayList<>();
    pathsTest.add("src/test/java/uk/comp2211/group13/invaliddata/server_log2.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/invaliddata/impression_log2.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/invaliddata/click_log2.csv");
    int result = data.ingest(pathsTest);

    Assert.assertSame("Scanner file not found", 1, result);
  }

  /**
   * This test is to test log mismatch
   */
  @Test
  public void MismatchTest() {
    ArrayList<String> pathsTest = new ArrayList<>();
    pathsTest.add("src/test/java/uk/comp2211/group13/invaliddata/click_log_mismatch.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");
    int result = data.ingest(pathsTest);
    Assert.assertSame("click and server mismatch", 3, result);
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

