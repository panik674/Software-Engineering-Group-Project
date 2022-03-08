package uk.comp2211.group13.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.component.ValueBlock;
import uk.comp2211.group13.component.ValueCanvas;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppPane;
import uk.comp2211.group13.ui.AppWindow;

import java.text.ParseException;

public class ValuesScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(ValuesScene.class);

    private StackPane valuesPane;
    private VBox vBox;

    private ValueBlock nOB_Block;
    private Button pAndVToggle_1;

    private ValueBlock bR_Block;
    private Button pAndVToggle_2;

    private HBox row1;
    private HBox row2;
    private HBox row3;

    private Float value;

    private Boolean booleanForNum;
    private Boolean booleanForRate;
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
        // Initialising the boolean objects
        booleanForNum = true;
        booleanForRate = true;
        // First row
        ValueBlock nOI_Block = new ValueBlock("Number of impressions", requestValue(Metric.Impressions)); //TODO: Add binding
        ValueBlock nOC_Block = new ValueBlock("Number of Clicks", requestValue(Metric.Clicks));
        ValueBlock nOU_Block = new ValueBlock("Number of Uniques", requestValue(Metric.Unique));
        nOB_Block = new ValueBlock("Number of Bounces", requestValue(Metric.BouncePage));

        pAndVToggle_1 = new Button("Visits");
        nOB_Block.getVBox().getChildren().add(pAndVToggle_1);
        pAndVToggle_1.setOnMouseClicked(this::toggleForNum);
        pAndVToggle_1.setMaxWidth(50);

        row1 = new HBox(nOI_Block, nOC_Block, nOU_Block, nOB_Block);
        hBoxSetter(row1);

        // Second row
        ValueBlock nOCon_Block = new ValueBlock("Number of Conversions", requestValue(Metric.Conversions)); //TODO: Add binding
        ValueBlock tC_Block = new ValueBlock("Total Cost", requestValue(Metric.TotalCost));
        ValueBlock cTR_Block = new ValueBlock("CTR", requestValue(Metric.CTR));
        ValueBlock cPA_Block  = new ValueBlock("CPA", requestValue(Metric.CPA));
        row2 = new HBox(nOCon_Block, tC_Block, cTR_Block, cPA_Block);
        hBoxSetter(row2);

        // Third row
        ValueBlock cPC_Block = new ValueBlock("CPC", requestValue(Metric.CPC)); //TODO: Add binding
        ValueBlock cPM_Block = new ValueBlock("CPM", requestValue(Metric.CPM));
        bR_Block = new ValueBlock("Bounce Rate", requestValue(Metric.BounceRatePage));

        pAndVToggle_2 = new Button("Visits");
        bR_Block.getVBox().getChildren().add(pAndVToggle_2);
        pAndVToggle_2.setOnMouseClicked(this::toggleForRate);
        pAndVToggle_2.setMaxWidth(50);

        row3 = new HBox(cPC_Block, cPM_Block, bR_Block);
        hBoxSetter(row3);
    }

    private void hBoxSetter (HBox row) {
        vBox.getChildren().add(row);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(10, 10, 10, 10));
        row.setSpacing(47);
    }

    private String requestValue (Metric metric) {
        try {

            // For know we are assuming that the data will be given will just be on this range. To be improved on Sprint 2
            value = (appWindow.getMetrics().request(metric, "2015-01-01 12:00:00", "2015-01-14 12:00:00", Granularity.None)).get(Utility.string2Date("2015-01-01 12:00:00"));
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println(value);
        return value.toString();

    }

    private void toggleForNum(MouseEvent mouseEvent) {
        booleanForNum = !booleanForNum;

        if (!booleanForNum) {
            pAndVToggle_1.setText("Pages");
            nOB_Block.setValue(requestValue(Metric.BounceVisit));
        } else {
            pAndVToggle_1.setText("Visits");
            nOB_Block.setValue(requestValue(Metric.BouncePage));
        }

    }

    private void toggleForRate(MouseEvent mouseEvent) {
        booleanForRate = !booleanForRate;

        if (!booleanForRate) {
            pAndVToggle_2.setText("Pages");
            bR_Block.setValue(requestValue(Metric.BounceRateVisit));
        } else {
            pAndVToggle_2.setText("Visits");
            bR_Block.setValue(requestValue((Metric.BounceRatePage)));
        }
    }
}
