package de.idrinth.waraddonclient.implementation.model;

public class Tag {

    private final String name;

    private final java.util.HashMap<String, Addon> list = new java.util.HashMap();

    private final javax.swing.JCheckBoxMenuItem item;

    public Tag(String tag) {
        this.name = tag;
        item = new javax.swing.JCheckBoxMenuItem();
        item.setText(name);
        item.setSelected(true);
        item.addActionListener((java.awt.event.ActionEvent evt) -> {
            de.idrinth.waraddonclient.factory.Interface.build().newFilter();
        });
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

    public void addMember(Addon addon) {
        list.put(addon.getName(), addon);
    }

    public void checkMembers() {
        list.keySet().stream().filter((addon) -> (!list.get(addon).hasTag(name))).forEach((addon) -> {
            list.remove(addon);
        });
    }
}
