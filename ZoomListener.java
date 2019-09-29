import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;
public class ZoomListener implements MouseWheelListener {
    private double zoom;
    private WorldMap panel;

    public ZoomListener(WorldMap panel) {
        zoom = 1;
        this.panel = panel;
    }

    public double getZoom() {
        return zoom;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.isControlDown()) {
            zoom += e.getUnitsToScroll()/10.0;
            panel.repaint();
        }
    }
}
