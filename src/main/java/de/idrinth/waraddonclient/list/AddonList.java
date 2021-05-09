package de.idrinth.waraddonclient.list;

import de.idrinth.waraddonclient.model.ActualAddon;
import de.idrinth.waraddonclient.service.Request;
import java.util.ArrayList;
import java.util.HashMap;
import javax.json.JsonArray;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AddonList implements java.lang.Runnable {

    private final HashMap<String, ActualAddon> list = new HashMap<>();

    private final ArrayList<ActualAddon> rows = new ArrayList<>();

    private final HashMap<String, WatchedFile> watchedFilesMap = new HashMap<>();

    private int duration = 15;

    private long lastRefreshed;
    
    private final Request client;
    
    private DefaultTableModel model;

    public AddonList(Request client) {
        this.client = client;
    }

    public void setModel(DefaultTableModel model) {
        this.model = model;
    }

    /**
     * set the refresh frequency
     *
     * @param dur
     */
    public void setDuration(int dur) {
        duration = dur;
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
     * handles updating the addonlists
     */
    @Override
    public void run() {
        int failuresInARow = 0;
        while (true) {
            while (System.currentTimeMillis() < lastRefreshed + duration * 60000) {
                de.idrinth.waraddonclient.service.Sleeper.sleep(250);
            }
            try {
                new JsonProcessor(client.getAddonList(), model).run();
                failuresInARow = 0;
                lastRefreshed = System.currentTimeMillis();
            } catch (Exception exception) {
                de.idrinth.waraddonclient.factory.Logger.build().error(exception);
                failuresInARow++;
                if (failuresInARow > 5) {
                    JOptionPane.showMessageDialog(null, exception.getLocalizedMessage());
                    Runtime.getRuntime().exit(0);
                }
            }
        }
    }

    public class JsonProcessor {

        private final javax.json.JsonArray json;
        
        private DefaultTableModel model;

        public JsonProcessor(JsonArray parse, DefaultTableModel model) {
            json = parse;
            this.model = model;
        }

        /**
         * works through the datab provided by the website's api
         *
         * @throws java.lang.Exception
         */
        public void run() throws java.lang.Exception {
            if (json == null) {
                throw new java.lang.Exception("no content in json");
            }
            for (int counter = json.size(); counter > 0; counter--) {
                processJsonAddon(new ActualAddon(json.getJsonObject(counter - 1), client));
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

        private java.io.File file;

        private final java.util.ArrayList<de.idrinth.waraddonclient.model.ActualAddon> list = new ArrayList<>();

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
                de.idrinth.waraddonclient.service.Sleeper.sleep(100);
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
