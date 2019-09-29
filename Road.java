import java.util.Random;

import java.awt.Graphics2D;

public class Road {
    private UnstraightLine line;

    public Road() {
        line = new UnstraightLine();
    }

    public void draw(Graphics2D g) {
        line.draw(g);
    }
}
