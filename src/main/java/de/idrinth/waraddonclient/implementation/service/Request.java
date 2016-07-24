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

public class Request {

    private final String baseUrl = "https://tools.idrinth.de/";
    private volatile boolean requestActive = false;
    private org.apache.http.impl.client.CloseableHttpClient client = null;
    private javax.net.ssl.SSLContext sslContext = null;

    /**
     * Throws an exception if there's issues with the ssl-certificates
     *
     * @throws java.lang.Exception
     */
    public Request() throws java.lang.Exception {
        de.idrinth.ssl.TrustManager manager = new de.idrinth.ssl.TrustManager();
        sslContext = org.apache.http.ssl.SSLContextBuilder.create().loadTrustMaterial(
                manager.keyStore,
                manager
        ).build();
    }

    /**
     * gets a list of avaible addons from the website's api
     *
     * @return javax.json.JsonArray
     * @throws java.lang.Exception
     */
    public javax.json.JsonArray getAddonList() throws java.lang.Exception {
        org.apache.http.HttpResponse response = executionHandler(new org.apache.http.client.methods.HttpGet(baseUrl + "addon-api/"));
        javax.json.JsonArray data = javax.json.Json.createReader(response.getEntity().getContent()).readArray();
        client.close();
        return data;
    }

    /**
     * downloads an addon-zip
     *
     * @param url
     * @return java.io.InputStream
     * @throws java.lang.Exception
     */
    public java.io.InputStream getAddonDownload(String url) throws java.lang.Exception {
        org.apache.http.HttpResponse response = executionHandler(new org.apache.http.client.methods.HttpGet(baseUrl + "addons/" + url));
        return response.getEntity().getContent();
    }

    /**
     * requests data from an url
     *
     * @param uri
     * @return org.apache.http.HttpResponse
     * @throws java.lang.Exception
     */
    protected synchronized org.apache.http.HttpResponse executionHandler(org.apache.http.client.methods.HttpRequestBase uri) throws java.lang.Exception {
        uri.setConfig(org.apache.http.client.config.RequestConfig.DEFAULT);
        uri.setHeader("User-Agent", "IdrinthAddonClient/" + de.idrinth.waraddonclient.configuration.Version.getLocalVersion());
        uri.setHeader("Cache-Control", "no-cache");
        while (requestActive) {
            de.idrinth.waraddonclient.implementation.service.Sleeper.sleep(150);
        }
        requestActive = true;
        //de.idrinth.ssl.TrustManager manager = new de.idrinth.ssl.TrustManager();
        client = org.apache.http.impl.client.HttpClientBuilder.create()
                .useSystemProperties()
                .setSSLContext(sslContext)
                .build();
        org.apache.http.HttpResponse response = client.execute(uri);
        if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299) {
            throw new java.net.ConnectException(response.getStatusLine().getReasonPhrase());
        }
        requestActive = false;
        return response;
    }

    /**
     * tries to upload a file
     *
     * @param url
     * @param file
     * @return boolean
     * @throws java.lang.Exception
     */
    public boolean upload(String url, java.io.File file) throws java.lang.Exception {
        org.apache.http.client.methods.HttpPost request = new org.apache.http.client.methods.HttpPost(url);
        request.setEntity(new org.apache.http.entity.FileEntity(file));
        boolean wasSuccess = executionHandler(request) != null;
        try {
            client.close();
        } catch (java.io.IOException exception) {
            de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
        }
        return wasSuccess;
    }

    /**
     * gets the version string from github
     *
     * @return String
     * @throws java.lang.Exception
     */
    public String getVersion() throws java.lang.Exception {
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
            de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
        }
        return version;
    }
}
