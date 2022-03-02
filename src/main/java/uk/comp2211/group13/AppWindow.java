package uk.comp2211.group13;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.Metrics;
import uk.comp2211.group13.scenes.BaseScene;

// The code here has been taken from our last year Programming II Coursework
/**
 * The App Pane is a special pane which will scale anything inside it to the screen and maintain the aspect ratio.
 *
 * Drawing will be scaled appropriately.
 *
 * This takes the worry about the layout out and will allow the app to scale to any resolution easily.
 *
 * It uses the width and height given which should match the main window size. This will be the base drawing resolution,
 * but will be scaled up or down as the window is resized.
 *
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
   * @param stage stage
   * @param width window width
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
    this.metrics = new Metrics();

    logger.info("Initialised AppWindow loading start screen");

    // Go to start screen
  }

  /**
   * Display start screen
   */
  public void startScreen() {
    //loadScene(new Scene()); TODO: Create MenuScene
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
   * Get the current scene being displayed.
   *
   * @return scene
   */
  public Scene getScene() {
    return scene;
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
