package de.idrinth.waraddonclient;

import de.idrinth.waraddonclient.cli.AliasOption;
import de.idrinth.waraddonclient.cli.AliasParser;
import de.idrinth.waraddonclient.service.*;
import de.idrinth.waraddonclient.model.CmdAddonList;
import de.idrinth.waraddonclient.service.logger.CliLogger;
import de.idrinth.waraddonclient.service.logger.MultiLogger;

import javax.xml.parsers.ParserConfigurationException;

import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;

public class CliMain extends BaseMain {

    private final String[] args;

    public CliMain(String[] args) {
        this.args = args;
        add(new CliLogger(false));
    }

    protected void main(MultiLogger logger, Config config, Request client, FileSystem file) throws FileSystem.FileSystemException, ParserConfigurationException {
        Options options = new Options();
        options.addOption("v", "version", false, "Get the version of the WARAddonClient.");
        options.addOption("p", "verbose", false, "Print more messages to screen.");
        options.addOption(new AliasOption("s", "location", true, "Set the location of the WAR-Folder.", "setlocation", "set-location"));
        options.addOption(new AliasOption("u", "update-all", false, "Update all avaible Addons to the latest version.", "updateonly"));
        options.addOption("i", "install", true, "Install/Update given addon.");
        options.addOption("r", "remove", true, "Remove given addon.");
        options.addOption("h", "help", false, "This output.");
        options.addOption("b", "backup", false, "Backups all addons.");
        options.addOption("z", "restore", true, "Restores from a specified backup.");

        CommandLineParser parser = new AliasParser();
        try {
            CommandLine cli = parser.parse(options, args);
            logger.add(new CliLogger(cli.hasOption("verbose")));
            if (cli.hasOption("version")) {
                System.out.println(config.getVersion());
                return;
            }
            if (cli.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("waraddonclient", options, true);
                return;
            }
            if (cli.hasOption("location")) {
                config.setWARPath(cli.getOptionValue("location"));
                logger.info("Set location to "+config.getWARPath());
            }
            file.checkPosition();
            CmdAddonList addonList = new CmdAddonList(client, logger, new XmlParser(), config);
            addonList.run();
            if (cli.hasOption("update-all")) {
                addonList.update();
                logger.info("Updated all Add-Ons");
                return;
            }
            if (cli.hasOption("install")) {
                addonList.install(cli.getOptionValue("install"));
                logger.info("Installed " + cli.getOptionValue("install"));
                return;
            }
            if (cli.hasOption("remove")) {
                addonList.remove(cli.getOptionValue("remove"));
                logger.info("Removed " + cli.getOptionValue("remove"));
                return;
            }
            if (cli.hasOption("backup")) {
                Backup bk = new Backup(config);
                bk.create();
                logger.info("Created a backup");
                return;
            }
            if (cli.hasOption("restore")) {
                Backup bk = new Backup(config);
                File restore = new File(cli.getOptionValue("restore"));
                bk.restore(restore);
                logger.info("Restored from file " + restore.getAbsolutePath());
                return;
            }
        } catch(ParseException | IOException ex) {
            logger.error(ex);
        }
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("waraddonclient", options, true);
    }

    public void makeCommonOptions (Options options) {
        options.addOption("v", "version", false, "Get the version of the WARAddonClient.");
        options.addOption("s", "set-location", true, "Set the location of the WAR-Folder.");
        options.addOption("u", "update-all", false, "Update all avaible Addons to the latest version.");
        options.addOption("i", "install", true, "Install/Update given addon.");
        options.addOption("r", "remove", true, "Remove given addon.");
        options.addOption("h", "help", false, "This output.");
    }
}
