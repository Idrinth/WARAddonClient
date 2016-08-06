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

public class AddonSettings {

    private String file = "";

    private boolean enabled;

    private String reason = "";

    private String url = "";

    private String name;

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
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        de.idrinth.waraddonclient.factory.User.build().setEnabled(name, enabled);
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

    /**
     * initialises from addon-name
     *
     * @param name
     */
    public AddonSettings(String name) {
        this.name = name;
        this.enabled = de.idrinth.waraddonclient.factory.User.build().getEnabled(name);
        refresh();
    }

    /**
     *
     * @return boolean
     */
    public boolean showSettings() {
        return hasSettings;
    }

    /**
     * refres
     */
    final public void refresh() {
        file = "";
        reason = "";
        url = "";
        hasSettings = false;
        java.io.File folder = de.idrinth.waraddonclient.implementation.service.FindAddonFolder.find(name);
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

    /**
     *
     * @param fileEntry
     */
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
            de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
        }
    }

    /**
     * checks if the node has any information that is useful
     *
     * @param item
     */
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
