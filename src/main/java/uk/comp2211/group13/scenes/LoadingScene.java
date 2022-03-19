package uk.comp2211.group13.scenes;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

        VBox loadingVBox = new VBox();
        loadingVBox.setSpacing(10);
        loadingVBox.setPadding(new Insets(10, 10, 10, 10));
        loadingVBox.setAlignment(Pos.CENTER);
        mainPane.setCenter(loadingVBox);

        Text appTitle = new Text("Loading, Checking and Knitting...");
        loadingVBox.getChildren().add(appTitle);

        ImageView loadingImage = new ImageView(new Image(getClass().getResource("/granny.gif").toExternalForm()));

        loadingImage.setFitWidth(600);
        loadingImage.setPreserveRatio(true);
        loadingVBox.getChildren().add(loadingImage);

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
