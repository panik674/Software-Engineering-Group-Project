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
import java.util.HashMap;
import java.util.HashSet;

import static uk.comp2211.group13.data.Save.getDirectory;


public class SaveTest  {
    private Data data;
    private Save save;
    public  HashMap<Filter, String[]> genderFilters;
    public  HashMap<Filter, String[]> ageFilters;
    public  HashMap<Filter, String[]> incomeFilters;
    public  HashMap<Filter, String[]> contextFilters;
    public  Date startDate;
    public  Date endDate;
    public  Granularity granularity;
    @Before
    public void setupData() throws ParseException {
        save = new Save();
        genderFilters = new HashMap<>();
        ageFilters = new HashMap<>();
        incomeFilters = new HashMap<>();
        contextFilters = new HashMap<>();
        startDate = new Date();
        endDate = new Date();

        genderFilters.put(Filter.Gender,new String[]{"Male", "Female"});
        ageFilters.put(Filter.Age,new String[]{"<25", "25-34", "35-44", "45-54", ">54"});
        incomeFilters.put(Filter.Income,new String[]{"Low", "Medium", "High"});
        contextFilters.put(Filter.Context,new String[]{"News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel"});
        endDate = Utility.string2Date("2015-01-15 12:00:00");
        startDate = Utility.string2Date("2015-01-01 12:00:00");

        save.saveFilters(genderFilters,ageFilters,incomeFilters,contextFilters,startDate,endDate,Granularity.Second);
        save.loadFilters();
    }
    /**This test will test save filters
     * */
    @Test
    public void testSaveFilters() throws FileNotFoundException {
        String path = getDirectory();
        FileReader genderFile = new FileReader(path+ "gender.json");
        FileReader ageFile = new FileReader(path +  "age.json");
        FileReader incomeFile = new FileReader(path + "income.json");
        FileReader contextFile = new FileReader(path +"context.json" );
        FileReader dateFile = new FileReader(path + "date.json");
        FileReader granularityFile = new FileReader(path + "granularity.json");
        Assert.assertNotNull(genderFile);
        Assert.assertNotNull(ageFile);
        Assert.assertNotNull(incomeFile);
        Assert.assertNotNull(contextFile);
        Assert.assertNotNull(dateFile);
        Assert.assertNotNull(granularityFile);
    }
    /**This test will test load filter**/
    @Test
    public void testLoadFilters() {
        Assert.assertNotNull(Save.incomeFilters);
        Assert.assertNotNull(Save.ageFilters);
        Assert.assertNotNull(Save.genderFilters);
        Assert.assertNotNull(Save.contextFilters);
        Assert.assertNotNull(Save.endDate);
        Assert.assertNotNull(Save.startDate);
        Assert.assertNotNull(Save.granularity);

    }
}