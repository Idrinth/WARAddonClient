package de.idrinth.waraddonclient.list;

import de.idrinth.waraddonclient.model.Tag;
import de.idrinth.waraddonclient.service.Sleeper;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JMenu;

public class TagList implements java.lang.Runnable {

    private final JMenu menu;

    private final HashMap<String, Tag> tags = new HashMap<>();

    private final ArrayList<String> tagNames = new ArrayList<>();
    
    private final AddonList addonList;

    private long lastRefreshed;

    public TagList(JMenu jmenu, AddonList addonList) {
        this.menu = jmenu;
        this.addonList = addonList;
    }

    /**
     * processes the addon lst to see what addon is tagged with a specific tag
     */
    private void processAddons() {
        for (int counter = 0; counter < addonList.size(); counter++) {
            for (String tag : addonList.get(counter).getTags()) {
                if (!tags.containsKey(tag)) {
                    tags.put(tag, new Tag(tag));
                }
                tags.get(tag).addMember(addonList.get(counter));
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
        java.util.ArrayList<String> active = new ArrayList<>();
        tagNames.stream().filter(tag -> (tags.get(tag).isActive())).forEach(tag -> active.add(tag));
        return active;
    }

    /**
     * removes unneeded tags and adds new tags to the menu
     */
    private void processTags() {
        tagNames.stream().map(name -> {
            tags.get(name).checkMembers();
            return name;
        }).forEach(name -> {
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
        Sleeper.sleep(10000);
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
