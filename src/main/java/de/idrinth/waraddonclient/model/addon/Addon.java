package de.idrinth.waraddonclient.model.addon;

import de.idrinth.waraddonclient.service.ProgressReporter;
import java.util.List;

public interface Addon {

    List<String> getTags();

    boolean hasTag(String tag);

    String getVersion();

    String getInstalled();

    String getDescription(String language);

    String getName();

    String getStatus();

    public void install(ProgressReporter progress);

    public void uninstall(ProgressReporter progress);

    String getFile();

    String getReason();

    String getUrl();
    
    boolean showSettings();
    
    Object[] getTableRow();

    int getEndorsements();

    int getDownloads();
}
