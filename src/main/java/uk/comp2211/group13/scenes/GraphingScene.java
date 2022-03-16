package uk.comp2211.group13.scenes;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;
import uk.comp2211.group13.ui.AppPane;
import java.security.KeyStore;
import java.util.*;

public class GraphingScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(WelcomeScene.class);

  private StackPane graphingPane;
  private LineChart lineChart;
  private HashMap<Date, Float> metric;
  private HashMap<Filter, String[]> filters = new HashMap<>();
  private Metric currentMetric;
  private String[] GenderList = {};
  private String[] AgeList = {};
  private String[] IncomeList = {};
  private String[] ContextList = {};
  private Utility util = new Utility();


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
    graphingButton.setStyle("-fx-background-color: #01ffff");
    hBoxCharts.getChildren().add(graphingButton);

    Button histogramButton = new Button("Histogram");
    hBoxCharts.getChildren().add(histogramButton);
    histogramButton.setOnMouseClicked(this::changeHistogramScene);


    //Creating a ComboBox widget which will allow the user to choose which metric graph to display
    String[] metrics = {"Number of Clicks", "Number of Impressions", "Number of Uniques", "Number of Bounce Pages", "Number of Bounce Visits", "Number of Conversions", "Total Costs", "CTR", "CPA", "CPC", "CPM", "Bounce Visit Rate", "Bounce Page Rate"};
    ComboBox<String> metricBox = new ComboBox<>(FXCollections.observableArrayList(metrics));
    metricBox.setValue(metrics[0]);
    vbox.getChildren().add(metricBox);

    //Creating a MenuButton to allow the user to select filters to apply to the graph
    String[] filterNames = {"Gender: Male", "Gender: Female", "Age: <25", "Age: 25-34", "Age: 35-44", "Age: 45-54", "Age: >54", "Income: Low", "Income: Medium", "Income: High", "Context: News", "Context: Shopping", "Context: Social Media", "Context: Blog", "Context: Hobbies", "Context: Travel"};
    MenuButton menuButton = new MenuButton("Filters");
    for (String i : filterNames) {
      //Creating different CheckMenuItems for each filter
      String[] currentFilter = i.split(" ", 2);
      CheckMenuItem CMItem = new CheckMenuItem(i);
      //Binding actions to the CheckMenuItems
      CMItem.setOnAction(e -> {
        //Adding filters to the graphs when they are selected
        if (CMItem.isSelected()) {
          addFilters(util.filterType(currentFilter[1]),currentFilter[1]);
          vbox.getChildren().remove(lineChart);
          metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
          metricGraph(vbox, metricBox.getValue(), metric);

        }
        //Removing filters from the graphs when they are unselected
        else {
          removeFilters(util.filterType(currentFilter[1]),currentFilter[1]);
          vbox.getChildren().remove(lineChart);
          metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
          metricGraph(vbox, metricBox.getValue(), metric);
        }
      });
      menuButton.getItems().add(CMItem);
    }
    vbox.getChildren().add(menuButton);


    //Setting the default metric graph to the "Clicks" metric. The data is requested and is then parsed to the metricGraph method
    currentMetric = Metric.Clicks;
    metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);

    metricGraph(vbox, metricBox.getValue(), metric);

    //Adding an action the metric ComboBox which removes the current graph from the VBox and adds a new one in its place with the selected metric's data requested and plotted
    metricBox.setOnAction(e -> {
      vbox.getChildren().remove(lineChart);
      if ("Number of Clicks".equals(metricBox.getValue())) {
        currentMetric = Metric.Clicks;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);

      } else if ("Number of Impressions".equals(metricBox.getValue())) {
        currentMetric = Metric.Impressions;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);

      } else if ("Number of Uniques".equals(metricBox.getValue())) {
        currentMetric = Metric.Unique;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);

      } else if ("Number of Bounce Pages".equals(metricBox.getValue())) {
        currentMetric = Metric.BouncePage;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);

      } else if ("Number of Bounce Visits".equals(metricBox.getValue())) {
        currentMetric = Metric.BounceVisit;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);

      } else if ("Number of Conversions".equals(metricBox.getValue())) {
        currentMetric = Metric.Conversions;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        ;
        metricGraph(vbox, metricBox.getValue(), metric);

      } else if ("Total Costs".equals(metricBox.getValue())) {
        currentMetric = Metric.TotalCost;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);

      } else if ("CTR".equals(metricBox.getValue())) {
        currentMetric = Metric.CTR;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);

      } else if ("CPA".equals(metricBox.getValue())) {
        currentMetric = Metric.CPA;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);

      } else if ("CPC".equals(metricBox.getValue())) {
        currentMetric = Metric.CPC;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);

      } else if ("CPM".equals(metricBox.getValue())) {
        currentMetric = Metric.CPM;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);

      } else if ("Bounce Visit Rate".equals(metricBox.getValue())) {
        currentMetric = Metric.BounceRateVisit;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);

      } else {
        currentMetric = Metric.BounceRatePage;
        metric = appWindow.getMetrics().request(currentMetric, appWindow.getData().getMinDate(), appWindow.getData().getMaxDate(), filters, Granularity.Day);
        metricGraph(vbox, metricBox.getValue(), metric);
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
   *
   * @param vertBox      - VBox in which the graph is placed in
   * @param metricLabel  - Label for the y-axis
   * @param metricToPlot - Metric data
   */
  public void metricGraph(VBox vertBox, String metricLabel, HashMap<Date, Float> metricToPlot) {
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
      dataValues.getData().add(new XYChart.Data(i.toString(), metricToPlot.get(i)));
    }
    lineChart.getData().add(dataValues);
  }

  private void changeValuesScene(MouseEvent mouseEvent) {
    appWindow.valuesScreen();
  }

  private void changeHistogramScene(MouseEvent mouseEvent) {
    appWindow.histogramScreen();
  }

  /**
   * Method to remove filters from the 'filters' HashMap
   *
   * @param filtType - The type of the filter being removed
   * @param filt - The filter which is being removed
   */
  public void removeFilters(Filter filtType, String filt) {
    //Checking the filter type, removing the filter from the appropriate filter String[] list and adding it to the filters HashMap
    if (filtType == Filter.Gender) {
      List<String> list = new ArrayList<String>(Arrays.asList(GenderList));
      list.remove(filt);
      GenderList = list.toArray(new String[0]);
      filters.put(Filter.Gender, GenderList);
    } else if (filtType == Filter.Age) {
      List<String> list = new ArrayList<String>(Arrays.asList(AgeList));
      list.remove(filt);
      AgeList = list.toArray(new String[0]);
      filters.put(Filter.Age, AgeList);
    } else if (filtType == Filter.Income) {
      List<String> list = new ArrayList<String>(Arrays.asList(IncomeList));
      list.remove(filt);
      IncomeList = list.toArray(new String[0]);
      filters.put(Filter.Income, IncomeList);
    } else {
      List<String> list = new ArrayList<String>(Arrays.asList(ContextList));
      list.remove(filt);
      ContextList = list.toArray(new String[0]);
      filters.put(Filter.Context, ContextList);
    }
    //Removing the HashMap entries with empty lists, so all metric values are displayed without any filters applied
    for (Filter i : filters.keySet()){
      if (filters.get(i).length == 0){
        filters.remove(i);
      }
    }
  }

  /**
   * Method to add filters from the 'filters' HashMap
   *
   * @param filtType - The type of the filter being added
   * @param filt - The filter which is being added
   */
  public void addFilters(Filter filtType, String filt) {
    //Checking the filter type, adding the filter from the appropriate filter String[] list and adding it to the filters HashMap
    if (filtType == Filter.Gender) {
      List<String> list = new ArrayList<String>(Arrays.asList(GenderList));
      list.add(filt);
      GenderList = list.toArray(new String[0]);
      filters.put(Filter.Gender, GenderList);
    } else if (filtType == Filter.Age) {
      List<String> list = new ArrayList<String>(Arrays.asList(AgeList));
      list.add(filt);
      AgeList = list.toArray(new String[0]);
      filters.put(Filter.Age, AgeList);
    } else if (filtType == Filter.Income) {
      List<String> list = new ArrayList<String>(Arrays.asList(IncomeList));
      list.add(filt);
      IncomeList = list.toArray(new String[0]);
      filters.put(Filter.Income, IncomeList);
    } else {
      List<String> list = new ArrayList<String>(Arrays.asList(ContextList));
      list.add(filt);
      ContextList = list.toArray(new String[0]);
      filters.put(Filter.Context, ContextList);
    }
  }
}
