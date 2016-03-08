/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Service.FileWatcher;

/**
 *
 * @author BJ
 */
public class AddonList implements java.lang.Runnable {
    protected javax.swing.JTable AddonList;
    protected Service.FileWatcher watcher;
    protected java.util.ArrayList list=new java.util.ArrayList();
    protected User user;
    protected java.util.Hashtable<String,watchedFile> watchedFilesMap=new java.util.Hashtable();
    Web.Request request=new Web.Request();
    public AddonList(javax.swing.JTable AddonList,User user) {
        this.AddonList=AddonList;
        this.user=user;
    }
    public void setWatcher(Service.FileWatcher watch) {
        if(watcher==null) {
            watcher=watch;
        }
    }
    public Addon get(int i) {
        return (Addon) list.get(i);
    }
    public int size() {
        return list.size();
    }

    public boolean add(Addon addon) {
        return list.add(addon);
    }
    public java.util.Hashtable<String,watchedFile> getWatchedFiles() {
        return watchedFilesMap;
    }

    public void run() {
        javax.json.JsonArray parse = request.getAddonList();
        int counter = 0;
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) this.AddonList.getModel();
        if(parse==null) {
            try{
                Thread.sleep(250);
            } catch(java.lang.InterruptedException e){
                System.out.println(e.getMessage());
            }
            new java.lang.Thread(this).start();
            return;
        }
        while (parse.size() > counter) {
            Data.Addon addon=new Data.Addon(parse.getJsonObject(counter),user,request);
            list.add(addon);
            model.addRow(addon.getTableRow());
            if(!addon.getUploadData().getFile().isEmpty()) {
                if(!watchedFilesMap.containsKey(addon.getUploadData().getFile().toLowerCase())) {
                    watchedFilesMap.put(addon.getUploadData().getFile().toLowerCase(), new watchedFile());
                }
                watchedFilesMap.get(addon.getUploadData().getFile().toLowerCase()).addAddon(addon);
            }
            counter++;
        }
    }
    public class watchedFile implements java.lang.Runnable{
        boolean active=false;
        java.io.File file;
        java.util.ArrayList<Data.Addon> list=new java.util.ArrayList();
        public void addAddon(Data.Addon addon) {
            list.add(addon);
        }
        public void setFileToProcess(java.io.File file) {
            while(active) {
                try{
                    Thread.sleep(100);
                } catch(java.lang.InterruptedException exception) {
                    System.out.println(exception.getMessage());
                }
            }
            active=true;
            this.file=file;
            new java.lang.Thread(this).start();
        }
        public void run() {
            if(file==null||!file.exists()) {
                active=false;
                return;
            }
            for(Data.Addon addon:list) {
                addon.fileWasChanged(file);
            }
            active=false;
        }
    }
}
