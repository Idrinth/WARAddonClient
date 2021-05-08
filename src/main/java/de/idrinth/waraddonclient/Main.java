package de.idrinth.waraddonclient;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public final class Main {

    private static final String SUN_JAVA_COMMAND = "sun.java.command";

    private Main() {
        //not to be used
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            de.idrinth.factory.Logger.build().log("Starting", de.idrinth.Logger.LEVEL_INFO);
            de.idrinth.waraddonclient.factory.AddonList.build();
            de.idrinth.waraddonclient.factory.FileWatcher.build();
            de.idrinth.waraddonclient.factory.Interface.build().setVisible(true);
        });
    }

    public static void restart() throws IOException {
        try {
            StringBuilder cmd = new StringBuilder("\"" + System.getProperty("java.home") + "/bin/java\" ");
            ManagementFactory.getRuntimeMXBean().getInputArguments().stream().filter(arg -> (!arg.contains("-agentlib"))).map(arg -> {
                cmd.append(arg);
                return arg;
            }).forEachOrdered(item -> cmd.append(" "));

            String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
            cmd.append("-jar " );
            cmd.append(new File(mainCommand[0]).getPath());
            for (int i = 1; i < mainCommand.length; i++) {
                cmd.append(" ");
                cmd.append(mainCommand[i]);
            }
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        Runtime.getRuntime().exec(cmd.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.exit(0);
        } catch (Exception e) {
            throw new IOException("Error while trying to restart the application", e);
        }
    }
}
