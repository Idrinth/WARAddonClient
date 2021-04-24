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

package de.idrinth.waraddonclient.gui.tablefilter;

public class TextCategory extends javax.swing.RowFilter {

    private final java.util.regex.Pattern textfilter;

    private final java.util.ArrayList<String> tags;

    /**
     *
     * @param text
     * @param tagList
     */
    public TextCategory(String text, java.util.ArrayList<String> tagList) {
        textfilter = java.util.regex.Pattern.compile("(?i)" + java.util.regex.Pattern.quote(text));
        tags = tagList;
    }

    /**
     * apply textfilter tp displayed addon list
     *
     * @param entry
     * @return boolean
     */
    @Override
    public boolean include(Entry entry) {
        de.idrinth.waraddonclient.implementation.model.Addon addon = de.idrinth.waraddonclient.factory.AddonList.build().get(entry.getStringValue(1));
        if (addon == null) {
            return false;
        }
        if (!textfilter.matcher(addon.getName()).find()) {
            return false;
        }
        return isInAllowedCategory(addon);
    }

    /**
     * Does any category match?
     *
     * @param addon
     * @return boolean
     */
    private boolean isInAllowedCategory(de.idrinth.waraddonclient.implementation.model.Addon addon) {
        return tags.isEmpty() || tags.stream().anyMatch((tag) -> (addon.hasTag(tag)));
    }

}
