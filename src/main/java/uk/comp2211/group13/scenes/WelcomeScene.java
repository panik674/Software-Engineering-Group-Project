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
import javafx.stage.DirectoryChooser;
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

    Text appTitle = new Text("Welcome to The Old Biddy App");
    mainPane.setCenter(appTitle);

    vbox = new VBox();
    mainPane.setBottom(vbox);
    vbox.setAlignment(Pos.CENTER);
    vbox.setPadding(new Insets(10, 10, 10, 10));
    vbox.setSpacing(10);

    HBox loadHbox = new HBox();
    vbox.getChildren().add(loadHbox);
    loadHbox.setAlignment(Pos.CENTER);
    loadHbox.setPadding(new Insets(10, 10, 10, 10));
    loadHbox.setSpacing(10);

    Button loadFilesButton = new Button("Load Files");
    loadHbox.getChildren().add(loadFilesButton);
    loadFilesButton.setOnMouseClicked(this::fileLoader);

    Button loadFolderButton = new Button("Load Folder");
    loadHbox.getChildren().add(loadFolderButton);
    loadFolderButton.setOnMouseClicked(this::folderLoader);

    HBox tncHbox = new HBox();
    vbox.getChildren().add(tncHbox);
    tncHbox.setAlignment(Pos.CENTER);
    tncHbox.setPadding(new Insets(10, 10, 10, 10));
    tncHbox.setSpacing(10);

    radioButton = new RadioButton();
    tncHbox.getChildren().add(radioButton);

    Text termsText = new Text("I have read and agreed to the terms and conditions of this app");
    tncHbox.getChildren().add(termsText);

    error = new Text("");
    vbox.getChildren().add(error);
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
        clearError();

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
            displayError("Please select the correct formats of the file");
          }
        } else {
          displayError("Please select exactly three files!");
        }
      } else {
        displayError("Please accept terms and conditions prior to trying to load files.");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void folderLoader(MouseEvent event) {
    try {
      if (radioButton.isSelected()) {
        String folderPath;
        clearError();

        // create a File chooser
        DirectoryChooser directoryChooser = new DirectoryChooser();
        // get the file selected
        File folder = directoryChooser.showDialog(appWindow.getStage());
        folderPath = folder.getAbsolutePath();

        if (appWindow.getData().ingest(folderPath) == 0) {
          appWindow.valuesScreen();
        } else {
          displayError("The loaded folder is invalid");
        }
      } else {
        displayError("Please accept terms and conditions prior to trying to load folders.");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void displayError(String message) {
    vbox.getChildren().remove(error);
    error = new Text(message);
    vbox.getChildren().add(error);
  }
  private void clearError() {
    vbox.getChildren().remove(error);
  }
}
