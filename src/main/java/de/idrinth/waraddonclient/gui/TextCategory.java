package de.idrinth.waraddonclient.gui;

import de.idrinth.waraddonclient.model.AddonList;
import de.idrinth.waraddonclient.model.ActualAddon;
import java.util.List;

public class TextCategory extends javax.swing.RowFilter {

    private final java.util.regex.Pattern textfilter;
    
    private final AddonList addonList;

    public TextCategory(String text, AddonList addonList) {
        textfilter = java.util.regex.Pattern.compile("(?i)" + java.util.regex.Pattern.quote(text));
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
        ActualAddon addon = addonList.get(entry.getStringValue(1));
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
    private boolean isInAllowedCategory(ActualAddon addon) {
        List<String> tags = addonList.getActiveTags();
        return tags.isEmpty() || tags.stream().anyMatch((tag) -> (addon.hasTag(tag)));
    }

}
