/**
 * Heavily inspired by https://www.logicbig.com/tutorials/java-swing/frame-location-size-pref.html
 */
package de.idrinth.waraddonclient.gui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class FrameMonitor {

  public static void registerFrame(javax.swing.JFrame frame, String frameUniqueId, int defaultX, int defaultY, int defaultW, int defaultH) {
      Preferences prefs = Preferences.userRoot().node(FrameMonitor.class.getName() + "-" + frameUniqueId);
      frame.setLocation(getLocation(prefs, defaultX, defaultY));
      frame.setSize(getSize(prefs, defaultW, defaultH));

      frame.addComponentListener(new Adapter(new DelayedRunner(400,() -> updatePref(frame, prefs))));
  }

  private static void updatePref(javax.swing.JFrame frame, Preferences prefs) {
      java.awt.Point location = frame.getLocation();
      prefs.putInt("x", location.x);
      prefs.putInt("y", location.y);
      java.awt.Dimension size = frame.getSize();
      prefs.putInt("w", size.width);
      prefs.putInt("h", size.height);
  }

  private static java.awt.Dimension getSize(Preferences pref, int defaultWidth, int defaultHeight) {
      int width = pref.getInt("w", defaultWidth);
      int height = pref.getInt("h", defaultHeight);
      java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
      if (width <= 0 || width > screen.width) {
          width = screen.width;
      }
      if (height <= 0 || height > screen.height) {
          height = screen.height;
      }
      return new java.awt.Dimension(width, height);
  }

  private static java.awt.Point getLocation(Preferences pref, int defaultX, int defaultY) {
      int x = pref.getInt("x", defaultX);
      int y = pref.getInt("y", defaultY);
      java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
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
    public Adapter(DelayedRunner updater) {
        this.updater = updater;
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
  private static class DelayedRunner {
    private Timer timer;

    public DelayedRunner(int delay, Runnable callback) {
        timer = new Timer(delay, e -> {
            timer.stop();
            callback.run();
        });
    }

    public void update() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> {timer.restart();});
        } else {
            timer.restart();
        }
    }
  }
}