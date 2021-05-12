package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.Utils;
import de.idrinth.waraddonclient.model.ActualAddon;
import de.idrinth.waraddonclient.model.Tag;
import de.idrinth.waraddonclient.service.FileLogger;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.XmlParser;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.json.JsonArray;
import javax.swing.JMenu;
import javax.swing.table.DefaultTableModel;

public class AddonList implements java.lang.Runnable {

    private final HashMap<String, ActualAddon> list = new HashMap<>();

    private final ArrayList<ActualAddon> rows = new ArrayList<>();

    private final HashMap<String, WatchedFile> watchedFilesMap = new HashMap<>();
    
    private final Request client;
    
    private DefaultTableModel model;
    
    private final FileLogger logger;

    private JMenu menu;

    private final HashMap<String, Tag> tags = new HashMap<>();

    private final ArrayList<String> tagNames = new ArrayList<>();
    
    private ActionListener listener;
    
    private final XmlParser parser;

    public AddonList(Request client, FileLogger logger, XmlParser parser) {
        this.client = client;
        this.logger = logger;
        this.parser = parser;
    }

    public void setMenu(JMenu menu, ActionListener listener) {
        this.menu = menu;
        this.listener = listener;
    }

    public void setModel(DefaultTableModel model) {
        this.model = model;
    }

    public ActualAddon get(int position) {
        return rows.get(position);
    }

    public ActualAddon get(String name) {
        return list.get(name);
    }

    /**
     * amount of addons handled
     *
     * @return int
     */
    public int size() {
        return list.size();
    }

    /**
     * adds an addon to the global list
     *
     * @param addon
     */
    public void add(de.idrinth.waraddonclient.model.ActualAddon addon) {
        list.put(addon.getName(), addon);
        rows.add(addon);
    }

    /**
     * returns a list of files watched
     */
    public HashMap<String, WatchedFile> getWatchedFiles() {
        return watchedFilesMap;
    }

    /**
     * processes the addon lst to see what addon is tagged with a specific tag
     */
    private void processAddons() {
        for (int counter = 0; counter < size(); counter++) {
            for (String tag : get(counter).getTags()) {
                if (!tags.containsKey(tag)) {
                    tags.put(tag, new Tag(tag, listener));
                }
                tags.get(tag).addMember(get(counter));
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

    @Override
    public void run() {
        try {
            new JsonProcessor(client.getAddonList(), model).run();
            processAddons();
            processTags();
        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    public class JsonProcessor {

        private final javax.json.JsonArray json;
        
        private DefaultTableModel model;

        public JsonProcessor(JsonArray parse, DefaultTableModel model) {
            json = parse;
            this.model = model;
        }

        public void run() throws IOException {
            if (json == null) {
                throw new IOException("no content in json");
            }
            for (int counter = json.size(); counter > 0; counter--) {
                processJsonAddon(new ActualAddon(json.getJsonObject(counter - 1), client, logger, parser));
            }
        }

        /**
         *
         * @param model
         * @param addon
         * @return javax.swing.table.DefaultTableMode
         */
        private void newAddon(ActualAddon addon) {
            add(addon);
            model.addRow(addon.getTableRow());
            if (!addon.getUploadData().getFile().isEmpty()) {
                if (!watchedFilesMap.containsKey(addon.getUploadData().getFile().toLowerCase())) {
                    watchedFilesMap.put(addon.getUploadData().getFile().toLowerCase(), new WatchedFile());
                }
                watchedFilesMap.get(addon.getUploadData().getFile().toLowerCase()).addAddon(addon);
            }
        }

        private void processJsonAddon(de.idrinth.waraddonclient.model.ActualAddon addon) {
            if (list.containsKey(addon.getName())) {
                existingAddon(addon);
                return;
            }
            newAddon(addon);
        }

        private void existingAddon(ActualAddon addon) {
            if (!list.containsKey(addon.getName())) {
                return;
            }
            list.get(addon.getName()).update(addon);
            String[] data = list.get(addon.getName()).getTableRow();
            model.setValueAt(data[0], rows.indexOf(list.get(addon.getName())), 0);
            model.setValueAt(data[1], rows.indexOf(list.get(addon.getName())), 1);
            model.setValueAt(data[2], rows.indexOf(list.get(addon.getName())), 2);
            model.setValueAt(data[3], rows.indexOf(list.get(addon.getName())), 3);
        }
    }

    public class WatchedFile implements java.lang.Runnable {

        private boolean active;

        private File file;

        private final ArrayList<ActualAddon> list = new ArrayList<>();

        /**
         * Adds an addon to watch an be handled here
         *
         * @param addon
         */
        public void addAddon(de.idrinth.waraddonclient.model.ActualAddon addon) {
            list.add(addon);
        }

        public void setFileToProcess(java.io.File file2process) {
            while (active) {
                Utils.sleep(100, logger);
            }
            active = true;
            file = file2process;
            new java.lang.Thread(this).start();
        }

        /**
         * check if a file was actually updated and is avaible
         */
        @Override
        public void run() {
            if (file == null || !file.exists()) {
                active = false;
                return;
            }
            list.stream().forEach(addon -> addon.fileWasChanged(file));
            active = false;
        }
    }
}
