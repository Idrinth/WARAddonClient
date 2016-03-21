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
                    System.out.println(exception.getMessage());
                }
            }
        }
        java.io.File file = new java.io.File(folder.getPath() + "/self.idrinth");
        if (file.exists()) {
            try {
                org.w3c.dom.NodeList list = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).getElementsByTagName("version");
                for (int counter = 0; counter < list.getLength(); counter++) {
                    this.installed = list.item(counter).getTextContent();
                    return;
                }
            } catch (javax.xml.parsers.ParserConfigurationException | javax.xml.parsers.FactoryConfigurationError | org.xml.sax.SAXException | java.io.IOException exception) {
                System.out.println(exception.getMessage());
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

    public void install() throws java.io.IOException {
        uninstall();
        java.io.InputStream file = de.idrinth.waraddonclient.factory.RemoteRequest.build().getAddonDownload(slug + "/download/" + version.replace(".", "-") + "/");
        new net.codejava.utility.Unzip().unzip(
                file,
                "./Interface/AddOns/");
        file.close();
        java.io.FileWriter versionWriter = new java.io.FileWriter("./Interface/AddOns/" + name + "/self.idrinth");
        versionWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<UiMod>\n"
                + "    <name>" + name + "</name>\n"
                + "    <version>" + version + "(sys)</version>\n"
                + "</UiMod>");
        versionWriter.close();
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
            de.idrinth.waraddonclient.factory.RemoteRequest.build().upload(addonSettings.getUrl(), file);
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
