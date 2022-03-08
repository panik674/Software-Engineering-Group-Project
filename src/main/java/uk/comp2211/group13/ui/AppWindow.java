package uk.comp2211.group13.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.App;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.scenes.BaseScene;
import uk.comp2211.group13.scenes.GraphingScene;
import uk.comp2211.group13.scenes.HistogramScene;
import uk.comp2211.group13.scenes.ValuesScene;
import uk.comp2211.group13.scenes.WelcomeScene;

// This code has been inspired by code from COMP 1206's TetrECS

/**
 * The AppWindow is the single window for the app where everything takes place. To move between screens in the program,
 * we simply change the scene.
 * <p>
 * The AppWindow has methods to launch each of the different parts of the app by switching scenes.
 */

public class AppWindow {

  private static final Logger logger = LogManager.getLogger(AppWindow.class);

  /**
   * Stores the width and height
   */
  private final int width;
  private final int height;

  /**
   * Stores stage.
   */
  private final Stage stage;

  /**
   * Stores the currently displayed scene class.
   */
  private BaseScene currentScene;

  /**
   * Stores the scene created by the currently displayed scene class.
   */
  private Scene scene;

  /**
   * Stores the data object used to ingest data and reply to data requests
   */
  private final Data data;

  /**
   * Stores the metrics object used to calculate metrics and reply to request.
   */
  private final Metrics metrics;

  /**
   * Creates a new AppWindow using the given stage using the specified width and height.
   *
   * @param stage  stage
   * @param width  window width
   * @param height window height
   */
  public AppWindow(Stage stage, int width, int height) {
    this.stage = stage;
    this.width = width;
    this.height = height;

    // Setup Stage
    setupStage();

    // Setup default scene
    setupDefaultScene();

    // Create Data and Metric objects
    this.data = new Data();
    this.metrics = new Metrics(this.data);

    logger.info("Initialised AppWindow loading start screen");

    // Go to welcome screen
    welcomeScreen();
  }

  /**
   * Display start screen
   */
  public void welcomeScreen() {
    loadScene(new WelcomeScene(this));
  }

  /**
   * Display values screen
   */
  public void valuesScreen() {
    loadScene(new ValuesScene(this));
  }

  /**
   * Display graphs screen
   */
  public void graphingScreen() {
    loadScene(new GraphingScene(this));
  }

  /**
   * Display histogram screen
   */
  public void histogramScreen() {
    loadScene(new HistogramScene(this));
  }

  /**
   * This is used to set the default settings for the stage. (ie, title and dimensions)
   */
  private void setupStage() {
    stage.setTitle("Think of witty name");  // TODO: Think of witty name
    stage.setMinWidth(width);
    stage.setMinHeight(height);
    stage.setOnCloseRequest(ev -> App.getInstance().shutdown());
  }

  /**
   * Used to set up the default scene (empty black scene)
   */
  public void setupDefaultScene() {
    this.scene = new Scene(new Pane(), width, height, Color.BLACK);
    stage.setScene(this.scene);
  }

  public void loadScene(BaseScene newScene) {
    newScene.build();
    currentScene = newScene;
    scene = newScene.setScene();
    stage.setScene(scene);

    // Initialise the scene when ready
    Platform.runLater(() -> currentScene.initialise());
  }

  /**
   * Exit the programme
   */
  public void exit() {
    App.getInstance().shutdown();
  }

  /**
   * Get the current scene being displayed.
   *
   * @return scene
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * Get the app stage.
   *
   * @return stage
   */
  public Stage getStage() {
    return stage;
  }

  /**
   * Get the width of the AppWindow.
   *
   * @return width
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Get the height of the AppWindow.
   *
   * @return height
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Get the data.
   *
   * @return data
   */
  public Data getData() {
    return data;
  }

  /**
   * Get the metrics.
   *
   * @return metrics
   */
  public Metrics getMetrics() {
    return metrics;
  }
}
