package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.Config;

public class Request {

    private final String baseUrl = "https://tools.idrinth.de/";

    private volatile boolean requestActive;

    private org.apache.http.impl.client.CloseableHttpClient client;

    private final javax.net.ssl.SSLContext sslContext;

    /**
     * Throws an exception if there's issues with the ssl-certificates
     *
     * @throws java.lang.Exception
     */
    public Request() throws java.lang.Exception {
        de.idrinth.ssl.TrustManager manager = new de.idrinth.ssl.TrustManager();
        sslContext = org.apache.http.ssl.SSLContextBuilder.create().loadTrustMaterial(
                manager.getKeyStore(),
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
    private synchronized org.apache.http.HttpResponse executionHandler(org.apache.http.client.methods.HttpRequestBase uri) throws java.lang.Exception {
        uri.setConfig(org.apache.http.client.config.RequestConfig.DEFAULT);
        uri.setHeader("User-Agent", "IdrinthAddonClient/" + Config.getVersion());
        uri.setHeader("Cache-Control", "no-cache");
        while (requestActive) {
            de.idrinth.waraddonclient.service.Sleeper.sleep(150);
        }
        requestActive = true;
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
            de.idrinth.factory.Logger.build().log(exception, de.idrinth.Logger.LEVEL_ERROR);
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
            de.idrinth.factory.Logger.build().log(exception, de.idrinth.Logger.LEVEL_ERROR);
        }
        return version;
    }
}