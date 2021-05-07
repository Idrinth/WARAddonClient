package de.idrinth.waraddonclient.list;

public class Tag implements java.lang.Runnable {

    private final javax.swing.JMenu menu;

    private final java.util.HashMap<String, de.idrinth.waraddonclient.model.Tag> tags = new java.util.HashMap();

    private final java.util.ArrayList<String> tagNames = new java.util.ArrayList();

    private long lastRefreshed;

    public Tag(javax.swing.JMenu jmenu) {
        this.menu = jmenu;
    }

    /**
     * processes the addon lst to see what addon is tagged with a specific tag
     */
    private void processAddons() {
        for (int counter = 0; counter < de.idrinth.waraddonclient.factory.AddonList.build().size(); counter++) {
            for (String tag : de.idrinth.waraddonclient.factory.AddonList.build().get(counter).getTags()) {
                if (!tags.containsKey(tag)) {
                    tags.put(tag, new de.idrinth.waraddonclient.model.Tag(tag));
                }
                tags.get(tag).addMember(de.idrinth.waraddonclient.factory.AddonList.build().get(counter));
                if (!tagNames.contains(tag)) {
                    tagNames.add(tag);
                }
            }
        }
    }

    /**
     * get the selected tags
     */
    public java.util.ArrayList<String> getActiveTags() {
        java.util.ArrayList<String> active = new java.util.ArrayList();
        tagNames.stream().filter((tag) -> (tags.get(tag).isActive())).forEach((tag) -> {
            active.add(tag);
        });
        return active;
    }

    /**
     * removes unneeded tags and adds new tags to the menu
     */
    private void processTags() {
        tagNames.stream().map((name) -> {
            tags.get(name).checkMembers();
            return name;
        }).forEach((name) -> {
            if (!tags.get(name).hasMembers()) {
                menu.remove(tags.get(name).getMenu());
                tags.remove(name);
                tagNames.remove(name);
            } else if (tags.get(name).getMenu().getParent() == null) {
                menu.add(tags.get(name).getMenu());
            }
        });
    }

    /**
     * update the addon-tag relationships
     */
    @Override
    public void run() {
        de.idrinth.waraddonclient.service.Sleeper.sleep(10000);
        while (true) {
            while (System.currentTimeMillis() < lastRefreshed + 300000) {
                de.idrinth.waraddonclient.service.Sleeper.sleep(1000000);
            }
            processAddons();
            processTags();
            lastRefreshed = System.currentTimeMillis();
        }
    }
}
