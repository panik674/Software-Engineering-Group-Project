package uk.comp2211.group13.listeners;

import uk.comp2211.group13.component.TabButton;
import uk.comp2211.group13.panes.BasePane;

public interface TabClickedListener {
    /**
     * Handle tabs clicking
     */
    void tabClicked (TabButton tabButton, BasePane basePane);
}
