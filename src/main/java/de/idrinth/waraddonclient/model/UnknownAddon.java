package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.Utils;
import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.service.BaseLogger;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.FactoryConfigurationError;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UnknownAddon implements Addon {
    
    private boolean hasSettings = false;

    private String file = "";

    private String reason = "";

    private String url = "";

    private String name;

    private String installed = "-";

    private final Request client;
    
    private final BaseLogger logger;
    
    private final XmlParser parser;
    
    private final Config config;
    
    private final File folder;
    
    public UnknownAddon(File folder, Request client, BaseLogger logger, XmlParser parser, Config config) throws InvalidArgumentException {
        this.client = client;
        this.logger = logger;
        this.parser = parser;
        this.config = config;
        this.folder = folder;
        if (new File(folder.getAbsolutePath() + config.getVersionFile()).exists()) {
            throw new InvalidArgumentException("Folder is known Add-On folder.");
        }
        for (java.io.File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()
                    && FilenameUtils.getExtension(fileEntry.getName()).equalsIgnoreCase("mod")) {
                try {
                    NodeList list = parser.parse(fileEntry).getElementsByTagName("UiMod");
                    installed = list.item(0).getAttributes().getNamedItem("version").getTextContent();
                    name = list.item(0).getAttributes().getNamedItem("name").getTextContent();
                } catch (FactoryConfigurationError | SAXException | IOException exception) {
                    logger.error(exception);
                }
            }
        }
        if (name == null) {
            throw new InvalidArgumentException("Folder is no Add-On folder.");
        }
        refresh();
    }

    @Override
    public ArrayList<String> getTags() {
        ArrayList <String> list = new ArrayList<>();
        list.add("untagged");
        return list;
    }

    /**
     * does this addon have the given tag?
     *
     * @param tag
     * @return boolean
     */
    public boolean hasTag(String tag) {
        return false;
    }

    public String getVersion() {
        return "unknown";
    }

    public String getInstalled() {
        return installed;
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
        row[2] = "";
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
        return "<p><strong>There is currently no Description for " + name + ".</strong></p>"
                + "<p>You can help by adding the addon and one at <a href=\"http://tools.idrinth.de/addons/\">http://tools.idrinth.de/addons/</a>.</p>";
    }

    /**
     * get a list of descriptions
     *
     * @return java.util.HashMap
     */
    public HashMap<String, String> getDescriptions() {
        return new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void uninstall() throws IOException {
        Utils.deleteFolder(folder);
        installed="-";
    }

    public void fileWasChanged(File changedFile) {
        if (config.isEnabled(name) && changedFile.isFile() && changedFile.getName().equalsIgnoreCase(file)) {
            try {
                client.upload(url, changedFile);
            } catch (Exception exception) {
                logger.warn(exception);
            }
        }
    }

    @Override
    public String getStatus() {
        return "?";
    }
    
    public boolean showSettings() {
        return hasSettings;
    }

    public final void refresh() {
        file = "";
        reason = "";
        url = "";
        hasSettings = false;
        if (folder.exists()) {
            for (File fileEntry : folder.listFiles()) {
                processFile(fileEntry);
                if (hasSettings) {
                    return;
                }
            }
        }
    }

    private void processFile(java.io.File fileEntry) {
        if (fileEntry.isDirectory()) {
            return;
        }
        if (!"upload.idrinth".equalsIgnoreCase(fileEntry.getName())) {
            return;
        }
        try {
            NodeList list = parser.parse(fileEntry).getFirstChild().getChildNodes();
            for (int counter = 0; counter < list.getLength(); counter++) {
                processNode(list.item(counter));
            }
            hasSettings = true;
        } catch (FactoryConfigurationError | SAXException | IOException exception) {
            logger.error(exception);
        }
    }

    private void processNode(org.w3c.dom.Node item) {
        if ("file".equalsIgnoreCase(item.getNodeName())) {
            file = item.getTextContent();
            return;
        }
        if ("url".equalsIgnoreCase(item.getNodeName())) {
            url = item.getTextContent();
            return;
        }
        if ("reason".equalsIgnoreCase(item.getNodeName())) {
            reason = item.getTextContent();
        }
    }
    @Override
    public String getFile() {
        return file;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void install() throws IOException {
        throw new UnsupportedOperationException("You can't install an unknown Add-On.");
    }
}
