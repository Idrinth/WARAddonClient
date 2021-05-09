package de.idrinth.waraddonclient.factory;

import de.idrinth.waraddonclient.gui.Window;

public final class Interface {

    private static de.idrinth.waraddonclient.gui.Window instance;

    private Interface() {
        //this is static only
    }

    public static synchronized Window build() {
        return instance;
    }

    public static synchronized void set(Window window) {
        instance = window;
    }
}
