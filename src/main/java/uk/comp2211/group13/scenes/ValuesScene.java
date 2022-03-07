package uk.comp2211.group13.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private VBox vBox;

    private HBox row1;
    private HBox row2;
    private HBox row3;
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
        logger.info("Initialising Value Scene");
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

        vBox = new VBox();
        mainPane.setCenter(vBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setSpacing(10);

        buildBlocks();
    }

    @Override
    public void events() {
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ESCAPE) return;
            appWindow.welcomeScreen();
        });
    }

    private void buildBlocks() {
        // First row
        ValueBlock nOI_Block = new ValueBlock("Number of impressions", "0k"); //TODO: Add binding
        ValueBlock nOC_Block = new ValueBlock("Number of Clicks", "0k");
        ValueBlock nOU_Block = new ValueBlock("Number of Uniques", "0k");
        ValueBlock nOB_Block = new ValueBlock("Number of Bounces", "0k");
        row1 = new HBox(nOI_Block, nOC_Block, nOU_Block, nOB_Block);
        hBoxSetter(row1);

        // Second row
        ValueBlock nOCon_Block = new ValueBlock("Number of Conversions", "0k"); //TODO: Add binding
        ValueBlock tC_Block = new ValueBlock("Total Cost", "0k");
        ValueBlock cTR_Block = new ValueBlock("CTR", "0k");
        ValueBlock cPA_Block  = new ValueBlock("CPA", "0k");
        row2 = new HBox(nOCon_Block, tC_Block, cTR_Block, cPA_Block);
        hBoxSetter(row2);

        // Third row
        ValueBlock cPC_Block = new ValueBlock("CPC", "0k"); //TODO: Add binding
        ValueBlock cPM_Block = new ValueBlock("CPM", "0k");
        ValueBlock bR_Block = new ValueBlock("Bounce Rate", "0k");
        row3 = new HBox(cPC_Block, cPM_Block, bR_Block);
        hBoxSetter(row3);
    }

    private void hBoxSetter (HBox row) {
        vBox.getChildren().add(row);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(10, 10, 10, 10));
        row.setSpacing(47);
    }
}
