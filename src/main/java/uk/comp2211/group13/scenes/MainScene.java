package uk.comp2211.group13.scenes;

import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.component.TabButton;
import uk.comp2211.group13.panes.BasePane;
import uk.comp2211.group13.panes.GraphPane;
import uk.comp2211.group13.panes.HistogramPane;
import uk.comp2211.group13.panes.OverviewPane;
import uk.comp2211.group13.ui.AppPane;
import uk.comp2211.group13.ui.AppWindow;

import java.util.ArrayList;

public class MainScene extends BaseScene {
    private static final Logger logger = LogManager.getLogger(MainScene.class);

    private StackPane valuesPane;

    private BasePane currentPane;

    private OverviewPane overviewPane;
    private GraphPane graphPane;
    private HistogramPane histogramPane;

    private VBox mainVbox;
    private HBox horizontalPane;
    private StackPane stackPane;

    private HBox hBox;

    private ImageView plusImage;

    private Boolean togglePlus;

    public ArrayList<TabButton> tabButtons;

    /**
     * Creates a new scene.
     *
     * @param appWindow the app window that displays the scene
     */
    public MainScene(AppWindow appWindow) {
        super(appWindow);
    }

    /**
     * Initialise the scene and start the game
     */
    @Override
    public void initialise() {
        logger.info("Initialising Value Scene");
        events();
    }

    /**
     * Build the Values window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new AppPane(appWindow.getWidth(), appWindow.getHeight());

        valuesPane = new StackPane();
        valuesPane.setMaxWidth(appWindow.getWidth());
        valuesPane.setMaxHeight(appWindow.getHeight());

        root.getChildren().add(valuesPane);

        /*tabButtons = new ArrayList<>();
        //Set up base TabButtons
        TabButton tabButton1 = new TabButton("Overview 1", valuesPane, overviewPane);
        TabButton tabButton2 = new TabButton("Graph 1", valuesPane, graphPane);
        TabButton tabButton3 = new TabButton("Histogram 1", valuesPane, histogramPane);
        tabButtons.add(tabButton1);
        tabButtons.add(tabButton2);
        tabButtons.add(tabButton3);*/

        mainVbox = new VBox();
        valuesPane.getChildren().add(mainVbox);

        stackPane = new StackPane();
        overviewPane = new OverviewPane(appWindow);
        graphPane = new GraphPane(appWindow);
        histogramPane = new HistogramPane(appWindow);

        baseBuild();
        stackPane.setMaxWidth(appWindow.getWidth() - horizontalPane.getWidth());
        stackPane.setMaxHeight(appWindow.getHeight() - horizontalPane.getHeight());

        VBox.setVgrow(stackPane, Priority.NEVER);
        mainVbox.getChildren().add(stackPane);

        stackPane.getChildren().add(overviewPane);
        currentPane = overviewPane;
        //mainPane.getCharts().setOnMouseClicked(this::changeScene);
    }

    public void baseBuild () {
        horizontalPane = new HBox();
        horizontalPane.setStyle("-fx-border-color: black;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                + "-fx-border-radius: 5;");
        horizontalPane.setMinHeight(40);
        horizontalPane.setMaxHeight(40);
        //horizontalPane.set
        mainVbox.getChildren().add(horizontalPane);
        VBox.setVgrow(horizontalPane, Priority.ALWAYS);


        hBox = new HBox();
        TabButton tabButton1 = new TabButton("Overview 1", stackPane, overviewPane, currentPane);
        //tabButton1.setOnMouseClicked(this::changePane);
        TabButton tabButton2 = new TabButton("Graph 1", stackPane, graphPane, currentPane);
        //tabButton2.getTabButton().setOnMouseClicked(this::changePane);
        TabButton tabButton3 = new TabButton("Histogram 1", stackPane, histogramPane, currentPane);
        //tabButton3.getTabButton().setOnMouseClicked(this::changePane);
        tabButton1.setTabClosedListener(this::tabClosed);
        tabButton2.setTabClosedListener(this::tabClosed);
        tabButton3.setTabClosedListener(this::tabClosed);

        hBox.getChildren().add(tabButton1);
        hBox.getChildren().add(tabButton2);
        hBox.getChildren().add(tabButton3);

        HBox.setHgrow(hBox, Priority.ALWAYS);


        plusImage = new ImageView(new Image(getClass().getResource("/plus.png").toExternalForm()));
        togglePlus = true;

        plusImage.setFitWidth(25);
        plusImage.setPreserveRatio(true);

        plusImage.setOnMouseClicked(this::addPane);

        //Add the text and button the the horizontal box
        horizontalPane.getChildren().add(hBox);
        horizontalPane.getChildren().add(plusImage);
    }

    private void changePane(MouseEvent mouseEvent) {
        logger.info("Tab Button Clicked");
        valuesPane.getChildren().clear();

        //valuesPane.getChildren().add(currentPane);
    }

    @Override
    public void events() {
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            appWindow.welcomeScreen();
        });
    }

    private void tabClosed (TabButton tabButton) {
        hBox.getChildren().remove(tabButton);
    }

    private void addPane (MouseEvent mouseEvent) {
        RotateTransition rotate = new RotateTransition(Duration.millis(2000), plusImage);
        if (togglePlus) {
            rotate.setFromAngle(0);
            rotate.setToAngle(45);
        } else {
            rotate.setFromAngle(45);
            rotate.setToAngle(0);
        }
        rotate.play();
        togglePlus = !togglePlus;
    }
}
