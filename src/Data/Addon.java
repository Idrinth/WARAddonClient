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
package Data;

/**
 *
 * @author Björn Büttner
 */
public class Addon {

    private String description;
    private String version;
    private String slug;
    private String name;
    private String installed = "-";
    private Service.Request request;
    private java.util.ArrayList tags = new java.util.ArrayList();
    private AddonSettings addonSettings = null;

    public Addon(javax.json.JsonObject addon, User user, Service.Request request) {
        this.request = request;
        description = getStringFromObject("description", addon);
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
        addonSettings = new AddonSettings(name, user);
    }

    public String getVersion() {
        findInstalled();
        return installed;
    }

    public final AddonSettings getUploadData() {
        return addonSettings;
    }

    protected final void findInstalled() {
        installed = "-";
        java.io.File folder = new Service.FindAddonFolder().find(name);
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
        String value = "";
        if (!data.isNull(key)) {
            value = data.getString(key);
        }
        return value;
    }

    public String[] getTableRow() {
        String[] row = new String[3];
        row[0] = this.name;
        row[1] = this.version;
        row[2] = this.installed;
        return row;
    }

    public String getDescription() {
        if (description.isEmpty()) {
            return "<html><p><strong>There is currently no Description for " + name + ".</strong></p>"
                    + "<p>You can help by adding one at <a href=\"http://tools.idrinth.de/addons/" + slug
                    + "/\">http://tools.idrinth.de/addons/" + slug + "/</a>.</p>";
        }
        return "<html>" + description;
    }

    public String getName() {
        return name;
    }

    public void install() throws java.io.IOException {
        uninstall();
        java.io.InputStream file = request.getAddonDownload(slug + "/download/" + version.replace(".", "-") + "/");
        new Externals.UnzipUtility().unzip(
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
            request.upload(addonSettings.getUrl(), file);
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
