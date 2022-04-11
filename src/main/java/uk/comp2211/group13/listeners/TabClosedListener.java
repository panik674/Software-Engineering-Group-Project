package uk.comp2211.group13.listeners;

import uk.comp2211.group13.component.TabButton;
import uk.comp2211.group13.panes.BasePane;

public interface TabClosedListener {
    /**
     * Handle a closing tabs event
     */
    void tabClosed (TabButton tabButton, String type, BasePane basePane);
}
