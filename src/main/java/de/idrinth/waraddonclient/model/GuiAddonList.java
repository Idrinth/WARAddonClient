package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.model.addon.Addon;
import de.idrinth.waraddonclient.model.addon.ActualAddon;
import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.Utils;
import de.idrinth.waraddonclient.service.logger.BaseLogger;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.XmlParser;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.json.JsonArray;
import javax.swing.JMenu;
import javax.swing.table.DefaultTableModel;

public class GuiAddonList extends AddonList
{
    private ActionListener listener;
    
    private DefaultTableModel model;

    private JMenu menu;

    private final HashMap<String, Tag> tags = new HashMap<>();

    private final ArrayList<String> tagNames = new ArrayList<>();

    private final HashMap<String, WatchedFile> watchedFilesMap = new HashMap<>();
    
    private final ArrayList<Object[]> rowsToAdd = new ArrayList<>();
    
    public GuiAddonList(Request client, BaseLogger logger, XmlParser parser, Config config) {
        super(client, logger, parser, config);
    }

    public void setMenu(JMenu menu, ActionListener listener) {
        this.menu = menu;
        this.listener = listener;
    }

    public void setModel(DefaultTableModel model) {
        this.model = model;
        for (Object[] row: rowsToAdd) {
            model.addRow(row);
        }
        rowsToAdd.clear();
    }
    /**
     * processes the addon lst to see what addon is tagged with a specific tag
     */
    private void processAddons() {
        for (Addon addon : rows) {
            for (String tag : addon.getTags()) {
                if (!tags.containsKey(tag)) {
                    tags.put(tag, new Tag(tag, listener));
                }
                tags.get(tag).addMember(addon);
                if (!tagNames.contains(tag)) {
                    tagNames.add(tag);
                }
            }
        }
    }
    
    @Override
    public void add(Addon addon) {
        super.add(addon);
        if (model == null) {
            rowsToAdd.add(addon.getTableRow());
            return;
        }
        model.addRow(addon.getTableRow());
    }

    public List<String> getActiveTags() {
        ArrayList<String> active = new ArrayList<>();
        tagNames.stream().filter(tag -> (tags.get(tag).isActive())).forEach(active::add);
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
            } else {
                menu.add(tags.get(name).getMenu());
            }
        });
    }

    public HashMap<String, WatchedFile> getWatchedFiles() {
        return watchedFilesMap;
    }

    @Override
    public void run() {
        processAddonDir();
        try {
            new JsonProcessor(client.getAddonList(), model).run();
        } catch (IOException exception) {
           logger.error(exception);
        }
        processAddons();
        processTags();
    }

    public class WatchedFile implements java.lang.Runnable {

        private boolean active;

        private File file;

        private final ArrayList<ActualAddon> list = new ArrayList<>();

        public void addAddon(ActualAddon addon) {
            list.add(addon);
        }

        public void setFileToProcess(File file2process) {
            while (active) {
                Utils.sleep(100, logger);
            }
            active = true;
            file = file2process;
            new java.lang.Thread(this).start();
        }

        @Override
        public void run() {
            if (file == null || !file.exists()) {
                active = false;
                return;
            }
            list.forEach(addon -> addon.fileWasChanged(file));
            active = false;
        }
    }
    
    public class JsonProcessor extends AddonList.JsonProcessor {
        
        private final DefaultTableModel model;

        public JsonProcessor(JsonArray parse, DefaultTableModel model) {
            super(parse);
            this.model = model;
        }

        @Override
        protected void newAddon(ActualAddon addon) {
            super.newAddon(addon);
            if (!addon.getFile().isEmpty()) {
                if (!watchedFilesMap.containsKey(addon.getFile().toLowerCase())) {
                    watchedFilesMap.put(addon.getFile().toLowerCase(), new WatchedFile());
                }
                watchedFilesMap.get(addon.getFile().toLowerCase()).addAddon(addon);
            }
        }

        @Override
        protected void existingAddon(ActualAddon addon) {
            super.existingAddon(addon);
            updateModel(addon);
        }

        @Override
        protected void unknownAddon(ActualAddon addon) {
            super.unknownAddon(addon);
            updateModel(addon);
        }
        
        private void updateModel(ActualAddon addon) {
            Object[] data = list.get(addon.getName()).getTableRow();
            int index = rows.indexOf(list.get(addon.getName()));
            model.setValueAt(data[0], index, 0);
            model.setValueAt(data[1], index, 1);
            model.setValueAt(data[2], index, 2);
            model.setValueAt(data[3], index, 3);
            model.setValueAt(data[4], index, 4);
            model.setValueAt(data[5], index, 5);
        }
    }
}
