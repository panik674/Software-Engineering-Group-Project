package uk.comp2211.group13.scenes;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.log.Impression;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.ui.AppWindow;
import uk.comp2211.group13.ui.AppPane;
import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.enums.Metric;

import java.io.File;

public class HistogramScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(WelcomeScene.class);

    private StackPane graphingPane;
    private LineChart lineChart;
    /**
     * Creates a new scene.
     *
     * @param appWindow the app window that displays the scene
     */
    public HistogramScene(AppWindow appWindow) {
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

        histogram(vbox);

        System.out.println(appWindow.getMetrics().request(Metric.Impressions, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day));
    }

    @Override
    public void events() {
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            appWindow.exit();
        });
    }

    public void histogram(VBox vertBox){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Dates");
        yAxis.setLabel("Click Costs");

        BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setTitle("Histogram to show Click Costs Distribution");
        barChart.setCategoryGap(0);
        barChart.setBarGap(0);
        XYChart.Series dataValues = new XYChart.Series();
        barChart.getData().add(dataValues);
        vertBox.getChildren().add(barChart);
    }



}
