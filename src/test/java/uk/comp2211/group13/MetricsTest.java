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
import uk.comp2211.group13.enums.Path;

import java.math.BigDecimal;
import java.util.HashMap;

public class MetricsTest {
    private Metrics metrics;
    private Logs logs;

    @Before
    public void setupData() {
        Data data = new Data();

        HashMap<Path, String> pathsTest = new HashMap<>();
        pathsTest.put(Path.Click, "src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.put(Path.Impression, "src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.put(Path.Server, "src/test/java/uk/comp2211/group13/testdata/server_log.csv");
        data.ingest(pathsTest);

        metrics = new Metrics(data);

        HashMap<Filter, String> filter = new HashMap<>();
        filter.put(Filter.StartDatetime, "2015-01-01 12:00:00");
        filter.put(Filter.EndDatetime, "2015-01-15 12:00:00");
        logs = data.request(filter);
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
    public void bounceRatePageTest() {
        double result = 0.3622037470340729;
        Assert.assertEquals(
                BigDecimal.valueOf(result),
                BigDecimal.valueOf(metrics.bounceRatePage(logs))
        );
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
    public void costThousandTest() {
        double result = 1.0019391775131226;
        Assert.assertEquals(
                BigDecimal.valueOf(result),
                BigDecimal.valueOf(metrics.costThousand(logs))
        );
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
    public void costClickTest() {
        double result = 4.916221618652344;
        Assert.assertEquals(
                BigDecimal.valueOf(result),
                BigDecimal.valueOf(metrics.costClick(logs))
        );
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
    public void requestTest() {
        Assert.assertNotNull(
                metrics.request(
                        Metric.BouncePage,
                        "2015-01-01 12:00:00",
                        "2015-01-15 12:00:00",
                        Granularity.Second
                )
        );
    }
}
