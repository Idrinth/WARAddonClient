package de.idrinth.waraddonclient;

import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.model.CmdAddonList;
import de.idrinth.waraddonclient.service.FileLogger;
import de.idrinth.waraddonclient.service.FileSystem;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.XmlParser;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CliMain extends BaseMain {

    private final String[] args;

    public CliMain(String[] args) {
        this.args = args;
    }

    protected void main(FileLogger logger, Config config, Request client, FileSystem file) throws FileSystem.FileSystemException, ParserConfigurationException {
        
        Options options = new Options();
        options.addOption("v", "version", false, "Get the version of the WARAddonClient.");
        options.addOption("s", "set-location", true, "Set the location of the WAR-Folder.");
        options.addOption("u", "update-only", false, "Update all avaible Addons to the latest version.");
        options.addOption("i", "install", true, "Install/Update given addon.");
        options.addOption("r", "remove", true, "Remove given addon.");
        options.addOption("h", "help", false, "This output.");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cli = parser.parse(options, args);
            if (cli.hasOption("version")) {
                System.out.println(config.getVersion());
                return;
            }
            if (cli.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("waraddonclient", options, true);
                return;
            }
            if (cli.hasOption("set-location")) {
                config.setWARPath(cli.getOptionValue("set-location"));
                System.out.println("WAR-Path set.");
                return;
            }
            file.checkPosition();
            CmdAddonList addonList = new CmdAddonList(client, logger, new XmlParser(), config);
            addonList.run();
            if (cli.hasOption("update-only")) {
                addonList.update();
            }
            if (cli.hasOption("install")) {
                addonList.install(cli.getOptionValue("install"));
            }
            if (cli.hasOption("remove")) {
                addonList.remove(cli.getOptionValue("remove"));
            }
        } catch(ParseException ex) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("waraddonclient", options, true);
            this.error(ex);
        }
    }

    @Override
    public void error(Exception ex) {
        ex.printStackTrace();
    }
}
