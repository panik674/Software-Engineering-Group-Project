package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Logs;
import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.enums.Filter;

import java.util.HashMap;

public class MetricsRequestOverViewTest {
    private Data data;
    private Metrics metrics;
    private Logs logs;
    private HashMap<Filter, String[]> filter;

    @Before
    public void setupData() {
        data = new Data();

        data.ingest("src/test/java/uk/comp2211/group13/testdata");

        metrics = new Metrics(data);

        filter = new HashMap<>();
        logs = data.request(data.getMinDate(), data.getMaxDate(), filter);
    }

    @Test
    public void allFiltersRequestOverviewTest(){
        for (String gender : Utility.getGenders) {
            for (String age : Utility.getAges) {
                for (String income : Utility.getIncomes) {
                    for (String context : Utility.getContexts) {
                        filter = new HashMap<>();

                        filter.put(Filter.Gender, new String[]{gender});
                        filter.put(Filter.Age, new String[]{age});
                        filter.put(Filter.Income, new String[]{income});
                        filter.put(Filter.Context, new String[]{context});

                        Assert.assertNotNull(metrics.requestOverview(data.getMinDate(),data.getMaxDate(),filter));
                    }
                }
            }
        }
    }

    @Test
    public void allRequestOverviewTest() {
        Assert.assertNotNull(metrics.requestOverview(data.getMinDate(), data.getMaxDate(), new HashMap<>()));
    }


    @Test
    public void dateTest() {
        Assert.assertNotNull(metrics.requestOverview(data.getMinDate(), data.getMaxDate(), filter));
    }

    @Test
    public void sameDateTest() {
        Assert.assertNotNull(metrics.requestOverview(data.getMinDate(), data.getMinDate(), filter));
    }

    @Test
    public void invalidDateTest() {
        try{
            Assert.assertNull(metrics.requestOverview(data.getMaxDate(), data.getMinDate(),filter));
        }catch (Exception e){
            e.getMessage();
        }
    }
    @Test
    public void testInvalidFilters() {
        try{
            HashMap<Filter, String[]> filter = new HashMap<>();
            filter.put(Filter.Gender, new String[]{"gender"});

            Assert.assertNull(metrics.requestOverview(data.getMinDate(),data.getMaxDate(),filter));

            filter = new HashMap<>();
            filter.put(Filter.Age, new String[]{"age"});

            Assert.assertNull(metrics.requestOverview(data.getMinDate(),data.getMaxDate(),filter));

            filter = new HashMap<>();
            filter.put(Filter.Income, new String[]{"income"});

            Assert.assertNull(metrics.requestOverview(data.getMinDate(),data.getMaxDate(),filter));

            filter = new HashMap<>();
            filter.put(Filter.Context, new String[]{"context"});

            Assert.assertNull(metrics.requestOverview(data.getMinDate(),data.getMaxDate(),filter));

        }catch (Exception e){
            e.getMessage();
        }
    }
}
