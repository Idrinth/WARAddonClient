package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.Config;
import de.idrinth.waraddonclient.Utils;
import java.io.File;
import java.io.IOException;
import de.idrinth.waraddonclient.model.TrustManager;

public class Request {

    private static final String BASE_URL = "https://tools.idrinth.de/";

    private volatile boolean requestActive;

    private org.apache.http.impl.client.CloseableHttpClient client;

    private final javax.net.ssl.SSLContext sslContext;

    private final FileLogger logger;

    public Request(TrustManager manager, FileLogger logger) {
        this.logger = logger;
        sslContext = org.apache.http.ssl.SSLContextBuilder.create().loadTrustMaterial(
                manager.getKeyStore(),
                manager
        ).build();
    }

    public javax.json.JsonArray getAddonList() throws IOException {
        org.apache.http.HttpResponse response = executionHandler(new org.apache.http.client.methods.HttpGet(BASE_URL + "addon-api/"));
        javax.json.JsonArray data = javax.json.Json.createReader(response.getEntity().getContent()).readArray();
        client.close();
        return data;
    }

    public java.io.InputStream getAddonDownload(String url) throws IOException {
        org.apache.http.HttpResponse response = executionHandler(new org.apache.http.client.methods.HttpGet(BASE_URL + "addons/" + url));
        return response.getEntity().getContent();
    }

    private synchronized org.apache.http.HttpResponse executionHandler(org.apache.http.client.methods.HttpRequestBase uri) throws IOException {
        uri.setConfig(org.apache.http.client.config.RequestConfig.DEFAULT);
        uri.setHeader("User-Agent", "IdrinthsWARAddonClient/" + Config.getVersion());
        uri.setHeader("Cache-Control", "no-cache");
        while (requestActive) {
            Utils.sleep(150, logger);
        }
        requestActive = true;
        client = org.apache.http.impl.client.HttpClientBuilder.create()
                .useSystemProperties()
                .setSSLContext(sslContext)
                .build();
        org.apache.http.HttpResponse response = client.execute(uri);
        if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299) {
            requestActive = false;
            throw new java.net.ConnectException(response.getStatusLine().getReasonPhrase());
        }
        requestActive = false;
        return response;
    }

    public boolean upload(String url, File file) {
        org.apache.http.client.methods.HttpPost request = new org.apache.http.client.methods.HttpPost(url);
        request.setEntity(new org.apache.http.entity.FileEntity(file));
        try {
            boolean wasSuccess = executionHandler(request) != null;
            client.close();
            return wasSuccess;
        } catch (IOException exception) {
            logger.error(exception);
        }
        return false;
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
            logger.error(exception);
        }
        return version;
    }
}
