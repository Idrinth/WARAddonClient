package de.idrinth.waraddonclient;

import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.model.TrustManager;
import de.idrinth.waraddonclient.service.FileLogger;
import de.idrinth.waraddonclient.service.FileSystem;
import de.idrinth.waraddonclient.service.Request;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.xml.parsers.ParserConfigurationException;

abstract class BaseMain {
    protected abstract void main(FileLogger logger, Config config, Request client, FileSystem file) throws FileSystem.FileSystemException, ParserConfigurationException, IOException;
    public void run() throws CertificateException, IOException, URISyntaxException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, FileSystem.FileSystemException, ParserConfigurationException {
        Config config = new Config();
        FileLogger logger = new FileLogger(config.getLogFile());
        Request client = new Request(new TrustManager(logger), logger, config);
        FileSystem file = new FileSystem(config);
        this.main(logger, config, client, file);
    }
    public abstract void error(Exception ex);
}
