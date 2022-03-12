package uk.comp2211.group13.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppPane;
import uk.comp2211.group13.ui.AppWindow;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HistogramScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(WelcomeScene.class);

  private StackPane graphingPane;
  private HashMap<Date, Float> clickCosts;

  /**
   * Creates a new scene.
   *
   * @param appWindow the app window that displays the scene
   */
  public HistogramScene(AppWindow appWindow) {
    super(appWindow);

    clickCosts = appWindow.getMetrics().request(
        Metric.ClickCost,
        appWindow.getData().getMinDate(),
        appWindow.getData().getMaxDate(),
        new HashMap<>(),
        Granularity.Day
    );
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

    //Building the root pane
    root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

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
    HBox hBox = new HBox();
    hBox.setPadding(new Insets(10, 10, 10, 10));
    vbox.getChildren().add(hBox);

    Button valuesButton = new Button("Metrics Values");
    hBox.getChildren().add(valuesButton);
    valuesButton.setOnMouseClicked(this::changeValuesScene);

    Button chartsButton = new Button("Metrics Charts");
    chartsButton.setStyle("-fx-background-color: #01ffff");
    hBox.getChildren().add(chartsButton);

    HBox hBoxCharts = new HBox();
    hBox.setPadding(new Insets(10, 10, 10, 10));
    vbox.getChildren().add(hBoxCharts);

    Button graphingButton = new Button("Graph");
    hBoxCharts.getChildren().add(graphingButton);
    graphingButton.setOnMouseClicked(this::changeGraphingScene);

    Button histogramButton = new Button("Histogram");
    histogramButton.setStyle("-fx-background-color: #01ffff");
    hBoxCharts.getChildren().add(histogramButton);

    //Building the histogram for the ClickCosts
    histogram(vbox);
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
   * Method to build the ClickCost histogram
   *
   * @param vertBox - The VBox which will contain the Histogram
   */
  public void histogram(VBox vertBox) {
    //Setting up the x and y axes and labelling them accordingly
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    xAxis.setLabel("Dates");
    yAxis.setLabel("Click Costs (£)");

    //Building the bar chart widget
    BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
    barChart.setTitle("Histogram to show Click Costs Distribution");

    //Setting the chart gaps to zero, as this is how histograms must be presented
    barChart.setCategoryGap(0);
    barChart.setBarGap(0);

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
    barChart.getData().add(dataValues);
    dataValues.setName("Click Costs");
    vertBox.getChildren().add(barChart);
  }

  private void changeValuesScene(MouseEvent mouseEvent) {
    appWindow.valuesScreen();
  }

  private void changeGraphingScene(MouseEvent mouseEvent) {
    appWindow.graphingScreen();
  }


}
