/*
 * Copyright (C) 2016 Björn Büttner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.idrinth.waraddonclient.implementation.model;

public class Addon {

    private java.util.Hashtable<String, String> descriptions = new java.util.Hashtable();

    private String version;

    private String slug;

    private String name;

    private String installed = "-";

    private java.util.ArrayList<String> tags = new java.util.ArrayList();

    private AddonSettings addonSettings;

    private static final String basePath = "./Interface/AddOns/";

    private static final String versionFile = "/self.idrinth";

    /**
     * Initialize from a json object
     *
     * @param addon
     */
    public Addon(javax.json.JsonObject addon) {
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
        addonSettings = new AddonSettings(name);
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
    public void update(Addon addon) {
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
        String[] row = new String[3];
        row[0] = this.name;
        row[1] = this.version;
        row[2] = this.installed;
        return row;
    }

    /**
     * return a languages description if avaible, otherwise a default
     *
     * @param language
     * @return String
     */
    public String getDescription(String language) {
        if (descriptions.containsKey(language) && !descriptions.get(language).isEmpty()) {
            return "<html>" + descriptions.get(language);
        }
        if (!descriptions.get("en").isEmpty()) {
            return "<html>" + descriptions.get("en");
        }
        return "<html><p><strong>There is currently no Description for " + name + ".</strong></p>"
                + "<p>You can help by adding one at <a href=\"http://tools.idrinth.de/addons/" + slug
                + "/\">http://tools.idrinth.de/addons/" + slug + "/</a>.</p>";
    }

    /**
     * get a list of descriptions
     *
     * @return java.util.Hashtable
     */
    public java.util.Hashtable<String, String> getDescriptions() {
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
                de.idrinth.waraddonclient.factory.RemoteRequest.build().upload(addonSettings.getUrl(), file);
            } catch (Exception exception) {
                de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelWarn);
            }
        }
    }

    /**
     * empties a folder
     *
     * @param folder
     */
    private void emptyFolder(java.io.File folder) {
        if (folder == null || !folder.exists()) {
            return;
        }
        for (java.io.File file : folder.listFiles()) {
            if (file.isDirectory()) {
                emptyFolder(file);
            }
            file.delete();
        }
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
            java.io.File addonFolder = new java.io.File(basePath + name);
            emptyFolder(addonFolder);
            addonFolder.delete();
        }

        /**
         * downloads a zip for the install()-method
         *
         * @return java.io.File
         * @throws java.lang.Exception
         */
        private java.io.File getZip() throws java.lang.Exception {
            java.io.File file = new java.io.File(basePath + slug + ".zip");
            try (java.io.InputStream stream = de.idrinth.waraddonclient.factory.RemoteRequest.build().getAddonDownload(slug + "/download/" + version.replace(".", "-") + "/")) {
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
            (new net.lingala.zip4j.core.ZipFile(file)).extractAll(basePath);
            org.apache.commons.io.FileUtils.deleteQuietly(file);
            org.apache.commons.io.FileUtils.writeStringToFile(new java.io.File(basePath + name + versionFile), "<?xml version=\"1.0\" encoding=\"UTF-8\"?><UiMod><name>" + name + "</name><version>" + version + "(sys)</version></UiMod>");
        }

    }

    private class VersionFinder {

        private final java.io.File folder;

        /**
         *
         * @param base
         */
        public VersionFinder() {
            folder = new de.idrinth.waraddonclient.implementation.service.FindAddonFolder().find(name);
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
                        de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
                    }
                }
            }
            return false;
        }

        /**
         * tries to find and set the default version
         */
        private void getDownloadVersion() {
            java.io.File file = new java.io.File(folder.getPath() + versionFile);
            if (file.exists()) {
                try {
                    org.w3c.dom.NodeList list = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).getElementsByTagName("version");
                    installed = list.item(0).getTextContent();
                } catch (javax.xml.parsers.ParserConfigurationException | javax.xml.parsers.FactoryConfigurationError | org.xml.sax.SAXException | java.io.IOException exception) {
                    de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
                }
            }

        }

        /**
         * starts searching for an installed version
         */
        public void run() {
            if (folder == null || !folder.exists()) {
                return;
            }
            installed = "unknown";
            if (processDirectory()) {
                return;
            }
            getDownloadVersion();
        }
    }
}
