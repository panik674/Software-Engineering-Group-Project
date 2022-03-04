package uk.comp2211.group13.scenes;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.component.ValueBlock;
import uk.comp2211.group13.component.ValueCanvas;
import uk.comp2211.group13.ui.AppPane;
import uk.comp2211.group13.ui.AppWindow;

public class ValuesScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(ValuesScene.class);

    private StackPane valuesPane;
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
        logger.info("Initialising Welcome Scene");
        events();
    }

    /**
     * Build the Values window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(),appWindow.getHeight());

        valuesPane = new StackPane();
        valuesPane.setMaxWidth(appWindow.getWidth());
        valuesPane.setMaxHeight(appWindow.getHeight());

        root.getChildren().add(valuesPane);

        var mainPane = new BorderPane();
        valuesPane.getChildren().add(mainPane);

        //Text text = new Text("Values goes here");
        ValueBlock valueBlock = new ValueBlock("Number of impressions", "0k"); //TODO: Add binding
        mainPane.setCenter(valueBlock);
    }

    @Override
    public void events() {
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            appWindow.welcomeScreen();
        });
    }
}
