package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Logs;
import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;

import java.math.BigDecimal;
import java.util.HashMap;

public class MetricsRequestTest {
  private Data data;
  private Metrics metrics;
  private Logs logs;
  private HashMap<Filter, String[]> filter;

  @Before
  public void setupData() {
    data = new Data();

    data.ingest("src/test/java/uk/comp2211/group13/testdata");

    metrics = new Metrics(data);

    filter = new HashMap<>();
    logs = data.request(data.getMinDate(), data.getMaxDate(), filter);
  }

  @Test
  public void allMetricRequestTest() {
    for (Metric metric : Metric.values()) {
      Assert.assertNotNull(metrics.request(metric, data.getMinDate(), data.getMaxDate(), new HashMap<>(), Granularity.Day));
    }
  }

  @Test
  public void allGranularityRequestTest() {
    for (Granularity granularity : Granularity.values()) {
      Assert.assertNotNull(metrics.request(Metric.Impressions, data.getMinDate(), data.getMaxDate(), new HashMap<>(), granularity));
    }
  }

  @Test
  public void allFiltersRequestTest(){
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

            Assert.assertNotNull(metrics.request(Metric.Impressions,data.getMinDate(),data.getMaxDate(),filter,Granularity.Day));
          }
        }
      }
    }
  }


  @Test
  public void dateTest() {
    Assert.assertNotNull(metrics.request(Metric.Impressions,data.getMinDate(), data.getMaxDate(), filter,Granularity.Day));
  }

  @Test
  public void sameDateTest() {
    Assert.assertNotNull(metrics.request(Metric.Impressions,data.getMinDate(), data.getMinDate(), filter,Granularity.Day));
  }

  @Test
  public void invalidDateTest() {
    try{
    Assert.assertNull(metrics.request(Metric.Impressions,data.getMaxDate(), data.getMinDate(),filter,Granularity.Day));
    }catch (Exception e){
      e.getMessage();
    }
  }

  @Test
  public void testInvalidFilters() {
    try{
      HashMap<Filter, String[]> filter = new HashMap<>();
      filter.put(Filter.Gender, new String[]{"gender"});

      Assert.assertNull(metrics.request(Metric.Impressions,data.getMinDate(),data.getMaxDate(),filter,Granularity.Day));

      filter = new HashMap<>();
      filter.put(Filter.Age, new String[]{"age"});

      Assert.assertNull(metrics.request(Metric.Impressions,data.getMinDate(),data.getMaxDate(),filter,Granularity.Day));

      filter = new HashMap<>();
      filter.put(Filter.Income, new String[]{"income"});

      Assert.assertNull(metrics.request(Metric.Impressions,data.getMinDate(),data.getMaxDate(),filter,Granularity.Day));

      filter = new HashMap<>();
      filter.put(Filter.Context, new String[]{"context"});

      Assert.assertNull(metrics.request(Metric.Impressions,data.getMinDate(),data.getMaxDate(),filter,Granularity.Day));

    }catch (Exception e){
      e.getMessage();
    }
  }

  @Test
  public void testInvalidGranularity(){
    try {
      Granularity granularity = Granularity.valueOf("month");
      Assert.assertNull(metrics.request(Metric.Impressions,data.getMinDate(),data.getMaxDate(),filter,granularity));
    }catch (Exception e){
      e.getMessage();
    }

  }

  @Test
  public void clickRateTest() {
    double result = 20.31952476501465;
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.clickRate(logs)));
  }

  @Test
  public void clickRateNotNegative() {
    Assert.assertTrue(metrics.clickRate(logs) > 0);
  }

  @Test
  public void bounceRatePageTest() {
    double result = 0.3622037470340729;
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.bounceRatePage(logs)));
  }

  @Test
  public void bounceRatePageNotNegativeTest() {
    Assert.assertTrue(metrics.bounceRatePage(logs) > 0);
  }

  @Test
  public void conversionRateTest() {
    double result = 0.08468837291002274;
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.conversionRate(logs)));
  }

  @Test
  public void conversionrateNotNegative() {
    Assert.assertTrue(metrics.conversionRate(logs) > 0);
  }

  @Test
  public void costThousandTest() {
    double result = 1.0019391775131226;
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.costThousand(logs)));
  }

  @Test
  public void costThousandNotNegativeTest() {
    Assert.assertTrue(metrics.costThousand(logs) > 0);
  }

  @Test
  public void costAcquisitionTest() {
    double result = 58.29112243652344;
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.costAcquisition(logs)));
  }

  @Test
  public void costAcquisitionNotNegativeTest() {
    Assert.assertTrue(metrics.costAcquisition(logs) > 0);
  }

  @Test
  public void costClickTest() {
    double result = 4.916221618652344;
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.costClick(logs)));
  }

  @Test
  public void costClickNotNegative() {
    Assert.assertTrue(metrics.costClick(logs) > 0);
  }

  @Test
  public void totalCastTest() {
    double result = 118097.8125;
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(logs.getImpressionCost() + logs.getClickCost()));
  }

  @Test
  public void totalCastNotNegative() {
    Assert.assertTrue((logs.getImpressionCost() + logs.getClickCost() > 0));
  }

}
