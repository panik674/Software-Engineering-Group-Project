package uk.comp2211.group13.threading;

import uk.comp2211.group13.ui.AppWindow;

import java.io.File;
public class FolderThreading extends Threading {
    private final File folder;

    public FolderThreading (File folder, AppWindow appWindow) {
        super(appWindow);
        this.folder = folder;
    }

    @Override
    public void run() {
        String folderPath = folder.getAbsolutePath();

        if (appWindow.getData().ingest(folderPath) == 0) {
            displayValuesListener.displayValues();
        } else {
            errorListener.errorDisplay("The loaded folder is invalid");
        }
    }
}
