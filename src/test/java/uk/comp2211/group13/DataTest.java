package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Logs;
import uk.comp2211.group13.enums.Path;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class DataTest {
    private Data data = new Data();
    private Logs logs = new Logs(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    public void ingest(){
        HashMap<Path, String> pathsTest = new HashMap<>();
        pathsTest.put(Path.Click,"src/test/java/uk/comp2211/group13/click_log.csv");
        pathsTest.put(Path.Impression,"src/test/java/uk/comp2211/group13/impression_log.csv");
        pathsTest.put(Path.Server,"src/test/java/uk/comp2211/group13/server_log.csv");
        data.ingest(pathsTest);
        logs = data.request();
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
        Assert.assertNotNull(logs.getClicks());
        Assert.assertEquals(resultFor2_week,logs.getClicks());
    }
    @Test
    public void getImpressionTest(){
        ingest();
        int resultFor2_week = 486104;
        Assert.assertNotNull(logs.getImpressions());
        Assert.assertEquals(resultFor2_week,logs.getImpressions());
    }
    @Test
    public void getBounceVisitTest(){
        ingest();
        int resultFor2_week = 5095;
        Assert.assertNotNull(logs.getBounceVisit());
        Assert.assertEquals(resultFor2_week,logs.getBounceVisit());

    }
    @Test
    public void getBouncePagTest(){
        ingest();
        int result = 8665;
        Assert.assertNotNull(logs.getBouncePage());
        Assert.assertEquals(result,logs.getBouncePage());
    }
    @Test
    public void getConversionsTest(){
        ingest();
        int resultFor2_week = 2026;
        Assert.assertNotNull(logs.getConversions());
        Assert.assertEquals(resultFor2_week,logs.getConversions());
    }
    @Test
    public void getUniquesTest(){
        ingest();
        int resultFor2_week = 23806;
        Assert.assertNotNull(logs.getUniques());
        Assert.assertEquals( resultFor2_week,logs.getUniques());
    }
    @Test
    public void getClickCost(){
        ingest();
        double resultFor2_week = 117610.765625;
        Assert.assertNotNull(logs.getClickCost());
       Assert.assertEquals(BigDecimal.valueOf(resultFor2_week),BigDecimal.valueOf(logs.getClickCost()));
    }
    @Test
    public void getImpressionCost(){
        ingest();
        double resultFor2_week =487.0466613769531;
        Assert.assertNotNull(logs.getImpressionCost());
        Assert.assertEquals(BigDecimal.valueOf(resultFor2_week),BigDecimal.valueOf(logs.getImpressionCost()));
    }

}

