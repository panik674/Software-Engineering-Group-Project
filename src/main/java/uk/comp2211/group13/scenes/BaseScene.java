package uk.comp2211.group13.scenes;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.ui.AppPane;
import uk.comp2211.group13.ui.AppWindow;

public abstract class BaseScene {
  protected final AppWindow appWindow;

  protected AppPane root;
  protected Scene scene;

  /**
   * Creates a new scene.
   *
   * @param appWindow the app window that displays the scene
   */
  public BaseScene(AppWindow appWindow) {
    this.appWindow = appWindow;
  }

  /**
   * Initialise this scene. Called after creation.
   */
  public abstract void initialise();

  /**
   * Build the layout of the scene.
   */
  public abstract void build();

  /**
   * Create a new JFX scene using the root contained within this scene.
   *
   * @return JavaFX scene
   */
  public Scene setScene() {
    var previous = appWindow.getScene();
    Scene scene = new Scene(root, previous.getWidth(), previous.getHeight(), Color.BLACK);
    // scene.getStylesheets().add(getClass().getResource("/style/game.css").toExternalForm()); TODO: Decide if we want to apply a style sheet
    this.scene = scene;
    return scene;
  }

  /**
   * This method add listeners for the required keyboard and mouse events
   */
  public abstract void events();

  /**
   * Get the JavaFX scene contained inside.
   *
   * @return JavaFX scene
   */
  public Scene getScene() {
    return this.scene;
  }
}
