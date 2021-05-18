package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.Utils;
import java.io.File;
import java.io.IOException;
import de.idrinth.waraddonclient.model.TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

public class Request {

    private volatile boolean requestActive;

    private org.apache.http.impl.client.CloseableHttpClient client;

    private final javax.net.ssl.SSLContext sslContext;

    private final FileLogger logger;
    
    private final Config config;

    public Request(TrustManager manager, FileLogger logger, Config config) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.logger = logger;
        this.config = config;
        sslContext = org.apache.http.ssl.SSLContextBuilder.create().loadTrustMaterial(
                manager.getKeyStore(),
                manager
        ).build();
    }

    public javax.json.JsonArray getAddonList() throws IOException {
        org.apache.http.HttpResponse response = executionHandler(new org.apache.http.client.methods.HttpGet(config.getURL() + "addon-api/"));
        javax.json.JsonArray data = javax.json.Json.createReader(response.getEntity().getContent()).readArray();
        client.close();
        return data;
    }

    public java.io.InputStream getAddonDownload(String url) throws IOException {
        org.apache.http.HttpResponse response = executionHandler(new org.apache.http.client.methods.HttpGet(config.getURL() + "addons/" + url));
        return response.getEntity().getContent();
    }

    private synchronized org.apache.http.HttpResponse executionHandler(org.apache.http.client.methods.HttpRequestBase uri) throws IOException {
        uri.setConfig(org.apache.http.client.config.RequestConfig.DEFAULT);
        uri.setHeader("User-Agent", "IdrinthsWARAddonClient/" + config.getVersion());
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
        String version = "";
        try {
            HttpGet request = new HttpGet("https://api.github.com/repos/Idrinth/WARAddonClient/releases/latest");
            HttpResponse response = executionHandler(request);
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
