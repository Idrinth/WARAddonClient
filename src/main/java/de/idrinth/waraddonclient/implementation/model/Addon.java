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
    private AddonSettings addonSettings = null;

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
        findInstalled();
        addonSettings = new AddonSettings(name);
    }

    public java.util.ArrayList<String> getTags() {
        return tags;
    }

    public boolean hasTag(String tag) {
        for (String hasTag : tags) {
            if (tag.equalsIgnoreCase(hasTag)) {
                return true;
            }
        }
        return false;
    }

    public void update(Addon addon) {
        version = addon.getVersion();
        tags = addon.getTags();
        descriptions = addon.getDescriptions();
    }

    public String getVersion() {
        return version;
    }

    public String getInstalled() {
        findInstalled();
        return installed;
    }

    public final AddonSettings getUploadData() {
        return addonSettings;
    }

    protected final void findInstalled() {
        installed = "-";
        java.io.File folder = new de.idrinth.waraddonclient.implementation.service.FindAddonFolder().find(name);
        if (folder == null || !folder.exists()) {
            return;
        }
        for (java.io.File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()
                    && org.apache.commons.io.FilenameUtils.getExtension(fileEntry.getName()).equalsIgnoreCase("mod")) {
                try {
                    installed = "unknown";
                    org.w3c.dom.NodeList list = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileEntry).getElementsByTagName("UiMod");
                    for (int counter = 0; counter < list.getLength(); counter++) {
                        this.installed = list.item(counter).getAttributes().getNamedItem("version").getTextContent();
                        return;
                    }
                } catch (javax.xml.parsers.ParserConfigurationException | javax.xml.parsers.FactoryConfigurationError | org.xml.sax.SAXException | java.io.IOException exception) {
                    de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
                }
            }
        }
        java.io.File file = new java.io.File(folder.getPath() + "/self.idrinth");
        if (file.exists()) {
            try {
                org.w3c.dom.NodeList list = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).getElementsByTagName("version");
                this.installed = list.item(0).getTextContent();
            } catch (javax.xml.parsers.ParserConfigurationException | javax.xml.parsers.FactoryConfigurationError | org.xml.sax.SAXException | java.io.IOException exception) {
                de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
            }
        }
    }

    protected final String getStringFromObject(String key, javax.json.JsonObject data) {
        if (key != null && data != null && data.containsKey(key) && !data.isNull(key)) {
            return java.util.regex.Pattern.compile("^\"|\"$").matcher(data.get(key).toString()).replaceAll("");
        }
        return "";
    }

    public String[] getTableRow() {
        String[] row = new String[3];
        row[0] = this.name;
        row[1] = this.version;
        row[2] = this.installed;
        return row;
    }

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

    public java.util.Hashtable<String, String> getDescriptions() {
        return descriptions;
    }

    public String getName() {
        return name;
    }

    private java.io.File getZip() throws java.lang.Exception {
        java.io.File file = new java.io.File("./Interface/AddOns/" + slug + ".zip");
        java.io.InputStream stream = de.idrinth.waraddonclient.factory.RemoteRequest.build().getAddonDownload(slug + "/download/" + version.replace(".", "-") + "/");
        org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file);
        stream.close();
        return file;
    }

    private void extractZip() throws java.lang.Exception {
        java.io.File file = getZip();
        (new net.lingala.zip4j.core.ZipFile(file)).extractAll("./Interface/AddOns/");
        org.apache.commons.io.FileUtils.deleteQuietly(file);
        org.apache.commons.io.FileUtils.writeStringToFile(new java.io.File("./Interface/AddOns/" + name + "/self.idrinth"), "<?xml version=\"1.0\" encoding=\"UTF-8\"?><UiMod><name>" + name + "</name><version>" + version + "(sys)</version></UiMod>");
    }

    public void install() throws java.lang.Exception {
        uninstall();
        extractZip();
        if (installed.equals("-")) {
            addonSettings.refresh();
        }
    }

    public void uninstall() {
        java.io.File addonFolder = new java.io.File("./Interface/AddOns/" + name);
        emptyFolder(addonFolder);
        addonFolder.delete();
        addonSettings.refresh();
        addonSettings.setEnabled(false);
    }

    public void fileWasChanged(java.io.File file) {
        if (addonSettings.isEnabled() && file.isFile() && file.getName().equalsIgnoreCase(addonSettings.getFile())) {
            try {
                de.idrinth.waraddonclient.factory.RemoteRequest.build().upload(addonSettings.getUrl(), file);
            } catch (Exception exception) {
                de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelWarn);
            }
        }
    }

    protected void emptyFolder(java.io.File folder) {
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
}
