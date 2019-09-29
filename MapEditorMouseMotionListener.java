import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
public class MapEditorMouseMotionListener implements MouseMotionListener {
    //private JPanel panel;
    private WorldMap map;

    public MapEditorMouseMotionListener(WorldMap map) {
        //this.panel = panel;
        this.map = map;
    }

    public void mouseDragged(MouseEvent e) {
        for(int i=0;i < map.getContinentMaps().length;i++) {
            if (map.getContinentMaps()[i].getMouseMotionListener().getDragTarget() != null) {
                map.getContinentMaps()[i].getMouseMotionListener().mouseDragged(e);
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        // Do Nothing
    }
}
