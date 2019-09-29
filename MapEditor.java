import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MapEditor extends JPanel {
    private /*CityMap*KingdomMap*ContinentMap*/WorldMap map;
    //private ContinentMap map2;
    private boolean editMode;

    public MapEditor() {
        this(null);
    }
    public MapEditor(WorldMap map) {
        this.map = map;

        setLayout(new BorderLayout());
        //add(map, BorderLayout.CENTER);
        //addMouseListener(new MapEditorMouseListener(this, map.getContinentMaps()));
        //addMouseMotionListener(new MapEditorMouseMotionListener(map.getContinentMaps()));
    }

    public void setMap(WorldMap map) {
        this.map = map;
        addMouseListener(new MapEditorMouseListener(this, map));
        addMouseMotionListener(new MapEditorMouseMotionListener(map));
    }

    public boolean getEditMode() {
        return editMode;
    }
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        map.setDrawMode(editMode);
    }

    public Dimension getPreferredSize() {
        return map.getSize();
    }

    public void paintComponent(Graphics g) {
        map.draw(g);
    }
}
