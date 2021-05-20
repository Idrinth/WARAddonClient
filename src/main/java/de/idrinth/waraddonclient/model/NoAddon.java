package de.idrinth.waraddonclient.model;

import java.io.IOException;

public class NoAddon implements de.idrinth.waraddonclient.model.Addon {

    @Override
    public java.util.ArrayList<String> getTags() {
        return new java.util.ArrayList<>();
    }

    @Override
    public boolean hasTag(String tag) {
        return false;
    }

    @Override
    public String getVersion() {
        return "";
    }

    @Override
    public String getInstalled() {
        return "";
    }

    @Override
    public String getDescription(String language) {
        return "<html><h1>Welcome to the client.</h1><p>To get something more useful here, select an addon to the left.</p>";
    }

    @Override
    public String getName() {
        return "Welcome";
    }

    @Override
    public void install() throws IOException {
        throw new UnsupportedOperationException("You can not uninstall an addon that doesn't exist.");
    }

    @Override
    public void uninstall() throws IOException {
        throw new UnsupportedOperationException("You can not uninstall an addon that doesn't exist");
    }

    @Override
    public String getStatus() {
        return "unknown";
    }

    @Override
    public String getFile() {
        return "";
    }

    @Override
    public String getReason() {
        return "";
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public boolean showSettings() {
        return false;
    }

    @Override
    public String[] getTableRow() {
        String[] row = new String[4];
        row[0] = "";
        row[1] = "";
        row[2] = "";
        row[3] = "";
        return row;
    }
}
