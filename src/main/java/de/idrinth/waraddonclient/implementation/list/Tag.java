/*
 * Copyright (C) 2016 Björn Büttner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.idrinth.waraddonclient.implementation.list;

public class Tag implements java.lang.Runnable {

    private javax.swing.JMenu menu;

    private java.util.HashMap<String, de.idrinth.waraddonclient.implementation.model.Tag> tags = new java.util.HashMap();

    private java.util.ArrayList<String> tagNames = new java.util.ArrayList();

    private long lastRefreshed;

    /**
     * Create a tag-handler for a menu entry
     *
     * @param menu
     */
    public Tag(javax.swing.JMenu menu) {
        this.menu = menu;
    }

    /**
     * processes the addon lst to see what addon is tagged with a specific tag
     */
    private void processAddons() {
        for (int counter = 0; counter < de.idrinth.waraddonclient.factory.AddonList.build().size(); counter++) {
            for (String tag : de.idrinth.waraddonclient.factory.AddonList.build().get(counter).getTags()) {
                if (!tags.containsKey(tag)) {
                    tags.put(tag, new de.idrinth.waraddonclient.implementation.model.Tag(tag));
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
     *
     * @return
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
        de.idrinth.waraddonclient.implementation.service.Sleeper.sleep(10000);
        while (true) {
            while (System.currentTimeMillis() < lastRefreshed + 300000) {
                de.idrinth.waraddonclient.implementation.service.Sleeper.sleep(1000000);
            }
            processAddons();
            processTags();
            lastRefreshed = System.currentTimeMillis();
        }
    }
}
