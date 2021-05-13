package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.Config;
import de.idrinth.waraddonclient.Utils;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import net.lingala.zip4j.exception.ZipException;

public class Backup {
    private final Config config;
    public Backup(Config config) {
        this.config = config;
    }

    public void create() throws ZipException {
        String warDir = config.getWARPath();
        java.io.File folder = new java.io.File(warDir+"/backups");
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        File file = new java.io.File(warDir+"/backups/"+ now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")) +".zip");
        net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(file);
        zip.addFolder(new java.io.File(warDir+"/user"));
        zip.addFolder(new java.io.File(warDir+"/Interface"));
    }

    public void restore(java.io.File backup) throws ZipException, IOException {
        create();
        String warDir = config.getWARPath();
        net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(backup);
        Utils.emptyFolder(new java.io.File(warDir+"/Interface"));
        Utils.emptyFolder(new java.io.File(warDir+"/user"));
        zip.extractAll(warDir);
    }
}
