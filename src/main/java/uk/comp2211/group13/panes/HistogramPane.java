package uk.comp2211.group13.panes;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;

import java.time.ZoneId;
import java.util.*;

public class HistogramPane extends BasePane {

    private static final Logger logger = LogManager.getLogger(HistogramPane.class);

    private BarChart histogram;
    private StackPane graphingPane;
    private HashMap<Date, Float> clickCosts  ;
    private HashMap<Filter, String[]> genderFilters = new HashMap<>();
    private HashMap<Filter, String[]> ageFilters = new HashMap<>();
    private HashMap<Filter, String[]> incomeFilters = new HashMap<>();
    private HashMap<Filter, String[]> contextFilters = new HashMap<>();
    private String[] GenderList = {};
    private String[] AgeList = {};
    private String[] IncomeList = {};
    private String[] ContextList = {};
    private VBox vbox = new VBox();
    private HBox filterHbox = new HBox();
    private Utility util = new Utility();
    private Granularity granularity = Granularity.Day;
    private Date startDate = appWindow.getData().getMinDate();
    private Date endDate = appWindow.getData().getMaxDate();
    private HBox dateHbox = new HBox();

    public HistogramPane(AppWindow appWindow) {
        super(appWindow);

        clickCosts = appWindow.getMetrics().request(
                Metric.ClickCost,
                appWindow.getData().getMinDate(),
                appWindow.getData().getMaxDate(),
                new HashMap<>(),
                Granularity.Day
        );

        build();
    }

    /**
     * Build the layout of the scene.
     */
    @Override
    public void build() {
        dateHbox.getChildren().add(regionBuild());
        dateHbox.getChildren().add(dateCalen("Start"));
        dateHbox.getChildren().add(regionBuild());
        dateHbox.getChildren().add(dateCalen("End"));
        dateHbox.getChildren().add(regionBuild());
        vbox.getChildren().add(dateHbox);

        String[] timeGrans = {"Day","Hour","Month","Year"};
        ComboBox granularityBox = new ComboBox(FXCollections.observableArrayList(timeGrans));
        granularityBox.setValue(timeGrans[0]);
        granularityBox.setOnAction(e -> {
            vbox.getChildren().remove(histogram);
            if ("Day".equals(granularityBox.getValue())){
                granularity = Granularity.Day;
            }
            else if ("Hour".equals(granularityBox.getValue())){
                granularity = Granularity.Hour;
            }
            else if ("Month".equals(granularityBox.getValue())){
                granularity = Granularity.Month;
            }
            else {
                granularity = Granularity.Year;
            }
            clickCosts = appWindow.getMetrics().request(Metric.ClickCost, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);
            histogramBuild();
        });
        vbox.getChildren().add(granularityBox);

        MenuButton genderBox = filterMenu(new String[]{"Male", "Female"},"Gender");
        filterHbox.getChildren().add(regionBuild());
        MenuButton ageBox = filterMenu(new String[]{"<25", "25-34", "35-44", "45-54", ">54"},"Age");
        filterHbox.getChildren().add(regionBuild());
        MenuButton incomeBox = filterMenu(new String[]{"Low", "Medium", "High"},"Income");
        filterHbox.getChildren().add(regionBuild());
        MenuButton contextBox = filterMenu(new String[]{"News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel"},"Context");
        vbox.getChildren().add(filterHbox);

        //Creating a button to disable all of the current filters
        Button filterReset = new Button("Remove all filters");
        //Binding an action event to the button
        filterReset.setOnAction(new EventHandler<ActionEvent>() {
            //Method to clear all the filter hashmaps, uncheck all the items in the filter drop-downs and rebuild the graph accordingly
            @Override public void handle(ActionEvent e) {
                vbox.getChildren().remove(histogram);
                filterRemove(genderFilters, genderBox);
                filterRemove(ageFilters, ageBox);
                filterRemove(incomeFilters, incomeBox);
                filterRemove(contextFilters, contextBox);
                clickCosts = appWindow.getMetrics().request(Metric.ClickCost, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);
                histogramBuild();

            }
        });

        vbox.getChildren().add(filterReset);

        //Building the histogram for the ClickCosts
        histogramBuild();
    }

