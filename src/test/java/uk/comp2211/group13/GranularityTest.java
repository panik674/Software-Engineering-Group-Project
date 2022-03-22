package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;

import java.text.ParseException;
import java.util.*;

public class GranularityTest {
    @Test
    public void granularityFunctionTest() throws ParseException {
        Data data = new Data();
        Metrics metrics = new Metrics(data);
        ArrayList<String> pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");

        Assert.assertNotNull(metrics.request(Metric.Clicks,
                Utility.string2Date("2015-01-01 12:00:00"),
                Utility.string2Date("2015-01-15 12:00:00"),
                new HashMap<>(),
                Granularity.Day));

    }
}

