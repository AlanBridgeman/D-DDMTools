import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

public class ContinentMap /*extends JPanel*/ {
    private int x;
    private int y;
    private MutablePolygon poly;
    private PointDragDropMouseListener dndListener;
    private ZoomListener zoomListener;
    private boolean drawMode;

    public ContinentMap(WorldMap panel, int x, int y) {
        poly = RandomMutablePolygon.generate();
        poly.move(x, y);

        this.x = x;
        this.y = y;

        drawMode = false;

        zoomListener = new ZoomListener(panel);
        //this.addMouseWheelListener(zoomListener);
        dndListener = new PointDragDropMouseListener(poly, panel, zoomListener);
    }

    public void setDrawMode(boolean drawMode) {
        this.drawMode = drawMode;
    }

    public PointDragDropMouseListener getMouseListener() {
        return dndListener;
    }
    public PointDragDropMouseListener getMouseMotionListener() {
        return dndListener;
    }

    public boolean contains(int x, int y) {
        boolean isContained = new Rectangle(poly.getBounds().x - 5, poly.getBounds().y - 5, poly.getBounds().width + 5, poly.getBounds().height + 5).contains(x, y);
        return isContained;
    }

    /*
     * Need to override the default getPreferredSize method so that the scollpane is sized correctly
     */
    //@Override
    public Dimension getSize() {
        Dimension dimensions =  new Dimension(poly.getBounds().width + poly.getBounds().x, poly.getBounds().height + poly.getBounds().y);
        return dimensions;
    }

    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D)g;

        // Save the transformation state so that we can reset back to it when this object is done painting
        //AffineTransform oldTransform = g2D.getTransform();

        // Scale to the current zoom level
        //g2D.scale(zoomListener.getZoom(), zoomListener.getZoom());

        // Draw the polygon for the map
        g2D.draw(poly.getZoomedPoly(zoomListener.getZoom()));

        // Check if its "Draw Mode" because then draw the vertex indicators
        if(drawMode) {
            g2D.setColor(Color.MAGENTA);
            for (int i = 0; i < poly.npoints; i++) {
                g2D.draw(new Ellipse2D.Double((poly.xpoints[i] * zoomListener.getZoom()) - 5/*/zoomListener.getZoom()*/, (poly.ypoints[i] * zoomListener.getZoom()) - 5/*/zoomListener.getZoom()*/, 10/*/zoomListener.getZoom()*/, 10/*/zoomListener.getZoom()*/));
            }
        }

        // Check if we have a drag target or highlight target so that we can draw the appropriate indicators
        if(dndListener.hasDragTarget()) {
            g2D.setColor(Color.GREEN);
            g2D.draw(new Ellipse2D.Double((dndListener.getDragTarget().x * zoomListener.getZoom()) - 5/*/zoomListener.getZoom()*/, (dndListener.getDragTarget().y * zoomListener.getZoom()) - 5/*/zoomListener.getZoom()*/, 10/*/zoomListener.getZoom()*/, 10/*/zoomListener.getZoom()*/));
        }
        else if(dndListener.hasHighlightTarget()) {
            g2D.setColor(Color.GREEN);
            g2D.fill(new Ellipse2D.Double((dndListener.getHighlightTarget().x * zoomListener.getZoom()) - 5/*/zoomListener.getZoom()*/, (dndListener.getHighlightTarget().y * zoomListener.getZoom()) - 5/*/zoomListener.getZoom()*/, 10/*/zoomListener.getZoom()*/,10/*/zoomListener.getZoom()*/));
        }

        g2D.setColor(Color.BLACK);

        //g2D.setTransform(oldTransform);
    }
}