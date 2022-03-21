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

public class MetricsTest {
  private Metrics metrics;
  private Logs logs;

  @Before
  public void setupData() {
    Data data = new Data();

    ArrayList<String> pathsTest = new ArrayList<>();
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
    pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");
    data.ingest(pathsTest);

    metrics = new Metrics(data);

    HashMap<Filter, String[]> filter = new HashMap<>();
    logs = data.request(data.getMinDate(), data.getMaxDate(), filter);
  }

  @Test
  public void clickRateTest() {
    double result = 20.31952476501465;
    Assert.assertEquals(
        BigDecimal.valueOf(result),
        BigDecimal.valueOf(metrics.clickRate(logs))
    );
  }

  @Test
  public void clickRateNotNegative(){
    Assert.assertTrue(metrics.clickRate(logs)>0);
  }

  @Test
  public void bounceRatePageTest() {
    double result = 0.3622037470340729;
    Assert.assertEquals(
        BigDecimal.valueOf(result),
        BigDecimal.valueOf(metrics.bounceRatePage(logs))
    );
  }

  @Test
  public void bounceRatePageNotNegativeTest(){
    Assert.assertTrue(metrics.bounceRatePage(logs)>0);
  }

  @Test
  public void conversionRateTest() {
    double result = 0.08468837291002274;
    Assert.assertEquals(
        BigDecimal.valueOf(result),
        BigDecimal.valueOf(metrics.conversionRate(logs))
    );
  }

  @Test
  public void conversionrateNotNegative(){
    Assert.assertTrue(metrics.conversionRate(logs)>0);
  }

  @Test
  public void costThousandTest() {
    double result = 1.0019391775131226;
    Assert.assertEquals(
        BigDecimal.valueOf(result),
        BigDecimal.valueOf(metrics.costThousand(logs))
    );
  }

  @Test
  public void costThousandNotNegativeTest(){
    Assert.assertTrue(metrics.costThousand(logs)>0);
  }
  @Test
  public void costAcquisitionTest() {
    double result = 58.29112243652344;
    Assert.assertEquals(
        BigDecimal.valueOf(result),
        BigDecimal.valueOf(metrics.costAcquisition(logs))
    );
  }

  @Test
  public void costAcquisitionNotNegativeTest(){
    Assert.assertTrue(metrics.costAcquisition(logs)>0);
  }
  @Test
  public void costClickTest() {
    double result = 4.916221618652344;
    Assert.assertEquals(
        BigDecimal.valueOf(result),
        BigDecimal.valueOf(metrics.costClick(logs))
    );
  }
  @Test
  public void costClickNotNegative(){
    Assert.assertTrue(metrics.costClick(logs)>0);
  }
  @Test
  public void totalCastTest() {
    double result = 118097.8125;
    Assert.assertEquals(
        BigDecimal.valueOf(result),
        BigDecimal.valueOf(logs.getImpressionCost() + logs.getClickCost())
    );
  }
  @Test
  public void totalCastNotNegative(){
    Assert.assertTrue((logs.getImpressionCost()+logs.getClickCost()>0));
  }

  @Test
  public void requestTest() throws ParseException {
    Assert.assertNotNull(
        metrics.request(
            Metric.BouncePage,
            Utility.string2Date("2015-01-01 12:00:00"),
            Utility.string2Date("2015-01-15 12:00:00"),
            new HashMap<>(),
            Granularity.Second
        )
    );
  }
}
