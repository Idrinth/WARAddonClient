/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Web;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;


/**
 *
 * @author BJ
 */
public class Request {
    private final String baseUrl="http://tools.idrinth.de/";
    private boolean requestActive=false;
    private org.apache.http.impl.client.CloseableHttpClient client = null;

    public javax.json.JsonArray getAddonList() {
        try{
            org.apache.http.HttpResponse response=executionHandler(new org.apache.http.client.methods.HttpGet(baseUrl+"addon-api/"));
            if(response==null) {
                return null;
            }
            javax.json.JsonArray data = javax.json.Json.createReader(response.getEntity().getContent()).readArray();
            client.close();
            return data;
        }catch (java.net.MalformedURLException exeption) {
            System.out.println(exeption.getMessage());
        }catch (java.io.IOException exeption) {
            System.out.println(exeption.getMessage());
        }
        return null;
    }

    public java.io.InputStream getAddonDownload(String url) {
         try{
            org.apache.http.HttpResponse response=executionHandler(new org.apache.http.client.methods.HttpGet(baseUrl+"addons/"+url));
            if(response==null) {
                return null;
            }
            return response.getEntity().getContent();
        }catch (java.net.MalformedURLException exeption) {
            System.out.println(exeption.getMessage());
        }catch (java.io.IOException exeption) {
            System.out.println(exeption.getMessage());
        }
        return null;
    }
    protected org.apache.http.HttpResponse executionHandler(org.apache.http.client.methods.HttpRequestBase uri) {
        uri.setConfig(RequestConfig.DEFAULT);
        uri.setHeader("User-Agent", "IdrinthAddonClient");
        uri.setHeader("Cache-Control", "no-cache");      
        while(requestActive) {
            try{
                Thread.sleep(150);
            } catch(java.lang.InterruptedException exception) {
                //don't care
            }
        }
        client=org.apache.http.impl.client.HttpClientBuilder.create().build();
        requestActive=true;
        org.apache.http.HttpResponse response=null;
        try{
            response= client.execute(uri);
            if(response.getStatusLine().getStatusCode()<200||response.getStatusLine().getStatusCode()>299) {
                response=null;
            }
        }catch(java.io.IOException exception) {
            System.out.println(exception.getMessage());
        }
        requestActive=false;
        return response;
    }
    public boolean upload(String url,java.io.File file) {
        org.apache.http.client.methods.HttpPost request = new org.apache.http.client.methods.HttpPost(url);
        request.setEntity(new org.apache.http.entity.FileEntity(file));
        boolean wasSuccess= executionHandler(request)==null?false:true;
        try{
            client.close();
        }catch(java.io.IOException exception) {
            System.out.println(exception.getMessage());
        }
        return wasSuccess;
    }
}
