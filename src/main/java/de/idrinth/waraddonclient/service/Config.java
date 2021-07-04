package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.Main;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.commons.io.IOUtils;

public class Config {

    private static final String KEY_WAR_PATH = "war-path";

    private static final String KEY_THEME = "theme";

    private static final String KEY_LANGUAGE = "language";

    private static final String KEY_WINDOW_LOCATION_X = "window-x";

    private static final String KEY_WINDOW_LOCATION_Y = "window-y";

    private static final String KEY_WINDOW_WIDTH = "window-width";

    private static final String KEY_WINDOW_HEIGHT = "window-height";

    private static final String KEY_PROGRESS_LOCATION_X = "progress-x";

    private static final String KEY_PROGRESS_LOCATION_Y = "progress-y";

    private static final String KEY_PROGRESS_WIDTH = "progress-width";

    private static final String KEY_PROGRESS_HEIGHT = "progress-height";

    private static final String KEY_AUTO_CLOSE = "auto-close";

    private static final String KEY_PREFIX_ADDON_UPLOAD = "addon-upload-";

    private static final String LOG_FILE = "waraddonclient.log";

    private static final String LINUX_LOG_FILE = "/var/log/waraddonclient.log";

    private static final String ADDON_FOLDER = "/Interface/AddOns/";

    private static final String LOGS = "/logs/";

    private static final String BASE_URL = "https://tools.idrinth.de/";

    private static final String VERSION_FILE = "/self.idrinth";

    private final Preferences prefs =  Preferences.userNodeForPackage(Main.class);

    private final String version;

    private final File logFile;

    private final File jarDir;

    public Config() throws IOException, URISyntaxException {
        //check to look for old conf
        if (getWARPath().equals(".")) {
            Preferences oldPref = Preferences.userRoot().node(Config.class.getCanonicalName());
            prefs.put(KEY_THEME, oldPref.get(KEY_THEME, getTheme()));
            prefs.put(KEY_WAR_PATH, oldPref.get(KEY_WAR_PATH, getWARPath()));
            prefs.put(KEY_LANGUAGE, oldPref.get(KEY_LANGUAGE, getLanguage()));
            prefs.putInt(KEY_WINDOW_HEIGHT, oldPref.getInt(KEY_WINDOW_HEIGHT, getWindowDimension().height));
            prefs.putInt(KEY_WINDOW_WIDTH, oldPref.getInt(KEY_WINDOW_WIDTH, getWindowDimension().width));
            prefs.putInt(KEY_WINDOW_LOCATION_X, oldPref.getInt(KEY_WINDOW_LOCATION_X, getWindowPosition().x));
            prefs.putInt(KEY_WINDOW_LOCATION_Y, oldPref.getInt(KEY_WINDOW_LOCATION_Y, getWindowPosition().y));
            //cleanup and remove old preferences as we have now extracted all info from it
            try {
                oldPref.removeNode();
                oldPref.flush();
            } catch (BackingStoreException e) {
                //do nothing
            }
        }
        version = IOUtils.toString(Objects.requireNonNull(Config.class.getResourceAsStream("/version")), StandardCharsets.UTF_8);
        jarDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        if (System.getProperty("os.name").startsWith("Windows")) {
            logFile = new File(jarDir.getAbsolutePath(), LOG_FILE);
        } else {
            logFile = new File(LINUX_LOG_FILE);
        }
    }

    public void setWARPath(String path) {
        prefs.put(KEY_WAR_PATH, path);
    }

    public String getWARPath() {
        return prefs.get(KEY_WAR_PATH, ".");
    }

    public String getTheme() {
        return prefs.get(KEY_THEME, "Nimbus");
    }

    public void setTheme(String theme) {
        prefs.put(KEY_THEME, theme);
    }

    public String getLanguage() {
        return prefs.get(KEY_LANGUAGE, "en");
    }

    public void setLanguage(String language) {
        prefs.put(KEY_LANGUAGE, language);
    }

    public int getAutoClose() {
        return prefs.getInt(KEY_AUTO_CLOSE, 60);
    }

    public void setAutoClose(int autoClose) {
        prefs.putInt(KEY_AUTO_CLOSE, autoClose);
    }

    public void setWindowDimension(Dimension dimension) {
        prefs.putInt(KEY_WINDOW_WIDTH, dimension.width);
        prefs.putInt(KEY_WINDOW_HEIGHT, dimension.height);
    }

    public Dimension getWindowDimension() {
        return new Dimension(
            prefs.getInt(KEY_WINDOW_WIDTH, 800),
            prefs.getInt(KEY_WINDOW_HEIGHT, 450)
        );
    }

    public void setWindowPosition(Point point) {
        prefs.putInt(KEY_WINDOW_LOCATION_X, point.x);
        prefs.putInt(KEY_WINDOW_LOCATION_Y, point.y);
    }

    public Point getWindowPosition() {
        return new Point(
            prefs.getInt(KEY_WINDOW_LOCATION_X, 0),
            prefs.getInt(KEY_WINDOW_LOCATION_Y, 0)
        );
    }

    public void setProgressDimension(Dimension dimension) {
        prefs.putInt(KEY_PROGRESS_WIDTH, dimension.width);
        prefs.putInt(KEY_PROGRESS_HEIGHT, dimension.height);
    }

    public Dimension getProgressDimension() {
        return new Dimension(
            prefs.getInt(KEY_PROGRESS_WIDTH, 300),
            prefs.getInt(KEY_PROGRESS_HEIGHT, 150)
        );
    }

    public void setProgressPosition(Point point) {
        prefs.putInt(KEY_PROGRESS_LOCATION_X, point.x);
        prefs.putInt(KEY_PROGRESS_LOCATION_Y, point.y);
    }

    public Point getProgressPosition() {
        return new Point(
            prefs.getInt(KEY_PROGRESS_LOCATION_X, 0),
            prefs.getInt(KEY_PROGRESS_LOCATION_Y, 0)
        );
    }

    public String getVersion() {
        return version;
    }

    public boolean isEnabled(String addon) {
        return prefs.getBoolean(KEY_PREFIX_ADDON_UPLOAD + addon, false);
    }

    public void setEnabled(String addon, boolean enable) {
        prefs.putBoolean(KEY_PREFIX_ADDON_UPLOAD + addon, enable);
    }

    public File getLogFile() {
        return logFile;
    }

    public String getAddonFolder() {
        return getWARPath() + ADDON_FOLDER;
    }

    public String getLogsFolder() {
        return getWARPath() + LOGS;
    }

    public String getURL() {
        return BASE_URL;
    }
    
    public File getJarDir() {
        return jarDir;
    }
    public String getVersionFile() {
        return VERSION_FILE;
    }
}
