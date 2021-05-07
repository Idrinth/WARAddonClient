package de.idrinth.waraddonclient.factory;

import de.idrinth.waraddonclient.gui.Window;

public final class Interface {

    private static de.idrinth.waraddonclient.gui.Window instance;

    private Interface() {
        //this is static only
    }

    /**
     * gets the GUI-object
     */
    public static synchronized Window build() {
        if (instance == null) {
            instance = new de.idrinth.waraddonclient.gui.Window();
        }
        return instance;
    }
}
