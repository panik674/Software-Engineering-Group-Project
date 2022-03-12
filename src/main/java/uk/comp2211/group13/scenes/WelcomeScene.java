package uk.comp2211.group13.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import uk.comp2211.group13.enums.Path;
import uk.comp2211.group13.ui.AppPane;
import uk.comp2211.group13.ui.AppWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WelcomeScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(WelcomeScene.class);

  private StackPane welcomePane;
  private VBox vbox;
  private RadioButton radioButton;
  private Text error;

  /**
   * Creates a new scene.
   *
   * @param appWindow the app window that displays the scene
   */
  public WelcomeScene(AppWindow appWindow) {
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

    root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

    welcomePane = new StackPane();
    welcomePane.setMaxWidth(appWindow.getWidth());
    welcomePane.setMaxHeight(appWindow.getHeight());

    root.getChildren().add(welcomePane);

    var mainPane = new BorderPane();
    welcomePane.getChildren().add(mainPane);

    Text appTitle = new Text("Welcome to 'Witty Name' App");
    mainPane.setCenter(appTitle);

    vbox = new VBox();
    mainPane.setBottom(vbox);
    vbox.setAlignment(Pos.CENTER);
    vbox.setPadding(new Insets(10, 10, 10, 10));
    vbox.setSpacing(10);

    Button loadButton = new Button("Load Data");
    vbox.getChildren().add(loadButton);
    loadButton.setOnMouseClicked(this::fileLoader);

    HBox hbox = new HBox();
    vbox.getChildren().add(hbox);
    hbox.setAlignment(Pos.CENTER);
    hbox.setPadding(new Insets(10, 10, 10, 10));
    hbox.setSpacing(10);

    radioButton = new RadioButton();
    hbox.getChildren().add(radioButton);

    Text termsText = new Text("I have read and agreed to the terms and conditions of this app");
    hbox.getChildren().add(termsText);
  }

  @Override
  public void events() {
    scene.setOnKeyPressed((e) -> {
      if (e.getCode() != KeyCode.ESCAPE) return;
      appWindow.exit();
    });
  }

  // Code was inspired by: https://www.geeksforgeeks.org/javafx-filechooser-class/
  public void fileLoader(MouseEvent event) {
    try {
      if (radioButton.isSelected()) {
        ArrayList<String> stringPaths = new ArrayList<>();
        vbox.getChildren().remove(error);
        // create a File chooser
        FileChooser fileChooser = new FileChooser();
        // get the file selected
        List<File> files = fileChooser.showOpenMultipleDialog(appWindow.getStage());

        if (files.size() == 3) {
          for (File file : files) {
            if (file != null) {
              stringPaths.add(file.getAbsolutePath());
            }
          }
          if (appWindow.getData().ingest(stringPaths) == 0) {
            appWindow.valuesScreen();
          } else {
            error = new Text("Please select the correct formats of the file");
            vbox.getChildren().add(error);
          }
        } else {
          error = new Text("Please select exactly three files!");
          vbox.getChildren().add(error);
        }
      }
    } catch (Exception e) {

      System.out.println(e.getMessage());
    }
  }
}
