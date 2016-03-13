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
public class TagList implements java.lang.Runnable {

    javax.swing.JMenu menu;
    AddonList addons;
    java.util.HashMap<String, Tag> tags = new java.util.HashMap();
    java.util.ArrayList<String> tagNames = new java.util.ArrayList();

    public TagList(javax.swing.JMenu menu, AddonList addons) {
        this.menu = menu;
        this.addons = addons;
    }

    protected void processAddons() {
        for (int counter = 0; counter < addons.size(); counter++) {
            for (String tag : addons.get(counter).getTags()) {
                if (!tags.containsKey(tag)) {
                    tags.put(tag, new Tag(tag));
                }
                tags.get(tag).addMember(addons.get(counter));
                if (!tagNames.contains(tag)) {
                    tagNames.add(tag);
                }
            }
        }
    }

    protected void processTags() {
        for (String name : tagNames) {
            tags.get(name).checkMembers();
            if (!tags.get(name).hasMembers()) {
                menu.remove(tags.get(name).getMenu());
                tags.remove(name);
                tagNames.remove(name);
            } else if (tags.get(name).getMenu().getParent() == null) {
                menu.add(tags.get(name).getMenu());
            }
        }
    }

    public void run() {
        return;
        /*while (true) {
            //while (System.currentTimeMillis() < lastRefreshed + duration * 60000) {
            try {
                Thread.sleep(1000);
            } catch (java.lang.InterruptedException e) {
                System.out.println(e.getMessage());
            }
            //}
            processAddons();
            processTags();
        }*/
    }
}
