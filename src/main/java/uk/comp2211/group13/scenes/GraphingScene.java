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
import uk.comp2211.group13.data.log.Impression;
import uk.comp2211.group13.ui.AppWindow;
import uk.comp2211.group13.ui.AppPane;
import uk.comp2211.group13.data.Metrics;

import java.io.File;

public class GraphingScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(WelcomeScene.class);

    private StackPane graphingPane;
    private LineChart lineChart;
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



        String [] metrics = {"Number of Clicks", "Number of Impressions", "Number of Uniques", "Number of Bounces", "Number of Conversions", "Total Cost", "CTR", "CPA", "CPC", "CPM", "Bounce Rate"};
        ComboBox metricBox = new ComboBox(FXCollections.observableArrayList(metrics));
        metricBox.setValue(metrics [0]);
        vbox.getChildren().add(metricBox);

        ComboBox filterBox = new ComboBox();
        filterBox.setValue("Choose a Filter");
        vbox.getChildren().add(filterBox);

        metricGraph(vbox, (String) metricBox.getValue(), new int [] {1,2,3,4,5});
        metricBox.setOnAction(e -> {
            vbox.getChildren().remove(lineChart);
            metricGraph(vbox, (String) metricBox.getValue(), new int [] {1,2,3,4,5});
        });

    }

    @Override
    public void events() {
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            appWindow.exit();
        });
    }
    public void metricGraph(VBox vertBox, String metricLabel, int [] values){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Time");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(metricLabel);

        lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Graph to show " + metricLabel);
        vertBox.getChildren().add(lineChart);

        XYChart.Series dataValues = new XYChart.Series();
        dataValues.setName("Will be a filter in future");


        for (int i : values){
            dataValues.getData().add(new XYChart.Data("*data*", i));
        }
        lineChart.getData().add(dataValues);

    }



}
