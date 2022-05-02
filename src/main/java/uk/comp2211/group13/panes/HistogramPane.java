package uk.comp2211.group13.panes;

import uk.comp2211.group13.component.FilterComponent;
import uk.comp2211.group13.component.HistogramComponent;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;

public class HistogramPane extends BasePane {

    private HistogramComponent histogram;

    /**
     * Creates the graphing pane
     *
     * @param appWindow - The app window that displays the scene
     */
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
        buildComponents();
    }

    /**
     * Updates settings and builds histogram
     */
    public void buildGraph(){
        metric = RequestData(Metric.ClickCost,startDate,endDate,mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters),granularity);
        filterHbox.getChildren().remove(histogram);
        histogram = new HistogramComponent(metric);
        histogram.setPrefWidth(appWindow.getWidth()*2/3);
        histogram.setPrefHeight(appWindow.getHeight());
        filterHbox.getChildren().add(histogram);
    }

    /**
     * Polymorphically redefining the resetBounces method to prevent non-existent spinner widgets being referenced
     */
    @Override
    public void resetBounces() {
    }
}
