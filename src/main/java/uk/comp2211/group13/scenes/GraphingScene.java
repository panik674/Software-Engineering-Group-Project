package uk.comp2211.group13.scenes;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;
import uk.comp2211.group13.ui.AppPane;

import java.util.*;

public class GraphingScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(WelcomeScene.class);

    private StackPane graphingPane;
    private LineChart lineChart;
    private HashMap<Date, Float> metric;


    /**
     * Creates a new scene.
     *
     * @param appWindow the app window that displays the scene
     */
    public GraphingScene(AppWindow appWindow) {
        super(appWindow);
    }

    /**
     * Initialise the scene and start the game
     */
    @Override
    public void initialise() {
        logger.info("Initialising Welcome Scene");
        events();

    }

    /**
     * Build the Graphing window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        //Building the root pane
        root = new AppPane(appWindow.getWidth(),appWindow.getHeight());

        //Building the stackPane to hold a BorderPane
        graphingPane = new StackPane();
        graphingPane.setMaxWidth(appWindow.getWidth());
        graphingPane.setMaxHeight(appWindow.getHeight());
        root.getChildren().add(graphingPane);

        //Building the BorderPane to hold a VBox
        BorderPane mainPane = new BorderPane();
        graphingPane.getChildren().add(mainPane);

        //Building the VBox which will contain the main UI elements
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(30);
        mainPane.setTop(vbox);

        //Creating a Text widget for the graphing scene's title
        Text appTitle = new Text("Welcome to 'Witty Name' App");
        vbox.getChildren().add(appTitle);
        //Text appTitle = new Text("Welcome to 'Witty Name' App");
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().add(hBox);

        Button valuesButton = new Button("Metrics Values");
        hBox.getChildren().add(valuesButton);
        valuesButton.setOnMouseClicked(this::changeScene);

        Button chartsButton = new Button("Metrics Charts");
        chartsButton.setStyle("-fx-background-color: #01ffff");
        hBox.getChildren().add(chartsButton);

        HBox hBoxCharts = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().add(hBoxCharts);

        Button graphingButton = new Button("Graph");
        graphingButton.setStyle("-fx-background-color: #01ffff");
        hBoxCharts.getChildren().add(graphingButton);

        Button histogramButton = new Button("Histogram");
        hBoxCharts.getChildren().add(histogramButton);
        histogramButton.setOnMouseClicked(this::changeChart);


        //Creating a ComboBox widget which will allow the user to choose which metric graph to display
        String [] metrics = {"Number of Clicks", "Number of Impressions", "Number of Uniques", "Number of Bounce Pages", "Number of Bounce Visits", "Number of Conversions", "Total Costs", "CTR", "CPA", "CPC", "CPM", "Bounce Visit Rate", "Bounce Page Rate"};
        ComboBox<String> metricBox = new ComboBox<>(FXCollections.observableArrayList(metrics));
        metricBox.setValue(metrics [0]);
        vbox.getChildren().add(metricBox);

        //Creating a ComboBox widget which will allow the user to apply filters to the metric graphs in a future prototype
        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.setValue("Choose a Filter");
        vbox.getChildren().add(filterBox);

        //Setting the default metric graph to the "Clicks" metric. The data is requested and is then parsed to the metricGraph method
        metric = appWindow.getMetrics().request(Metric.Clicks, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
        metricGraph(vbox, (String) metricBox.getValue(), metric);

        //Adding an action the metric ComboBox which removes the current graph from the VBox and adds a new one in its place with the selected metric's data requested and plotted
        metricBox.setOnAction(e -> {
            vbox.getChildren().remove(lineChart);
            if ("Number of Clicks".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.Clicks, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else if ("Number of Impressions".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.Impressions, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else if ("Number of Uniques".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.Unique, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else if ("Number of Bounce Pages".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.BouncePage, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else if ("Number of Bounce Visits".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.BounceVisit, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else if ("Number of Conversions".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.Conversions, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else if ("Total Costs".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.TotalCost, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else if ("CTR".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.CTR, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else if ("CPA".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.CPA, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else if ("CPC".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.CPC, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else if ("CPM".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.CPM, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else if ("Bounce Visit Rate".equals(metricBox.getValue())) {
                metric = appWindow.getMetrics().request(Metric.BounceRateVisit, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }
            else {
                metric = appWindow.getMetrics().request(Metric.BounceRatePage, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
                metricGraph(vbox, (String) metricBox.getValue(), metric);
            }

        });

    }

    /**
     * Method to bind exit action to the esc key
     */
    @Override
    public void events() {
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            appWindow.welcomeScreen();
        });
    }

    /**
     * Method to plot a graph for a given metric
     * @param vertBox - VBox in which the graph is placed in
     * @param metricLabel - Label for the y-axis
     * @param metricToPlot - Metric data
     */
    public void metricGraph(VBox vertBox, String metricLabel, HashMap<Date, Float> metricToPlot){
        //Setting up the x and y axes and labelling them accordingly
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(metricLabel);

        //Building the line chart widget
        lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Graph to show " + metricLabel);
        vertBox.getChildren().add(lineChart);

        //A Chart series is created here which will hold the data values to be plotted
        XYChart.Series dataValues = new XYChart.Series();
        dataValues.setName("Will be a filter in future");

        //Here a list is created to store the Dates corresponding to the metric value which is then sorted into chronological order
        List<Date> dates = new ArrayList<Date>(metricToPlot.keySet());
        Collections.sort(dates);

        //Iterating through the list of dates and adding the date along with its corresponding metric value to the chart series and then adding the series to the graph
        for (Date i : dates) {
            dataValues.getData().add(new XYChart.Data(i.toString(),metricToPlot.get(i)));
        }
        lineChart.getData().add(dataValues);

    }

    private void changeScene (MouseEvent mouseEvent) {
        appWindow.valuesScreen();
    }

    private void changeChart (MouseEvent mouseEvent) {
        appWindow.histogramScreen();
    }

}