    /**
     * Method to build the ClickCost histogram
     */
    public void histogramBuild() {
        //Setting up the x and y axes and labelling them accordingly
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Dates");
        yAxis.setLabel("Click Costs (£)");

        //Building the bar chart widget
        histogram = new BarChart(xAxis, yAxis);
        histogram.setTitle("Histogram to show Click Costs Distribution");

        //Setting the chart gaps to zero, as this is how histograms must be presented
        histogram.setCategoryGap(0);
        histogram.setBarGap(0);

        //A Chart series is created here which will hold the data values to be plotted
        XYChart.Series dataValues = new XYChart.Series();

        //Here a list is created to store the Dates corresponding to the metric value which is then sorted into chronological order
        List<Date> dates = new ArrayList<Date>(clickCosts.keySet());
        Collections.sort(dates);

        //Iterating through the list of dates and adding the date along with its corresponding Click Cost value to the chart series and then adding the series to the histogram
        for (Date i : dates) {
            dataValues.getData().add(new XYChart.Data(i.toString(), clickCosts.get(i)));
        }

        //Adding the series to the chart, setting its name and adding it the parsed VBox
        histogram.getData().add(dataValues);
        dataValues.setName("Click Costs");
        vbox.getChildren().add(histogram);
    }

    /**
     * Method to remove filters from the 'filters' HashMap
     *
     * @param filtType - The type of the filter being removed
     * @param filt - The filter which is being removed
     */
    public void removeFilters(Filter filtType, String filt) {
        //Checking the filter type, removing the filter from the appropriate filter String[] list and adding it to the filters HashMap
        //Also removing the HashMap entries with empty lists, so all metric values are displayed without any filters applied
        if (filtType == Filter.Gender) {
            List<String> list = new ArrayList<String>(Arrays.asList(GenderList));
            list.remove(filt);
            GenderList = list.toArray(new String[0]);
            genderFilters.put(Filter.Gender, GenderList);
            for (Filter i : genderFilters.keySet()){
                if (genderFilters.get(i).length == 0){
                    genderFilters.remove(i);
                }
            }
        } else if (filtType == Filter.Age) {
            List<String> list = new ArrayList<String>(Arrays.asList(AgeList));
            list.remove(filt);
            AgeList = list.toArray(new String[0]);
            ageFilters.put(Filter.Age, AgeList);
            for (Filter i : ageFilters.keySet()){
                if (ageFilters.get(i).length == 0){
                    ageFilters.remove(i);
                }
            }
        } else if (filtType == Filter.Income) {
            List<String> list = new ArrayList<String>(Arrays.asList(IncomeList));
            list.remove(filt);
            IncomeList = list.toArray(new String[0]);
            incomeFilters.put(Filter.Income, IncomeList);
            for (Filter i : incomeFilters.keySet()){
                if (incomeFilters.get(i).length == 0){
                    incomeFilters.remove(i);
                }
            }
        } else {
            List<String> list = new ArrayList<String>(Arrays.asList(ContextList));
            list.remove(filt);
            ContextList = list.toArray(new String[0]);
            contextFilters.put(Filter.Context, ContextList);
            for (Filter i : contextFilters.keySet()){
                if (contextFilters.get(i).length == 0){
                    contextFilters.remove(i);
                }
            }
        }


    }

