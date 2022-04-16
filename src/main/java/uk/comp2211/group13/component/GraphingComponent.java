package uk.comp2211.group13.component;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uk.comp2211.group13.ui.AppWindow;

import java.util.*;

public class GraphingComponent extends StackPane {
    private String metricLabel;
    private HashMap<Date, Float> metricToPlot;
    private LineChart lineChart;
    private AppWindow appWindow;

    public GraphingComponent(String metricLabel, HashMap<Date, Float> metricToPlot){
        this.metricLabel = metricLabel;
        this.metricToPlot = metricToPlot;
        this.appWindow = appWindow;
        build();
    }

    public void build(){
        //Setting up the x and y axes and labelling them accordingly
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(metricLabel);

        //Building the line chart widget
        lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Graph to show " + metricLabel);

        //A Chart series is created here which will hold the data values to be plotted
        XYChart.Series dataValues = new XYChart.Series();
        dataValues.setName(metricLabel);

        //Here a list is created to store the Dates corresponding to the metric value which is then sorted into chronological order
        List<Date> dates = new ArrayList<Date>(metricToPlot.keySet());
        Collections.sort(dates);

        //Iterating through the list of dates and adding the date along with its corresponding metric value to the chart series and then adding the series to the graph
        for (Date i : dates) {
            dataValues.getData().add(new XYChart.Data(i.toString(), metricToPlot.get(i)));
        }
        lineChart.getData().add(dataValues);

        getChildren().add(lineChart);
    }
}