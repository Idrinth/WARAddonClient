package de.idrinth.waraddonclient.gui;

import de.idrinth.waraddonclient.Config;
import de.idrinth.waraddonclient.service.DelayedRunner;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class FrameRestorer {

    private final Config config;

    public FrameRestorer(Config config) {
        this.config = config;
    }

    public void restore(Window frame) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(getProcessedLocation(screen));
        frame.setSize(getProcessedSize(screen));
        frame.addComponentListener(new Adapter(new DelayedRunner(400, () -> {
            config.setWindowDimension(frame.getSize());
            config.setWindowPosition(frame.getLocation());
        })));
    }

    private Dimension getProcessedSize(Dimension screen) {
        Dimension dimension = config.getWindowDimension();
        int width = dimension.width;
        int height = dimension.height;
        if (width <= 0 || width > screen.width) {
            width = screen.width;
        }
        if (height <= 0 || height > screen.height) {
            height = screen.height;
        }
        return new java.awt.Dimension(width, height);
    }

    private Point getProcessedLocation(Dimension screen) {
        Point point = config.getWindowPosition();
        int x = point.x;
        int y = point.y;
        if (x < 0 || x > screen.width) {
            x = 0;
        }
        if (y < 0 || y > screen.height) {
            y = 0;
        }
        return new java.awt.Point(x, y);
    }

    private static class Adapter extends ComponentAdapter {

        private final DelayedRunner updater;

        public Adapter(DelayedRunner runner) {
            this.updater = runner;
        }

        @Override
        public void componentResized(ComponentEvent e) {
            updater.update();
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            updater.update();
        }
    }
}
