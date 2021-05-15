package de.idrinth.waraddonclient.service;

import de.idrinth.waraddonclient.Config;
import java.io.File;
import java.io.IOException;

public class Restarter {
    private final String baseDir;
    public Restarter(Config config) {
        baseDir = config.getJarDir().getAbsolutePath();
    }
    public void restart() throws IOException
    {
       try {
            File jar = new File(baseDir + "/WARAddonClient.jar");
            File exe = new File(baseDir + "/WARAddonClient.exe");
            if (exe.exists()) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        try {
                            Runtime.getRuntime().exec(exe.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                System.exit(0);
            } else if (jar.exists()) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        try {
                            Runtime.getRuntime().exec("java -jar "+jar.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                System.exit(0);
            }
            throw new IOException("Executable not found, did you replace it?");
        } catch (IOException e) {
            throw new IOException("Error while trying to restart the application", e);
        }
    }
}
