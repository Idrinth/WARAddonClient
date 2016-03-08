/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

/**
 *
 * @author BJ
 */
public class FileWatcher implements java.lang.Runnable{
    java.nio.file.WatchService watcher = null;
    Data.AddonList addons=null;

    @Override
    public void run() {
        try {
            java.nio.file.Path path = java.nio.file.FileSystems.getDefault().getPath("user", "settings");
            watcher = path.getFileSystem().newWatchService();
            java.nio.file.WatchEvent.Kind[] modes = new java.nio.file.WatchEvent.Kind[2];
            modes[0] = java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
            modes[1] = java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
            path.register(
                    watcher,
                    modes,
                    com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE);
            handleEvents();
        } catch (InterruptedException|java.io.IOException exception) {
            System.out.println(exception);
        }
    }
    protected void handleEvents() throws InterruptedException {
        while(true) {
            java.nio.file.WatchKey key = watcher.take();
            for (java.nio.file.WatchEvent event : key.pollEvents()) {
                if (event.kind() == java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
                        || event.kind() == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY) {
                    java.io.File file=new java.io.File("./user/settings/"+event.context().toString());
                    if(this.addons.getWatchedFiles().containsKey(file.getName().toLowerCase())) {
                        this.addons.getWatchedFiles().get(file.getName().toLowerCase()).setFileToProcess(file);
                    }
                }
            }
            key.reset();
        }
    }
    public FileWatcher(Data.AddonList addons) {
        this.addons=addons;
        addons.setWatcher(this);
    }
}
