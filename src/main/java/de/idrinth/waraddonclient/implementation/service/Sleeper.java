package de.idrinth.waraddonclient.implementation.service;

public final class Sleeper {

    /**
     * prevent init
     */
    private Sleeper() {
        //only static
    }

    /**
     * Sleeps for a given duration in the current thread
     *
     * @param duration
     */
    public static void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } catch (java.lang.InterruptedException exception) {
            de.idrinth.factory.Logger.build().log(exception, de.idrinth.Logger.levelInfo);
        }
    }
}
