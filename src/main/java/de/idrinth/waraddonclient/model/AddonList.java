package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.service.FileLogger;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.json.JsonArray;

public abstract class AddonList implements Runnable {

    protected final HashMap<String, ActualAddon> list = new HashMap<>();

    protected final ArrayList<ActualAddon> rows = new ArrayList<>();
    
    protected final Request client;
    
    protected final FileLogger logger;
    
    private final XmlParser parser;
    
    private final Config config;

    public AddonList(Request client, FileLogger logger, XmlParser parser, Config config) {
        this.client = client;
        this.logger = logger;
        this.parser = parser;
        this.config = config;
    }

    public ActualAddon get(int position) {
        return rows.get(position);
    }

    public ActualAddon get(String name) {
        return list.get(name);
    }

    /**
     * amount of addons handled
     *
     * @return int
     */
    public int size() {
        return list.size();
    }

    /**
     * adds an addon to the global list
     *
     * @param addon
     */
    public void add(de.idrinth.waraddonclient.model.ActualAddon addon) {
        list.put(addon.getName(), addon);
        rows.add(addon);
    }

    public class JsonProcessor {

        private final javax.json.JsonArray json;

        public JsonProcessor(JsonArray parse) {
            json = parse;
        }

        public void run() throws IOException {
            if (json == null) {
                throw new IOException("no content in json");
            }
            for (int counter = json.size(); counter > 0; counter--) {
                try {
                    processJsonAddon(new ActualAddon(json.getJsonObject(counter - 1), client, logger, parser, config));
                } catch (ActualAddon.InvalidArgumentException ex) {
                    logger.error(ex);
                }
            }
        }

        protected void newAddon(ActualAddon addon) {
            add(addon);
        }

        private void processJsonAddon(de.idrinth.waraddonclient.model.ActualAddon addon) {
            if (list.containsKey(addon.getName())) {
                existingAddon(addon);
                return;
            }
            newAddon(addon);
        }

        protected void existingAddon(ActualAddon addon) {
            list.get(addon.getName()).update(addon);
        }
    }
}
