package uk.comp2211.group13.panes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.Utility;
import uk.comp2211.group13.component.FilterComponent;
import uk.comp2211.group13.component.ValueBlock;
import uk.comp2211.group13.enums.Filter;
import uk.comp2211.group13.enums.Granularity;
import uk.comp2211.group13.enums.Metric;
import uk.comp2211.group13.ui.AppWindow;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OverviewPane extends BasePane {
    private static final Logger logger = LogManager.getLogger(OverviewPane.class);

    private VBox vBox;

    private ValueBlock nOI_Block;
    private ValueBlock nOC_Block;
    private ValueBlock nOU_Block;
    private ValueBlock nOB_Block;

    private Button pAndVToggle_1;

    private ValueBlock nOCon_Block;
    private ValueBlock tC_Block;
    private ValueBlock cTR_Block;
    private ValueBlock cPA_Block;

    // Third row
    private ValueBlock cPC_Block;
    private ValueBlock cPM_Block;
    private ValueBlock bR_Block;

    private Button pAndVToggle_2;

    private HBox row1;
    private HBox row2;
    private HBox row3;
    private HBox row4;



    private Float value;

    private Boolean booleanForNum;
    private Boolean booleanForRate;


    private Utility util = new Utility();

    public OverviewPane (AppWindow appWindow) {
        super(appWindow);
        filters = new FilterComponent("Overview");
        build();
    }

    /**
     * Build the layout of the scene.
     */
    @Override
    public void build() {

        // Setting up stackPane that will have the filter component


        // Vbox that will contain the values
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        filterHbox.getChildren().add(vBox);
        updateButtonAction(filters);
        resetButtonAction(filters);
        filters.setPrefWidth(appWindow.getWidth()/3);
        filters.setPrefHeight(appWindow.getHeight());
        filters.setStyle("-fx-border-color: black;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                + "-fx-border-radius: 5;");
        filterHbox.getChildren().add(filters);
        setCenter(filterHbox);


        buildBlocks();
    }

    /**
     * Build the blocks of the overview values block.
     */
    private void buildBlocks() {
        // Initialising the boolean objects
        booleanForNum = true;
        booleanForRate = true;
        // First row
        nOI_Block = new ValueBlock("Number of Impressions", requestValue(Metric.Impressions)); //TODO: Add binding
        nOC_Block = new ValueBlock("Number of Clicks", requestValue(Metric.Clicks));
        nOU_Block = new ValueBlock("Number of Uniques", requestValue(Metric.Unique));

        row1 = new HBox(nOI_Block, nOC_Block, nOU_Block);
        hBoxSetter(row1);

        // Second row
        nOB_Block = new ValueBlock("Number of Bounces", requestValue(Metric.BouncePage));

        pAndVToggle_1 = new Button("Visits");
        pAndVToggle_1.setTooltip(new Tooltip("Toggle between Bounce Visits and Bounce Pages"));
        nOB_Block.getVBox().getChildren().add(pAndVToggle_1);
        pAndVToggle_1.setOnMouseClicked(this::toggleForNum);
        pAndVToggle_1.setMaxWidth(50);

        nOCon_Block = new ValueBlock("Rate of Conversions", requestValue(Metric.Conversions)); //TODO: Add binding
        tC_Block = new ValueBlock("Total Cost (£)", requestValue(Metric.TotalCost));
        row2 = new HBox(nOB_Block, nOCon_Block, tC_Block);
        hBoxSetter(row2);

        // Third row
        cTR_Block = new ValueBlock("CTR", requestValue(Metric.CTR));
        cPA_Block = new ValueBlock("CPA", requestValue(Metric.CPA));
        cPC_Block = new ValueBlock("CPC", requestValue(Metric.CPC)); //TODO: Add binding

        row3 = new HBox(cTR_Block, cPA_Block, cPC_Block);
        hBoxSetter(row3);

        cPM_Block = new ValueBlock("CPM", requestValue(Metric.CPM));
        bR_Block = new ValueBlock("Bounce Rate", requestValue(Metric.BounceRatePage));

        pAndVToggle_2 = new Button("Visits");
        pAndVToggle_1.setTooltip(new Tooltip("Toggle between Bounce Visits and Bounce Pages"));
        bR_Block.getVBox().getChildren().add(pAndVToggle_2);
        pAndVToggle_2.setOnMouseClicked(this::toggleForRate);
        pAndVToggle_2.setMaxWidth(50);

        row4 = new HBox(cPM_Block, bR_Block);
        hBoxSetter(row4);
    }

    /**
     * Set the HBox of each row of the values block
     *
     * @param row - The HBox of the row
     */
    private void hBoxSetter(HBox row) {
        vBox.getChildren().add(row);
        vBox.setPrefWidth(appWindow.getWidth()*2/3);
        vBox.setPrefHeight(appWindow.getHeight());
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(10, 10, 10, 10));
        row.setSpacing(20);
    }

    /**
     * Request the value for each metric
     *
     * @param metric - The metric requesting its value
     */
    private String requestValue(Metric metric) {
        try {
            value = (
                    appWindow.getMetrics().request(
                            metric,
                            appWindow.getData().getMinDate(),
                            appWindow.getData().getMaxDate(),
                            mergeFilter(genderFilters,ageFilters,incomeFilters,contextFilters),
                            Granularity.None
                    )
            ).get(
                    appWindow.getData().getMinDate()
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        switch (metric) {
            case Impressions, Clicks, Unique, BouncePage, BounceVisit -> {
                return Integer.toString(Math.round(value));
            }
            case Conversions, CTR, CPA, CPC, CPM, BounceRatePage, BounceRateVisit -> {
                return Float.toString(Math.round(value * 1000) / (float) 1000);
            }
            case TotalCost -> {
                return (NumberFormat.getCurrencyInstance().format(value/100.0)).replace("£", "");
            }
            default -> {
                return value.toString();
            }
        }
    }

    /**
     * Toggle between Bounces per visit and per page
     *
     * @param mouseEvent - The mouse click event
     */
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

    /**
     * Toggle between Bounces Rate per visit and per page
     *
     * @param mouseEvent - The mouse click event
     */
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

    private void reRequestFilteredValues () {
        nOI_Block.setValue(requestValue(Metric.Impressions));
        nOC_Block.setValue(requestValue(Metric.Clicks));
        nOU_Block.setValue(requestValue(Metric.Unique));
        if (pAndVToggle_1.getText().equals("Pages")) {
            nOB_Block.setValue(requestValue(Metric.BounceVisit));
        } else {
            nOB_Block.setValue(requestValue(Metric.BouncePage));
        }

        // Second row
        nOCon_Block.setValue(requestValue(Metric.Conversions));
        tC_Block.setValue(requestValue(Metric.TotalCost));
        cTR_Block.setValue(requestValue(Metric.CTR));
        cPA_Block.setValue(requestValue(Metric.CPA));

        // Third row
        cPC_Block.setValue(requestValue(Metric.CPC));
        cPM_Block.setValue(requestValue(Metric.CPM));
        if (pAndVToggle_2.getText().equals("Pages")) {
            bR_Block.setValue(requestValue(Metric.BounceRateVisit));
        } else {
            bR_Block.setValue(requestValue((Metric.BounceRatePage)));
        }
    }

    /**
     * Calls the reRequestFilteredValues method. This must take place in this method as it is used polymorphically in the parent class
     */
    @Override
    public void buildGraph() {
        appWindow.getMetrics().setBouncePages(filters.getBouncePage());
        appWindow.getMetrics().setBounceSeconds(filters.getBounceVisit());
        reRequestFilteredValues();
    }

    /**
     *Method to Build and grow regions to provide spacing for the filter HBox
     *
     * @return - The grown region
     */
    public Region regionBuild(){
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    /**
     * Polymorphically redefined updateButtonAction to remove the settings that do not apply to the overview pane
     *
     * @param filterComponent - The corresponding filter component
     */
    public void updateButtonAction(FilterComponent filterComponent) {
        filterComponent.getUpdateButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                granularity = Utility.getGranularity(filterComponent.getGranularity());
                GenderList = addGenderFilters(filterComponent, GenderList);
                AgeList = addAgeFilters(filterComponent, AgeList);
                IncomeList = addIncomeFilters(filterComponent, IncomeList);
                ContextList = addContextFilters(filterComponent, ContextList);
                genderFilters = addFilters(genderFilters, Filter.Gender, GenderList);
                ageFilters = addFilters(ageFilters, Filter.Age, AgeList);
                incomeFilters = addFilters(incomeFilters, Filter.Income, IncomeList);
                contextFilters = addFilters(contextFilters, Filter.Context, ContextList);
                buildGraph();
                filterHbox.getChildren().get(1).toFront();


            }
        });
    }

    /**
     * Polymorphically redefined resetButtonAction to remove the settings that do not apply to the overview pane
     *
     * @param filterComponent - The corresponding filter component
     */
    public void resetButtonAction(FilterComponent filterComponent){
        filterComponent.getResetButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                filters.resetCheckBoxes();
                granularity = Granularity.Day;
                filters.fireDay();
                GenderList = listClear(GenderList);
                AgeList = listClear(AgeList);
                IncomeList = listClear(IncomeList);
                ContextList = listClear(ContextList);
                genderFilters.clear();
                ageFilters.clear();
                incomeFilters.clear();
                contextFilters.clear();
                resetBounces();
                buildGraph();
                filterHbox.getChildren().get(1).toFront();
            }
        });
    }


}
