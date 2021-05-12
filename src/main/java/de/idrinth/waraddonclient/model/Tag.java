package de.idrinth.waraddonclient.model;

import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JCheckBoxMenuItem;

public class Tag {

    private final String name;

    private final HashMap<String, ActualAddon> list = new java.util.HashMap<>();

    private final JCheckBoxMenuItem item;

    public Tag(String tag, ActionListener update) {
        this.name = tag;
        item = new javax.swing.JCheckBoxMenuItem();
        item.setText(name);
        item.setSelected(true);
        item.addActionListener(update);
    }

    public javax.swing.JCheckBoxMenuItem getMenu() {
        return item;
    }

    public boolean isActive() {
        return item.isSelected();
    }

    public boolean hasMembers() {
        return !list.isEmpty();
    }

    public void addMember(ActualAddon addon) {
        list.put(addon.getName(), addon);
    }

    public void checkMembers() {
        list.keySet().stream().filter(addon -> (!list.get(addon).hasTag(name))).forEach(addon -> list.remove(addon));
    }
}
