package de.idrinth.waraddonclient.factory;

public final class AddonList {

    private static de.idrinth.waraddonclient.implementation.list.Addon instance;

    /**
     * prevent initialization
     */
    private AddonList() {
        //this is static only
    }

    /**
     * gets the list of addons
     *
     * @return de.idrinth.waraddonclient.implementation.list.Addon
     */
    public synchronized static de.idrinth.waraddonclient.implementation.list.Addon build() {
        if (instance == null) {
            instance = new de.idrinth.waraddonclient.implementation.list.Addon();
            new java.lang.Thread(instance).start();
        }
        return instance;
    }
}
