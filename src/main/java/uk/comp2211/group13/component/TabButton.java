package uk.comp2211.group13.component;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.comp2211.group13.listeners.TabClosedListener;
import uk.comp2211.group13.panes.BasePane;

public class TabButton extends StackPane {

    private static final Logger logger = LogManager.getLogger(TabButton.class);


    private String name;
    private int increment;
    private StackPane stackPane;
    private BasePane basePane;
    private BasePane currentPane;

    public Button tabButton;
    public ImageView x;

    public TabClosedListener tabClosedListener;


    public TabButton (String name, int increment, StackPane stackPane, BasePane basePane, BasePane currentPane) {
        this.name = name;
        this.increment = increment;
        this.stackPane = stackPane;
        this.basePane = basePane;
        this.currentPane = currentPane;

        format();
    }

    /**
     * Set the format of the tab button
     */
    private void format () {
        HBox hBox = new HBox();

        tabButton = new Button(name + " " + increment);
        tabButton.setStyle("-fx-background-color: #21bdd4;" + "-fx-border-color: grey;");

        tabButton.setTextFill(Color.WHITE);
        tabButton.setPrefWidth(85);
        tabButton.setPrefHeight(38);

        tabButton.setOnMouseClicked(this::changePane);

        x = new ImageView(new Image(getClass().getResource("/x.png").toExternalForm()));

        x.setFitWidth(12);
        x.setPreserveRatio(true);

        x.setOnMouseClicked(this::closePane);

        hBox.getChildren().add(tabButton);
        hBox.getChildren().add(x);

        getChildren().add(hBox);
    }

    /**
     * Handle when the tab button gets clicked
     *
     * @param mouseEvent the mouse clicking event
     */
    private void changePane (MouseEvent mouseEvent) {
        logger.info("Tab Button has been clicked!");
        stackPane.getChildren().clear();

        currentPane = basePane;
        stackPane.getChildren().add(basePane);
    }

    /**
     * Handle when the tab button gets closed (aka clicking the "x" image)
     *
     * @param mouseEvent the mouse clicking event
     */
    private void closePane (MouseEvent mouseEvent) {
        logger.info("Tab has been close!");

        tabClosedListener.tabClosed(this, name);

        if (currentPane == basePane) {
            stackPane.getChildren().clear();
            Text text = new Text("Select or add a tab!");
            text.setStyle("-fx-font-size: 24;" + "-fx-font-weight: bold;");
            stackPane.getChildren().add(text);
        }
    }

    /**
     * Get the tabButton Button
     *
     * @return tabButton
     */
    public Button getTabButton () {
        return tabButton;
    }

    /**
     * Get the "x" image
     *
     * @return x
     */
    public ImageView getX () {
        return x;
    }

    /**
     * Get the associated with the tab Button
     *
     * @return basePane
     */
    public BasePane getBasePane () {
        return  basePane;
    }

    /**
     * Set the listener to handle an event when a tab gets closed
     * @param listener listener to add
     */
    public void setTabClosedListener (TabClosedListener listener) {
        this.tabClosedListener = listener;
    }
}
