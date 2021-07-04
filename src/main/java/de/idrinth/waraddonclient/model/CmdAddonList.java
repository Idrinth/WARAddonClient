package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.service.ProgressReporter;
import de.idrinth.waraddonclient.service.logger.BaseLogger;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class CmdAddonList extends AddonList {

    private final ProgressReporter reporter;

    public CmdAddonList(Request client, BaseLogger logger, XmlParser parser, Config config, ProgressReporter reporter) {
        super(client, logger, parser, config);
        this.reporter = reporter;
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
        reporter.start("Install", () -> {
            if (!found.get()) {
                logger.error("No addon found matching name: " + addonName);
            }
        });
        rows.stream().filter(addon -> (addon.getName().equals(addonName))).forEachOrdered(addon -> {
            addon.install(reporter);
            found.set(true);
        });
        reporter.stop();
    }

    public void remove (String addonName) {
        AtomicBoolean found = new AtomicBoolean(false);
        reporter.start("Remove", () -> {
            if (!found.get()) {
                logger.error("No addon found matching name: " + addonName);
            }
        });
        rows.stream().filter(addon -> (addon.getName().equals(addonName))).forEachOrdered(addon -> {
            addon.uninstall(reporter);
            found.set(true);
        });
        reporter.stop();
    }

    public void update () {
        reporter.start("Updating All", () -> {});
        rows.stream().filter(addon -> (addon.getStatus().equals("X"))).forEachOrdered(addon -> {
            addon.install(reporter);
        });
        reporter.stop();
    }
    
}
