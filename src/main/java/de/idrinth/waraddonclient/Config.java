package de.idrinth.waraddonclient;

import java.util.prefs.Preferences;

public class Config {

    private static final Preferences prefs = Preferences.userRoot().node(Config.class.getClass().getName());

    private Config() {
    }

    public static void setWARPath(String path) {
        prefs.put("war-path", path);
    }

    public static String getWARPath() {
        return prefs.get("war-path", ".");
    }

}
