package uk.comp2211.group13.panes;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.component.FilterComponent;
import uk.comp2211.group13.data.Save;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public abstract class BasePane extends BorderPane {

    public AppWindow appWindow;
    protected FilterComponent filters;
    protected HashMap<Date, Float> metric;
    protected HashMap<Filter, String[]> genderFilters = new HashMap<>();
    protected HashMap<Filter, String[]> ageFilters = new HashMap<>();
    protected HashMap<Filter, String[]> incomeFilters = new HashMap<>();
    protected HashMap<Filter, String[]> contextFilters = new HashMap<>();
    protected String[] GenderList = {};
    protected String[] AgeList = {};
    protected String[] IncomeList = {};
    protected String[] ContextList = {};
    protected HBox filterHbox = new HBox();
    private String[] metrics = {"Number of Clicks", "Number of Impressions", "Number of Uniques", "Number of Bounce Pages", "Number of Bounce Visits", "Rate of Conversions", "Total Costs", "CTR", "CPA", "CPC", "CPM", "Bounce Visit Rate", "Bounce Page Rate"};
    private ComboBox<String> metricBox = new ComboBox<>(FXCollections.observableArrayList(metrics));
    protected Metric currentMetric = Metric.Clicks;
    protected Granularity granularity = Granularity.Day;
    protected Date startDate;
    protected Date endDate;


    /**
     * Creates Base Scene
     *
     * @param appWindow - The app window that displays the scene
     */
    public BasePane (AppWindow appWindow) {
        this.appWindow = appWindow;
        startDate = appWindow.getData().getMinDate();
        endDate = appWindow.getData().getMaxDate();
    }

    /**
     * Build the layout of the pane.
     */
    public abstract void build();

    /**
     * Requests metric data
     *
     * @param currentMetric - The metric to be requested
     * @param startDate - The start date of the data
     * @param endDate - The end date of the data
     * @param filterMap - The filters hashmap
     * @param granularity - The granularity of the data
     * @return - The data hashmap
     */
    public HashMap<Date, Float> RequestData(Metric currentMetric, Date startDate, Date endDate, HashMap<Filter, String[]> filterMap, Granularity granularity){
        return appWindow.getMetrics().request(currentMetric, startDate, endDate, filterMap, granularity);
    }

    /**
     * Sets up and builds the required components
     */
    public void buildComponents() {
        filters.setStartDate(startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        filters.setEndDate(endDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        buildGraph();
        updateButtonAction(filters);
        resetButtonAction(filters);
        saveButtonAction(filters);
        loadButtonAction(filters);
        filters.setPrefWidth(appWindow.getWidth()/3);
        filters.setPrefHeight(appWindow.getHeight());
        filters.setStyle("-fx-border-color: black;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                + "-fx-border-radius: 5;");
        filterHbox.getChildren().add(filters);
        setCenter(filterHbox);
    }

    /**
     * Binds actions to the update button
     *
     * @param filterComponent - The corresponding filter component
     */
    public void updateButtonAction(FilterComponent filterComponent) {
        filterComponent.getUpdateButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (filterComponent.getStartDate().before(filterComponent.getEndDate())) {
                    startDate = filterComponent.getStartDate();
                    endDate = filterComponent.getEndDate();
                    granularity = Utility.getGranularity(filterComponent.getGranularity());
                    GenderList = addGenderFilters(filterComponent, GenderList);
                    AgeList = addAgeFilters(filterComponent, AgeList);
                    IncomeList = addIncomeFilters(filterComponent, IncomeList);
                    ContextList = addContextFilters(filterComponent, ContextList);
                    genderFilters = addFilters(genderFilters, Filter.Gender, GenderList);
                    ageFilters = addFilters(ageFilters, Filter.Age, AgeList);
                    incomeFilters = addFilters(incomeFilters, Filter.Income, IncomeList);
                    contextFilters = addFilters(contextFilters, Filter.Context, ContextList);
                    buildGraph();
                    filterHbox.getChildren().get(0).toFront();

                }

            }
        });
    }

    /**
     * Binds actions to the reset button
     *
     * @param filterComponent - The corresponding filter component
     */
    public void resetButtonAction(FilterComponent filterComponent){
        filterComponent.getResetButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (filterComponent.getStartDate().before(filterComponent.getEndDate()))
                {
                    filters.resetCheckBoxes();
                    granularity = Granularity.Day;
                    filters.fireDay();
                    GenderList = listClear(GenderList);
                    AgeList = listClear(AgeList);
                    IncomeList = listClear(IncomeList);
                    ContextList = listClear(ContextList);
                    genderFilters.clear();
                    ageFilters.clear();
                    incomeFilters.clear();
                    contextFilters.clear();
                    resetBounces();
                    buildGraph();
                    filterHbox.getChildren().get(0).toFront();
                }

            }
        });
    }

    public void saveButtonAction(FilterComponent filterComponent) {
        filterComponent.getSaveButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Save.saveFilters(genderFilters, ageFilters, incomeFilters, contextFilters, startDate, endDate, granularity);
            }
        });
    }

    public void loadButtonAction(FilterComponent filterComponent) {
        filterComponent.getLoadButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Save.loadFilters();
//                System.out.println(Save.ageFilters.toString());
//                System.out.println(Save.genderFilters.toString());
//                System.out.println(Save.incomeFilters.toString());
//                System.out.println(Save.contextFilters.toString());
                if (Save.startDate.before(Save.endDate)) {
                    startDate = Save.startDate;
                    endDate = Save.endDate;
                    granularity = Save.granularity;
                    GenderList = Save.genderFilters.get(Filter.Gender);
                    AgeList = Save.ageFilters.get(Filter.Age);
                    IncomeList = Save.incomeFilters.get(Filter.Income);
                    ContextList = Save.contextFilters.get(Filter.Context);
                    genderFilters = Save.genderFilters;
                    ageFilters = Save.ageFilters;
                    incomeFilters = Save.incomeFilters;
                    contextFilters = Save.incomeFilters;
                    filterComponent.setCheckBoxes(GenderList, AgeList, IncomeList, ContextList, granularity);
                    filterComponent.setStartDate(Save.startDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                    filterComponent.setEndDate(Save.endDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                    buildGraph();
                    filterHbox.getChildren().get(0).toFront();

                }
            }
        });

    }


    /**
     * Builds the graph or histogram. Defined in those classes and used polymorphically here
     */
    public void buildGraph(){
    }

    /**
     * Resets the visit and page spinners
     */
    public void resetBounces(){
        filters.resetVisitSpnr();
        filters.resetPageSpnr();
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

    /**
     * Adds filters to the gender String list if its corresponding checkbox is selected
     *
     * @param filterComponent - The corresponding filter component
     * @param filters - The string list of filters
     * @return - The updated string list
     */
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

    /**
     * Adds filters to the age String list if its corresponding checkbox is selected
     *
     * @param filterComponent - The corresponding filter component
     * @param filters - The string list of filters
     * @return - The updated string list
     */
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
        if (filterComponent.getAgeRange5Filter()){
            list.add(">54");
        }
        return list.toArray(new String[0]);
    }

    /**
     * Adds filters to the income String list if its corresponding checkbox is selected
     *
     * @param filterComponent - The corresponding filter component
     * @param filters - The string list of filters
     * @return - The updated string list
     */
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

    /**
     * Adds filters to the context String list if its corresponding checkbox is selected
     *
     * @param filterComponent - The corresponding filter component
     * @param filters - The string list of filters
     * @return - The updated string list
     */
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

    /**
     * Adds filters to the filter hashmap if its string list isn't empty. if it is, the filter is removed
     *
     * @param filterHashMap - The filter hashmap
     * @param filter - The filter type to be added or removed
     * @param filters - The filter type's string list of filters
     * @return The updated filter hashmap
     */
    public HashMap<Filter,String[]> addFilters(HashMap<Filter,String[]> filterHashMap, Filter filter, String[] filters){
        if (filters.length != 0){
            filterHashMap.put(filter,filters);
        }
        else{
            filterHashMap.remove(filter);
        }
        return filterHashMap;
    }

    /**
     * Getter method for the current metric
     *
     * @return - The current metric
     */
    public Metric getCurrentMetric(){
        return currentMetric;
    }

    /**
     * Getter method for the metric data
     *
     * @return - The metric data hashmap
     */
    public HashMap<Date,Float> getMetric(){
        return metric;
    }

    /**
     * Getter method for the filter component
     *
     * @return
     */
    public FilterComponent getFilters(){
        return filters;
    }

    /**
     * Setter method for the Metric data
     *
     * @param metric - The metric data hashmap
     */
    public void setMetric(HashMap<Date,Float> metric){
        metric = RequestData(currentMetric,startDate,endDate,mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters),granularity);
    }

    /**
     * Clears the filter string lists
     *
     * @param stringList - The filter string list to be cleared
     * @return The cleared string list
     */
    public String[] listClear(String[] stringList){
        return (new String[0]);
    }

}
