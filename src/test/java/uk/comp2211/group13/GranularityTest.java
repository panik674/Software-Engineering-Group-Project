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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class GranularityTest {
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
    public void granularitySecondTest() throws ParseException {
        Assert.assertNotNull(metrics.request(
                Metric.Clicks,
                Utility.string2Date("2015-01-01 12:00:00"),
                Utility.string2Date("2015-01-15 12:00:00"),
                new HashMap<>(),
                Granularity.Second));
    }
    @Test
    public void granularityHourTest() throws ParseException {
        Assert.assertNotNull(metrics.request(
                Metric.Clicks,
                Utility.string2Date("2015-01-01 12:00:00"),
                Utility.string2Date("2015-01-15 12:00:00"),
                new HashMap<>(),
                Granularity.Hour));
    }
    @Test
    public void granularityDayTest() throws ParseException {
        Assert.assertNotNull(metrics.request(
                Metric.Clicks,
                Utility.string2Date("2015-01-01 12:00:00"),
                Utility.string2Date("2015-01-15 12:00:00"),
                new HashMap<>(),
                Granularity.Day));
    }
    @Test
    public void granularityMonthTest() throws ParseException {
        Assert.assertNotNull(metrics.request(
                Metric.Clicks,
                Utility.string2Date("2015-01-01 12:00:00"),
                Utility.string2Date("2015-01-15 12:00:00"),
                new HashMap<>(),
                Granularity.Month));
    }

    @Test
    public void granularityYearTest() throws ParseException {
        Assert.assertNotNull(metrics.request(
                Metric.Clicks,
                Utility.string2Date("2015-01-01 12:00:00"),
                Utility.string2Date("2015-01-15 12:00:00"),
                new HashMap<>(),
                Granularity.Year));
    }

}
