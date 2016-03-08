/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Web;

import java.io.IOException;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author BJ
 */
public class ResponseCache extends java.net.ResponseCache {

    @Override
    public CacheResponse get(URI uri, String string, Map<String, List<String>> map) throws IOException {
        return null;
    }

    @Override
    public CacheRequest put(URI uri, URLConnection urlc) throws IOException {
        return null;
    }
    
}
