package de.idrinth.waraddonclient.service;

import javax.swing.JLabel;

public class Version implements java.lang.Runnable {
    private final Request client;
    private JLabel label;

    public Version(Request client) {
        this.client = client;
    }

    public void setVersion(JLabel version) {
        this.label = version;
    }

    @Override
    public void run() {
        de.idrinth.waraddonclient.service.Sleeper.sleep(2500);
        try {
            label.setText(client.getVersion());
        } catch (java.lang.Exception exception) {
            de.idrinth.waraddonclient.factory.Logger.build().error(exception);
        }
    }
}
