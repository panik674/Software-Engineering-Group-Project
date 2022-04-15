package uk.comp2211.group13.panes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import uk.comp2211.group13.component.FilterComponent;
import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;

import java.util.*;

public abstract class BasePane extends BorderPane {

    public AppWindow appWindow;

    public BasePane (AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    /**
     * Build the layout of the pane.
     */
    public abstract void build();



    public HashMap<Date, Float> RequestData(Metric currentMetric, Date startDate, Date endDate, HashMap<Filter, String[]> filterMap, Granularity granularity){
        return appWindow.getMetrics().request(currentMetric, startDate, endDate, filterMap, granularity);
    }

    /**
     * Method to merge the three filter hashmaps to apply them all
     *
     * @param genderFilters - The filter hashmap for the gender filters
     * @param ageFilters - The filter hashmap for the age filters
     * @param incomeFilters - The filter hashmap for the income filters
     * @param contextFilters - The filter hashmap for the context filters
     * @return - The merged filter hashmap
     */
    public HashMap<Filter, String[]> mergeFilter (HashMap<Filter, String[]> genderFilters, HashMap<Filter, String[]> ageFilters, HashMap<Filter, String[]> incomeFilters, HashMap<Filter, String[]> contextFilters){
        HashMap<Filter, String[]> combinedFilters = new HashMap<>();
        combinedFilters.putAll(genderFilters);
        combinedFilters.putAll(ageFilters);
        combinedFilters.putAll(incomeFilters);
        combinedFilters.putAll(contextFilters);
        return combinedFilters;
    }

    public String[] addGenderFilters (FilterComponent filterComponent,String[] filters){
        List<String> list = new ArrayList<String>(Arrays.asList(filters));
        list.clear();
        if(filterComponent.getMaleFilter()){
            list.add("Male");
        }
        if (filterComponent.getFemaleFilter()){
            list.add("Female");
        }
        return list.toArray(new String[0]);
    }

    public String[] addAgeFilters (FilterComponent filterComponent,String[] filters){
        List<String> list = new ArrayList<String>(Arrays.asList(filters));
        list.clear();
        if(filterComponent.getAgeRange1Filter()){
            list.add("<25");
        }
        if (filterComponent.getAgeRange2Filter()){
            list.add("25-34");
        }
        if (filterComponent.getAgeRange3Filter()){
            list.add("35-44");
        }
        if (filterComponent.getAgeRange4Filter()){
            list.add("45-54");
        }
        if (filterComponent.getAgeRange4Filter()){
            list.add(">54");
        }
        return list.toArray(new String[0]);
    }

    public String[] addIncomeFilters (FilterComponent filterComponent,String[] filters){
        List<String> list = new ArrayList<String>(Arrays.asList(filters));
        list.clear();
        if(filterComponent.getLowIncomeFilter()){
            list.add("Low");
        }
        if (filterComponent.getMediumIncomeFilter()){
            list.add("Medium");
        }
        if (filterComponent.getHighIncomeFilter()){
            list.add("High");
        }
        return list.toArray(new String[0]);
    }

    public String[] addContextFilters (FilterComponent filterComponent,String[] filters){
        List<String> list = new ArrayList<String>(Arrays.asList(filters));
        list.clear();
        if(filterComponent.getNewsFilter()){
            list.add("News");
        }
        if (filterComponent.getShoppingFilter()){
            list.add("Shopping");
        }
        if (filterComponent.getsocialMediaFilter()){
            list.add("Social Media");
        }
        if (filterComponent.getBlogFilter()){
            list.add("Blog");
        }
        if (filterComponent.getHobbyFilter()){
            list.add("Hobbies");
        }
        if (filterComponent.getsocialMediaFilter()){
            list.add("Social Media");
        }
        if (filterComponent.getTravelFilter()){
            list.add("Travel");
        }
        return list.toArray(new String[0]);
    }

    public HashMap<Filter,String[]> addFilters(HashMap<Filter,String[]> filterHashMap, Filter filter, String[] filters){
        if (filters.length != 0){
            filterHashMap.put(filter,filters);
        }
        else{
            filterHashMap.remove(filter);
        }
        return filterHashMap;
    }


}
