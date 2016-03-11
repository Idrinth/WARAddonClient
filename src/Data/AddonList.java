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
package Data;

/**
 *
 * @author Björn Büttner
 */
public class AddonList implements java.lang.Runnable {

    protected javax.swing.JTable AddonList;
    protected Service.FileWatcher watcher;
    protected java.util.HashMap<String, Addon> list = new java.util.HashMap();
    protected java.util.ArrayList<Addon> rows = new java.util.ArrayList();
    protected User user;
    protected java.util.Hashtable<String, watchedFile> watchedFilesMap = new java.util.Hashtable();
    protected Service.Request request;
    private int duration = 15;
    private long lastRefreshed = 0;

    public AddonList(javax.swing.JTable AddonList, User user, Service.Request request) {
        this.AddonList = AddonList;
        this.user = user;
        this.request = request;
    }

    public void setDuration(int dur) {
        duration = dur;
    }

    public void setWatcher(Service.FileWatcher watch) {
        if (watcher == null) {
            watcher = watch;
        }
    }

    public Addon get(int i) {
        return (Addon) rows.get(i);
    }

    public Addon get(String name) {
        return (Addon) list.get(name);
    }

    public int size() {
        return list.size();
    }

    public void add(Addon addon) {
        list.put(addon.getName(), addon);
        rows.add(addon);
    }

    public java.util.Hashtable<String, watchedFile> getWatchedFiles() {
        return watchedFilesMap;
    }

    public void run() {
        while (true) {
            while (System.currentTimeMillis() < lastRefreshed + duration * 60000) {
                try {
                    Thread.sleep(250);
                } catch (java.lang.InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Updating...");
            javax.json.JsonArray parse = request.getAddonList();
            if (parse != null) {
                parseJsonResult(parse);
            }
            lastRefreshed = System.currentTimeMillis();
        }
    }

    protected void parseJsonResult(javax.json.JsonArray parse) {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) this.AddonList.getModel();
        int counter = 0;
        while (parse.size() > counter) {
            Data.Addon addon = new Data.Addon(parse.getJsonObject(counter), user, request);
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

        boolean active = false;
        java.io.File file;
        java.util.ArrayList<Data.Addon> list = new java.util.ArrayList();

        public void addAddon(Data.Addon addon) {
            list.add(addon);
        }

        public void setFileToProcess(java.io.File file) {
            while (active) {
                try {
                    Thread.sleep(100);
                } catch (java.lang.InterruptedException exception) {
                    System.out.println(exception.getMessage());
                }
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
            for (Data.Addon addon : list) {
                addon.fileWasChanged(file);
            }
            active = false;
        }
    }
}
