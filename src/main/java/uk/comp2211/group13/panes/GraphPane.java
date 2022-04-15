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
import jdk.jshell.execution.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.component.FilterComponent;
import uk.comp2211.group13.component.GraphingComponent;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;

import java.time.ZoneId;
import java.util.*;

public class GraphPane extends BasePane {

    private static final Logger logger = LogManager.getLogger(GraphPane.class);
    private GraphingComponent lineChart;
    private FilterComponent filters = new FilterComponent("Graph");
    private HashMap<Date, Float> metric;
    private HashMap<Filter, String[]> genderFilters = new HashMap<>();
    private HashMap<Filter, String[]> ageFilters = new HashMap<>();
    private HashMap<Filter, String[]> incomeFilters = new HashMap<>();
    private HashMap<Filter, String[]> contextFilters = new HashMap<>();
    private String[] GenderList = {};
    private String[] AgeList = {};
    private String[] IncomeList = {};
    private String[] ContextList = {};
    private HBox filterHbox = new HBox();
    private String[] metrics = {"Number of Clicks", "Number of Impressions", "Number of Uniques", "Number of Bounce Pages", "Number of Bounce Visits", "Rate of Conversions", "Total Costs", "CTR", "CPA", "CPC", "CPM", "Bounce Visit Rate", "Bounce Page Rate"};
    private ComboBox<String> metricBox = new ComboBox<>(FXCollections.observableArrayList(metrics));
    private Metric currentMetric = Metric.Clicks;
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
        metric = RequestData(currentMetric,startDate,endDate,mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters),granularity);
        filterHbox.getChildren().remove(lineChart);
        lineChart = new GraphingComponent(filters.getMetric(),metric);
        lineChart.setPrefWidth(appWindow.getWidth()*2/3);
        lineChart.setPrefHeight(appWindow.getHeight());
        filterHbox.getChildren().add(lineChart);
    }

    public void updateButtonAction(FilterComponent filterComponent){
        filterComponent.getUpdateButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (filterComponent.getStartDate().before(filterComponent.getEndDate())) {
                    currentMetric = Utility.getMetric(filterComponent.getMetric());
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
                    filterHbox.getChildren().remove(lineChart);
                    buildGraph();
                    filterHbox.getChildren().get(0).toFront();
                }

            }
        });
    }






}
