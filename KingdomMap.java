import java.util.Arrays;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.geom.Ellipse2D;

public class KingdomMap {

    public KingdomMap() {
        super();
    }

    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D)g;

        /*Road road = new Road();
        road.draw(g2D);*/

        int numCity = 4;
        CityMap[] cityMaps = new CityMap[4];
        Ellipse2D[] ellipses = getConcentricCircles(100, numCity);
        for(int i=ellipses.length;i > 0;i--) {
            cityMaps[i - 1] = new CityMap(ellipses[i - 1], (i == ellipses.length ? null : ellipses[i]));
            cityMaps[i - 1].draw(g2D);
        }
    }

    public Ellipse2D[] getConcentricCircles(int radius, int numCircle) {
        Ellipse2D[] ellipses = new Ellipse2D[numCircle];
        for(int i=1;i <= numCircle;i++) {
            ellipses[i - 1] = new Ellipse2D.Float(10 + radius * (numCircle - i), 10 + radius * (numCircle - i), radius * (2 * i), radius * (2 * i));
        }
        return ellipses;
    }
}
