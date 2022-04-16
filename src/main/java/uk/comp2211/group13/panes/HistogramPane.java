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


    public HistogramPane(AppWindow appWindow) {
        super(appWindow);
        filters = new FilterComponent("Histogram");
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
        metric = RequestData(Metric.ClickCost,startDate,endDate,mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters),granularity);
        filterHbox.getChildren().remove(histogram);
        filterHbox.getChildren().remove(histogram);
        histogram = new HistogramComponent(metric);
        histogram.setPrefWidth(appWindow.getWidth()*2/3);
        histogram.setPrefHeight(appWindow.getHeight());
        filterHbox.getChildren().add(histogram);
    }










}
