package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Save;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import static uk.comp2211.group13.data.Save.getDirectory;


public class SaveTest  {
    private Data data;
    private Save save;
    private HashSet<Filter> filters;
    private HashSet<Metric> metrics;
    private Date startDate;
    private Date endDate;
    private Granularity granularity;
    @Before
    public void setupData() throws ParseException {
        save = new Save();
        filters =new HashSet<>();
        metrics = new HashSet<>();
        startDate = new Date();
        endDate = new Date();

        filters.add(Filter.Gender);
        metrics.add(Metric.Impressions);
        endDate = Utility.string2Date("2015-01-15 12:00:00");
        startDate = Utility.string2Date("2015-01-01 12:00:00");
        save.saveFilters(filters,metrics,Granularity.Second,startDate,endDate);
        save.loadFilters();
    }
    /**This test will test save filters
     * */
    @Test
    public void testSaveFilters(){
        String path = getDirectory();
        File file = new File(path);
        Assert.assertNotNull(file);
    }
    /**This test will test load filter**/
    @Test
    public void testLoadFilters() {
        Assert.assertNotNull(save.filters);
        Assert.assertNotNull(save.metrics);
        Assert.assertNotNull(save.granularity);
        Assert.assertNotNull(save.startDate);
        Assert.assertNotNull(save.endDate);
    }
}