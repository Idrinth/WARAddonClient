package de.idrinth.waraddonclient.service;

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

    public void create(ProgressReporter reporter) throws ZipException {
        reporter.incrementMax(3);
        String warDir = config.getWARPath();
        java.io.File folder = new java.io.File(warDir+"/backups");
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        reporter.incrementCurrent();
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        File file = new java.io.File(warDir+"/backups/"+ now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")) +".zip");
        net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(file);
        zip.addFolder(new java.io.File(warDir+"/user"));
        reporter.incrementCurrent();
        zip.addFolder(new java.io.File(warDir+"/Interface"));
        reporter.incrementCurrent();
    }

    public void restore(java.io.File backup, ProgressReporter reporter) throws IOException {
        reporter.incrementMax(3);
        create(reporter);
        String warDir = config.getWARPath();
        net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(backup);
        reporter.incrementCurrent();
        Utils.emptyFolder(new java.io.File(warDir+"/Interface"));
        reporter.incrementCurrent();
        Utils.emptyFolder(new java.io.File(warDir+"/user"));
        reporter.incrementCurrent();
        zip.extractAll(warDir);
        reporter.incrementCurrent();
    }
}
