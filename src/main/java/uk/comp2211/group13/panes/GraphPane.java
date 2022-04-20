package uk.comp2211.group13.panes;

import uk.comp2211.group13.Utility;
import uk.comp2211.group13.component.FilterComponent;
import uk.comp2211.group13.component.GraphingComponent;
import uk.comp2211.group13.ui.AppWindow;

public class GraphPane extends BasePane {

    private GraphingComponent lineChart;

    /**
     * Creates the graphing pane
     *
     * @param appWindow - The app window that displays the scene
     */
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
        buildComponents();
    }

    /**
     * Updates settings and builds linechart
     */
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







