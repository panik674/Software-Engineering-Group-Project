package uk.comp2211.group13.scenes;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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


    private Text overviewText;
    private Text graphText;
    private Text histogramText;

    private HBox addTabHbox;

    private int overviewInc;
    private int graphInc;
    private int histogramInc;

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
        horizontalPane.setAlignment(Pos.CENTER);
        //horizontalPane.set
        mainVbox.getChildren().add(horizontalPane);
        VBox.setVgrow(horizontalPane, Priority.ALWAYS);


        // initialise increments
        overviewInc = 1;
        graphInc = 1;
        histogramInc = 1;

        hBox = new HBox();
        TabButton tabButton1 = new TabButton("Overview", overviewInc, stackPane, overviewPane, currentPane);
        //tabButton1.setOnMouseClicked(this::changePane);
        TabButton tabButton2 = new TabButton("Graph", graphInc, stackPane, graphPane, currentPane);
        //tabButton2.getTabButton().setOnMouseClicked(this::changePane);
        TabButton tabButton3 = new TabButton("Histogram", histogramInc, stackPane, histogramPane, currentPane);
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

        //Hbox for the options of tab when pressing the plus symbol
        addTabHbox = new HBox();
        addTabHbox.setSpacing(5);
        addTabHbox.setStyle("-fx-background-color: #21bdd4");
        //addTabHbox.setPrefHeight(20);
        addTabHbox.setAlignment(Pos.CENTER);
        //addTabHbox.setPadding(new Insets(0, 5, 0, 5));

        overviewText = new Text("Overview");
        graphText = new Text("Graph");
        histogramText = new Text("Histogram");

        overviewText.setFill(Color.WHITE);
        graphText.setFill(Color.WHITE);
        histogramText.setFill(Color.WHITE);

        overviewText.setOnMouseClicked(this::newOverview);
        graphText.setOnMouseClicked(this::newGraph);
        histogramText.setOnMouseClicked(this::newHistogram);


        //Add the text and button the the horizontal box
        horizontalPane.getChildren().add(hBox);
        horizontalPane.getChildren().add(addTabHbox);
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

    private void tabClosed (TabButton tabButton, String type) {
        hBox.getChildren().remove(tabButton);
        switch (type) {
            case "Overview" -> overviewInc--;
            case "Graph" -> graphInc--;
            case "Histogram" -> histogramInc--;
            default -> System.err.println("Wrong type");
        }
    }

    private void addPane (MouseEvent mouseEvent) {
        RotateTransition rotate = new RotateTransition(Duration.millis(1000), plusImage);
        if (togglePlus) {
            rotate.setFromAngle(0);
            rotate.setToAngle(45);
            addTabHbox.getChildren().clear();
            addTabHbox.getChildren().add(overviewText);
            addTabHbox.getChildren().add(graphText);
            addTabHbox.getChildren().add(histogramText);
            transitionInHbox();
        } else {
            rotate.setFromAngle(45);
            rotate.setToAngle(0);
            transitionOutHbox();
            //addTabHbox.getChildren().clear();
        }
        rotate.play();
        togglePlus = !togglePlus;
    }

    private void transitionInHbox () {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), addTabHbox);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    private void transitionOutHbox () {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), addTabHbox);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished((e) -> addTabHbox.getChildren().clear());

    }

    private void newOverview (MouseEvent mouseEvent) {
        logger.info("New Overview Tab Button has been added!");
        overviewInc++;

        OverviewPane newOverviewPane = new OverviewPane(appWindow);
        currentPane = newOverviewPane;

        TabButton tabButton = new TabButton("Overview", overviewInc, stackPane, newOverviewPane, currentPane); // Add increment
        tabButton.setTabClosedListener(this::tabClosed);
        hBox.getChildren().add(tabButton);

        stackPane.getChildren().clear();

        stackPane.getChildren().add(newOverviewPane);

        addPane(mouseEvent);
    }

    private void newGraph (MouseEvent mouseEvent) {
        logger.info("New Graph Tab Button has been added!");
        graphInc++;

        GraphPane newGraphPane = new GraphPane(appWindow);
        currentPane = newGraphPane;

        TabButton tabButton = new TabButton("Graph", graphInc, stackPane, newGraphPane, currentPane); // Add increment
        tabButton.setTabClosedListener(this::tabClosed);
        hBox.getChildren().add(tabButton);

        stackPane.getChildren().clear();

        stackPane.getChildren().add(newGraphPane);

        addPane(mouseEvent);
    }

    private void newHistogram (MouseEvent mouseEvent) {
        logger.info("New Histogram Tab Button has been added!");
        histogramInc++;

        HistogramPane newHistogramPane  = new HistogramPane(appWindow);
        currentPane = newHistogramPane;

        TabButton tabButton = new TabButton("Histogram", histogramInc, stackPane, newHistogramPane, currentPane); // Add increment
        tabButton.setTabClosedListener(this::tabClosed);
        hBox.getChildren().add(tabButton);

        stackPane.getChildren().clear();

        stackPane.getChildren().add(newHistogramPane);

        addPane(mouseEvent);
    }
}
