package uk.comp2211.group13.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.component.ValueBlock;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppPane;
import uk.comp2211.group13.ui.AppWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ValuesScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(ValuesScene.class);

  private StackPane valuesPane;
  private VBox vBox;

  private ValueBlock nOI_Block;
  private ValueBlock nOC_Block;
  private ValueBlock nOU_Block;
  private ValueBlock nOB_Block;

  private Button pAndVToggle_1;

  private ValueBlock nOCon_Block;
  private ValueBlock tC_Block;
  private ValueBlock cTR_Block;
  private ValueBlock cPA_Block;

  // Third row
  private ValueBlock cPC_Block; //TODO: Add binding
  private ValueBlock cPM_Block;
  private ValueBlock bR_Block;

  private Button pAndVToggle_2;

  private HBox row1;
  private HBox row2;
  private HBox row3;

  private HBox filterHbox;

  private Float value;

  private Boolean booleanForNum;
  private Boolean booleanForRate;

  private HashMap<Filter, String[]> genderFilters = new HashMap<>();
  private HashMap<Filter, String[]> ageFilters = new HashMap<>();
  private HashMap<Filter, String[]> incomeFilters = new HashMap<>();
  private HashMap<Filter, String[]> contextFilters = new HashMap<>();

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
  public ValuesScene(AppWindow appWindow) {
    super(appWindow);
  }

  /**
   * Initialise the scene and start the game
   */
  @Override
  public void initialise() {
    logger.info("Initialising Value Scene");
    events();
  }

  /**
   * Build the Values window
   */
  @Override
  public void build() {
    logger.info("Building " + this.getClass().getName());

    root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

    valuesPane = new StackPane();
    valuesPane.setMaxWidth(appWindow.getWidth());
    valuesPane.setMaxHeight(appWindow.getHeight());

    root.getChildren().add(valuesPane);

    var mainPane = new BorderPane();
    valuesPane.getChildren().add(mainPane);


    HBox hBox = new HBox();
    hBox.setPadding(new Insets(10, 10, 10, 10));
    mainPane.setTop(hBox);

    Button valuesButton = new Button("Metrics Values");
    valuesButton.setStyle("-fx-background-color: #01ffff");
    hBox.getChildren().add(valuesButton);

    Button chartsButton = new Button("Metrics Charts");
    hBox.getChildren().add(chartsButton);
    chartsButton.setOnMouseClicked(this::changeScene);


    vBox = new VBox();
    mainPane.setCenter(vBox);
    vBox.setAlignment(Pos.CENTER);
    vBox.setPadding(new Insets(10, 10, 10, 10));
    vBox.setSpacing(10);

    buildBlocks();
  }

  @Override
  public void events() {
    scene.setOnKeyPressed((e) -> {
      if (e.getCode() != KeyCode.ESCAPE) return;
      appWindow.welcomeScreen();
    });
  }

  private void buildBlocks() {
    // Initialising the boolean objects
    booleanForNum = true;
    booleanForRate = true;
    // First row
    nOI_Block = new ValueBlock("Number of impressions", requestValue(Metric.Impressions)); //TODO: Add binding
    nOC_Block = new ValueBlock("Number of Clicks", requestValue(Metric.Clicks));
    nOU_Block = new ValueBlock("Number of Uniques", requestValue(Metric.Unique));
    nOB_Block = new ValueBlock("Number of Bounces", requestValue(Metric.BouncePage));

    pAndVToggle_1 = new Button("Visits");
    nOB_Block.getVBox().getChildren().add(pAndVToggle_1);
    pAndVToggle_1.setOnMouseClicked(this::toggleForNum);
    pAndVToggle_1.setMaxWidth(50);

    row1 = new HBox(nOI_Block, nOC_Block, nOU_Block, nOB_Block);
    hBoxSetter(row1);

    // Second row
    nOCon_Block = new ValueBlock("Rate of Conversions", requestValue(Metric.Conversions)); //TODO: Add binding
    tC_Block = new ValueBlock("Total Cost", requestValue(Metric.TotalCost));
    cTR_Block = new ValueBlock("CTR", requestValue(Metric.CTR));
    cPA_Block = new ValueBlock("CPA", requestValue(Metric.CPA));
    row2 = new HBox(nOCon_Block, tC_Block, cTR_Block, cPA_Block);
    hBoxSetter(row2);

    // Third row
    cPC_Block = new ValueBlock("CPC", requestValue(Metric.CPC)); //TODO: Add binding
    cPM_Block = new ValueBlock("CPM", requestValue(Metric.CPM));
    bR_Block = new ValueBlock("Bounce Rate", requestValue(Metric.BounceRatePage));

    pAndVToggle_2 = new Button("Visits");
    bR_Block.getVBox().getChildren().add(pAndVToggle_2);
    pAndVToggle_2.setOnMouseClicked(this::toggleForRate);
    pAndVToggle_2.setMaxWidth(50);

    row3 = new HBox(cPC_Block, cPM_Block, bR_Block);
    hBoxSetter(row3);

    filterHbox = new HBox();

    filterMenu(new String[]{"Male", "Female"},"Gender");
    filterHbox.getChildren().add(regionBuild());
    filterMenu(new String[]{"<25", "25-34", "35-44", "45-54", ">54"},"Age");
    filterHbox.getChildren().add(regionBuild());
    filterMenu(new String[]{"Low", "Medium", "High"},"Income");
    filterHbox.getChildren().add(regionBuild());
    filterMenu(new String[]{"News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel"},"Context");
    vBox.getChildren().add(filterHbox);

    /*ChoiceBox<String> filtersChoices = new ChoiceBox<>();
    filtersChoices.setValue("Select a filter");
    filtersChoices.setMaxWidth(100);

    filtersChoices.getItems().add("Filter1");
    filtersChoices.getItems().add("Filter2");
   // filtersChoices.get

    vBox.getChildren().add(filtersChoices);*/
  }

  private void hBoxSetter(HBox row) {
    vBox.getChildren().add(row);
    row.setAlignment(Pos.CENTER);
    row.setPadding(new Insets(10, 10, 10, 10));
    row.setSpacing(47);
  }

  private String requestValue(Metric metric) {
    try {

      // For know we are assuming that the data will be given will just be on this range. To be improved on Sprint 2
      value = (
          appWindow.getMetrics().request(
              metric,
              appWindow.getData().getMinDate(),
              appWindow.getData().getMaxDate(),
              mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters),
              Granularity.None
          )
      ).get(
          appWindow.getData().getMinDate()
      );
    } catch (Exception e) {
      System.out.println(e);
    }

    System.out.println(value);
    return value.toString();

  }

  private void toggleForNum(MouseEvent mouseEvent) {
    booleanForNum = !booleanForNum;

    if (!booleanForNum) {
      pAndVToggle_1.setText("Pages");
      nOB_Block.setValue(requestValue(Metric.BounceVisit));
    } else {
      pAndVToggle_1.setText("Visits");
      nOB_Block.setValue(requestValue(Metric.BouncePage));
    }

  }

  private void toggleForRate(MouseEvent mouseEvent) {
    booleanForRate = !booleanForRate;

    if (!booleanForRate) {
      pAndVToggle_2.setText("Pages");
      bR_Block.setValue(requestValue(Metric.BounceRateVisit));
    } else {
      pAndVToggle_2.setText("Visits");
      bR_Block.setValue(requestValue((Metric.BounceRatePage)));
    }
  }

  private void reRequestFilteredValues () {
    nOI_Block.setValue(requestValue(Metric.Impressions));
    nOC_Block.setValue(requestValue(Metric.Clicks));
    nOU_Block.setValue(requestValue(Metric.Unique));
    if (pAndVToggle_1.getText().equals("Pages")) {
      nOB_Block.setValue(requestValue(Metric.BounceVisit));
    } else {
      nOB_Block.setValue(requestValue(Metric.BouncePage));
    }

    // Second row
    nOCon_Block.setValue(requestValue(Metric.Conversions));
    tC_Block.setValue(requestValue(Metric.TotalCost));
    cTR_Block.setValue(requestValue(Metric.CTR));
    cPA_Block.setValue(requestValue(Metric.CPA));

    // Third row
    cPC_Block.setValue(requestValue(Metric.CPC));
    cPM_Block.setValue(requestValue(Metric.CPM));
    if (pAndVToggle_2.getText().equals("Pages")) {
      bR_Block.setValue(requestValue(Metric.BounceRateVisit));
    } else {
      bR_Block.setValue(requestValue((Metric.BounceRatePage)));
    }
  }

  /**
   * Method to remove filters from the 'filters' HashMap
   *
   * @param filtType - The type of the filter being removed
   * @param filt - The filter which is being removed
   */
  public void removeFilters(Filter filtType, String filt) {
    //Checking the filter type, removing the filter from the appropriate filter String[] list and adding it to the filters HashMap
    //Also removing the HashMap entries with empty lists, so all metric values are displayed without any filters applied
    if (filtType == Filter.Gender) {
      List<String> list = new ArrayList<String>(Arrays.asList(GenderList));
      list.remove(filt);
      GenderList = list.toArray(new String[0]);
      genderFilters.put(Filter.Gender, GenderList);
      for (Filter i : genderFilters.keySet()){
        if (genderFilters.get(i).length == 0){
          genderFilters.remove(i);
        }
      }
    } else if (filtType == Filter.Age) {
      List<String> list = new ArrayList<String>(Arrays.asList(AgeList));
      list.remove(filt);
      AgeList = list.toArray(new String[0]);
      ageFilters.put(Filter.Age, AgeList);
      for (Filter i : ageFilters.keySet()){
        if (ageFilters.get(i).length == 0){
          ageFilters.remove(i);
        }
      }
    } else if (filtType == Filter.Income) {
      List<String> list = new ArrayList<String>(Arrays.asList(IncomeList));
      list.remove(filt);
      IncomeList = list.toArray(new String[0]);
      incomeFilters.put(Filter.Income, IncomeList);
      for (Filter i : incomeFilters.keySet()){
        if (incomeFilters.get(i).length == 0){
          incomeFilters.remove(i);
        }
      }
    } else {
      List<String> list = new ArrayList<String>(Arrays.asList(ContextList));
      list.remove(filt);
      ContextList = list.toArray(new String[0]);
      contextFilters.put(Filter.Context, ContextList);
      for (Filter i : contextFilters.keySet()){
        if (contextFilters.get(i).length == 0){
          contextFilters.remove(i);
        }
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
      genderFilters.put(Filter.Gender, GenderList);
    } else if (filtType == Filter.Age) {
      List<String> list = new ArrayList<String>(Arrays.asList(AgeList));
      list.add(filt);
      AgeList = list.toArray(new String[0]);
      ageFilters.put(Filter.Age, AgeList);
    } else if (filtType == Filter.Income) {
      List<String> list = new ArrayList<String>(Arrays.asList(IncomeList));
      list.add(filt);
      IncomeList = list.toArray(new String[0]);
      incomeFilters.put(Filter.Income, IncomeList);
    } else {
      List<String> list = new ArrayList<String>(Arrays.asList(ContextList));
      list.add(filt);
      ContextList = list.toArray(new String[0]);
      contextFilters.put(Filter.Context, ContextList);
    }
  }

  /**
   * Method to create a filter MenuButton and bind actions to its items
   *
   * @param filterNames - List of filters to be added to the MenuButton
   * @param filterType - Label for the MenuButton
   */
  public void filterMenu(String[] filterNames, String filterType){
    //Creating a MenuButton to allow the user to select filters to apply to the graph
    MenuButton menuButton = new MenuButton(filterType);
    for (String i : filterNames) {
      //Creating different CheckMenuItems for each filter
      CheckMenuItem CMItem = new CheckMenuItem(i);
      //Binding actions to the CheckMenuItems
      CMItem.setOnAction(e -> {
        //Adding filters to the graphs when they are selected
        if (CMItem.isSelected()) {
          addFilters(util.filterType(i),i);
          reRequestFilteredValues();
        }
        //Removing filters from the graphs when they are unselected
        else {
          removeFilters(util.filterType(i),i);
          reRequestFilteredValues();
        }
      });
      menuButton.getItems().add(CMItem);
    }
    filterHbox.getChildren().add(menuButton);
  }

  /**
   * Method to merge the three filter hashmaps to apply them all when plotting the linechart
   *
   * @param genderFilters - The filter hashmap for the gender filters
   * @param ageFilters - The filter hashmap for the age filters
   * @param incomeFilters - The filter hashmap for the income filters
   * @param contextFilters - The filter hashmap for the context filters
   * @return - The merged filter hashmap
   */
  public HashMap<Filter, String[]> mergeFilter (HashMap<Filter, String[]> genderFilters, HashMap<Filter, String[]> ageFilters, HashMap<Filter, String[]> incomeFilters, HashMap<Filter, String[]> contextFilters){
    HashMap<Filter, String[]> combinedFilters = new HashMap<>();
    combinedFilters.putAll(genderFilters);
    combinedFilters.putAll(ageFilters);
    combinedFilters.putAll(incomeFilters);
    combinedFilters.putAll(contextFilters);
    return combinedFilters;
  }

  /**
   *Method to Build and grow regions to provide spacing for the filter HBox
   *
   * @return - The grown region
   */
  public Region regionBuild(){
    Region region = new Region();
    HBox.setHgrow(region,Priority.ALWAYS);
    return region;
  }

  private void changeScene(MouseEvent mouseEvent) {
    appWindow.graphingScreen();
  }
}
