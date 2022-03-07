package uk.comp2211.group13.scenes;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;
import uk.comp2211.group13.ui.AppPane;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GraphingScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(WelcomeScene.class);

    private StackPane graphingPane;
    private LineChart lineChart;

    private HashMap<Date, Float> Clicks = appWindow.getMetrics().request(Metric.Clicks, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> Impressions = appWindow.getMetrics().request(Metric.Impressions, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> Uniques = appWindow.getMetrics().request(Metric.Unique, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> BouncePage = appWindow.getMetrics().request(Metric.BouncePage, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> BounceVisit = appWindow.getMetrics().request(Metric.BounceVisit, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> Conversions = appWindow.getMetrics().request(Metric.Conversions, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> TotalCost = appWindow.getMetrics().request(Metric.TotalCost, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> CTR = appWindow.getMetrics().request(Metric.CTR, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> CPA = appWindow.getMetrics().request(Metric.CPA, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> CPC = appWindow.getMetrics().request(Metric.CPC, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> CPM = appWindow.getMetrics().request(Metric.CPM, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> BounceRateVisit = appWindow.getMetrics().request(Metric.BounceRateVisit, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
    private HashMap<Date, Float> BounceRatePage = appWindow.getMetrics().request(Metric.BounceRatePage, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
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
     * Build the Welcome window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(),appWindow.getHeight());

        graphingPane = new StackPane();
        graphingPane.setMaxWidth(appWindow.getWidth());
        graphingPane.setMaxHeight(appWindow.getHeight());

        root.getChildren().add(graphingPane);

        var mainPane = new BorderPane();

        graphingPane.getChildren().add(mainPane);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(30);
        mainPane.setTop(vbox);

        Text appTitle = new Text("Welcome to 'Witty Name' App");
        vbox.getChildren().add(appTitle);



        String [] metrics = {"Number of Clicks", "Number of Impressions", "Number of Uniques", "Number of Bounce Pages", "Number of Bounce Visits", "Number of Conversions", "Total Costs", "CTR", "CPA", "CPC", "CPM", "Bounce Visit Rate", "Bounce Page Rate"};
        ComboBox<String> metricBox = new ComboBox<>(FXCollections.observableArrayList(metrics));
        metricBox.setValue(metrics [0]);
        vbox.getChildren().add(metricBox);

        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.setValue("Choose a Filter");
        vbox.getChildren().add(filterBox);

        metricGraph(vbox, metricBox.getValue(), Clicks);
        metricBox.setOnAction(e -> {
            vbox.getChildren().remove(lineChart);
            if ("Number of Clicks".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), Clicks);
            }
            else if ("Number of Impressions".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), Impressions);
            }
            else if ("Number of Uniques".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), Uniques);
            }
            else if ("Number of Bounce Pages".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), BouncePage);
            }
            else if ("Number of Bounce Visits".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), BounceVisit);
            }
            else if ("Number of Conversions".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), Conversions);
            }
            else if ("Total Costs".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), TotalCost);
            }
            else if ("CTR".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), CTR);
            }
            else if ("CPA".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), CPA);
            }
            else if ("CPC".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), CPC);
            }
            else if ("CPM".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), CPM);
            }
            else if ("Bounce Visit Rate".equals(metricBox.getValue())) {
                metricGraph(vbox, metricBox.getValue(), BounceRateVisit);
            }
            else {
                metricGraph(vbox, metricBox.getValue(), BounceRatePage);
            }

        });

    }

    @Override
    public void events() {
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            appWindow.exit();
        });
    }
    public void metricGraph(VBox vertBox, String metricLabel, HashMap<Date, Float> metric){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(metricLabel);

        lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Graph to show " + metricLabel);
        vertBox.getChildren().add(lineChart);

        XYChart.Series dataValues = new XYChart.Series();
        dataValues.setName("Will be a filter in future");
        for (Map.Entry<Date, Float> entry : metric.entrySet()) {
            dataValues.getData().add(new XYChart.Data(entry.getKey().toString(),entry.getValue()));
        }
        lineChart.getData().add(dataValues);

    }



}
