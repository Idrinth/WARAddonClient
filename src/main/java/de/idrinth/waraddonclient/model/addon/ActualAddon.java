package de.idrinth.waraddonclient.model.addon;

import com.github.zafarkhaja.semver.Version;
import de.idrinth.waraddonclient.service.Config;
import de.idrinth.waraddonclient.Utils;
import de.idrinth.waraddonclient.model.InvalidArgumentException;
import de.idrinth.waraddonclient.service.logger.BaseLogger;
import de.idrinth.waraddonclient.service.Request;
import de.idrinth.waraddonclient.service.SilencingErrorHandler;
import de.idrinth.waraddonclient.service.XmlParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.swing.JEditorPane;
import javax.xml.parsers.FactoryConfigurationError;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ActualAddon implements de.idrinth.waraddonclient.model.addon.Addon {

    private HashMap<String, String> descriptions = new HashMap<>();
    
    private boolean hasSettings = false;

    private String file = "";

    private String reason = "";

    private String url = "";

    private String version;

    private final String slug;

    private final String name;

    private int endorsements;

    private int downloads;

    private String installed = "-";

    private ArrayList<String> tags = new ArrayList<>();

    private final Request client;
    
    private final BaseLogger logger;
    
    private final XmlParser parser;
    
    private final Config config;
    
    private String defaultDescription = "";
    
    private boolean loadedDescription = false;

    public ActualAddon(javax.json.JsonObject addon, Request client, BaseLogger logger, XmlParser parser, Config config) throws InvalidArgumentException {
        if (addon == null) {
            throw new InvalidArgumentException("Addon is null");
        }
        this.client = client;
        this.logger = logger;
        this.parser = parser;
        this.config = config;
        version = getStringFromObject("version", addon);
        slug = getStringFromObject("slug", addon);
        name = getStringFromObject("name", addon);
        try {
            downloads = Integer.parseInt(getStringFromObject("downloads", addon));
        } catch (NumberFormatException ex) {
            downloads = 0;
        }
        try {
            endorsements = Integer.parseInt(getStringFromObject("endorsements", addon));
        } catch (NumberFormatException ex) {
            endorsements = 0;
        }
        JsonArray tagList = addon.getJsonArray("tags");
        int counter = 0;
        while (tagList.size() > counter) {
            tags.add(tagList.getString(counter));
            counter++;
        }
        new VersionFinder(find(name)).run();
        refresh();
    }

    private File find(String name) {
        File folder = new File(config.getAddonFolder());
        if (folder.exists()) {
            for (File innerFolder : Objects.requireNonNull(folder.listFiles())) {
                if (innerFolder.getName().equalsIgnoreCase(name)) {
                    return innerFolder;
                }

            }
        }
        return new File(config.getAddonFolder() + name);
    }

    public java.util.ArrayList<String> getTags() {
        return tags;
    }

    /**
     * does this addon have the given tag?
     *
     * @param tag
     * @return boolean
     */
    public boolean hasTag(String tag) {
        return tags.stream().anyMatch(tag::equalsIgnoreCase);
    }

    /**
     * updates this instance with data from another instance
     *
     * @param addon
     */
    public void update(ActualAddon addon) {
        version = addon.getVersion();
        tags = addon.getTags();
        descriptions = addon.getDescriptions();
    }

    /**
     * the avaible version
     *
     * @return String
     */
    public String getVersion() {
        return version;
    }

    public String getInstalled() {
        return installed;
    }

    /**
     * try to find a string in a jsonObject
     *
     * @param key
     * @param data
     * @return String
     */
    private String getStringFromObject(String key, javax.json.JsonObject data) {
        if (key != null && data != null && data.containsKey(key) && !data.isNull(key)) {
            return java.util.regex.Pattern.compile("^\"|\"$").matcher(data.get(key).toString()).replaceAll("");
        }
        return "";
    }

    /**
     * get the table row configuration for this addon
     *
     * @return String[[
     */
    public Object[] getTableRow() {
        Object[] row = new Object[6];
        row[0] = getStatus();
        row[1] = name;
        row[2] = version;
        row[3] = installed;
        row[4] = endorsements;
        row[5] = downloads;
        return row;
    }

    public String getDescription(String language) {
        String description = "<p><strong>There is currently no Description for " + name + ".</strong></p>"
                + "<p>You can help by adding one at <a href=\"http://tools.idrinth.de/addons/" + slug
                + "/\">http://tools.idrinth.de/addons/" + slug + "/</a>.</p>"
                + "<p>"+ defaultDescription +"</p>";
        if (descriptions.containsKey(language) && !descriptions.get(language).isEmpty()) {
            description = descriptions.get(language);
        } else if (descriptions.get("en") != null &&  !descriptions.get("en").isEmpty()) {
            description = descriptions.get("en");
        }
        return "<html>" + description.replaceAll("(\\\\n|\\\\r)+", " ").replaceAll("\\\\\"", "\"");
    }

    public Runnable loadDescription(JEditorPane field, String language) {
        if (!descriptions.isEmpty() || loadedDescription) {
            return () -> {};
        }
        loadedDescription = true;
        return () -> {
            try {
                JsonObject addon = client.getAddon(slug);
                descriptions.put("en", getStringFromObject("description", addon));
                descriptions.put("de", getStringFromObject("description_de", addon));
                descriptions.put("fr", getStringFromObject("description_fr", addon));
                field.setText(getDescription(language));
            } catch (IOException ex) {
                loadedDescription = false;
                logger.error("Failed loading addon-data from server.");
            }
        };
    }

    /**
     * get a list of descriptions
     *
     * @return java.util.HashMap
     */
    public java.util.HashMap<String, String> getDescriptions() {
        return descriptions;
    }

    public String getName() {
        return name;
    }

    public void install() throws IOException {
        (new Updater()).run(find(name), true);
    }

    public void uninstall() throws IOException {
        (new Updater()).run(find(name), false);
    }

    public void fileWasChanged(File changedFile) {
        if (config.isEnabled(name) && changedFile.isFile() && changedFile.getName().equalsIgnoreCase(file)) {
            try {
                client.upload(url, changedFile);
            } catch (Exception exception) {
                logger.warn(exception);
            }
        }
    }

    @Override
    public String getStatus() {
        if("-".equals(getInstalled())) {
            return " ";
        }
        try {
            Version remote = Version.valueOf(getVersion());
            Version local = Version.valueOf(getInstalled());
            if (remote.equals(local)) {
                return "✓";
            }
            if (remote.lessThan(local)) {
                return "?";
            }
        } catch (com.github.zafarkhaja.semver.ParseException e) {
            return "X";
        }
        return "X";
    }

    @Override
    public int getEndorsements() {
        return endorsements;
    }

    @Override
    public int getDownloads() {
        return downloads;
    }

    private class Updater {
        public void run(File folder, boolean redeploy) throws IOException {
            uninstall(folder);
            if (redeploy) {
                install();
            }
            refresh();
            config.setEnabled(name, false);
        }

        private void uninstall(File addonFolder) throws IOException {
            try {
                File versionFile = new File(addonFolder.getPath() + config.getVersionFile());
                if (!versionFile.exists()) {
                    File zip = getZip();
                    writeMetaDataFile(new ZipFile(zip));
                    FileUtils.deleteQuietly(zip);
                }
                NodeList list = parser.parse(versionFile).getElementsByTagName("folder");
                if (list.getLength() == 0) {
                    File zip = getZip();
                    writeMetaDataFile(new ZipFile(zip));
                    FileUtils.deleteQuietly(zip);
                    list = parser.parse(versionFile).getElementsByTagName("folder");
                }
                for (int i=0;i<list.getLength();i++) {
                    Utils.deleteFolder(new File(config.getAddonFolder() + list.item(i).getTextContent()));
                }
            } catch (IOException | SAXException e) {
                logger.warn(e);
            }
            Utils.deleteFolder(addonFolder);
            installed="-";
        }

        private java.io.File getZip() throws IOException {
            File zip = new File(System.getProperty("java.io.tmpdir") + "/" + slug + ".zip");
            try (InputStream stream = client.getAddonDownload(slug + "/download/" + version.replace(".", "-") + "/")) {
                FileUtils.copyInputStreamToFile(stream, zip);
            }
            return zip;
        }

        private void install() throws IOException {
            File zip = getZip();
            ZipFile zipFile = new ZipFile(zip);
            zipFile.extractAll(config.getAddonFolder());
            writeMetaDataFile(zipFile);
            FileUtils.deleteQuietly(zip);
            installed=version;
        }

        private void writeMetaDataFile(ZipFile zipFile) throws IOException {
            File tmp = new File(System.getProperty("java.io.tmpdir") + "/waraddonfolder/" + name);
            Utils.deleteFolder(tmp);
            tmp.mkdirs();
            zipFile.extractAll(tmp.getAbsolutePath());
            StringBuilder sb = new StringBuilder();
            ArrayList<String> folders = new ArrayList<>();
            for (File folder : Objects.requireNonNull(tmp.listFiles())) {
                sb.append("<folder>");
                sb.append(folder.getName());
                sb.append("</folder>");
                folders.add(folder.getName());
            }
            Utils.deleteFolder(tmp);
            File target = find(name);
            folders.add(target.getName());
            target.mkdirs();
            for (String folder : folders) {
                FileUtils.writeStringToFile(
                    new File(config.getAddonFolder() + folder + config.getVersionFile()),
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><UiMod>"
                    + "<name>" + name + "</name>"
                    + "<version>" + version + "</version>"
                    + "<folders>" + sb + "</folders>"
                    + "</UiMod>",
                    StandardCharsets.UTF_8
                );
            }
        }
    }

    private class VersionFinder {

        private final File folder;

        public VersionFinder(File folder) {
            this.folder = folder;
        }

        private boolean processDirectory() {
            for (java.io.File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (!fileEntry.isDirectory()
                        && org.apache.commons.io.FilenameUtils.getExtension(fileEntry.getName()).equalsIgnoreCase("mod")) {
                    try {
                        Document doc = parser.parse(fileEntry, new SilencingErrorHandler(logger));
                        NodeList list = doc.getElementsByTagName("UiMod");
                        installed = list.item(0).getAttributes().getNamedItem("version").getTextContent();
                        NodeList description = doc.getElementsByTagName("Description");
                        if (description.getLength() > 0) {
                            defaultDescription = description.item(0).getAttributes().getNamedItem("text").getTextContent();
                        }
                        return true;
                    } catch (FactoryConfigurationError | SAXException | IOException exception) {
                        logger.warn(exception);
                    }
                }
            }
            return false;
        }

        /**
         * tries to find and set the default version
         */
        private boolean getDownloadVersion() {
            File versionFile = new File(folder.getPath() + config.getVersionFile());
            if (versionFile.exists()) {
                try {
                    NodeList list = parser.parse(versionFile).getElementsByTagName("version");
                    installed = list.item(0).getTextContent().replace("(sys)", "");
                    return true;
                } catch (FactoryConfigurationError | SAXException | IOException exception) {
                    logger.warn(exception);
                }
            }
            return false;
        }

        /**
         * starts searching for an installed version
         */
        public void run() {
            if (folder == null || !folder.exists()) {
                return;
            }
            if (!getDownloadVersion() && !processDirectory()) {
                installed = "-";
            }
        }
    }
    
    public boolean showSettings() {
        return hasSettings;
    }

    public final void refresh() {
        file = "";
        reason = "";
        url = "";
        hasSettings = false;
        File folder = find(name);
        if (folder.exists()) {
            for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                processFile(fileEntry);
                if (hasSettings) {
                    return;
                }
            }
        }
    }

    private void processFile(java.io.File fileEntry) {
        if (fileEntry.isDirectory()) {
            return;
        }
        if (!"upload.idrinth".equalsIgnoreCase(fileEntry.getName())) {
            return;
        }
        try {
            NodeList list = parser.parse(fileEntry).getFirstChild().getChildNodes();
            for (int counter = 0; counter < list.getLength(); counter++) {
                processNode(list.item(counter));
            }
            hasSettings = true;
        } catch (FactoryConfigurationError | SAXException | IOException exception) {
            logger.error(exception);
        }
    }

    private void processNode(org.w3c.dom.Node item) {
        if ("file".equalsIgnoreCase(item.getNodeName())) {
            file = item.getTextContent();
            return;
        }
        if ("url".equalsIgnoreCase(item.getNodeName())) {
            url = item.getTextContent();
            return;
        }
        if ("reason".equalsIgnoreCase(item.getNodeName())) {
            reason = item.getTextContent();
        }
    }
    @Override
    public String getFile() {
        return file;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
