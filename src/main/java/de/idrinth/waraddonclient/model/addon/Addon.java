package de.idrinth.waraddonclient.model.addon;

import java.io.IOException;
import java.util.List;

public interface Addon {

    public List<String> getTags();

    public boolean hasTag(String tag);

    public String getVersion();

    public String getInstalled();

    public String getDescription(String language);

    public String getName();

    public String getStatus();

    public void install() throws IOException;

    public void uninstall() throws IOException;

    public String getFile();

    public String getReason();

    public String getUrl();
    
    public boolean showSettings();
    
    public Object[] getTableRow();

    public int getEndorsements();

    public int getDownloads();
}
