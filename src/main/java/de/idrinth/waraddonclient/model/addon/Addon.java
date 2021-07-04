package de.idrinth.waraddonclient.model.addon;

import de.idrinth.waraddonclient.service.ProgressReporter;
import java.util.List;

public interface Addon {

    public List<String> getTags();

    public boolean hasTag(String tag);

    public String getVersion();

    public String getInstalled();

    public String getDescription(String language);

    public String getName();

    public String getStatus();

    public void install(ProgressReporter progress);

    public void uninstall(ProgressReporter progress);

    public String getFile();

    public String getReason();

    public String getUrl();
    
    public boolean showSettings();
    
    public Object[] getTableRow();

    public int getEndorsements();

    public int getDownloads();
}
