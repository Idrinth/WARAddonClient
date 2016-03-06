/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Web;


/**
 *
 * @author BJ
 */
public class Request {
    private final String baseUrl="http://tools.idrinth.de/";

    public javax.json.JsonArray getAddonList() {
        try{
            java.io.InputStream stream =new java.net.URL(baseUrl+"addon-api/").openConnection().getInputStream();
            return javax.json.Json.createReader(stream).readArray();
        }catch (java.net.MalformedURLException exeption) {
            System.out.println(exeption.getMessage());
        }catch (java.io.IOException exeption) {
            System.out.println(exeption.getMessage());
        }
        return null;
    }

    public java.io.InputStream getAddonDownload(String url) {
         try{
            return new java.net.URL(baseUrl+"addons/"+url).openConnection().getInputStream();
        }catch (java.net.MalformedURLException exeption) {
            System.out.println(exeption.getMessage());
        }catch (java.io.IOException exeption) {
            System.out.println(exeption.getMessage());
        }
        return null;
    }
}
