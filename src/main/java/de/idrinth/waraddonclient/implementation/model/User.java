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

public class User {

    private org.w3c.dom.Document xml;

    private final String xmlPath = "./idrinth.xml";

    private final String addonTag = "AddOn";

    private final String enabledAttribute = "enabled";

    /**
     *
     */
    public User() {
        java.io.File file = new java.io.File(xmlPath);
        try {
            if (file.exists()) {
                xml = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            }
        } catch (java.io.IOException | javax.xml.parsers.ParserConfigurationException | org.xml.sax.SAXException exception) {
            de.idrinth.factory.Logger.build().log(exception, de.idrinth.Logger.LEVEL_ERROR);
        }
        try {
            if (xml == null) {
                xml = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                xml.appendChild(xml.createElement(addonTag + "s"));
            }
        } catch (javax.xml.parsers.ParserConfigurationException exception) {
            de.idrinth.factory.Logger.build().log(exception, de.idrinth.Logger.LEVEL_ERROR);
        }
    }

    /**
     * may data be uploaded?
     *
     * @param name
     * @return boolean
     */
    public boolean getEnabled(String name) {
        org.w3c.dom.NodeList list = xml.getElementsByTagName(addonTag);
        for (int counter = 0; counter < list.getLength(); counter++) {
            if (name.equalsIgnoreCase(list.item(counter).getTextContent())) {
                return java.lang.Boolean.parseBoolean(list.item(counter).getAttributes().getNamedItem(enabledAttribute).getTextContent());
            }
        }
        return false;
    }

    /**
     * writes an xml for configuration
     */
    private void writeDocument() {
        try {
            javax.xml.transform.TransformerFactory.newInstance().newTransformer().transform(
                    new javax.xml.transform.dom.DOMSource(xml),
                    new javax.xml.transform.stream.StreamResult(new java.io.File(xmlPath))
            );
        } catch (javax.xml.transform.TransformerException exception) {
            de.idrinth.factory.Logger.build().log(exception, de.idrinth.Logger.LEVEL_ERROR);
        }
    }

    /**
     * Set the enabled flag to signify that an addon may upload data
     *
     * @param name
     * @param isEnabled
     */
    public void setEnabled(String name, boolean isEnabled) {
        setEnabled(name, java.lang.Boolean.toString(isEnabled));
    }

    /**
     * Set the enabled flag to signify that an addon may upload data
     *
     * @param name
     * @param isEnabled
     */
    private void setEnabled(String name, String isEnabled) {
        org.w3c.dom.NodeList list = xml.getElementsByTagName(addonTag);
        for (int counter = 0; counter < list.getLength(); counter++) {
            if (name.equalsIgnoreCase(list.item(counter).getTextContent())) {
                list.item(counter).getAttributes().getNamedItem(enabledAttribute).setNodeValue(isEnabled);
                writeDocument();
                return;
            }
        }
        org.w3c.dom.Element node = xml.createElement(addonTag);
        node.setAttribute(enabledAttribute, isEnabled);
        node.setTextContent(name);
        xml.getFirstChild().appendChild(node);
        writeDocument();
    }
}
