package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.comp2211.group13.enums.Filter;
import java.util.*;

public class FilterTest {
    private HashMap<Filter,String[]> filterMap ;
    private String[] age;
    private String[] gender;
    private String[] income;
    private String[] context;


    @Before
    public void setupData() {
        age = new String[]{"<25", "25-34", "35-44", "45-54", ">54"};
        gender = new String[] {"Male", "Female"};
        income = new String[]{"Low", "Medium", "High"};
        context = new String[]{"News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel"};
        filterMap = new HashMap<>();
        filterMap.put(Filter.Age, age);
        filterMap.put(Filter.Gender,gender);
        filterMap.put(Filter.Income,income);
        filterMap.put(Filter.Context,context);
    }
    @Test
    public void filterNumberTest(){
        int result = 4;
        Assert.assertNotNull(filterMap);
        Assert.assertEquals(result,filterMap.size());
    }
    @Test
    public void ageFilterLackTest(){
        String[] result = {"<25", "25-34", "35-44"};
        Assert.assertNotNull(filterMap.get(Filter.Age));
        Assert.assertTrue(filterMap.get(Filter.Age).length != result.length);
    }
    @Test
    public void ageFilterTest(){
        String[] result = {"<25", "25-34", "35-44", "45-54", ">54"};
        int size = 5;
        Assert.assertTrue(filterMap.get(Filter.Age).length == size);
        Assert.assertArrayEquals(result,filterMap.get(Filter.Age));
    }

    @Test
    public void genderFilterTest(){
        String[] result ={"Male", "Female"};
        Assert.assertArrayEquals(result,filterMap.get(Filter.Gender));
    }
    @Test
    public void genderLackTest(){
        String[] result = {"Male"};
        Assert.assertTrue(filterMap.get(Filter.Gender).length!=result.length);
    }
    @Test
    public void incomeFilterTest(){
        String[] result = {"Low", "Medium", "High"};
        Assert.assertArrayEquals(result,filterMap.get(Filter.Income));
    }
    @Test
    public void incomeLackTest(){
        String[] result = {"Low"};
        Assert.assertTrue(filterMap.get(Filter.Income).length!=result.length);
    }
    @Test
    public void contextFilterTest(){
        String[] result = {"News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel"};
        Assert.assertArrayEquals(result,filterMap.get(Filter.Context));
    }
    @Test
    public void contextLackTest(){
        String[] result = {"News", "Shopping"};
        Assert.assertTrue(filterMap.get(Filter.Context).length!= result.length);
    }
    
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

