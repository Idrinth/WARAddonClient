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

    public void create(ProgressReporter reporter) throws IOException, InterruptedException {
        reporter.incrementMax(3);
        String warDir = config.getWARPath();
        java.io.File folder = new java.io.File(warDir+"/backups");
        while (!folder.exists()) {
            if (folder.mkdirs()) {
                Thread.sleep(1000);
            }
        }
        reporter.incrementCurrent();
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        File file = new java.io.File(folder+"/"+ now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")) +".zip");
        try (net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(file)) {
            File user = new File(warDir + "/user");
            if (user.exists()) {
                zip.addFolder(user);
            }
            reporter.incrementCurrent();
            File addons = new File(warDir + "/Interface");
            if (addons.exists()) {
                zip.addFolder(addons);
            }
            reporter.incrementCurrent();
        }
    }

    public void restore(java.io.File backup, ProgressReporter reporter) throws IOException, InterruptedException {
        reporter.incrementMax(4);
        create(reporter);
        String warDir = config.getWARPath();
        try (net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(backup)) {
            reporter.incrementCurrent();
            Utils.emptyFolder(new File(warDir + "/Interface"));
            reporter.incrementCurrent();
            Utils.emptyFolder(new File(warDir + "/user"));
            reporter.incrementCurrent();
            zip.extractAll(warDir);
            reporter.incrementCurrent();
        }
    }
}
