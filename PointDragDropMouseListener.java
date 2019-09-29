import java.awt.Point;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;

public class PointDragDropMouseListener implements MouseListener, MouseMotionListener {
    private MutablePolygon poly;       // The polygon being manipulated
    private WorldMap panel;            // The panel to update on events
    private JPopupMenu popupMenu;      // The popup menu when someone right clicks
    private ZoomListener zoomListener; // A MouseWheelListener that is used to control the zoom which is needed to translate mouse coordinates
    private Point dragTarget;          // Used to store the point being moved/dragged
    private Point highlightTarget;     // Used with popup to indicate selected vertex

    public PointDragDropMouseListener(MutablePolygon poly, WorldMap panel, ZoomListener zoomListener) {
        this.poly = poly;
        this.panel = panel;
        this.zoomListener = zoomListener;

        popupMenu = new JPopupMenu();

        dragTarget = null;
        highlightTarget = null;
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public boolean hasDragTarget() {
        return (dragTarget != null);
    }

    public Point getDragTarget() {
        return dragTarget;
    }

    public boolean hasHighlightTarget() {
        return (highlightTarget != null);
    }

    public Point getHighlightTarget() {
        return highlightTarget;
    }

    //
    // Mouse Listener
    //

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        System.out.println("Pressed: " + e.getX() + ", " + e.getY());
        if(e.getButton() == MouseEvent.BUTTON1 && !e.isControlDown()) {
            dragTarget = poly.getVertexInRange((int)(e.getX() / zoomListener.getZoom()), (int)(e.getY() / zoomListener.getZoom()), 5, 5);
            if(dragTarget != null) {
                System.out.println("Drag point is set [X: " + dragTarget.x + ", Y: " + dragTarget.y + "]");
            }
        }
        else if(e.getButton() == MouseEvent.BUTTON3 || e.isControlDown()) {
            //JPopupMenu popupMenu = new JPopupMenu();

            // Check if the user has clicked on a vertex (well, technically within range of a vertex but...)
            highlightTarget = poly.getVertexInRange((int)(e.getX() / zoomListener.getZoom()), (int)(e.getY() / zoomListener.getZoom()), (int)(5 / zoomListener.getZoom()), (int)(5 / zoomListener.getZoom()));

            // If the user did click on a vertex then add the delete option to the popup if instead the clicked on a line then add the add option
            if(highlightTarget != null) {
                JMenuItem delPopupItem = new JMenuItem("Delete Vertex");
                delPopupItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        poly.removePoint(highlightTarget.x, highlightTarget.y);

                        // highlightTarget needs to be set to null here because it has to be set for prevous clall
                        highlightTarget = null;

                        // Redraw the polygon without the vertex and removing the highlight ellipse
                        panel.repaint();
                    }
                });
                popupMenu.add(delPopupItem);
            }
            else if(poly.onLine((int)(e.getX() / zoomListener.getZoom()), (int)(e.getY() / zoomListener.getZoom()))) {
                JMenuItem addPopupItem = new JMenuItem("Add Vertex");
                addPopupItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent action) {
                        poly.addPoint((int)(e.getX() / zoomListener.getZoom()), (int)(e.getY() / zoomListener.getZoom()));
                        panel.repaint();
                    }
                });
                popupMenu.add(addPopupItem);
            }

            // A listener for when the popup is canceled that we can clear the highlightTarget variable
            popupMenu.addPopupMenuListener(new PopupMenuListener() {
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }
                public void popupMenuCanceled(PopupMenuEvent e) {
                    highlightTarget = null;
                    panel.repaint();
                }
            });

            popupMenu.show(panel.getEditor(), e.getX(), e.getY());

            panel.repaint();
        }
    }
    public void mouseReleased(MouseEvent e) {
        poly.resetBounds();
        dragTarget = null;
        panel.repaint();
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    //
    // Mouse Motion Listener
    //

    public void mouseDragged(MouseEvent e) {
        if(dragTarget != null) {
            poly.moveVertex(dragTarget.x, dragTarget.y, (int)(e.getX() / zoomListener.getZoom() - dragTarget.x), (int)(e.getY() / zoomListener.getZoom() - dragTarget.y));

            // Because moveVertex relies on the coordinates of the vertex as the first two parameters need to update the dragTarget point
            dragTarget.x += (int)(e.getX() / zoomListener.getZoom() - dragTarget.x);
            dragTarget.y += (int)(e.getY() / zoomListener.getZoom() - dragTarget.y);

            // Redraw the polygon as the vertex is moved
            panel.repaint();
        }
    }
    public void mouseMoved(MouseEvent e) {}
}
