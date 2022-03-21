package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Logs;
import uk.comp2211.group13.enums.Filter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class LogsTest {
    private Logs logs;
    private ArrayList<String> pathsTest;
    private Data data;
    private  HashMap<Filter, String[]> filter;

    @Before
    public void setupData() {
        data = new Data();

        pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");
        data.ingest(pathsTest);

        filter = new HashMap<>();
        logs = data.request(data.getMinDate(), data.getMaxDate(), filter);
    }

    @Test
    public void getClicksTest() {
        int resultFor2_week = 23923;
        Assert.assertEquals(resultFor2_week, logs.getClicks());
    }
    @Test
    public void ClicksNotNegativeTest(){
        Assert.assertTrue(logs.getClicks()>0);
    }

    @Test
    public void getImpressionTest() {
        int resultFor2_week = 486104;
        Assert.assertEquals(resultFor2_week, logs.getImpressions());
    }
    @Test public void ImpressionNotNegativeTest(){
        Assert.assertTrue(logs.getImpressions()>0);
    }

    @Test
    public void getBounceVisitTest() {
        int resultFor2_week = 5095;
        Assert.assertEquals(resultFor2_week, logs.getBounceVisit(15));
    }
    @Test void bounceVisitNotNegative(){
        Assert.assertTrue(logs.getBounceVisit(15)>0);
    }
    @Test
    public void getBouncePagTest() {
        int result = 8665;
        Assert.assertEquals(result, logs.getBouncePage(1));
    }
    @Test
    public void bouncePagNotNegative(){
        Assert.assertTrue(logs.getBouncePage(1)>0);
    }
    @Test
    public void getConversionsTest() {
        int resultFor2_week = 2026;
        Assert.assertEquals(resultFor2_week, logs.getConversions());
    }
    @Test
    public void conversionsTest(){
        Assert.assertTrue(logs.getConversions()>0);
    }

    @Test
    public void getUniquesTest() {
        int resultFor2_week = 23806;
        Assert.assertEquals(resultFor2_week, logs.getUniques());
    }
    @Test
    public void uniquesNotNegateive(){
        Assert.assertTrue(logs.getUniques()>0);
    }
    @Test
    public void getClickCost() {
        double resultFor2_week = 117610.765625;
        Assert.assertEquals(BigDecimal.valueOf(resultFor2_week), BigDecimal.valueOf(logs.getClickCost()));
    }

    @Test
    public void ClickCostNotNegative(){
        Assert.assertTrue(logs.getClickCost()>0);
    }
    @Test
    public void getImpressionCost() {
        double resultFor2_week = 487.0466613769531;
        Assert.assertEquals(BigDecimal.valueOf(resultFor2_week), BigDecimal.valueOf(logs.getImpressionCost()));
    }

    @Test
    public void impressionCost(){
        Assert.assertTrue(logs.getImpressionCost()>0);
    }
}
