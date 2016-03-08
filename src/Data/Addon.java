/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 *
 * @author BJ
 */
public class Addon {

    private String description;
    private String version;
    private String slug;
    private String name;
    private String installed = "-";
    private Web.Request request;
    private java.util.ArrayList tags = new java.util.ArrayList();
    private AddonSettings addonSettings=null;

    public Addon(javax.json.JsonObject addon,User user,Web.Request request) {
        this.request=request;
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
        addonSettings = new AddonSettings(name,user);
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
        if(description.isEmpty()) {
            return "<html><p><strong>There is currently no Description for "+name+".</strong></p>"
                    +"<p>You can help by adding one at <a href=\"http://tools.idrinth.de/addons/"+slug
                    +"/\">http://tools.idrinth.de/addons/"+slug+"/</a>.</p>";
        }
        return "<html>" + description;
    }

    public String getName() {
        return name;
    }

    public void install() throws java.io.IOException {
        uninstall();
        java.io.InputStream file = request.getAddonDownload(slug + "/download/" + version.replace(".", "-") + "/");
        new Service.UnzipUtility().unzip(
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
        if(installed.equals("-")) {
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
        if(addonSettings.isEnabled()&&file.isFile()&&file.getName().equalsIgnoreCase(addonSettings.getFile())) {
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
