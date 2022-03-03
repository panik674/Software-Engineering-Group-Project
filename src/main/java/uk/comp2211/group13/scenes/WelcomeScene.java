package uk.comp2211.group13.scenes;

import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.ui.AppWindow;

public class WelcomeScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(WelcomeScene.class);

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

    }

    @Override
    public void events() {
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            appWindow.exit();
        });
    }
}
