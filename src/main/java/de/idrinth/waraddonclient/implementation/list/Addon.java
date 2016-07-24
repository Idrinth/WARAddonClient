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

public class Addon implements java.lang.Runnable {

    protected java.util.HashMap<String, de.idrinth.waraddonclient.implementation.model.Addon> list = new java.util.HashMap();
    protected java.util.ArrayList<de.idrinth.waraddonclient.implementation.model.Addon> rows = new java.util.ArrayList();
    protected java.util.Hashtable<String, watchedFile> watchedFilesMap = new java.util.Hashtable();
    private int duration = 15;
    private long lastRefreshed = 0;

    public void setDuration(int dur) {
        duration = dur;
    }

    public de.idrinth.waraddonclient.implementation.model.Addon get(int i) {
        return (de.idrinth.waraddonclient.implementation.model.Addon) rows.get(i);
    }

    public de.idrinth.waraddonclient.implementation.model.Addon get(String name) {
        return (de.idrinth.waraddonclient.implementation.model.Addon) list.get(name);
    }

    public int size() {
        return list.size();
    }

    public void add(de.idrinth.waraddonclient.implementation.model.Addon addon) {
        list.put(addon.getName(), addon);
        rows.add(addon);
    }

    public java.util.Hashtable<String, watchedFile> getWatchedFiles() {
        return watchedFilesMap;
    }

    public void run() {
        int failuresInARow = 0;
        while (true) {
            while (System.currentTimeMillis() < lastRefreshed + duration * 60000) {
                de.idrinth.waraddonclient.implementation.service.Sleeper.sleep(250);
            }
            try {
                javax.json.JsonArray parse = de.idrinth.waraddonclient.factory.RemoteRequest.build().getAddonList();
                if (parse != null) {
                    parseJsonResult(parse);
                }
                failuresInARow = 0;
            } catch (Exception exception) {
                de.idrinth.factory.Logger.build().log(exception.getMessage(), de.idrinth.Logger.levelError);
                failuresInARow++;
                if (failuresInARow > 5) {
                    /**
                     * @todo find a nicer option
                     */
                    System.exit(72);
                    return;
                }
            }
            lastRefreshed = System.currentTimeMillis();
        }
    }

    protected void parseJsonResult(javax.json.JsonArray parse) {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) de.idrinth.waraddonclient.factory.Interface.build().getAddonTable().getModel();
        int counter = 0;
        while (parse.size() > counter) {
            de.idrinth.waraddonclient.implementation.model.Addon addon = new de.idrinth.waraddonclient.implementation.model.Addon(parse.getJsonObject(counter));
            if (list.containsKey(addon.getName())) {
                list.get(addon.getName()).update(addon);
                String[] data = list.get(addon.getName()).getTableRow();
                model.setValueAt(data[0], rows.indexOf(list.get(addon.getName())), 0);
                model.setValueAt(data[1], rows.indexOf(list.get(addon.getName())), 1);
                model.setValueAt(data[2], rows.indexOf(list.get(addon.getName())), 2);
            } else {
                this.add(addon);
                model.addRow(addon.getTableRow());
                if (!addon.getUploadData().getFile().isEmpty()) {
                    if (!watchedFilesMap.containsKey(addon.getUploadData().getFile().toLowerCase())) {
                        watchedFilesMap.put(addon.getUploadData().getFile().toLowerCase(), new watchedFile());
                    }
                    watchedFilesMap.get(addon.getUploadData().getFile().toLowerCase()).addAddon(addon);
                }
            }
            counter++;
        }
    }

    public class watchedFile implements java.lang.Runnable {

        protected boolean active = false;
        protected java.io.File file;
        protected java.util.ArrayList<de.idrinth.waraddonclient.implementation.model.Addon> list = new java.util.ArrayList();

        public void addAddon(de.idrinth.waraddonclient.implementation.model.Addon addon) {
            list.add(addon);
        }

        public void setFileToProcess(java.io.File file) {
            while (active) {
                de.idrinth.waraddonclient.implementation.service.Sleeper.sleep(100);
            }
            active = true;
            this.file = file;
            new java.lang.Thread(this).start();
        }

        public void run() {
            if (file == null || !file.exists()) {
                active = false;
                return;
            }
            for (de.idrinth.waraddonclient.implementation.model.Addon addon : list) {
                addon.fileWasChanged(file);
            }
            active = false;
        }
    }
}
