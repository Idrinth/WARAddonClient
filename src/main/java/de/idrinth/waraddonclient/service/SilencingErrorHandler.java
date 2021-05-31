package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.service.logger.BaseLogger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SilencingErrorHandler implements ErrorHandler {
    private final BaseLogger logger;

    public SilencingErrorHandler(BaseLogger logger) {
        this.logger = logger;
    }

    @Override
    public void warning(SAXParseException exception) {
        logger.info(exception);
    }

    @Override
    public void error(SAXParseException exception) {
        logger.warn(exception);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        throw new SAXException(exception);
    }
}
