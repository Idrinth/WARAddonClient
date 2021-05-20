package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.service.BaseLogger;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class CmdAddonList extends AddonList {

    public CmdAddonList(Request client, BaseLogger logger, XmlParser parser, Config config) {
        super(client, logger, parser, config);
    }

    @Override
    public void run() {
        processAddonDir();
        try {
            new JsonProcessor(client.getAddonList()).run();
        } catch (IOException exception) {
            logger.error(exception);
        }
    }

    public void install (String addonName) {
        AtomicBoolean found = new AtomicBoolean(false);
        rows.stream().filter(addon -> (addon.getName().equals(addonName))).forEachOrdered(addon -> {
            try {
                addon.install();
                found.set(true);
            } catch (IOException ex) {
                logger.error(ex);
            }
        });
        if (!found.get()) {
            logger.error("No addon found matching name: " + addonName);
        }
    }

    public void remove (String addonName) {
        AtomicBoolean found = new AtomicBoolean(false);
        rows.stream().filter(addon -> (addon.getName().equals(addonName))).forEachOrdered(addon -> {
            try {
                addon.uninstall();
                found.set(true);
            } catch (IOException ex) {
                logger.error(ex);
            }
        });
        if (!found.get()) {
            logger.error("No addon found matching name: " + addonName);
        }
    }

    public void update () {
        rows.stream().filter(addon -> (addon.getStatus().equals("X"))).forEachOrdered(addon -> {
            try {
                addon.install();
            } catch (IOException ex) {
                logger.error(ex);
            }
        });
    }
    
}
