package de.idrinth.waraddonclient.cli;

import de.idrinth.waraddonclient.service.ProgressReporter;

public class Progress implements ProgressReporter {

    private int current=0;

    private int max=0;

    private Runnable callback;

    private boolean stopped;

    private void finish()
    {
        max = 0;
        current = 0;
        stopped = false;
        callback.run();
    }

    @Override
    public synchronized void incrementCurrent() {
        current++;
        if (current == max && stopped) {
            finish();
        } 
    }

    @Override
    public synchronized void incrementMax(int amount) {
        max += amount;
    }

    @Override
    public synchronized void start(String title, Runnable callback) {
        max=0;
        current=0;
        this.callback = callback;
        stopped=false;
    }

    @Override
    public synchronized void stop() {
        stopped = true;
        if (current == max) {
            finish();
        }
    }
    
}
