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

public class MainScene extends BaseScene {
    private static final Logger logger = LogManager.getLogger(MainScene.class);

    private StackPane mainPane;

    private BasePane currentPane;
    private TabButton currentTabButton;

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

    private HBox addTabHBox;

    private int overviewInc;
    private int graphInc;
    private int histogramInc;


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

        mainPane = new StackPane();
        mainPane.setMaxWidth(appWindow.getWidth());
        mainPane.setMaxHeight(appWindow.getHeight());

        root.getChildren().add(mainPane);

        mainVbox = new VBox();
        mainPane.getChildren().add(mainVbox);

        // Stack Pane that contains the pane (overview, graph, or histogram)
        stackPane = new StackPane();

        // Setting up the three initial panes
        overviewPane = new OverviewPane(appWindow);
        graphPane = new GraphPane(appWindow);
        histogramPane = new HistogramPane(appWindow);

        // Building the tab bar
        baseBuild();

        stackPane.setMaxWidth(appWindow.getWidth() - horizontalPane.getWidth());
        stackPane.setMaxHeight(appWindow.getHeight() - horizontalPane.getHeight());

        VBox.setVgrow(stackPane, Priority.NEVER);
        mainVbox.getChildren().add(stackPane);

        stackPane.getChildren().add(overviewPane);

