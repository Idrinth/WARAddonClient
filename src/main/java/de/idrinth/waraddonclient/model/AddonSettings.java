package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.Config;
import de.idrinth.waraddonclient.service.FileLogger;

public class AddonSettings {

    private String file = "";

    private boolean enabled;

    private String reason = "";

    private String url = "";

    private final String name;

    private boolean hasSettings;
    
    private final FileLogger logger;

    public String getFile() {
        return file;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enable) {
        this.enabled = enable;
        Config.setEnabled(name, enable);
    }

    public String getReason() {
        return reason;
    }

    public String getUrl() {
        return url;
    }

    public AddonSettings(String addon, FileLogger logger) {
        this.name = addon;
        this.logger = logger;
        this.enabled = Config.isEnabled(addon);
        refresh();
    }

    public boolean showSettings() {
        return hasSettings;
    }

    public final void refresh() {
        file = "";
        reason = "";
        url = "";
        hasSettings = false;
        java.io.File folder = de.idrinth.waraddonclient.service.AddonFolderLocator.find(name);
        if (folder == null || !folder.exists()) {
            return;
        }
        for (java.io.File fileEntry : folder.listFiles()) {
            processFile(fileEntry);
            if (hasSettings) {
                return;
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
            org.w3c.dom.NodeList list = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileEntry).getFirstChild().getChildNodes();
            for (int counter = 0; counter < list.getLength(); counter++) {
                processNode(list.item(counter));
            }
            hasSettings = true;
        } catch (javax.xml.parsers.ParserConfigurationException | javax.xml.parsers.FactoryConfigurationError | org.xml.sax.SAXException | java.io.IOException exception) {
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
}
