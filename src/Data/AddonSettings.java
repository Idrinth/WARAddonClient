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
public class AddonSettings {

    protected String file="";
    protected boolean enabled=false;
    protected String reason="";
    protected String url="";
    protected String name;
    protected boolean hasSettings=false;
    protected User user;

    public String getFile() {
        return file;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        user.setEnabled(name, enabled);
    }

    public String getReason() {
        return reason;
    }

    public String getUrl() {
        return url;
    }
    
    AddonSettings(String name,User user) {
        this.name=name;
        this.user=user;
        this.enabled=user.getEnabled(name);
        refresh();
    }
    public boolean showSettings() {
        return hasSettings;
    }
    final public void refresh() {
        file="";
        reason="";
        url="";
        hasSettings=false;
        java.io.File folder = new Service.FindAddonFolder().find(name);
        if (folder == null || !folder.exists()) {
            return;
        }
        for (java.io.File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()
                    && fileEntry.getName().equalsIgnoreCase("upload.idrinth")) {
                try {
                    org.w3c.dom.NodeList list = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileEntry).getFirstChild().getChildNodes();
                    for(int counter=0;counter<list.getLength();counter++) {
                        switch(list.item(counter).getNodeName().toLowerCase()){
                            case "file":
                                file=list.item(counter).getTextContent();
                                break;
                            case "url":
                                url=list.item(counter).getTextContent();
                                break;
                            case "reason":
                                reason=list.item(counter).getTextContent();
                                break;
                        }
                    }
                    hasSettings=true;
                    return;
                } catch (javax.xml.parsers.ParserConfigurationException | javax.xml.parsers.FactoryConfigurationError | org.xml.sax.SAXException | java.io.IOException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }
}