        currentPane = overviewPane;
    }

    /**
     * Build the tab bar at the top
     */
    public void baseBuild () {
        horizontalPane = new HBox();
        horizontalPane.setStyle("-fx-border-color: black;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                + "-fx-border-radius: 5;");
        horizontalPane.setMinHeight(40);
        horizontalPane.setMaxHeight(40);
        horizontalPane.setAlignment(Pos.CENTER);

        mainVbox.getChildren().add(horizontalPane);
        VBox.setVgrow(horizontalPane, Priority.ALWAYS);


        // initialise increments
        overviewInc = 1;
        graphInc = 1;
        histogramInc = 1;

        hBox = new HBox();
        TabButton tabButton1 = new TabButton("Overview", overviewInc, overviewPane, currentPane);

        TabButton tabButton2 = new TabButton("Graph", graphInc, graphPane, currentPane);

        TabButton tabButton3 = new TabButton("Histogram", histogramInc, histogramPane, currentPane);

        tabButton1.setTabClosedListener(this::tabClosed);
        tabButton2.setTabClosedListener(this::tabClosed);
        tabButton3.setTabClosedListener(this::tabClosed);

        tabButton1.setTabClickedListener(this::tabClicked);
        tabButton2.setTabClickedListener(this::tabClicked);
        tabButton3.setTabClickedListener(this::tabClicked);

        hBox.getChildren().add(tabButton1);
        hBox.getChildren().add(tabButton2);
        hBox.getChildren().add(tabButton3);

        HBox.setHgrow(hBox, Priority.ALWAYS);

        // Setting the overview tabButton as the default
        currentTabButton = tabButton1;
        currentTabButton.getTabButton().setStyle("-fx-background-color: #14606d");


        // Set up the tab adding symbol (plus)
        plusImage = new ImageView(new Image(getClass().getResource("/plus.png").toExternalForm()));
        togglePlus = true;

        plusImage.setFitWidth(25);
        plusImage.setPreserveRatio(true);

        plusImage.setOnMouseClicked(this::addPane);

        //Hbox for the options of tab when pressing the plus symbol
        addTabHBox = new HBox();
        addTabHBox.setSpacing(5);
        addTabHBox.setStyle("-fx-background-color: #21bdd4");
        addTabHBox.setAlignment(Pos.CENTER);

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
        horizontalPane.getChildren().add(addTabHBox);
        horizontalPane.getChildren().add(plusImage);
    }

    /**
     * This method add listeners for the required keyboard and mouse events
     */
    @Override
    public void events() {
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            appWindow.welcomeScreen();
        });
    }


    /**
     * Handle when a tab gets closed
     *
     * @param tabButton the tab button that has been closed
     * @param type the type of the tab that has been closed
     */
    private void tabClosed (TabButton tabButton, String type, BasePane basePane) {
        hBox.getChildren().remove(tabButton);

        if (currentPane == basePane) {
            stackPane.getChildren().clear();
            Text text = new Text("Select or add a tab!");
            text.setStyle("-fx-font-size: 24;" + "-fx-font-weight: bold;");
            stackPane.getChildren().add(text);
            currentTabButton = null;
        }

        /*if (currentTabButton != tabButton && currentTabButton != null) {
            currentTabButton.getTabButton().setStyle("-fx-background-color: #21bdd4");
        }*/

        /*switch (type) {
            case "Overview" -> overviewInc--;
            case "Graph" -> graphInc--;
            case "Histogram" -> histogramInc--;
            default -> System.err.println("Wrong type");
        }*/
    }

    /**
     * Handle when a tab Button gets clicked
     *
     * @param tabButton the tab button that has been clicked
     * @param basePane the basePane to be displayed
     */
    private void tabClicked (TabButton tabButton, BasePane basePane) {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(basePane);

        if (currentTabButton != null) {
            currentTabButton.getTabButton().setStyle("-fx-background-color: #21bdd4");
        }
        tabButton.getTabButton().setStyle("-fx-background-color: #14606d");

        currentTabButton = tabButton;
        currentPane = basePane;
    }

    /**
     * Handle when a tab is added and the animation for that
     *
     * @param mouseEvent the mouse clicking event
     */
    private void addPane (MouseEvent mouseEvent) {
        RotateTransition rotate = new RotateTransition(Duration.millis(1000), plusImage);
        if (togglePlus) {
            rotate.setFromAngle(0);
            rotate.setToAngle(45);
            addTabHBox.getChildren().clear();
            addTabHBox.getChildren().add(overviewText);
            addTabHBox.getChildren().add(graphText);
            addTabHBox.getChildren().add(histogramText);
            transitionInHBox();
        } else {
            rotate.setFromAngle(45);
            rotate.setToAngle(0);
            transitionOutHBox();
        }
        rotate.play();
        togglePlus = !togglePlus;
    }

    /**
     * Handle the appearing animation of the tab adding options
     */
    private void transitionInHBox() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), addTabHBox);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    /**
     * Handle the disappearing animation of the tab adding options
     */
    private void transitionOutHBox() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), addTabHBox);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished((e) -> addTabHBox.getChildren().clear());

    }

    /**
     * Handle when a new Overview tab is added
     *
     * @param mouseEvent the mouse clicking event
     */
    private void newOverview (MouseEvent mouseEvent) {
        logger.info("New Overview Tab Button has been added!");
        overviewInc++;

        OverviewPane newOverviewPane = new OverviewPane(appWindow);
        currentPane = newOverviewPane;

        TabButton tabButton = new TabButton("Overview", overviewInc, newOverviewPane, currentPane);

        setUpNewPane(tabButton, newOverviewPane);

        addPane(mouseEvent);
    }

    /**
     * Handle when a new Graph tab is added
     *
     * @param mouseEvent the mouse clicking event
     */
    private void newGraph (MouseEvent mouseEvent) {
        logger.info("New Graph Tab Button has been added!");
        graphInc++;

        GraphPane newGraphPane = new GraphPane(appWindow);
        currentPane = newGraphPane;

        TabButton tabButton = new TabButton("Graph", graphInc, newGraphPane, currentPane);
        setUpNewPane(tabButton, newGraphPane);

        addPane(mouseEvent);
    }

    /**
     * Handle when a new Histogram tab is added
     *
     * @param mouseEvent the mouse clicking event
     */
    private void newHistogram (MouseEvent mouseEvent) {
        logger.info("New Histogram Tab Button has been added!");
        histogramInc++;

        HistogramPane newHistogramPane  = new HistogramPane(appWindow);
        currentPane = newHistogramPane;

        TabButton tabButton = new TabButton("Histogram", histogramInc, newHistogramPane, currentPane);
        setUpNewPane(tabButton, newHistogramPane);

        addPane(mouseEvent);
    }

    /**
     * Handle when a new Pane needs setUp
     *
     * @param tabButton the new tab Button added
     * @param basePane the new basePane added
     */
    private void setUpNewPane (TabButton tabButton, BasePane basePane) {
        tabButton.setTabClosedListener(this::tabClosed);
        tabButton.setTabClickedListener(this::tabClicked);
        hBox.getChildren().add(tabButton);

        //Removing the highlight of te previous button and adding it to the new one
        if (currentTabButton != null) {
            currentTabButton.getTabButton().setStyle("-fx-background-color: #21bdd4");
        }
        tabButton.getTabButton().setStyle("-fx-background-color: #14606d");
        currentTabButton = tabButton;

        stackPane.getChildren().clear();

        stackPane.getChildren().add(basePane);
    }
}
