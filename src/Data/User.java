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
public class User {
    private org.w3c.dom.Document xml=null;
    public User() {
        java.io.File file=new java.io.File("./idrinth.xml");
        try{
            if(file.exists()) {
                xml = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            }
        } catch(java.io.IOException|javax.xml.parsers.ParserConfigurationException|org.xml.sax.SAXException exception) {}
        try{
        if(xml==null){
            xml = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            xml.appendChild(xml.createElement("AddOns"));
        }
        } catch(javax.xml.parsers.ParserConfigurationException exception) {}
    }
    public boolean getEnabled(String name) {
        org.w3c.dom.NodeList list=xml.getElementsByTagName("AddOn");
        for(int counter=0;counter<list.getLength();counter++) {
            if(name.equalsIgnoreCase(list.item(counter).getTextContent())) {
                return list.item(counter).getAttributes().item(0).getTextContent().equalsIgnoreCase("true");
            }
        }
        return false;
    }
    protected void writeDocument() {
        try{
            javax.xml.transform.TransformerFactory.newInstance().newTransformer().transform(
                    new javax.xml.transform.dom.DOMSource(xml),
                    new javax.xml.transform.stream.StreamResult(new java.io.File("./idrinth.xml"))
            );
        } catch(javax.xml.transform.TransformerException exception) {
            System.out.println(exception);
        }
    }
    public void setEnabled(String name,boolean isEnabled) {
        org.w3c.dom.NodeList list=xml.getElementsByTagName("AddOn");
        for(int counter=0;counter<list.getLength();counter++) {
            if(name.equalsIgnoreCase(list.item(counter).getTextContent())) {
                list.item(counter).getAttributes().item(0).setNodeValue(isEnabled?"true":"false");
                writeDocument();
                return;
            }
        }
        org.w3c.dom.Element node = xml.createElement("AddOn");
        node.setAttribute("enabled", isEnabled?"true":"false");
        node.setTextContent(name);
        xml.getFirstChild().appendChild(node);
        writeDocument();
    }
}
