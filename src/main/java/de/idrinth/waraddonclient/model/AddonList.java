package de.idrinth.waraddonclient.model;

import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.service.logger.BaseLogger;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.json.JsonArray;

public abstract class AddonList implements Runnable {

    protected final HashMap<String, Addon> list = new HashMap<>();

    protected final ArrayList<Addon> rows = new ArrayList<>();
    
    private final HashMap<String, String> unknowns = new CaseInsensitiveHashMap<>();
    
    protected final Request client;
    
    protected final BaseLogger logger;
    
    private final XmlParser parser;
    
    private final Config config;

    public AddonList(Request client, BaseLogger logger, XmlParser parser, Config config) {
        this.client = client;
        this.logger = logger;
        this.parser = parser;
        this.config = config;
    }
    
    protected void processAddonDir() {
        for (File folder : new File(config.getAddonFolder()).listFiles()) {
            try {
                add(new UnknownAddon(folder, client, logger, parser, config));
                unknowns.put(folder.getName(), rows.get(rows.size() - 1).getName());
            } catch (InvalidArgumentException ex) {
                logger.info(ex);
            }
        }
    }

    public Addon get(int position) {
        return rows.get(position);
    }

    public Addon get(String name) {
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
    public void add(Addon addon) {
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
                } catch (InvalidArgumentException ex) {
                    logger.error(ex);
                }
            }
        }

        protected void newAddon(ActualAddon addon) {
            add(addon);
        }

        private void processJsonAddon(ActualAddon addon) {
            if (list.containsKey(addon.getName())) {
                existingAddon(addon);
                return;
            }
            if (unknowns.containsKey(addon.getName())) {
                unknownAddon(addon);
                return;
            }
            newAddon(addon);
        }

        protected void existingAddon(ActualAddon addon) {
            if (ActualAddon.class.isInstance(list.get(addon.getName()))) {
                ((ActualAddon) list.get(addon.getName())).update(addon);
                return;
            }
            rows.set(rows.indexOf(list.get(addon.getName())), addon);
            list.put(addon.getName(), addon);
        }

        protected void unknownAddon(ActualAddon addon) {
            String name = unknowns.get(addon.getName());
            rows.set(rows.indexOf(list.get(name)), addon);
            list.put(addon.getName(), addon);
            list.remove(name);
        }
    }
}
