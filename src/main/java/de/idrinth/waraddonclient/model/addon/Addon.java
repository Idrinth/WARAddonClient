package de.idrinth.waraddonclient.model.addon;

import java.io.IOException;
import java.util.List;

public interface Addon {

    List<String> getTags();

    boolean hasTag(String tag);

    String getVersion();

    String getInstalled();

    String getDescription(String language);

    String getName();

    String getStatus();

    void install() throws IOException;

    void uninstall() throws IOException;

    String getFile();

    String getReason();

    String getUrl();
    
    boolean showSettings();
    
    Object[] getTableRow();

    int getEndorsements();

    int getDownloads();
}
