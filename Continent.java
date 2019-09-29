public class Continent {
    private String name;
    private ContinentMap map;

    public Continent(String name) {
        this(name, null);
    }
    public Continent(String name, ContinentMap map) {
        this.name = name;
        this.map = map;
    }

    public void setMap(ContinentMap map) {
        this.map = map;
    }
    public ContinentMap getMap() {
        return map;
    }
}
