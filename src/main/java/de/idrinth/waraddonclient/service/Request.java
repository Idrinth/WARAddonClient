package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.model.BasicResponse;
import de.idrinth.waraddonclient.service.logger.BaseLogger;
import de.idrinth.waraddonclient.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.FileEntity;

public class Request {

    private volatile boolean requestActive;

    private CloseableHttpClient client;

    private final BaseLogger logger;
    
    private final Config config;

    public Request(BaseLogger logger, Config config) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.logger = logger;
        this.config = config;
    }

    public javax.json.JsonArray getAddonList() throws IOException {
        BasicResponse response = executionHandler(new HttpGet(config.getURL() + "addon-api2/"));
        JsonReader reader = Json.createReader(response.content);
        JsonArray data = reader.readArray();
        reader.close();
        client.close();
        return data;
    }

    public JsonObject getAddon(String slug) throws IOException {
        BasicResponse response = executionHandler(new HttpGet(config.getURL() + "addon-api2/"+slug+"/"));
        JsonReader reader = Json.createReader(response.content);
        JsonObject data = reader.readObject();
        reader.close();
        client.close();
        return data;
    }

    public java.io.InputStream getAddonDownload(String url) throws Exception {
        BasicResponse response = executionHandler(new HttpGet(config.getURL() + "addons/" + url));
        return new BufferedInputStream(response.content);
    }

    private synchronized BasicResponse executionHandler(HttpUriRequestBase uri) throws IOException {
        uri.setConfig(RequestConfig.DEFAULT);
        uri.setHeader("User-Agent", "IdrinthsWARAddonClient/" + config.getVersion());
        uri.setHeader("Cache-Control", "no-cache");
        while (requestActive) {
            Utils.sleep(150, logger);
        }
        requestActive = true;
        client = HttpClientBuilder.create()
                .useSystemProperties()
                .build();
        CloseableHttpResponse response = client.execute(uri);
        if (response.getCode() < 200 || response.getCode() > 299) {
            requestActive = false;
            throw new java.net.ConnectException(response.getReasonPhrase());
        }
        requestActive = false;
        return new BasicResponse(response.getCode(), response.getReasonPhrase(), response.getEntity().getContent(), response.containsHeader("Content-Encoding"));
    }

    public void upload(String url, File file) {
        HttpPost request = new HttpPost(url);
        request.setEntity(new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM));
        try(BasicResponse res = executionHandler(request)) {
            client.close();
        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    public String getVersion()
    {
        try {
            HttpGet request = new HttpGet("https://api.github.com/repos/Idrinth/WARAddonClient/releases/latest");
            BasicResponse response = executionHandler(request);
            JsonObject data;
            try (JsonReader reader = Json.createReader(response.content)) {
                data = reader.readObject();
            }
            String version = "unknown";
            if (data != null) {
                version = data.getString("tag_name");
            }
            client.close();
            return version;
        } catch (IOException exception) {
            return "unknown";
        }
    }
}
