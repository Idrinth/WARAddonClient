package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.Config;
import de.idrinth.waraddonclient.Utils;
import de.idrinth.waraddonclient.service.FileLogger;
import de.idrinth.waraddonclient.service.Request;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ActualAddon implements de.idrinth.waraddonclient.model.Addon {

    private HashMap<String, String> descriptions = new HashMap<>();

    private String version;

    private final String slug;

    private final String name;

    private String installed = "-";

    private ArrayList<String> tags = new ArrayList<>();

    private final AddonSettings addonSettings;

    private static final String BASE_PATH = "/Interface/AddOns/";

    private static final String VERSION_FILE = "/self.idrinth";

    private final Request client;
    
    private final FileLogger logger;
    
    public ActualAddon(javax.json.JsonObject addon, Request client, FileLogger logger) {
        this.client = client;
        this.logger = logger;
        descriptions.put("en", getStringFromObject("description", addon));
        descriptions.put("fr", getStringFromObject("description_fr", addon));
        descriptions.put("de", getStringFromObject("description_de", addon));
        version = getStringFromObject("version", addon);
        slug = getStringFromObject("slug", addon);
        name = getStringFromObject("name", addon);
        javax.json.JsonArray tagList = addon.getJsonArray("tags");
        int counter = 0;
        while (tagList.size() > counter) {
            tags.add(tagList.getString(counter));
            counter++;
        }
        new VersionFinder().run();
        addonSettings = new AddonSettings(name, logger);
    }

    /**
     * get the tags of this addon
     *
     * @return java.util.ArrayList
     */
    public java.util.ArrayList<String> getTags() {
        return tags;
    }

    /**
     * does this addon have the given tag?
     *
     * @param tag
     * @return boolean
     */
    public boolean hasTag(String tag) {
        return tags.stream().anyMatch((hasTag) -> (tag.equalsIgnoreCase(hasTag)));
    }

    /**
     * updates this instance with data from another instance
     *
     * @param addon
     */
    public void update(ActualAddon addon) {
        version = addon.getVersion();
        tags = addon.getTags();
        descriptions = addon.getDescriptions();
    }

    /**
     * the avaible version
     *
     * @return String
     */
    public String getVersion() {
        return version;
    }

    /**
     * the installed version
     *
     * @return String
     */
    public String getInstalled() {
        new VersionFinder().run();
        return installed;
    }

    /**
     * returns the settings for this addon
     *
     * @return AddonSettings
     */
    public final AddonSettings getUploadData() {
        return addonSettings;
    }

    /**
     * try to find a string in a jsonObject
     *
     * @param key
     * @param data
     * @return String
     */
    private final String getStringFromObject(String key, javax.json.JsonObject data) {
        if (key != null && data != null && data.containsKey(key) && !data.isNull(key)) {
            return java.util.regex.Pattern.compile("^\"|\"$").matcher(data.get(key).toString()).replaceAll("");
        }
        return "";
    }

    /**
     * get the table row configuration for this addon
     *
     * @return String[[
     */
    public String[] getTableRow() {
        String[] row = new String[4];
        row[0] = this.getStatus();
        row[1] = this.name;
        row[2] = this.version;
        row[3] = this.installed;
        return row;
    }

    /**
     * return a languages description if avaible, otherwise a default
     *
     * @param language
     * @return String
     */
    public String getDescription(String language) {
        String description = "<p><strong>There is currently no Description for " + name + ".</strong></p>"
                + "<p>You can help by adding one at <a href=\"http://tools.idrinth.de/addons/" + slug
                + "/\">http://tools.idrinth.de/addons/" + slug + "/</a>.</p>";
        if (descriptions.containsKey(language) && !descriptions.get(language).isEmpty()) {
            description = descriptions.get(language);
        } else if (!descriptions.get("en").isEmpty()) {
            description = descriptions.get("en");
        }
        return "<html>" + description;
    }

    /**
     * get a list of descriptions
     *
     * @return java.util.HashMap
     */
    public java.util.HashMap<String, String> getDescriptions() {
        return descriptions;
    }

    /**
     * get addon name
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * downloads a zip and unpacks it
     *
     * @throws java.lang.Exception
     */
    public void install() throws java.lang.Exception {
        (new Updater()).run(true);
    }

    /**
     * downloads a zip and unpacks it
     *
     * @throws java.lang.Exception
     */
    public void uninstall() throws java.lang.Exception {
        (new Updater()).run(false);
    }

    /**
     * starts uploading a file if so configured
     *
     * @param file
     */
    public void fileWasChanged(java.io.File file) {
        if (addonSettings.isEnabled() && file.isFile() && file.getName().equalsIgnoreCase(addonSettings.getFile())) {
            try {
                client.upload(addonSettings.getUrl(), file);
            } catch (Exception exception) {
                logger.warn(exception);
            }
        }
    }

    @Override
    public String getStatus() {
        if("-".equals(getInstalled())) {
            return " ";
        }
        try {
            com.github.zafarkhaja.semver.Version remote = com.github.zafarkhaja.semver.Version.valueOf(getVersion());
            com.github.zafarkhaja.semver.Version local = com.github.zafarkhaja.semver.Version.valueOf(getInstalled());
            if (remote.equals(local)) {
                return "✓";
            }
            if (remote.lessThan(local)) {
                return "?";
            }
        } catch (com.github.zafarkhaja.semver.ParseException e) {
            return "X";
        }
        return "X";
    }

    private class Updater {

        /**
         * deleted,updates or installs the addon
         *
         * @param redeploy
         * @throws java.lang.Exception
         */
        public void run(boolean redeploy) throws java.lang.Exception {
            uninstall();
            if (redeploy) {
                install();
            }
            addonSettings.refresh();
            addonSettings.setEnabled(false);
        }

        /**
         * removes all data of this addon from the harddrive
         */
        private void uninstall() {
            java.io.File addonFolder = new java.io.File(Config.getWARPath() + BASE_PATH + name);
            Utils.emptyFolder(addonFolder);
            addonFolder.delete();
            installed="-";
        }

        /**
         * downloads a zip for the install()-method
         *
         * @return java.io.File
         * @throws java.lang.Exception
         */
        private java.io.File getZip() throws IOException {
            java.io.File file = new java.io.File(Config.getWARPath() + BASE_PATH + slug + ".zip");
            try (java.io.InputStream stream = client.getAddonDownload(slug + "/download/" + version.replace(".", "-") + "/")) {
                org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file);
            }
            return file;
        }

        /**
         * extracts a zip downloaded for the install()-method
         *
         * @throws java.lang.Exception
         */
        private void install() throws java.lang.Exception {
            java.io.File file = getZip();
            (new net.lingala.zip4j.ZipFile(file)).extractAll(Config.getWARPath() + BASE_PATH);
            org.apache.commons.io.FileUtils.deleteQuietly(file);
            org.apache.commons.io.FileUtils.writeStringToFile(new java.io.File(Config.getWARPath() + BASE_PATH + name + VERSION_FILE), "<?xml version=\"1.0\" encoding=\"UTF-8\"?><UiMod><name>" + name + "</name><version>" + version + "</version></UiMod>");
            installed=version;
        }

    }

    private class VersionFinder {

        private final java.io.File folder;

        /**
         *
         * @param base
         */
        public VersionFinder() {
            folder = de.idrinth.waraddonclient.service.AddonFolderLocator.find(name);
        }

        /**
         * tries to find and set the usual version
         *
         * @return boolean
         */
        private boolean processDirectory() {
            for (java.io.File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()
                        && org.apache.commons.io.FilenameUtils.getExtension(fileEntry.getName()).equalsIgnoreCase("mod")) {
                    try {
                        org.w3c.dom.NodeList list = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileEntry).getElementsByTagName("UiMod");
                        installed = list.item(0).getAttributes().getNamedItem("version").getTextContent();
                        return true;
                    } catch (javax.xml.parsers.ParserConfigurationException | javax.xml.parsers.FactoryConfigurationError | org.xml.sax.SAXException | java.io.IOException exception) {
                        logger.error(exception);
                    }
                }
            }
            return false;
        }

        /**
         * tries to find and set the default version
         */
        private boolean getDownloadVersion() {
            java.io.File file = new java.io.File(folder.getPath() + VERSION_FILE);
            if (file.exists()) {
                try {
                    org.w3c.dom.NodeList list = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).getElementsByTagName("version");
                    installed = list.item(0).getTextContent().replace("(sys)", "");
                    return true;
                } catch (javax.xml.parsers.ParserConfigurationException | javax.xml.parsers.FactoryConfigurationError | org.xml.sax.SAXException | java.io.IOException exception) {
                    logger.error(exception);
                }
            }
            return false;
        }

        /**
         * starts searching for an installed version
         */
        public void run() {
            if (folder == null || !folder.exists()) {
                return;
            }
            if (!getDownloadVersion() && !processDirectory()) {
                installed = "unknown";
            }
        }
    }
}
