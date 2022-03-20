package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Save;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;


public class SaveTest  {
    private Data data;
    private Save save;
    private HashSet<Filter> filters;
    private HashSet<Metric> metrics;
    private Date startDate;
    private Date endDate;
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
        save.loadFilters("E:/save.json");
    }
    /**This test will test save filters
     * */
    @Test
    public void testSaveFilters() throws ParseException { data = new Data();

        Assert.assertNotNull("E:/save.json");
    }
    /**This test will test load filters
     * */
    @Test
    public void testLoadFilters() {
        Assert.assertNotNull(save.getFilters());
        Assert.assertNotNull(save.getEndDate());
        Assert.assertNotNull(save.getGranularity());
        Assert.assertNotNull(save.getMetrics());
        Assert.assertNotNull(save.getStartDate());
    }

}