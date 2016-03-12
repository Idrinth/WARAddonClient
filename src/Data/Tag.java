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
public class Tag {

    protected String name;
    protected java.util.HashMap<String, Addon> list = new java.util.HashMap();
    protected javax.swing.JCheckBoxMenuItem item;

    public Tag(String name) {
        this.name = name;
        item = new javax.swing.JCheckBoxMenuItem();
        item.setText(name);
        item.setSelected(true);
    }

    public javax.swing.JCheckBoxMenuItem getMenu() {
        return item;
    }

    public boolean isActive() {
        return true;
    }

    public boolean hasMembers() {
        return true;
    }

    public void addMember(Addon addon) {

    }

    public void checkMembers() {

    }
}
