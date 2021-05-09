package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.Config;
import de.idrinth.waraddonclient.Utils;

public class Backup {
    private Backup() {
        //no construction here
    }

    public static void create() throws net.lingala.zip4j.exception.ZipException {
        String warDir = Config.getWARPath();
        java.io.File folder = new java.io.File(warDir+"/backups");
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.io.File file = new java.io.File(warDir+"/backups/"+ now.format(java.time.format.DateTimeFormatter.ofPattern("YYYY-MM-dd-HH-mm")) +".zip");
        net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(file);
        zip.addFolder(new java.io.File(warDir+"/user"));
        zip.addFolder(new java.io.File(warDir+"/Interface"));
    }

    public static void restore(java.io.File backup) throws net.lingala.zip4j.exception.ZipException {
        create();
        String warDir = Config.getWARPath();
        net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(backup);
        Utils.emptyFolder(new java.io.File(warDir+"/Interface"));
        Utils.emptyFolder(new java.io.File(warDir+"/user"));
        zip.extractAll(warDir);
    }
}
