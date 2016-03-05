/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 *
 * @author BJ
 */
public class Addon {
    private String description;
    private String version;
    private String slug;
    private String name;
    private java.util.ArrayList tags=new java.util.ArrayList();
    public Addon Addon(javax.json.JsonObject addon) {
        description=addon.getString("description");
        version=addon.getString("version");
        slug=addon.getString("slug");
        name=addon.getString("name");
        javax.json.JsonArray tagList=addon.getJsonArray("tags");
        int counter=0;
        while(null!=tagList.getString(counter)) {
            tags.add(tagList.getString(counter));
            counter++;
        }
        return this;
    }
}
