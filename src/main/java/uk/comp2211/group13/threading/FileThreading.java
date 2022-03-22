package uk.comp2211.group13.threading;

import uk.comp2211.group13.ui.AppWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileThreading extends Threading {

    private final List<File> files;

    public FileThreading (List<File> files, AppWindow appWindow) {
        super(appWindow);
        this.files = files;
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
}

