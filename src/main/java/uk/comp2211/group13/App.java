package uk.comp2211.group13;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.ui.AppWindow;

/**
 * JavaFX Application Class
 */
public class App extends Application {

  private static final Logger logger = LogManager.getLogger(App.class);

  /**
   * Base resolution width and height
   */
  private final int width = 1000;
  private final int height = 711;

  /**
   * This stores the instance of the app, so we can return it with getInstance()
   */
  private static App instance;

  /**
   * This stores the currently displayed stage
   */
  private Stage stage;

  public static void main(String[] args) {
    logger.info("Starting App");
    launch();
  }

  /**
   * Called by JFX with the primary stage as a parameter, and opens the AppWindow
   *
   * @param stage the default stage
   */
  @Override
  public void start(Stage stage) {
    instance = this;
    this.stage = stage;

    // Open AppWindow
    logger.info("Creating AppWindow");
    var appWindow = new AppWindow(stage, width, height);

    this.stage.show();
  }

  /**
   * Shutdown / Stop Application
   */
  public void shutdown() {
    logger.info("Shutting down App");
    System.exit(0);
  }

  /**
   * Get the App instance
   *
   * @return the app
   */
  public static App getInstance() {
    return instance;
  }
}
