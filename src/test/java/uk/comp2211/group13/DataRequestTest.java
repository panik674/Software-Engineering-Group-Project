package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.enums.Filter;

import java.text.ParseException;
import java.util.HashMap;

public class DataRequestTest {
  private Data data;
  private HashMap<Filter, String[]> filterMap;

  @Before
  public void setupData() {
    data = new Data();
    data.ingest("src/test/java/uk/comp2211/group13/testdata");

    filterMap = new HashMap<>();
  }

  @Test
  public void testRequest() {
    Assert.assertNotNull(data.request());
  }

  @Test
  public void dateTest() {
    HashMap<Filter, String[]> filter = new HashMap<>();
    Assert.assertNotNull(data.request(data.getMinDate(), data.getMaxDate(), filter));
  }

  @Test
  public void sameDateTest() {
    HashMap<Filter, String[]> filter = new HashMap<>();
    Assert.assertNotNull(data.request(data.getMinDate(), data.getMinDate(), filter));
  }

  @Test
  public void invalidDateTest() {
    HashMap<Filter, String[]> filter = new HashMap<>();
    Assert.assertNull(data.request(data.getMaxDate(), data.getMinDate(), filter));
  }

  @Test
  public void testAllFilters() {
    HashMap<Filter, String[]> filter = new HashMap<>();

    filter.put(Filter.Gender, Utility.getGenders);
    filter.put(Filter.Age, Utility.getAges);
    filter.put(Filter.Income, Utility.getIncomes);
    filter.put(Filter.Context, Utility.getContexts);

    Assert.assertNotNull(data.request(data.getMinDate(), data.getMaxDate(), filter));
  }

  @Test
  public void testRequestFilters() {
    HashMap<Filter, String[]> filter;
    for (String gender : Utility.getGenders) {
      for (String age : Utility.getAges) {
        for (String income : Utility.getIncomes) {
          for (String context : Utility.getContexts) {
            filter = new HashMap<>();

            filter.put(Filter.Gender, new String[]{gender});
            filter.put(Filter.Age, new String[]{age});
            filter.put(Filter.Income, new String[]{income});
            filter.put(Filter.Context, new String[]{context});

            Assert.assertNotNull(data.request(data.getMinDate(), data.getMaxDate(), filter));
          }
        }
      }
    }
  }

  @Test
  public void testInvalidFilters() {
    HashMap<Filter, String[]> filter = new HashMap<>();
    filter.put(Filter.Gender, new String[]{"gender"});

    Assert.assertNull(data.request(data.getMinDate(), data.getMaxDate(), filter));

    filter = new HashMap<>();
    filter.put(Filter.Age, new String[]{"age"});

    Assert.assertNull(data.request(data.getMinDate(), data.getMaxDate(), filter));

    filter = new HashMap<>();
    filter.put(Filter.Income, new String[]{"income"});

    Assert.assertNull(data.request(data.getMinDate(), data.getMaxDate(), filter));

    filter = new HashMap<>();
    filter.put(Filter.Context, new String[]{"context"});

    Assert.assertNull(data.request(data.getMinDate(), data.getMaxDate(), filter));
  }

  @Test
  public void ageFilterFunctionTest() throws ParseException {
    String[] age = {"<25", "25-34", "35-44", "45-54", ">54"};
    filterMap.put(Filter.Age, age);
    int result = 23923;
    Assert.assertEquals(result, data.request(Utility.string2Date("2015-01-01 12:00:00"), Utility.string2Date("2015-01-15 12:00:00"), filterMap).getClicks());
  }


  @Test
  public void genderFilterFunctionTest() throws ParseException {
    String[] gender = {"Female"};
    filterMap.put(Filter.Gender, gender);
    int result = 15935;
    Assert.assertEquals(result, data.request(Utility.string2Date("2015-01-01 12:00:00"), Utility.string2Date("2015-01-15 12:00:00"), filterMap).getClicks());
  }

  @Test
  public void incomeFilterFunctionTest() throws ParseException {

    String[] income = {"Medium", "High"};
    filterMap.put(Filter.Income, income);
    int result = 19345;
    Assert.assertEquals(result, data.request(Utility.string2Date("2015-01-01 12:00:00"), Utility.string2Date("2015-01-15 12:00:00"), filterMap).getClicks());
  }

  @Test
  public void contextFilterFunctionTest() throws ParseException {
    String[] context = {"News", "Travel"};
    filterMap.put(Filter.Context, context);
    int result = 6716;
    Assert.assertEquals(result, data.request(Utility.string2Date("2015-01-01 12:00:00"), Utility.string2Date("2015-01-15 12:00:00"), filterMap).getClicks());
  }
}
