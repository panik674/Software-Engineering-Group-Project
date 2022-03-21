package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.enums.Filter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterTest {
    private Data data;
    @Before
    public void setupData() {
        data = new Data();

        ArrayList<String> pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");
        data.ingest(pathsTest);
    }
    @Test
    public void ageFilterTest() throws ParseException {
        HashMap<Filter, String[]> ageFilter = new HashMap<>();
        String[] ageFilterString ={"25-34"};
        ageFilter.put(Filter.Age,ageFilterString);
        Assert.assertEquals(7031,
                data.request(data.getMinDate(), data.getMaxDate(),ageFilter).getClicks());
    }
    @Test
    public void contextFilterTest(){
        HashMap<Filter, String[]> contextFilter = new HashMap<>();
        String[] travelFilter ={"Travel"};
        contextFilter.put(Filter.Context,travelFilter);
        Assert.assertEquals(0,
                data.request(data.getMinDate(), data.getMaxDate(),contextFilter).getClicks());
        HashMap<Filter, String[]> newsFilter = new HashMap<>();
        String[] news = {"News"};
        newsFilter.put(Filter.Context,news);
        Assert.assertEquals(6716,
                data.request(data.getMinDate(), data.getMaxDate(),newsFilter).getClicks());

    }

}
