package uk.comp2211.group13.threading;

import uk.comp2211.group13.listeners.DisplayValuesListener;
import uk.comp2211.group13.listeners.ErrorListener;
import uk.comp2211.group13.ui.AppWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileThreading extends Thread {

    private List<File> files;
    private AppWindow appWindow;
    private DisplayValuesListener displayValuesListener;
    private ErrorListener errorListener;

    public FileThreading (List<File> files, AppWindow appWindow) {
        this.files = files;
        this.appWindow = appWindow;
    }

    @Override
    public void run() {
        ArrayList<String> stringPaths = new ArrayList<>();
        for (File file : files) {
            if (file != null) {
                stringPaths.add(file.getAbsolutePath());
            }
        }
        if (appWindow.getData().ingest(stringPaths) == 0) {
            displayValuesListener.displayValues();
        } else {
            errorListener.errorDisplay("Please select the correct formats of the file!");
        }
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

