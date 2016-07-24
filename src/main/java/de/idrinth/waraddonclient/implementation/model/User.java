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

    /**
     *
     */
    public User() {
        java.io.File file = new java.io.File("./idrinth.xml");
        try {
            if (file.exists()) {
                xml = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            }
        } catch (java.io.IOException | javax.xml.parsers.ParserConfigurationException | org.xml.sax.SAXException exception) {
            de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
        }
        try {
            if (xml == null) {
                xml = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                xml.appendChild(xml.createElement("AddOns"));
            }
        } catch (javax.xml.parsers.ParserConfigurationException exception) {
            de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
        }
    }

    /**
     * may data be uploaded?
     *
     * @param name
     * @return boolean
     */
    public boolean getEnabled(String name) {
        org.w3c.dom.NodeList list = xml.getElementsByTagName("AddOn");
        for (int counter = 0; counter < list.getLength(); counter++) {
            if (name.equalsIgnoreCase(list.item(counter).getTextContent())) {
                return list.item(counter).getAttributes().item(0).getTextContent().equalsIgnoreCase("true");
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
                    new javax.xml.transform.stream.StreamResult(new java.io.File("./idrinth.xml"))
            );
        } catch (javax.xml.transform.TransformerException exception) {
            de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
        }
    }

    /**
     * Set the enabled flag to signify that an addon may upload data
     *
     * @param name
     * @param isEnabled
     */
    public void setEnabled(String name, boolean isEnabled) {
        org.w3c.dom.NodeList list = xml.getElementsByTagName("AddOn");
        for (int counter = 0; counter < list.getLength(); counter++) {
            if (name.equalsIgnoreCase(list.item(counter).getTextContent())) {
                list.item(counter).getAttributes().item(0).setNodeValue(isEnabled ? "true" : "false");
                writeDocument();
                return;
            }
        }
        org.w3c.dom.Element node = xml.createElement("AddOn");
        node.setAttribute("enabled", isEnabled ? "true" : "false");
        node.setTextContent(name);
        xml.getFirstChild().appendChild(node);
        writeDocument();
    }
}
