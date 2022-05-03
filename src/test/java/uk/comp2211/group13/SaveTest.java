package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.comp2211.group13.data.Save;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.util.*;

import static uk.comp2211.group13.data.Save.getDirectory;


public class SaveTest {
    private Save save;

    private int filterSet = 2;

    @Before
    public void setupData() {
        save = new Save();
    }

    @Test
    public void saveAndLoadTest() throws ParseException {
        Date startDate = Utility.string2Date("2015-01-15 12:00:00");
        Date endDate = Utility.string2Date("2015-01-01 12:00:00");

        for (int i = 0; i < 5; i++) {
            ArrayList<String> genders = new ArrayList<>(List.of(Utility.getGenders));
            ArrayList<String> ages = new ArrayList<>(List.of(Utility.getAges));
            ArrayList<String> incomes = new ArrayList<>(List.of(Utility.getIncomes));
            ArrayList<String> context = new ArrayList<>(List.of(Utility.getContexts));

            HashMap<Filter, String[]> genderFilters = new HashMap<>();
            HashMap<Filter, String[]> ageFilters = new HashMap<>();
            HashMap<Filter, String[]> incomeFilters = new HashMap<>();
            HashMap<Filter, String[]> contextFilters = new HashMap<>();

            Random random = new Random();
            ArrayList<String> temp = new ArrayList<>();
            while (temp.size() < filterSet) {
                int pos = random.nextInt(genders.size());

                temp.add(genders.get(pos));
                genders.remove(pos);
            }
            genderFilters.put(Filter.Gender, temp.toArray(new String[0]));


            temp = new ArrayList<>();
            while (temp.size() < filterSet) {
                int pos = random.nextInt(ages.size());

                temp.add(ages.get(pos));
                ages.remove(pos);
            }
            ageFilters.put(Filter.Age, temp.toArray(new String[0]));


            temp = new ArrayList<>();
            while (temp.size() < filterSet) {
                int pos = random.nextInt(incomes.size());

                temp.add(incomes.get(pos));
                incomes.remove(pos);
            }
            incomeFilters.put(Filter.Income, temp.toArray(new String[0]));


            temp = new ArrayList<>();
            while (temp.size() < filterSet) {
                int pos = random.nextInt(context.size());

                temp.add(context.get(pos));
                context.remove(pos);
            }
            contextFilters.put(Filter.Context, temp.toArray(new String[0]));

            save.saveFilters(genderFilters, ageFilters, incomeFilters, contextFilters, startDate, endDate, Granularity.Hour);

            save.loadFilters();

            Assert.assertEquals(genderFilters.get(Filter.Gender), Save.genderFilters.get(Filter.Gender));
            Assert.assertEquals(ageFilters.get(Filter.Age), Save.ageFilters.get(Filter.Age));
            Assert.assertEquals(incomeFilters.get(Filter.Income), Save.incomeFilters.get(Filter.Income));
            Assert.assertEquals(contextFilters.get(Filter.Context), Save.contextFilters.get(Filter.Context));
            Assert.assertEquals(startDate, Save.startDate);
            Assert.assertEquals(endDate, Save.endDate);
            Assert.assertEquals(Granularity.Hour, Save.granularity);
        }
    }
}