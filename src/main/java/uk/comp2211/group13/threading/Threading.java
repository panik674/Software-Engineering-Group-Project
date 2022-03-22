package uk.comp2211.group13.threading;

import uk.comp2211.group13.listeners.DisplayValuesListener;
import uk.comp2211.group13.listeners.ErrorListener;
import uk.comp2211.group13.ui.AppWindow;

public class Threading extends Thread {

    public AppWindow appWindow;

    public DisplayValuesListener displayValuesListener;
    public ErrorListener errorListener;

    public Threading (AppWindow appWindow) {
        this.appWindow = appWindow;
    }
    /**
     * Set the listener to handle an event when metric values loading happens
     * @param listener listener to add
     */
    public void setDisplayValuesListener (DisplayValuesListener listener) {
        this.displayValuesListener = listener;
    }

    /**
     * Set the listener to handle an event when an error happens when loading files
     * @param listener listener to add
     */
    public void setErrorListener (ErrorListener listener) {
        this.errorListener = listener;
    }
}
