package de.idrinth.waraddonclient.service;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class DelayedRunner {

    private Timer timer;

    public DelayedRunner(int delay, Runnable callback) {
        timer = new Timer(delay, e -> {
            timer.stop();
            callback.run();
        });
    }

    public void update() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> timer.restart());
        } else {
            timer.restart();
        }
    }
}
