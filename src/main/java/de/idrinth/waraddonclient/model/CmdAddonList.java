package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.Config;
import de.idrinth.waraddonclient.service.FileLogger;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.IOException;

public class CmdAddonList extends AddonList {

    private String options;

    public CmdAddonList(Request client, FileLogger logger, XmlParser parser, Config config, String option) {
        super(client, logger, parser, config);
        options = option;
    }

    @Override
    public void run() {
        try {
            new JsonProcessor(client.getAddonList()).run();
        } catch (IOException exception) {
            logger.error(exception);
        }
        if (options.startsWith("update")) {
            rows.stream().filter(addon -> (addon.getStatus().equals("X"))).forEachOrdered(addon -> {
                try {
                    addon.install();
                } catch (IOException ex) {
                    logger.error(ex);
                }
            });
        }
        else if (options.startsWith("install")) {
            rows.stream().filter(addon -> (addon.getName().equals(options.replace("install", "").trim()))).forEachOrdered(addon -> {
                try {
                    addon.install();
                } catch (IOException ex) {
                    logger.error(ex);
                }
            });
        }
        else if (options.startsWith("remove")) {
            rows.stream().filter(addon -> (addon.getName().equals(options.replace("remove", "").trim()))).forEachOrdered(addon -> {
                try {
                    addon.uninstall();
                } catch (IOException ex) {
                    logger.error(ex);
                }
            });
        }

    }
    
}
