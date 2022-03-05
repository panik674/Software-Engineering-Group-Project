package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Logs;
import uk.comp2211.group13.enums.Path;

import java.math.BigDecimal;
import java.util.HashMap;

public class DataTest {
    private Data data = new Data();
    private Logs logs = new Logs();

    public void ingest(){
        HashMap<Path, String> pathsTest = new HashMap<>();
        pathsTest.put(Path.Click,"src/test/java/uk/comp2211/group13/click_log.csv");
        pathsTest.put(Path.Impression,"src/test/java/uk/comp2211/group13/impression_log.csv");
        pathsTest.put(Path.Server,"src/test/java/uk/comp2211/group13/server_log.csv");
        data.ingest(pathsTest);
    }
    @Test
    public void ingestTest(){
        HashMap<Path, String> pathsTest = new HashMap<>();
        pathsTest.put(Path.Click,"src/test/java/uk/comp2211/group13/click_log.csv");
        pathsTest.put(Path.Impression,"src/test/java/uk/comp2211/group13/impression_log.csv");
        pathsTest.put(Path.Server,"src/test/java/uk/comp2211/group13/server_log.csv");
        Assert.assertSame("test ingest",true,data.ingest(pathsTest));
        Assert.assertNotNull(logs.serverLogs);
        Assert.assertNotNull(logs.clickLogs);
        Assert.assertNotNull(logs.impressionLogs);
    }
    @Test
    public void requestTest(){
        ingest();
        Assert.assertNotNull(data.request());
        Assert.assertNotNull(data.request().clickLogs);
        Assert.assertNotNull(data.request().impressionLogs);
        Assert.assertNotNull(data.request().serverLogs);
    }
    @Test
    public void getClicksTest(){
        ingest();
        int resultFor2_week = 23923;
        Assert.assertNotNull(data.getClicks());
        Assert.assertEquals(resultFor2_week,data.getClicks());
    }
    @Test
    public void getImpressionTest(){
        ingest();
        int resultFor2_week = 486104;
        Assert.assertNotNull(data.getImpressions());
        Assert.assertEquals(resultFor2_week,data.getImpressions());
    }
    @Test
    public void getBounceTest(){
        ingest();
        int resultFor2_week = 15247;
        Assert.assertNotNull(data.getBounces());
        Assert.assertEquals(resultFor2_week,data.getBounces());

    }
    @Test
    public void getConversionsTest(){
        ingest();
        int resultFor2_week = 2026;
        Assert.assertNotNull(data.getConversions());
        Assert.assertEquals(resultFor2_week,data.getConversions());
    }
    @Test
    public void getUniquesTest(){
        ingest();
        int resultFor2_week = 23806;
        Assert.assertNotNull(data.getUniques());
        Assert.assertEquals( resultFor2_week,data.getUniques());
    }
    @Test
    public void getClickCost(){
        ingest();
        double resultFor2_week = 117610.765625;
        Assert.assertNotNull(data.getClickCost());
       Assert.assertEquals(BigDecimal.valueOf(resultFor2_week),BigDecimal.valueOf(data.getClickCost()));
    }
    @Test
    public void getImpressionCost(){
        ingest();
        double resultFor2_week =487.0466613769531;
        Assert.assertNotNull(data.getImpressionCost());
        Assert.assertEquals(BigDecimal.valueOf(resultFor2_week),BigDecimal.valueOf(data.getImpressionCost()));
    }

}

