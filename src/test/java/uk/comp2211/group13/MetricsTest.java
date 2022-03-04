package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.enums.Path;

import java.math.BigDecimal;
import java.util.HashMap;

public class MetricsTest {
    Data data = new Data();
    Metrics metrics = new Metrics(data);
    public void ingest(){
        HashMap<Path, String> pathsTest = new HashMap<>();
        pathsTest.put(Path.Click,"src/test/java/uk/comp2211/group13/click_log.csv");
        pathsTest.put(Path.Impression,"src/test/java/uk/comp2211/group13/impression_log.csv");
        pathsTest.put(Path.Server,"src/test/java/uk/comp2211/group13/server_log.csv");
        data.ingest(pathsTest);
    }
    @Test
    public void clickRateTest(){
        ingest();
        double result = 20;
        Assert.assertNotNull(metrics.clickRate());
        Assert.assertEquals(BigDecimal.valueOf(result),BigDecimal.valueOf(metrics.clickRate()));
    }
    @Test
    public void bounceRateTest(){
        ingest();
        double result=0;
        Assert.assertNotNull(metrics.bounceRate());
        Assert.assertEquals(BigDecimal.valueOf(result),BigDecimal.valueOf(metrics.bounceRate()));
    }
    @Test
    public void conversionRateTest(){
        ingest();
        double result = 0;
        Assert.assertNotNull(metrics.conversionRate());
        Assert.assertEquals(BigDecimal.valueOf(result),BigDecimal.valueOf(metrics.conversionRate()));
    }
    @Test
    public void costThousandTest(){
        ingest();
        double result = 1.0019391775131226;
        Assert.assertNotNull(metrics.costThousand());
        Assert.assertEquals(BigDecimal.valueOf(result),BigDecimal.valueOf(metrics.costThousand()));
    }
    @Test
    public void costAcquisitionTest(){
        ingest();
        double result = 58.29112243652344;
        Assert.assertNotNull(metrics.costAcquisition());
        Assert.assertEquals(BigDecimal.valueOf(result),BigDecimal.valueOf(metrics.costAcquisition()));
    }
    @Test
    public void costClickTest(){
        ingest();
        double result = 4.916221618652344;
        Assert.assertNotNull(metrics.costClick());
        Assert.assertEquals(BigDecimal.valueOf(result),BigDecimal.valueOf(metrics.costClick()));
    }
    @Test
    public void totalCastTest(){
        ingest();
        double result = 118097.8125;
        Assert.assertNotNull(data.getImpressionCost()+data.getClickCost());
        Assert.assertEquals(BigDecimal.valueOf(result),BigDecimal.valueOf(data.getImpressionCost()+data.getClickCost()));
    }
}
