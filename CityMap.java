import java.awt.Graphics2D;

import java.awt.geom.Ellipse2D;

public class CityMap {
    private Ellipse2D outer;
    private Ellipse2D inner;

    public CityMap(Ellipse2D outer, Ellipse2D inner) {
        super();

        this.outer = outer;
        this.inner = inner;
    }

    public void draw(Graphics2D g) {
        g.draw(outer);
    }
}