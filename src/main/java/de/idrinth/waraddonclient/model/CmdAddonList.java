package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.Config;
import de.idrinth.waraddonclient.service.FileLogger;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.IOException;

public class CmdAddonList extends AddonList {
    
    public CmdAddonList(Request client, FileLogger logger, XmlParser parser, Config config) {
        super(client, logger, parser, config);
    }

    @Override
    public void run() {
        try {
            new JsonProcessor(client.getAddonList()).run();
        } catch (IOException exception) {
            logger.error(exception);
        }
        rows.stream().filter(addon -> (addon.getStatus().equals("X"))).forEachOrdered(addon -> {
            try {
                addon.install();
            } catch (IOException ex) {
                logger.error(ex);
            }
        });
    }
    
}
