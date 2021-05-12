package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.service.FileLogger;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AddonSettings {

    private String file = "";

    private String reason = "";

    private String url = "";

    private final String name;

    private boolean hasSettings;
    
    private final FileLogger logger;
    
    private final XmlParser parser;

    public String getFile() {
        return file;
    }

    public String getReason() {
        return reason;
    }

    public String getUrl() {
        return url;
    }

    public AddonSettings(String addon, FileLogger logger, XmlParser parser) {
        this.name = addon;
        this.logger = logger;
        this.parser = parser;
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
}
