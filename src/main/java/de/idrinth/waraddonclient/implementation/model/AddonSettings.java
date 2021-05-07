package de.idrinth.waraddonclient.implementation.model;

public class AddonSettings {

    private String file = "";

    private boolean enabled;

    private String reason = "";

    private String url = "";

    private final String name;

    private boolean hasSettings;

    /**
     *
     * @return String
     */
    public String getFile() {
        return file;
    }

    /**
     * is file upload enabled?
     *
     * @return boolean
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * activates/deactivates upload
     *
     * @param enabled
     */
    public void setEnabled(boolean enable) {
        this.enabled = enable;
        de.idrinth.waraddonclient.factory.User.build().setEnabled(name, enable);
    }

    /**
     *
     * @return String
     */
    public String getReason() {
        return reason;
    }

    /**
     *
     * @return String
     */
    public String getUrl() {
        return url;
    }

    public AddonSettings(String addon) {
        this.name = addon;
        this.enabled = de.idrinth.waraddonclient.factory.User.build().getEnabled(addon);
        refresh();
    }

    public boolean showSettings() {
        return hasSettings;
    }

    final public void refresh() {
        file = "";
        reason = "";
        url = "";
        hasSettings = false;
        java.io.File folder = de.idrinth.waraddonclient.implementation.service.AddonFolderLocator.find(name);
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
            de.idrinth.factory.Logger.build().log(exception, de.idrinth.Logger.LEVEL_ERROR);
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
