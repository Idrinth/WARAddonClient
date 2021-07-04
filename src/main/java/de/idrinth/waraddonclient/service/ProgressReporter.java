package de.idrinth.waraddonclient.service;

public interface ProgressReporter
{
    void incrementCurrent();

    void incrementMax(int amount);

    void start(String title, Runnable callback);

    void stop();
}
