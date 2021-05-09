package de.idrinth.waraddonclient.gui;

import de.idrinth.waraddonclient.list.AddonList;

public class TextCategory extends javax.swing.RowFilter {

    private final java.util.regex.Pattern textfilter;

    private final java.util.ArrayList<String> tags;
    
    private final AddonList addonList;

    public TextCategory(String text, java.util.ArrayList<String> tagList, AddonList addonList) {
        textfilter = java.util.regex.Pattern.compile("(?i)" + java.util.regex.Pattern.quote(text));
        tags = tagList;
        this.addonList = addonList;
    }

    /**
     * apply textfilter tp displayed addon list
     *
     * @param entry
     * @return boolean
     */
    @Override
    public boolean include(Entry entry) {
        de.idrinth.waraddonclient.model.ActualAddon addon = addonList.get(entry.getStringValue(1));
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
    private boolean isInAllowedCategory(de.idrinth.waraddonclient.model.ActualAddon addon) {
        return tags.isEmpty() || tags.stream().anyMatch((tag) -> (addon.hasTag(tag)));
    }

}
