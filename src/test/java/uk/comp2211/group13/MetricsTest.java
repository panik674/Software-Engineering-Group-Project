package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Logs;
import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.enums.Path;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MetricsTest {
  private Data data = new Data();
  private Metrics metrics = new Metrics(data);
  private Logs logs = new Logs(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

  public void ingest() {
    HashMap<Path, String> pathsTest = new HashMap<>();
    pathsTest.put(Path.Click, "src/test/java/uk/comp2211/group13/click_log.csv");
    pathsTest.put(Path.Impression, "src/test/java/uk/comp2211/group13/impression_log.csv");
    pathsTest.put(Path.Server, "src/test/java/uk/comp2211/group13/server_log.csv");
    data.ingest(pathsTest);
  }
  public void requestForTest(){
      HashMap<Filter,String> filter = new HashMap<>();
      filter.put(Filter.StartDatetime,"2015-01-01 12:00:00");
      filter.put(Filter.EndDatetime,"2015-01-15 12:00:00");
      logs =data.request(filter);
  }

  @Test
  public void clickRateTest() {
    ingest();
    requestForTest();
    double result = 20.31952476501465;
    Assert.assertNotNull(metrics.clickRate(logs));
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.clickRate(logs)));
  }
  @Test
  public void bounceRatePageTest() {
    ingest();
    requestForTest();
    double result = 0.3622037470340729;
    Assert.assertNotNull(metrics.bounceRatePage(logs));
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.bounceRatePage(logs)));
  }

  @Test
  public void conversionRateTest() {
    ingest();
    requestForTest();
    double result = 0.08468837291002274;
    Assert.assertNotNull(metrics.conversionRate(logs));
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.conversionRate(logs)));
  }

  @Test
  public void costThousandTest() {
    ingest();
    requestForTest();
    double result = 1.0019391775131226;
    Assert.assertNotNull(metrics.costThousand(logs));
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.costThousand(logs)));
  }

  @Test
  public void costAcquisitionTest() {
    ingest();
    requestForTest();
    double result = 58.29112243652344;
    Assert.assertNotNull(metrics.costAcquisition(logs));
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.costAcquisition(logs)));
  }

  @Test
  public void costClickTest() {
    ingest();
    requestForTest();
    double result = 4.916221618652344;
    Assert.assertNotNull(metrics.costClick(logs));
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(metrics.costClick(logs)));
  }

  @Test
  public void totalCastTest() {
    ingest();
    requestForTest();
    double result = 118097.8125;
    Assert.assertNotNull(logs.getImpressionCost() + logs.getClickCost());
    Assert.assertEquals(BigDecimal.valueOf(result), BigDecimal.valueOf(logs.getImpressionCost() + logs.getClickCost()));
  }
  @Test
  public void requestTest(){
    ingest();
    requestForTest();
    HashMap<Date,Logs> timelogs = new HashMap<>();
    Assert.assertNotNull(metrics.request(Metric.BouncePage,"2015-01-01 12:00:00","2015-01-15 12:00:00",Granularity.Second));
  }
}
