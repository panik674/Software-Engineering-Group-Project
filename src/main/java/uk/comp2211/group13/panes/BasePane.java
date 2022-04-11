package uk.comp2211.group13.panes;

import javafx.scene.layout.BorderPane;
import uk.comp2211.group13.ui.AppWindow;

public abstract class BasePane extends BorderPane {

    public AppWindow appWindow;

    public BasePane (AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    /**
     * Build the layout of the pane.
     */
    public abstract void build();
}
