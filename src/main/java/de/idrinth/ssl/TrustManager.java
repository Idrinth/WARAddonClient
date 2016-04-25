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

import java.io.BufferedInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class TrustManager implements org.apache.http.ssl.TrustStrategy {

    public KeyStore keyStore;
    protected javax.net.ssl.X509TrustManager manager;

    public TrustManager() throws Exception {
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

        throw new Exception("Couldn't initialize");
    }

    protected final void getStore() {
        String fileSep = System.getProperty("file.separator");
        java.io.File file = new java.io.File(System.getProperty("sun.boot.library.path"));
        while (new java.io.File(file.getAbsoluteFile() + fileSep + "lib").exists() != true) {
            file = file.getParentFile();
        }
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            System.setProperty("javax.net.ssl.trustStore", KeyStore.getDefaultType());
            System.setProperty("javax.net.ssl.keyStore", KeyStore.getDefaultType());
            try {
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
            } catch (Exception e) {
                keyStore.load(null);//Make an empty store
            }
            javax.net.ssl.TrustManagerFactory trustManagerFactory = javax.net.ssl.TrustManagerFactory.getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            javax.net.ssl.TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("TLS");
            sc.init(null, trustManagers, null);
            javax.net.ssl.SSLContext.setDefault(sc);
            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        } catch (java.io.IOException | java.security.GeneralSecurityException exception) {
            System.out.println(exception.getMessage());
        }
    }

    protected final void addCertToStore(String name) {
        try {
            java.net.URL resource = getClass().getResource("/certificates/" + name + ".cer");
            BufferedInputStream bis = new BufferedInputStream(resource.openStream());
            Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(bis);
            bis.close();
            keyStore.setCertificateEntry(name, cert);
        } catch (java.io.IOException | java.security.cert.CertificateException | java.security.KeyStoreException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) {
        try {
            manager.checkServerTrusted(chain, authType);
            return true;
        } catch (CertificateException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
