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
import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;

import java.time.ZoneId;
import java.util.*;

public class GraphPane extends BasePane {

    private static final Logger logger = LogManager.getLogger(GraphPane.class);

    private GraphingComponent lineChart;


    public GraphPane (AppWindow appWindow) {
        super(appWindow);
        filters = new FilterComponent("Graph");
        build();
    }

    /**
     * Build the layout of the scene.
     */
    @Override
    public void build() {
        build2();
        System.out.println(getCurrentMetric());
    }

    public void buildGraph(){
        appWindow.getMetrics().setBouncePages(filters.getBouncePage());
        appWindow.getMetrics().setBounceSeconds(filters.getBounceVisit());
        currentMetric = Utility.getMetric(filters.getMetric());
        metric = RequestData(currentMetric,startDate,endDate,mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters),granularity);
        filterHbox.getChildren().remove(lineChart);
        lineChart = new GraphingComponent(filters.getMetric(),metric);
        lineChart.setPrefWidth(appWindow.getWidth()*2/3);
        lineChart.setPrefHeight(appWindow.getHeight());
        filterHbox.getChildren().add(lineChart);
    }







    }







