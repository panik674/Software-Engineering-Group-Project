package uk.comp2211.group13.panes;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.component.FilterComponent;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;

import java.time.ZoneId;
import java.util.*;

public class GraphPane extends BasePane {

    private static final Logger logger = LogManager.getLogger(GraphPane.class);

    private LineChart lineChart;
    private HashMap<Date, Float> metric;
    private HashMap<Filter, String[]> genderFilters = new HashMap<>();
    private HashMap<Filter, String[]> ageFilters = new HashMap<>();
    private HashMap<Filter, String[]> incomeFilters = new HashMap<>();
    private HashMap<Filter, String[]> contextFilters = new HashMap<>();
    private Metric currentMetric;
    private String[] GenderList = {};
    private String[] AgeList = {};
    private String[] IncomeList = {};
    private String[] ContextList = {};
    private Utility util = new Utility();
    private VBox vbox = new VBox();
    private HBox filterHbox = new HBox();
    private String[] metrics = {"Number of Clicks", "Number of Impressions", "Number of Uniques", "Number of Bounce Pages", "Number of Bounce Visits", "Rate of Conversions", "Total Costs", "CTR", "CPA", "CPC", "CPM", "Bounce Visit Rate", "Bounce Page Rate"};
    private ComboBox<String> metricBox = new ComboBox<>(FXCollections.observableArrayList(metrics));
    private Granularity granularity = Granularity.Day;
    private Date startDate = appWindow.getData().getMinDate();
    private Date endDate = appWindow.getData().getMaxDate();
    private HBox dateHbox = new HBox();

    public GraphPane (AppWindow appWindow) {
        super(appWindow);
        build();
    }

    /**
     * Build the layout of the scene.
     */
    @Override
    public void build() {
        //Building the VBox which will contain the main UI elements

        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(20);
        setTop(vbox);

        //Creating a ComboBox widget which will allow the user to choose which metric graph to display
        metricBox.setValue(metrics[0]);
        vbox.getChildren().add(metricBox);
        vbox.getChildren().add(new HBox (regionBuild(), new FilterComponent("Graph"),regionBuild()));



        //Setting the default metric graph to the "Clicks" metric. The data is requested and is then parsed to the metricGraph method
        currentMetric = Metric.Clicks;
        metric = appWindow.getMetrics().request(currentMetric, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);


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
            vbox.getChildren().remove(lineChart);
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
            metric = appWindow.getMetrics().request(currentMetric, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);
            metricGraph(metricBox.getValue(), metric);
            System.out.println(metric);
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
                vbox.getChildren().remove(lineChart);
                filterRemove(genderFilters,genderBox);
                filterRemove(ageFilters,ageBox);
                filterRemove(incomeFilters,incomeBox);
                filterRemove(contextFilters,contextBox);
                metric = appWindow.getMetrics().request(currentMetric, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);
                metricGraph(metricBox.getValue(), metric);

            }
        });

        vbox.getChildren().add(filterReset);



        metricGraph(metricBox.getValue(), metric);

        //Adding an action the metric ComboBox which removes the current graph from the VBox and adds a new one in its place with the selected metric's data requested and plotted
        metricBox.setOnAction(e -> {
            vbox.getChildren().remove(lineChart);
            if ("Number of Clicks".equals(metricBox.getValue())) {
                currentMetric = Metric.Clicks;

            } else if ("Number of Impressions".equals(metricBox.getValue())) {
                currentMetric = Metric.Impressions;

            } else if ("Number of Uniques".equals(metricBox.getValue())) {
                currentMetric = Metric.Unique;

            } else if ("Number of Bounce Pages".equals(metricBox.getValue())) {
                currentMetric = Metric.BouncePage;

            } else if ("Number of Bounce Visits".equals(metricBox.getValue())) {
                currentMetric = Metric.BounceVisit;

            } else if ("Number of Conversions".equals(metricBox.getValue())) {
                currentMetric = Metric.Conversions;

            } else if ("Total Costs".equals(metricBox.getValue())) {
                currentMetric = Metric.TotalCost;

            } else if ("CTR".equals(metricBox.getValue())) {
                currentMetric = Metric.CTR;

            } else if ("CPA".equals(metricBox.getValue())) {
                currentMetric = Metric.CPA;

            } else if ("CPC".equals(metricBox.getValue())) {
                currentMetric = Metric.CPC;

            } else if ("CPM".equals(metricBox.getValue())) {
                currentMetric = Metric.CPM;

            } else if ("Bounce Visit Rate".equals(metricBox.getValue())) {
                currentMetric = Metric.BounceRateVisit;

            } else {
                currentMetric = Metric.BounceRatePage;
            }
            metric = appWindow.getMetrics().request(currentMetric, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);
            metricGraph(metricBox.getValue(), metric);
        });

    }

    /**
     * Method to plot a graph for a given metric
     *
     * @param metricLabel  - Label for the y-axis
     * @param metricToPlot - Metric data
     */
    public void metricGraph(String metricLabel, HashMap<Date, Float> metricToPlot) {
        //Setting up the x and y axes and labelling them accordingly
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(metricLabel);

        //Building the line chart widget
        lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Graph to show " + metricLabel);
        vbox.getChildren().add(lineChart);

        //A Chart series is created here which will hold the data values to be plotted
        XYChart.Series dataValues = new XYChart.Series();
        dataValues.setName("Will be a filter in future");

        //Here a list is created to store the Dates corresponding to the metric value which is then sorted into chronological order
        List<Date> dates = new ArrayList<Date>(metricToPlot.keySet());
        Collections.sort(dates);

        //Iterating through the list of dates and adding the date along with its corresponding metric value to the chart series and then adding the series to the graph
        for (Date i : dates) {
            dataValues.getData().add(new XYChart.Data(i.toString(), metricToPlot.get(i)));
        }
        lineChart.getData().add(dataValues);
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
                    vbox.getChildren().remove(lineChart);
                    metric = appWindow.getMetrics().request(currentMetric, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);
                    metricGraph(metricBox.getValue(), metric);

                }
                //Removing filters from the graphs when they are unselected
                else {
                    removeFilters(util.filterType(i),i);
                    vbox.getChildren().remove(lineChart);
                    metric = appWindow.getMetrics().request(currentMetric, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);
                    metricGraph(metricBox.getValue(), metric);
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

    /**
     * Method to clear a filter hashmap and unselecting all the items in its corresponding MenuButton to false
     *
     * @param filterMap - The filter HashMap to be cleared
     * @param filterBox - The filter MenuButton to have all its items unselected
     */
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
                vbox.getChildren().remove(lineChart);
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
                metric = appWindow.getMetrics().request(currentMetric, startDate, endDate, mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters), granularity);
                metricGraph(metricBox.getValue(), metric);
            }
        };
        dp.setOnAction(event);
        return dp;
    }

}
