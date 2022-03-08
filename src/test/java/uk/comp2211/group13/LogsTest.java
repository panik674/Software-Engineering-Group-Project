package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Logs;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Path;

import java.math.BigDecimal;
import java.util.HashMap;

public class LogsTest {
    private Logs logs;

    @Before
    public void setupData() {
        Data data = new Data();

        HashMap<Path, String> pathsTest = new HashMap<>();
        pathsTest.put(Path.Click, "src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.put(Path.Impression, "src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.put(Path.Server, "src/test/java/uk/comp2211/group13/testdata/server_log.csv");
        data.ingest(pathsTest);

        HashMap<Filter, String> filter = new HashMap<>();
        filter.put(Filter.StartDatetime, "2015-01-01 12:00:00");
        filter.put(Filter.EndDatetime, "2015-01-15 12:00:00");
        logs = data.request(filter);
    }

    @Test
    public void getClicksTest() {
        int resultFor2_week = 23923;
        Assert.assertEquals(resultFor2_week, logs.getClicks());
    }

    @Test
    public void getImpressionTest() {
        int resultFor2_week = 486104;
        Assert.assertEquals(resultFor2_week, logs.getImpressions());
    }

    @Test
    public void getBounceVisitTest() {
        int resultFor2_week = 5095;
        Assert.assertEquals(resultFor2_week, logs.getBounceVisit());
    }

    @Test
    public void getBouncePagTest() {
        int result = 8665;
        Assert.assertEquals(result, logs.getBouncePage());
    }

    @Test
    public void getConversionsTest() {
        int resultFor2_week = 2026;
        Assert.assertEquals(resultFor2_week, logs.getConversions());
    }

    @Test
    public void getUniquesTest() {
        int resultFor2_week = 23806;
        Assert.assertEquals(resultFor2_week, logs.getUniques());
    }

    @Test
    public void getClickCost() {
        double resultFor2_week = 117610.765625;
        Assert.assertEquals(BigDecimal.valueOf(resultFor2_week), BigDecimal.valueOf(logs.getClickCost()));
    }

    @Test
    public void getImpressionCost() {
        double resultFor2_week = 487.0466613769531;
        Assert.assertEquals(BigDecimal.valueOf(resultFor2_week), BigDecimal.valueOf(logs.getImpressionCost()));
    }
}
