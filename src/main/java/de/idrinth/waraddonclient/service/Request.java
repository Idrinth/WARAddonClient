package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.service.logger.BaseLogger;
import de.idrinth.waraddonclient.Utils;
import java.io.File;
import java.io.IOException;
import de.idrinth.waraddonclient.model.TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

public class Request {

    private volatile boolean requestActive;

    private org.apache.http.impl.client.CloseableHttpClient client;

    private final javax.net.ssl.SSLContext sslContext;

    private final BaseLogger logger;
    
    private final Config config;

    public Request(TrustManager manager, BaseLogger logger, Config config) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.logger = logger;
        this.config = config;
        sslContext = org.apache.http.ssl.SSLContextBuilder.create().loadTrustMaterial(
                manager.getKeyStore(),
                manager
        ).build();
    }

    public javax.json.JsonArray getAddonList() throws IOException {
        HttpResponse response = executionHandler(new HttpGet(config.getURL() + "addon-api2/"));
        JsonReader reader = Json.createReader(response.getEntity().getContent());
        JsonArray data = reader.readArray();
        reader.close();
        client.close();
        return data;
    }

    public JsonObject getAddon(String slug) throws IOException {
        HttpResponse response = executionHandler(new HttpGet(config.getURL() + "addon-api2/"+slug+"/"));
        JsonReader reader = Json.createReader(response.getEntity().getContent());
        JsonObject data = reader.readObject();
        reader.close();
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

    public void upload(String url, File file) {
        org.apache.http.client.methods.HttpPost request = new org.apache.http.client.methods.HttpPost(url);
        request.setEntity(new org.apache.http.entity.FileEntity(file));
        try {
            executionHandler(request);
            client.close();
        } catch (IOException exception) {
            logger.error(exception);
        }
    }

    public String getVersion() {
        String version = "";
        try {
            HttpGet request = new HttpGet("https://api.github.com/repos/Idrinth/WARAddonClient/releases/latest");
            HttpResponse response = executionHandler(request);
            JsonReader reader = Json.createReader(response.getEntity().getContent());
            JsonObject data = reader.readObject();
            reader.close();
            version = data.getString("tag_name");
            client.close();
        } catch (java.io.IOException exception) {
            //do nothing, not important
        }
        return version;
    }
}
