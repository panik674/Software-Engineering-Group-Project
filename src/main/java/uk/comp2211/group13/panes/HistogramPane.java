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
import uk.comp2211.group13.component.FilterComponent;
import uk.comp2211.group13.component.GraphingComponent;
import uk.comp2211.group13.component.HistogramComponent;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;

import java.time.ZoneId;
import java.util.*;

public class HistogramPane extends BasePane {

    private static final Logger logger = LogManager.getLogger(HistogramPane.class);

    private HistogramComponent histogram;
    private FilterComponent filters = new FilterComponent("Histogram");
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
        filters.setStartDate(startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        filters.setEndDate(endDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        buildGraph();
        updateButtonAction(filters);
        filters.setPrefWidth(appWindow.getWidth()/3);
        filters.setPrefHeight(appWindow.getHeight());
        filters.setStyle("-fx-border-color: black;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                + "-fx-border-radius: 5;");
        filterHbox.getChildren().add(filters);
        setCenter(filterHbox);
    }

    public void buildGraph(){
        clickCosts = RequestData(Metric.ClickCost,startDate,endDate,mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters),granularity);
        filterHbox.getChildren().remove(histogram);
        histogram = new HistogramComponent(clickCosts);
        histogram.setPrefWidth(appWindow.getWidth()*2/3);
        histogram.setPrefHeight(appWindow.getHeight());
        filterHbox.getChildren().add(histogram);
    }

    public void updateButtonAction(FilterComponent filterComponent){
        filterComponent.getUpdateButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (filterComponent.getStartDate().before(filterComponent.getEndDate())) {
                    startDate = filterComponent.getStartDate();
                    endDate = filterComponent.getEndDate();
                    granularity = Utility.getGranularity(filterComponent.getGranularity());
                    GenderList = addGenderFilters(filterComponent, GenderList);
                    //System.out.println(GenderList[0]);
                    AgeList = addAgeFilters(filterComponent, AgeList);
                    //System.out.println(AgeList[0]);
                    IncomeList = addIncomeFilters(filterComponent, IncomeList);
                    //System.out.println(IncomeList[0]);
                    ContextList = addContextFilters(filterComponent, ContextList);
                    //System.out.println(ContextList[0]);
                    genderFilters = addFilters(genderFilters, Filter.Gender, GenderList);
                    //genderFilters.put(Filter.Gender,GenderList);
                    ageFilters = addFilters(ageFilters, Filter.Age, AgeList);
                    incomeFilters = addFilters(incomeFilters, Filter.Income, IncomeList);
                    contextFilters = addFilters(contextFilters, Filter.Context, ContextList);
                    //metric = RequestData(currentMetric,startDate,endDate,mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters),granularity);
                    //System.out.println(mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters));
                    filterHbox.getChildren().remove(histogram);
                    buildGraph();
                    filterHbox.getChildren().get(0).toFront();
                }

            }
        });
    }







}
