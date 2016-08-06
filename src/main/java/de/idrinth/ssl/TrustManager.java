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
package de.idrinth.ssl;

public class TrustManager implements org.apache.http.ssl.TrustStrategy {

    private java.security.KeyStore keyStore;

    private javax.net.ssl.X509TrustManager manager;

    /**
     *
     * @throws java.lang.Exception
     */
    public TrustManager() throws java.lang.Exception {
        getStore();
        addCertToStore("StartComCertificationAuthority");
        addCertToStore("StartComClass2IVServerCA");
        addCertToStore("IdrinthDe");

        javax.net.ssl.TrustManagerFactory factory = javax.net.ssl.TrustManagerFactory.getInstance("PKIX");
        factory.init(keyStore);

        for (javax.net.ssl.TrustManager trustManager : factory.getTrustManagers()) {
            if (trustManager instanceof javax.net.ssl.X509TrustManager) {
                manager = (javax.net.ssl.X509TrustManager) trustManager;
                return;
            }
        }

        throw new java.lang.RuntimeException("Couldn't initialize Trustmanager due to lack of X509TrustManager");
    }

    /**
     *
     * @return java.security.KeyStore
     */
    public final java.security.KeyStore getKeyStore() {
        return keyStore;
    }

    /**
     *
     * @throws java.lang.Exception
     */
    private final void getStore() throws java.lang.Exception {
        String password = "changeit";
        keyStore = new KeystoreFinder().getKeystore(password);
        javax.net.ssl.TrustManagerFactory trustManagerFactory = javax.net.ssl.TrustManagerFactory.getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        javax.net.ssl.TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);
        javax.net.ssl.SSLContext.setDefault(sslContext);
        System.setProperty("javax.net.ssl.trustStorePassword", password);
    }

    /**
     *
     * @param name
     * @throws java.lang.Exception
     */
    private final void addCertToStore(String name) throws java.lang.Exception {
        java.net.URL resource = getClass().getResource("/certificates/" + name + ".cer");
        try (java.io.BufferedInputStream bis = new java.io.BufferedInputStream(resource.openStream())) {
            java.security.cert.Certificate cert = java.security.cert.CertificateFactory.getInstance("X.509").generateCertificate(bis);
            keyStore.setCertificateEntry(name, cert);
        }
    }

    /**
     *
     * @param chain
     * @param authType
     * @return boolean
     */
    @Override
    public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        try {
            manager.checkServerTrusted(chain, authType);
            return true;
        } catch (java.security.cert.CertificateException e) {
            de.idrinth.factory.Logger.build().log(e.getMessage(), de.idrinth.Logger.levelWarn);
        }
        return false;
    }

    class KeystoreFinder {

        public java.security.KeyStore getKeystore(String password) throws java.lang.Exception {
            java.security.KeyStore keyStore = java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType());
            System.setProperty("javax.net.ssl.trustStore", java.security.KeyStore.getDefaultType());
            System.setProperty("javax.net.ssl.keyStore", java.security.KeyStore.getDefaultType());
            keyStore.load(
                    new java.io.BufferedInputStream(
                            new java.io.FileInputStream(fileForKeystore())
                    ),
                    password.toCharArray()
            );
            return keyStore;
        }

        private String fileForKeystore() {
            String path = findStoreFolder().getAbsolutePath() + System.getProperty("file.separator");
            String prefered = path + "jssecacerts";
            String alternative = path + "cacerts";
            return new java.io.File(prefered).exists() ? prefered : alternative;
        }

        private java.io.File findStoreFolder() {
            String[] folders = "lib/security".split("/");
            String fileSep = System.getProperty("file.separator");
            java.io.File file = new java.io.File(System.getProperty("sun.boot.library.path"));
            while (!(new java.io.File(file.getAbsoluteFile() + fileSep + folders[0]).exists())) {
                file = file.getParentFile();
            }
            return new java.io.File(file.getAbsolutePath() + fileSep + folders[0] + fileSep + folders[1]);
        }
    }
}
