package uk.comp2211.group13.panes;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import uk.comp2211.group13.component.TabButton;
import uk.comp2211.group13.ui.AppWindow;

public abstract class BasePane extends BorderPane {

    public AppWindow appWindow;
    public Button chartsButton;

    public BasePane (AppWindow appWindow) {
        this.appWindow = appWindow;
        baseBuild();
    }

    public void baseBuild () {
        /*var horizontalPane = new HBox();
        horizontalPane.setStyle("-fx-border-color: black;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
                + "-fx-border-radius: 5;");
        setTop(horizontalPane);


        //horizontalPane.setPadding(new Insets(10, 10, 10, 10));

        //horizontalPane.setStyle("-fx-border-color: black;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
        //                + "-fx-border-insets: 5;" + "-fx-border-radius: 5;");
        //horizontalPane.setStyle("-fx-stroke: black;");
        *//*Button valuesButton = new Button("Metrics Values");
        valuesButton.setStyle("-fx-background-color: #01ffff");*//*

        HBox hBox = new HBox();
        TabButton tabButton1 = new TabButton("Overview 1");
        TabButton tabButton2 = new TabButton("Graph 1");
        TabButton tabButton3 = new TabButton("Histogram 1");

        hBox.getChildren().add(tabButton1);
        hBox.getChildren().add(tabButton2);
        hBox.getChildren().add(tabButton3);

        HBox.setHgrow(hBox, Priority.ALWAYS);


        ImageView plusImage = new ImageView(new Image(getClass().getResource("/plus.png").toExternalForm()));

        plusImage.setFitWidth(25);
        plusImage.setPreserveRatio(true);

        //If we press enter, send the message
        *//*text.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ENTER) return;
            sendCurrentMessage(text.getText());
            text.clear();
            text.requestFocus();
        });*//*

        //Add the text and button the the horizontal box
        horizontalPane.getChildren().add(hBox);
        horizontalPane.getChildren().add(plusImage);*/
    }

    public abstract void build();

    public Button getCharts () {
        return chartsButton;
    }
}
