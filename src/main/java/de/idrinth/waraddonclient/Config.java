package de.idrinth.waraddonclient;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.prefs.Preferences;
import org.apache.commons.io.IOUtils;

public class Config {

    private static final Preferences prefs = Preferences.userRoot().node(Config.class.getClass().getName());

    private static final String KEY_WAR_PATH = "war-path";

    private static final String KEY_THEME = "theme";

    private static final String KEY_LANGUAGE = "language";

    private static final String KEY_WINDOW_LOCATION_X = "window-x";

    private static final String KEY_WINDOW_LOCATION_Y = "window-y";

    private static final String KEY_WINDOW_WIDTH = "window-width";

    private static final String KEY_WINDOW_HEIGHT = "window-height";

    private static final String KEY_PREFIX_ADDON_UPLOAD = "addon-upload-";

    private static String version;

    private Config() {
    }

    public static void setWARPath(String path) {
        prefs.put(KEY_WAR_PATH, path);
    }

    public static String getWARPath() {
        return prefs.get(KEY_WAR_PATH, ".");
    }

    public static String getTheme() {
        return prefs.get(KEY_THEME, "Nimbus");
    }

    public static void setTheme(String theme) {
        prefs.put(KEY_THEME, theme);
    }

    public static String getLanguage() {
        return prefs.get(KEY_LANGUAGE, "en");
    }

    public static void setLanguage(String language) {
        prefs.put(KEY_LANGUAGE, language);
    }

    public static void setWindowDimension(Dimension dimension) {
        prefs.putInt(KEY_WINDOW_WIDTH, dimension.width);
        prefs.putInt(KEY_WINDOW_HEIGHT, dimension.height);
    }

    public static Dimension getWindowDimension() {
        return new Dimension(
                prefs.getInt(KEY_WINDOW_WIDTH, 800),
                prefs.getInt(KEY_WINDOW_HEIGHT, 450)
        );
    }

    public static void setWindowPosition(Point point) {
        prefs.putInt(KEY_WINDOW_LOCATION_X, point.x);
        prefs.putInt(KEY_WINDOW_LOCATION_Y, point.y);
    }

    public static Point getWindowPosition() {
        return new Point(
                prefs.getInt(KEY_WINDOW_LOCATION_X, 0),
                prefs.getInt(KEY_WINDOW_LOCATION_Y, 0)
        );
    }

    public static String getVersion() {
        if (version != null) {
            return version;
        }
        try {
            version = IOUtils.toString(Config.class.getResourceAsStream("/version"), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            return "0.0.0";
        }
        return version;
    }

    public static boolean isEnabled(String addon) {
        return prefs.getBoolean(KEY_PREFIX_ADDON_UPLOAD + addon, false);
    }

    public static void setEnabled(String addon, boolean enable) {
        prefs.putBoolean(KEY_PREFIX_ADDON_UPLOAD + addon, enable);
    }
}
