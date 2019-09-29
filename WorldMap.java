import java.util.List;
import java.util.Iterator;

import java.awt.Graphics;
import java.awt.Dimension;

public class WorldMap {
    private World world;
    private MapEditor editor;

    public WorldMap(World world, MapEditor editor) {
        this.world = world;
        this.editor = editor;
    }

    public MapEditor getEditor() {
        return editor;
    }

    public Dimension getSize() {
        int width = -1;
        int height = -1;

        for(Continent continent : world.getContinents()) {
            ContinentMap map = continent.getMap();
            width = Math.max(width, map.getSize().width);
            height = Math.max(height, map.getSize().height);
        }

        return new Dimension(width, height);
    }

    public ContinentMap[] getContinentMaps() {
        List<Continent> continents = world.getContinents();

        ContinentMap[] maps = new ContinentMap[continents.size()];

        Iterator<Continent> iter = continents.iterator();
        int index = 0;
        while(iter.hasNext()) {
            maps[index] = iter.next().getMap();
            index++;
        }

        return maps;
    }

    public void setDrawMode(boolean drawMode) {
        for(Continent continent : world.getContinents()) {
            continent.getMap().setDrawMode(drawMode);
        }
    }

    public ContinentMap generateNewContinentMap() {
        return new ContinentMap(this, 0, 0);
    }

    public void repaint() {
        editor.repaint();
    }

    public void draw(Graphics g) {
        for(Continent continent : world.getContinents()) {
            ContinentMap map = continent.getMap();
            map.draw(g);
        }
    }
}
