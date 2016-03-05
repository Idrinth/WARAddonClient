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
    public Addon(javax.json.JsonObject addon) {
        description=getStringFromObject("description",addon);
        version=getStringFromObject("version",addon);
        slug=getStringFromObject("slug",addon);
        name=getStringFromObject("name",addon);
        javax.json.JsonArray tagList=addon.getJsonArray("tags");
        int counter=0;
        while(tagList.size()>counter) {
            tags.add(tagList.getString(counter));
            counter++;
        }
        System.out.println(this);
    }
    protected final String getStringFromObject(String key,javax.json.JsonObject data) {
        String value="";
        if(!data.isNull(key)) {
            value=data.getString(key);
        }
        return value;
    }
    public String[] getTableRow() {
        String[] row=new String[3];
        row[0]=this.name;
        row[1]=this.version;
        row[2]="-";
        return row;
    }
    public String getDescription() {
        return "<html>"+description;
    }
}
