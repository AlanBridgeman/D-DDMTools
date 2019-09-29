import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MapEditorMouseListener implements MouseListener {
    private JPanel panel;
    private WorldMap map;

    public MapEditorMouseListener(JPanel panel, WorldMap map) {
        this.panel = panel;
        this.map = map;
    }

    public void mousePressed(MouseEvent e) {
        if(e.isControlDown()) {
            JPopupMenu popupMenu = new JPopupMenu();

            JMenu addMenu = new JMenu("Add...");
            JMenuItem addEllipse = new JMenuItem("Ellipse");
            addMenu.add(addEllipse);
            popupMenu.add(addMenu);

            boolean hasDisplayed = false;
            for(int i=0;i < map.getContinentMaps().length;i++) {
                if (map.getContinentMaps()[i].contains(e.getX(), e.getY())) {
                    hasDisplayed = true;
                    map.getContinentMaps()[i].getMouseListener().setPopupMenu(popupMenu);
                    map.getContinentMaps()[i].getMouseListener().mousePressed(e);
                }
            }

            if(!hasDisplayed) {
                System.out.println("Outside the ContinentMap");
                popupMenu.show(panel, e.getX(), e.getY());
            }
        }
        else {
            for(int i=0;i < map.getContinentMaps().length;i++) {
                if (map.getContinentMaps()[i].contains(e.getX(), e.getY())) {
                    map.getContinentMaps()[i].getMouseListener().mousePressed(e);
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        for(int i=0;i < map.getContinentMaps().length;i++) {
            if(map.getContinentMaps()[i].getMouseListener().getDragTarget() != null) {
                map.getContinentMaps()[i].getMouseListener().mouseReleased(e);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        // Do Nothing
    }

    public void mouseEntered(MouseEvent e) {
        // Do Nothing
    }

    public void mouseExited(MouseEvent e) {
        // Do Nothing
    }
}
