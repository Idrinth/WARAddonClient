package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.service.logger.BaseLogger;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.http.ssl.TrustStrategy;

public class TrustManager implements TrustStrategy {

    private java.security.KeyStore keyStore;

    private final javax.net.ssl.X509TrustManager manager;
    
    private final BaseLogger logger;

    public TrustManager(BaseLogger logger) throws CertificateException, KeyManagementException, KeyStoreException, IOException, NoSuchAlgorithmException {
        this.logger = logger;
        getStore();
        //tools.idrinth.de
        addCertToStore("DST Root CA X3");
        addCertToStore("Let's Encrypt Authority X1");
        //imgur
        addCertToStore("DigiCert");
        addCertToStore("DigiCert SHA2 Secure Server CA");

        javax.net.ssl.TrustManagerFactory factory = javax.net.ssl.TrustManagerFactory.getInstance("PKIX");
        factory.init(keyStore);

        for (javax.net.ssl.TrustManager trustManager : factory.getTrustManagers()) {
            if (trustManager instanceof javax.net.ssl.X509TrustManager) {
                manager = (javax.net.ssl.X509TrustManager) trustManager;
                return;
            }
        }

        throw new KeyStoreException("Couldn't initialize Trustmanager due to lack of X509TrustManager");
    }

    public final java.security.KeyStore getKeyStore() {
        return keyStore;
    }

    private void getStore() throws KeyManagementException, KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        String password = "changeit";
        keyStore = new KeystoreFinder().getKeystore(password);
        javax.net.ssl.TrustManagerFactory trustManagerFactory = javax.net.ssl.TrustManagerFactory.getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        javax.net.ssl.TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, trustManagers, null);
        javax.net.ssl.SSLContext.setDefault(sslContext);
        System.setProperty("javax.net.ssl.trustStorePassword", password);
    }

    private void addCertToStore(String name) throws IOException, KeyStoreException {
        java.net.URL resource = getClass().getResource("/certificates/" + name + ".cer");
        try {
            assert resource != null;
            try (java.io.BufferedInputStream bis = new java.io.BufferedInputStream(resource.openStream())) {
                java.security.cert.Certificate cert = java.security.cert.CertificateFactory.getInstance("X.509").generateCertificate(bis);
                keyStore.setCertificateEntry(name, cert);
            }
        } catch(CertificateException e) {
            logger.error(e);
        }
    }

    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) {
        try {
            manager.checkServerTrusted(chain, authType);
            return true;
        } catch (CertificateException e) {
            logger.warn(e);
        }
        return false;
    }

    static class KeystoreFinder {

        private final String fileSeperator;

        private static final String PREFERED_CERTIFICATES = "jssecacerts";

        private static final String ALTERNATE_CERTIFICATES = "cacerts";

        public KeystoreFinder() {
            fileSeperator = System.getProperty("file.separator");
        }

        public java.security.KeyStore getKeystore(String password) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
            java.security.KeyStore store = java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType());
            System.setProperty("javax.net.ssl.trustStore", java.security.KeyStore.getDefaultType());
            System.setProperty("javax.net.ssl.keyStore", java.security.KeyStore.getDefaultType());
            store.load(
                    new java.io.BufferedInputStream(
                            new java.io.FileInputStream(fileForKeystore())
                    ),
                    password.toCharArray()
            );
            return store;
        }

        private String fileForKeystore() {
            String path = findStoreFolder().getAbsolutePath() + fileSeperator;
            String prefered = path + PREFERED_CERTIFICATES;
            String alternative = path + ALTERNATE_CERTIFICATES;
            return new java.io.File(prefered).exists() ? prefered : alternative;
        }

        private java.io.File findStoreFolder() {
            String[] folders = "lib/security".split("/");
            java.io.File file = new java.io.File(System.getProperty("sun.boot.library.path"));
            while (!(new java.io.File(file.getAbsoluteFile() + fileSeperator + folders[0]).exists())) {
                file = file.getParentFile();
            }
            return new java.io.File(file.getAbsolutePath() + fileSeperator + folders[0] + fileSeperator + folders[1]);
        }
    }
}
