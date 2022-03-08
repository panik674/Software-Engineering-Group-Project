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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HistogramScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(WelcomeScene.class);

    private StackPane graphingPane;
    private HashMap <Date,Float>  ClickCosts = appWindow.getMetrics().request(Metric.ClickCost, "2015-01-01 12:00:00", "2015-01-14 12:00:00" , Granularity.Day);
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
        hBoxCharts.getChildren().add(graphingButton);
        graphingButton.setOnMouseClicked(this::changeChart);

        Button histogramButton = new Button("Histogram");
        histogramButton.setStyle("-fx-background-color: #01ffff");
        hBoxCharts.getChildren().add(histogramButton);

        histogram(vbox);


        System.out.println(ClickCosts);
    }

    @Override
    public void events() {
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            appWindow.welcomeScreen();
        });
    }

    public void histogram(VBox vertBox){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Dates");
        yAxis.setLabel("Click Costs (£)");

        BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setTitle("Histogram to show Click Costs Distribution");
        barChart.setCategoryGap(0);
        barChart.setBarGap(0);
        XYChart.Series dataValues = new XYChart.Series();
        for (Map.Entry<Date, Float> entry : ClickCosts.entrySet()) {
            dataValues.getData().add(new XYChart.Data(entry.getKey().toString(),entry.getValue()));
        }
        barChart.getData().add(dataValues);
        dataValues.setName("Click Costs");
        vertBox.getChildren().add(barChart);
    }

    private void changeScene (MouseEvent mouseEvent) {
        appWindow.valuesScreen();
    }

    private void changeChart (MouseEvent mouseEvent) {
        appWindow.graphingScreen();
    }


}
