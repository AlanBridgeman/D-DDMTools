import java.util.Random;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

public class UnstraightLine {
    private int xDistance;
    private int yDistance;

    public UnstraightLine() {
        this(new Random().nextInt(640), 0/*new Random().nextInt(480)*/);
    }
    public UnstraightLine(int moveX, int moveY) {
        this.xDistance = moveX;
        this.yDistance = moveY;
    }

    public void draw(Graphics2D g) {
        int numParts = new Random().nextInt(10);
        for(int i=0;i < numParts;i++) {
            int partXDist = xDistance/numParts;
            int partYDist = yDistance/numParts;
            if(new Random().nextBoolean()) {
                Point2D start = new Point2D.Float(10f + (partXDist * i), 10f /*+ (partYDist * i)*/);
                Rectangle2D rect = new Rectangle2D.Float((float) start.getX(), (float) start.getY(), partXDist, (float)new Random().nextInt(20)/*partYDist*/);
                Arc2D arc = new Arc2D.Float(rect, 0, (new Random().nextBoolean() ? 180 : -180), Arc2D.OPEN);
                g.draw(arc);
            }
            else {
                Point2D start = new Point2D.Float(10f + (partXDist * i), 15f + (partYDist * i));
                Point2D end = new Point2D.Float((float)start.getX() + partXDist, (float)start.getY() + partYDist);
                Line2D line = new Line2D.Float(start, end);
                g.draw(line);
            }
        }
    }
}
