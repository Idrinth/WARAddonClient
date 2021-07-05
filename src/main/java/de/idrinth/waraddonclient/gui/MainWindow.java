package de.idrinth.waraddonclient.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentListener;

public interface MainWindow {
    void setLocation(Point point);
    Point getLocation();
    void setSize(Dimension dimension);
    Dimension getSize();
    void addComponentListener(ComponentListener listener);
    void setVisible(boolean value);
}
