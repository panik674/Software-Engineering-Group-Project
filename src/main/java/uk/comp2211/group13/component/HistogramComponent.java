package uk.comp2211.group13.component;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class HistogramComponent extends StackPane {

    private BarChart histogram;
    private HashMap<Date, Float> clickCosts;

    public HistogramComponent(HashMap<Date, Float> clickCosts){
        this.clickCosts = clickCosts;
        build();
    }

    public void build(){
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
        getChildren().add(histogram);
    }
}
