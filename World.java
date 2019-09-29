import java.util.ArrayList;
import java.util.List;

public class World {
    private String name;
    private ArrayList<Continent> continents;
    private WorldMap map;

    public World() {
        continents = new ArrayList<Continent>();
        MapEditor editor = new MapEditor();
        map = new WorldMap(this, editor);
        editor.setMap(map);
    }

    public List<Continent> getContinents() {
        return continents;
    }

    public WorldMap getMap() {
        return map;
    }

    public void addContinent(Continent continent) {
        if(continent.getMap() == null) {
            continent.setMap(map.generateNewContinentMap());
        }
        continents.add(continent);
    }
}
