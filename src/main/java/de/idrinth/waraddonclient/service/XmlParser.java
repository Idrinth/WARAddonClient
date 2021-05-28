package de.idrinth.waraddonclient.service;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public class XmlParser {

    private final DocumentBuilder builder;

    public XmlParser() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        factory.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        builder = factory.newDocumentBuilder();
    }

    public Document parse(File input) throws SAXException, IOException {
        return builder.parse(input);
    }

    public Document parse(File input, ErrorHandler handler) throws SAXException, IOException {
        builder.setErrorHandler(handler);
        return builder.parse(input);
    }
}
