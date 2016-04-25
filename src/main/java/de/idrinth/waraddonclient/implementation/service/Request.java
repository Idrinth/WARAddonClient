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
package de.idrinth.waraddonclient.implementation.service;

import java.security.KeyStore;

public class Request {

    private final String baseUrl = "https://tools.idrinth.de/";
    private boolean requestActive = false;
    private org.apache.http.impl.client.CloseableHttpClient client = null;

    public javax.json.JsonArray getAddonList() {
        try {
            org.apache.http.HttpResponse response = executionHandler(new org.apache.http.client.methods.HttpGet(baseUrl + "addon-api/"));
            if (response == null) {
                return null;
            }
            javax.json.JsonArray data = javax.json.Json.createReader(response.getEntity().getContent()).readArray();
            client.close();
            return data;
        } catch (java.net.MalformedURLException exeption) {
            System.out.println(exeption.getMessage());
        } catch (java.io.IOException exeption) {
            System.out.println(exeption.getMessage());
        }
        return null;
    }

    public java.io.InputStream getAddonDownload(String url) {
        try {
            org.apache.http.HttpResponse response = executionHandler(new org.apache.http.client.methods.HttpGet(baseUrl + "addons/" + url));
            if (response == null) {
                return null;
            }
            return response.getEntity().getContent();
        } catch (java.net.MalformedURLException exeption) {
            System.out.println(exeption.getMessage());
        } catch (java.io.IOException exeption) {
            System.out.println(exeption.getMessage());
        }
        return null;
    }

    protected synchronized org.apache.http.HttpResponse executionHandler(org.apache.http.client.methods.HttpRequestBase uri) {
        uri.setConfig(org.apache.http.client.config.RequestConfig.DEFAULT);
        uri.setHeader("User-Agent", "IdrinthAddonClient/" + de.idrinth.waraddonclient.factory.Version.build().getLocalVersion());
        uri.setHeader("Cache-Control", "no-cache");
        while (requestActive) {
            try {
                Thread.sleep(150);
            } catch (java.lang.InterruptedException exception) {
                //don't care
            }
        }
        try {
            de.idrinth.ssl.TrustManager manager = new de.idrinth.ssl.TrustManager();
            client = org.apache.http.impl.client.HttpClientBuilder.create()
                    .useSystemProperties()
                    .setSSLContext(
                            org.apache.http.ssl.SSLContextBuilder.create().loadTrustMaterial(
                                    manager.keyStore,
                                    manager
                            ).build()
                    )
                    .build();
        } catch (Exception e) {
        }
        requestActive = true;
        org.apache.http.HttpResponse response = null;
        try {
            response = client.execute(uri);
            if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299) {
                response = null;
            }
        } catch (java.io.IOException exception) {
            System.out.println(exception.getMessage());
        }
        requestActive = false;
        return response;
    }

    public boolean upload(String url, java.io.File file) {
        org.apache.http.client.methods.HttpPost request = new org.apache.http.client.methods.HttpPost(url);
        request.setEntity(new org.apache.http.entity.FileEntity(file));
        boolean wasSuccess = executionHandler(request) == null ? false : true;
        try {
            client.close();
        } catch (java.io.IOException exception) {
            System.out.println(exception.getMessage());
        }
        return wasSuccess;
    }

    public String getVersion() {
        org.apache.http.client.methods.HttpGet request = new org.apache.http.client.methods.HttpGet("https://api.github.com/repos/Idrinth/WARAddonClient/releases/latest");
        org.apache.http.HttpResponse response = executionHandler(request);
        String version = "";
        try {
            if (response != null) {
                javax.json.JsonObject data = javax.json.Json.createReader(response.getEntity().getContent()).readObject();
                version = data.getString("tag_name");
            }
            client.close();
        } catch (java.io.IOException exception) {
            System.out.println(exception.getMessage());
        }
        return version;
    }
}
