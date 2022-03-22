package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.enums.Filter;
import java.text.ParseException;
import java.util.*;

public class FilterFunctionTest {
    private HashMap<Filter,String[]> filterMap ;
    private Data data;

    @Before
    public void setupData() {
        data = new Data();

        ArrayList<String> pathsTest = new ArrayList<>();
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/click_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/impression_log.csv");
        pathsTest.add("src/test/java/uk/comp2211/group13/testdata/server_log.csv");
        data.ingest(pathsTest);

        filterMap = new HashMap<>();
    }

    @Test
    public void ageFilterFunctionTest() throws ParseException {
        String[] age = {"<25", "25-34", "35-44", "45-54", ">54"};
        filterMap.put(Filter.Age,age);
        int result = 23923;
        Assert.assertEquals(result,data.request(Utility.string2Date("2015-01-01 12:00:00"),
                Utility.string2Date("2015-01-15 12:00:00"),
                filterMap
        ).getClicks());
    }


    @Test
    public void genderFilterFunctionTest() throws ParseException {
        String[] gender = {"Female"};
        filterMap.put(Filter.Gender,gender);
        int result = 15935;
        Assert.assertEquals(result,data.request(Utility.string2Date("2015-01-01 12:00:00"),
                Utility.string2Date("2015-01-15 12:00:00"),
                filterMap
        ).getClicks());
    }

    @Test
    public void incomeFilterFunctionTest() throws ParseException {

        String[] income =  { "Medium", "High"};
        filterMap.put(Filter.Income,income);
        int result = 19345;
        Assert.assertEquals(result,data.request(Utility.string2Date("2015-01-01 12:00:00"),
                Utility.string2Date("2015-01-15 12:00:00"),
                filterMap
        ).getClicks());
    }

    @Test
    public void contextFilterFunctionTest() throws ParseException {
        String[] context =  {"News", "Travel"};
        filterMap.put(Filter.Context,context);
        int result = 6716;
        Assert.assertEquals(result,data.request(Utility.string2Date("2015-01-01 12:00:00"),
                Utility.string2Date("2015-01-15 12:00:00"),
                filterMap
        ).getClicks());
    }
}


