package de.idrinth.waraddonclient.backup;

public class Backup {
    public static void create() throws net.lingala.zip4j.exception.ZipException {
        java.io.File folder = new java.io.File("backups");
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.io.File file = new java.io.File("backups/"+ now.format(java.time.format.DateTimeFormatter.ofPattern("YYYY-MM-dd-HH-mm")) +".zip");
        net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(file);
        zip.addFolder(new java.io.File("user"));
        zip.addFolder(new java.io.File("Interface"));
    }

    public static void restore(java.io.File backup) throws net.lingala.zip4j.exception.ZipException {
        create();
        net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(backup);
        emptyFolder(new java.io.File("Interface"));
        emptyFolder(new java.io.File("user"));
        zip.extractAll(".");
    }

    private static void emptyFolder(java.io.File folder) {
        if (folder == null || !folder.exists()) {
            return;
        }
        for (java.io.File file : folder.listFiles()) {
            if (file.isDirectory()) {
                emptyFolder(file);
            }
            file.delete();
        }
    }
}
