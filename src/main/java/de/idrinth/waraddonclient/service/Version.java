package de.idrinth.waraddonclient.service;

public class Version implements java.lang.Runnable {
    @Override
    public void run() {
        de.idrinth.waraddonclient.service.Sleeper.sleep(2500);
        try {
            de.idrinth.waraddonclient.factory.Interface.build().getRemoteVersionLabel().setText(de.idrinth.waraddonclient.factory.RemoteRequest.build().getVersion());
        } catch (java.lang.Exception exception) {
            de.idrinth.factory.Logger.build().log(exception, de.idrinth.Logger.LEVEL_ERROR);
        }
    }
}
