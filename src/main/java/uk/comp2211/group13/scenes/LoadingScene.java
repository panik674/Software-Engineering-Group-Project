package uk.comp2211.group13.scenes;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.threading.FileThreading;
import uk.comp2211.group13.ui.AppPane;
import uk.comp2211.group13.ui.AppWindow;

public class LoadingScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(LoadingScene.class);

    private StackPane loadingPane;

    private FileThreading fileThreading;

    /**
     * Creates a new scene.
     *
     * @param appWindow the app window that displays the scene
     */
    public LoadingScene(AppWindow appWindow, FileThreading fileThreading) {
        super(appWindow);
        this.fileThreading = fileThreading;
    }

    @Override
    public void initialise() {
        logger.info("Initialising Loading Scene");
        events();
    }

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

        loadingPane = new StackPane();
        loadingPane.setMaxWidth(appWindow.getWidth());
        loadingPane.setMaxHeight(appWindow.getHeight());

        root.getChildren().add(loadingPane);

        var mainPane = new BorderPane();
        loadingPane.getChildren().add(mainPane);

        Text appTitle = new Text("Loading...");
        mainPane.setCenter(appTitle);

        fileThreading.setDisplayValuesListener(this::displayValues);
        fileThreading.setErrorListener(this::errorDisplay);
    }

    @Override
    public void events() {
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            appWindow.welcomeScreen();
            fileThreading.stop();
        });
    }

    private void displayValues() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                appWindow.valuesScreen();
            }
        });

    }

    private void errorDisplay(String error) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                appWindow.welcomeScreen(error);
            }
        });
    }
}
