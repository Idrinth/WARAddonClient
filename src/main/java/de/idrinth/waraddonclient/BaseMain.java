package de.idrinth.waraddonclient;

import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.model.TrustManager;
import de.idrinth.waraddonclient.service.logger.BaseLogger;
import de.idrinth.waraddonclient.service.logger.FileLogger;
import de.idrinth.waraddonclient.service.FileSystem;
import de.idrinth.waraddonclient.service.logger.MultiLogger;
import de.idrinth.waraddonclient.service.Request;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.xml.parsers.ParserConfigurationException;

abstract class BaseMain {
    private final MultiLogger multiLogger = new MultiLogger();

    protected final void add(BaseLogger baseLogger) {
        multiLogger.add(baseLogger);
    }

    protected abstract void main(MultiLogger logger, Config config, Request client, FileSystem file) throws FileSystem.FileSystemException, ParserConfigurationException, IOException;

    public void run() {
        try {
            Config config = new Config();
            getLog(config);
            Request client = new Request(new TrustManager(multiLogger), multiLogger, config);
            FileSystem file = new FileSystem(config);
            this.main(multiLogger, config, client, file);
        } catch (ParserConfigurationException|FileSystem.FileSystemException|IOException|CertificateException|KeyManagementException|KeyStoreException|NoSuchAlgorithmException|URISyntaxException ex) {
            multiLogger.error(ex);
        }
    }

    public void getLog(Config cfg) throws IOException {
        try {
            add(new FileLogger(cfg.getLogFile()));
        } catch (IOException ex) {
            throw new IOException("Error with logging in " + cfg.getLogFile() + ": " + ex);
        }
    }
}