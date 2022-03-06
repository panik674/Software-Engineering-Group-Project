package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Logs;
import uk.comp2211.group13.enums.Filter;
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
        HashMap<Filter,String> filter = new HashMap<>();
        filter.put(Filter.StartDatetime,"src/test/java/uk/comp2211/group13/click_log.csv");
        filter.put(Filter.EndDatetime,"src/test/java/uk/comp2211/group13/click_log.csv");
        filter.put(Filter.StartDatetime,"src/test/java/uk/comp2211/group13/impression_log.csv");
        filter.put(Filter.EndDatetime,"src/test/java/uk/comp2211/group13/impression_log.csv");
        filter.put(Filter.EndDatetime,"src/test/java/uk/comp2211/group13/server_log.csv");
        filter.put(Filter.StartDatetime,"src/test/java/uk/comp2211/group13/server_log.csv");
        logs =data.request(filter);
        Assert.assertNotNull(data.request(filter));
        Assert.assertNotNull(data.request(filter).clickLogs);
        Assert.assertNotNull(data.request(filter).impressionLogs);
        Assert.assertNotNull(data.request(filter).serverLogs);
    }
}

