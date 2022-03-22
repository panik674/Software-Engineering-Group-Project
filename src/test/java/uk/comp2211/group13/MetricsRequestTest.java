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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class MetricsRequestTest {
  private Data data;
  private Metrics metrics;
  private Logs logs;

  @Before
  public void setupData() {
    data = new Data();

    data.ingest("src/test/java/uk/comp2211/group13/testdata");

    metrics = new Metrics(data);

    HashMap<Filter, String[]> filter = new HashMap<>();
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
