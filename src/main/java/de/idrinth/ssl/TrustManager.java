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

    public java.security.KeyStore keyStore;
    protected javax.net.ssl.X509TrustManager manager;

    /**
     *
     * @throws java.lang.Exception
     */
    public TrustManager() throws java.lang.Exception {
        getStore();
        addCertToStore("StartComCertificationAuthority");
        addCertToStore("StartComClass2IVServerCA");
        addCertToStore("IdrinthDe");

        javax.net.ssl.TrustManagerFactory trustManagerFactory = javax.net.ssl.TrustManagerFactory.getInstance("PKIX");
        trustManagerFactory.init(keyStore);

        for (javax.net.ssl.TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
            if (trustManager instanceof javax.net.ssl.X509TrustManager) {
                manager = (javax.net.ssl.X509TrustManager) trustManager;
                return;
            }
        }

        throw new java.lang.RuntimeException("Couldn't initialize Trustmanager due to lack of X509TrustManager");
    }

    /**
     *
     * @throws java.lang.Exception
     */
    protected final void getStore() throws java.lang.Exception {
        String fileSep = System.getProperty("file.separator");
        java.io.File file = new java.io.File(System.getProperty("sun.boot.library.path"));
        while (!(new java.io.File(file.getAbsoluteFile() + fileSep + "lib").exists())) {
            file = file.getParentFile();
        }
        keyStore = java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType());
        System.setProperty("javax.net.ssl.trustStore", java.security.KeyStore.getDefaultType());
        System.setProperty("javax.net.ssl.keyStore", java.security.KeyStore.getDefaultType());
        file = new java.io.File(file.getAbsolutePath() + fileSep + "lib" + fileSep + "security");
        keyStore.load(
                new java.io.BufferedInputStream(
                        new java.io.FileInputStream(
                                new java.io.File(
                                        file.getAbsolutePath() + fileSep + "jssecacerts"
                                ).exists()
                                        ? file.getAbsolutePath() + fileSep + "jssecacerts"
                                        : file.getAbsolutePath() + fileSep + "cacerts"
                        )
                ),
                "changeit".toCharArray()
        );
        javax.net.ssl.TrustManagerFactory trustManagerFactory = javax.net.ssl.TrustManagerFactory.getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        javax.net.ssl.TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("TLS");
        sc.init(null, trustManagers, null);
        javax.net.ssl.SSLContext.setDefault(sc);
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
    }

    /**
     *
     * @param name
     * @throws java.lang.Exception
     */
    protected final void addCertToStore(String name) throws java.lang.Exception {
        java.net.URL resource = getClass().getResource("/certificates/" + name + ".cer");
        java.io.BufferedInputStream bis = new java.io.BufferedInputStream(resource.openStream());
        java.security.cert.Certificate cert = java.security.cert.CertificateFactory.getInstance("X.509").generateCertificate(bis);
        bis.close();
        keyStore.setCertificateEntry(name, cert);
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
}
