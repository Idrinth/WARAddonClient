package de.idrinth.waraddonclient.implementation.list;

public class Addon implements java.lang.Runnable {

    private final java.util.HashMap<String, de.idrinth.waraddonclient.implementation.model.ActualAddon> list = new java.util.HashMap();

    private final java.util.ArrayList<de.idrinth.waraddonclient.implementation.model.ActualAddon> rows = new java.util.ArrayList();

    private final java.util.Hashtable<String, watchedFile> watchedFilesMap = new java.util.Hashtable();

    private int duration = 15;

    private long lastRefreshed;

    /**
     * set the refresh frequency
     *
     * @param dur
     */
    public void setDuration(int dur) {
        duration = dur;
    }

    /**
     * returns the addon at the list-position i
     *
     * @param position
     * @return de.idrinth.waraddonclient.implementation.model.ActualAddon
     */
    public de.idrinth.waraddonclient.implementation.model.ActualAddon get(int position) {
        return (de.idrinth.waraddonclient.implementation.model.ActualAddon) rows.get(position);
    }

    /**
     * return the addon with the given name
     *
     * @param name
     * @return de.idrinth.waraddonclient.implementation.model.ActualAddon
     */
    public de.idrinth.waraddonclient.implementation.model.ActualAddon get(String name) {
        return (de.idrinth.waraddonclient.implementation.model.ActualAddon) list.get(name);
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
    public void add(de.idrinth.waraddonclient.implementation.model.ActualAddon addon) {
        list.put(addon.getName(), addon);
        rows.add(addon);
    }

    /**
     * returns a list of files watched
     *
     * @return java.util.Hashtable
     */
    public java.util.Hashtable<String, watchedFile> getWatchedFiles() {
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
                de.idrinth.waraddonclient.implementation.service.Sleeper.sleep(250);
            }
            try {
                new JsonProcessor(de.idrinth.waraddonclient.factory.RemoteRequest.build().getAddonList()).run();
                failuresInARow = 0;
            } catch (Exception exception) {
                de.idrinth.factory.Logger.build().log(exception, de.idrinth.Logger.LEVEL_ERROR);
                failuresInARow++;
                if (failuresInARow > 5) {
                    de.idrinth.waraddonclient.factory.Interface.build().exitWithError(exception.getMessage());
                }
            }
            lastRefreshed = System.currentTimeMillis();
        }
    }

    public class JsonProcessor {

        private final javax.json.JsonArray json;

        public JsonProcessor(javax.json.JsonArray parse) {
            json = parse;
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
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) de.idrinth.waraddonclient.factory.Interface.build().getAddonTable().getModel();
            for (int counter = json.size(); counter > 0; counter--) {
                model = processJsonAddon(model, new de.idrinth.waraddonclient.implementation.model.ActualAddon(json.getJsonObject(counter - 1)));
            }
        }

        /**
         *
         * @param model
         * @param addon
         * @return javax.swing.table.DefaultTableMode
         */
        private javax.swing.table.DefaultTableModel newAddon(javax.swing.table.DefaultTableModel model, de.idrinth.waraddonclient.implementation.model.ActualAddon addon) {
            add(addon);
            model.addRow(addon.getTableRow());
            if (!addon.getUploadData().getFile().isEmpty()) {
                if (!watchedFilesMap.containsKey(addon.getUploadData().getFile().toLowerCase())) {
                    watchedFilesMap.put(addon.getUploadData().getFile().toLowerCase(), new watchedFile());
                }
                watchedFilesMap.get(addon.getUploadData().getFile().toLowerCase()).addAddon(addon);
            }
            return model;
        }

        /**
         *
         * @param model
         * @param addon
         * @return javax.swing.table.DefaultTableMode
         */
        private javax.swing.table.DefaultTableModel processJsonAddon(javax.swing.table.DefaultTableModel model, de.idrinth.waraddonclient.implementation.model.ActualAddon addon) {
            if (list.containsKey(addon.getName())) {
                return existingAddon(model, addon);
            }
            return newAddon(model, addon);
        }

        /**
         *
         * @param model
         * @param addon
         * @return javax.swing.table.DefaultTableMode
         */
        private javax.swing.table.DefaultTableModel existingAddon(javax.swing.table.DefaultTableModel model, de.idrinth.waraddonclient.implementation.model.ActualAddon addon) {
            if (!list.containsKey(addon.getName())) {
                return model;
            }
            list.get(addon.getName()).update(addon);
            String[] data = list.get(addon.getName()).getTableRow();
            model.setValueAt(data[0], rows.indexOf(list.get(addon.getName())), 0);
            model.setValueAt(data[1], rows.indexOf(list.get(addon.getName())), 1);
            model.setValueAt(data[2], rows.indexOf(list.get(addon.getName())), 2);
            model.setValueAt(data[3], rows.indexOf(list.get(addon.getName())), 3);
            return model;
        }
    }

    public class watchedFile implements java.lang.Runnable {

        private boolean active;

        private java.io.File file;

        private final java.util.ArrayList<de.idrinth.waraddonclient.implementation.model.ActualAddon> list = new java.util.ArrayList();

        /**
         * Adds an addon to watch an be handled here
         *
         * @param addon
         */
        public void addAddon(de.idrinth.waraddonclient.implementation.model.ActualAddon addon) {
            list.add(addon);
        }

        /**
         * sets a file up for uploading checks
         *
         * @param file
         */
        public void setFileToProcess(java.io.File file2process) {
            while (active) {
                de.idrinth.waraddonclient.implementation.service.Sleeper.sleep(100);
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
            list.stream().forEach((addon) -> {
                addon.fileWasChanged(file);
            });
            active = false;
        }
    }
}
