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

    protected String file = "";
    protected boolean enabled = false;
    protected String reason = "";
    protected String url = "";
    protected String name;
    protected boolean hasSettings = false;

    public String getFile() {
        return file;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        de.idrinth.waraddonclient.factory.User.build().setEnabled(name, enabled);
    }

    public String getReason() {
        return reason;
    }

    public String getUrl() {
        return url;
    }

    AddonSettings(String name) {
        this.name = name;
        this.enabled = de.idrinth.waraddonclient.factory.User.build().getEnabled(name);
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
        java.io.File folder = new de.idrinth.waraddonclient.implementation.service.FindAddonFolder().find(name);
        if (folder == null || !folder.exists()) {
            return;
        }
        for (java.io.File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()
                    && fileEntry.getName().equalsIgnoreCase("upload.idrinth")) {
                try {
                    org.w3c.dom.NodeList list = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileEntry).getFirstChild().getChildNodes();
                    for (int counter = 0; counter < list.getLength(); counter++) {
                        switch (list.item(counter).getNodeName().toLowerCase()) {
                            case "file":
                                file = list.item(counter).getTextContent();
                                break;
                            case "url":
                                url = list.item(counter).getTextContent();
                                break;
                            case "reason":
                                reason = list.item(counter).getTextContent();
                                break;
                        }
                    }
                    hasSettings = true;
                    return;
                } catch (javax.xml.parsers.ParserConfigurationException | javax.xml.parsers.FactoryConfigurationError | org.xml.sax.SAXException | java.io.IOException exception) {
                    de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
                }
            }
        }
    }
}
