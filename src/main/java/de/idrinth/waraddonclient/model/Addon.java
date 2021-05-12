package de.idrinth.waraddonclient.model;

import java.io.IOException;
import java.util.List;

public interface Addon {

    public List<String> getTags();

    public boolean hasTag(String tag);

    public String getVersion();

    public String getInstalled();

    public de.idrinth.waraddonclient.model.AddonSettings getUploadData();

    public String getDescription(String language);

    public String getName();

    public String getStatus();

    public void install() throws IOException;

    public void uninstall() throws IOException;
}
