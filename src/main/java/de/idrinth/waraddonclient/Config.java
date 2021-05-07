package de.idrinth.waraddonclient;

import java.awt.Dimension;
import java.awt.Point;
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

    public static String getTheme() {
        return prefs.get("theme", "Nimbus");
    }

    public static void setTheme(String theme) {
        prefs.put("theme", theme);
    }

    public static String getLanguage() {
        return prefs.get("language", "en");
    }

    public static void setLanguage(String language) {
        prefs.put("language", language);
    }

    public static void setWindowDimension(Dimension dimension) {
        prefs.putInt("window-width", dimension.width);
        prefs.putInt("window-height", dimension.height);
    }

    public static Dimension getWindowDimension() {
        return new Dimension(
            prefs.getInt("window-width", 800),
            prefs.getInt("window-height", 450)
        );
    }

    public static void setWindowPosition(Point point) {
        prefs.putInt("window-x", point.x);
        prefs.putInt("window-y", point.y);
    }

    public static Point getWindowPosition() {
        return new Point(
            prefs.getInt("window-x", 0),
            prefs.getInt("window-y", 0)
        );
    }

}
