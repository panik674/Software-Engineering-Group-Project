package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.enums.Filter;

import java.util.ArrayList;
import java.util.HashMap;

public class DateTest {
  private Data data;

  @Before
  public void setupData() {
    data = new Data();

    ArrayList<String> pathsTest = new ArrayList<>();
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");
    data.ingest(pathsTest);
  }

  @Test
  public void dateTest() {
    HashMap<Filter, String[]> filter = new HashMap<>();
    Assert.assertNotNull(data.request(data.getMaxDate(), data.getMinDate(), filter));
  }

  @Test
  public void invalidDateTest() {
    HashMap<Filter, String[]> filter = new HashMap<>();
    Assert.assertNull(data.request(data.getMaxDate(), data.getMinDate(), filter));
  }
}