    /**
     * Method to add filters from the 'filters' HashMap
     *
     * @param filtType - The type of the filter being added
     * @param filt - The filter which is being added
     */
    public void addFilters(Filter filtType, String filt) {
        //Checking the filter type, adding the filter from the appropriate filter String[] list and adding it to the filters HashMap
        if (filtType == Filter.Gender) {
            List<String> list = new ArrayList<String>(Arrays.asList(GenderList));
            list.add(filt);
            GenderList = list.toArray(new String[0]);
            genderFilters.put(Filter.Gender, GenderList);
        } else if (filtType == Filter.Age) {
            List<String> list = new ArrayList<String>(Arrays.asList(AgeList));
            list.add(filt);
            AgeList = list.toArray(new String[0]);
            ageFilters.put(Filter.Age, AgeList);
        } else if (filtType == Filter.Income) {
            List<String> list = new ArrayList<String>(Arrays.asList(IncomeList));
            list.add(filt);
            IncomeList = list.toArray(new String[0]);
            incomeFilters.put(Filter.Income, IncomeList);
        } else {
            List<String> list = new ArrayList<String>(Arrays.asList(ContextList));
            list.add(filt);
            ContextList = list.toArray(new String[0]);
            contextFilters.put(Filter.Context, ContextList);
        }
    }

    /**
     * Method to create a filter MenuButton and bind actions to its items
     *
     * @param filterNames - List of filters to be added to the MenuButton
     * @param filterType - Label for the MenuButton
     * @return The built MenuButton to allow for interaction later
     */
    public MenuButton filterMenu(String[] filterNames, String filterType){
        //Creating a MenuButton to allow the user to select filters to apply to the graph
        MenuButton menuButton = new MenuButton(filterType);
        for (String i : filterNames) {
            //Creating different CheckMenuItems for each filter
            CheckMenuItem CMItem = new CheckMenuItem(i);
            //Binding actions to the CheckMenuItems
            CMItem.setOnAction(e -> {
                //Adding filters to the graphs when they are selected
                if (CMItem.isSelected()) {
                    addFilters(util.filterType(i),i);
                    vbox.getChildren().remove(histogram);
                    clickCosts = appWindow.getMetrics().request(Metric.ClickCost, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);
                    histogramBuild();

                }
                //Removing filters from the graphs when they are unselected
                else {
                    removeFilters(util.filterType(i),i);
                    vbox.getChildren().remove(histogram);
                    clickCosts = appWindow.getMetrics().request(Metric.ClickCost, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);
                    histogramBuild();
                }
            });
            menuButton.getItems().add(CMItem);
        }
        filterHbox.getChildren().add(menuButton);

        return menuButton;
    }

    /**
     *Method to Build and grow regions to provide spacing for the filter HBox
     *
     * @return - The grown region
     */
    public Region regionBuild(){
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }


    /**
     * Method to merge the three filter hashmaps to apply them all when plotting the linechart
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

    public void filterRemove(HashMap<Filter,String[]> filterMap, MenuButton filterBox){
        filterMap.clear();
        for(MenuItem i : filterBox.getItems()){
            ((CheckMenuItem) i).setSelected(false);
        }
    }

    /**
     * Method to build DatePickers to choose the date range of the data graphed
     *
     * @param date - Used to specify which date instance variable needs to be queried
     * @return - The DatePicker
     */
    public DatePicker dateCalen(String date){
        DatePicker dp = new DatePicker();
        switch (date){
            case "Start" :
                dp.setValue(startDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());
                break;
            case "End" :
                dp.setValue(endDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());
        }
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                vbox.getChildren().remove(histogram);
                switch (date){
                    case "Start" :
                        startDate = java.util.Date.from(dp.getValue().atStartOfDay()
                                .atZone(ZoneId.systemDefault())
                                .toInstant());
                        break;
                    case "End" :
                        endDate = java.util.Date.from(dp.getValue().atTime(23, 59, 59)
                                .atZone(ZoneId.systemDefault())
                                .toInstant());
                        break;
                }
                clickCosts = appWindow.getMetrics().request(Metric.ClickCost, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);
                histogramBuild();
            }
        };
        dp.setOnAction(event);
        return dp;
    }

}
