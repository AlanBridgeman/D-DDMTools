import java.util.ArrayList;
import java.util.Arrays;

import java.awt.Polygon;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.geom.Line2D;

public class MutablePolygon extends Polygon {
    public MutablePolygon(int[] x, int[] y, int npoints) {
        super(x, y, npoints);
    }

    @Override
    public void addPoint(int x, int y) {
        int closeX = 0;
        int closeY = 0;
        int closePointIndex = 0;
        for (int i = 0; i < npoints; i++) {
            /*int distFromCloseX = Math.abs(closeX - x);                // Distance from the current closest X to the desired point's X
            int distFromCloseY = Math.abs(closeY - y);                // Distance from the current closest Y to the desired point's Y
            int distFromCloseTotal = distFromCloseX + distFromCloseY; // Summation of the x and y distance from the current closest point to the desired point
            int distFromCurrX = Math.abs(xpoints[i] - x);             // Distance from the current X to the desired point's X
            int distFromCurrY = Math.abs(ypoints[i] - y);             // Distance from the current Y to the desired point's Y
            int distFromCurrTotal = distFromCurrX + distFromCurrY;    // Summation of the x and y distance from the current x and y to the desired point

            System.out.println("Index: " + i + "\nClicked X: " + x + "\nClicked Y: " + y + "\nChecking X: " + xpoints[i] + "\nChecking Y: " + ypoints[i] + "\nCurrent Distance From Closest X: " + distFromCloseX + "\nCurrent Distance From Closest Y: " + distFromCloseY + "\nCurrent Distance From Closest Point: " + distFromCloseTotal + "\nDistance from Checked X to Clicked X: " + distFromCurrX + "\nDistance from Checked Y to Clicked Y: " + distFromCurrY + "\nDistance from Checked point: " + distFromCurrTotal);

            if (distFromCloseTotal >= distFromCurrTotal) {
                closeX = xpoints[i]; // Set the closest X to this X
                closeY = ypoints[i]; // Set the closest Y to this Y
                closePointIndex = i; // Set the index of thr closest point to the current loop counter value
                System.out.println("Current Point is closest so far.");
            }*/

            Line2D line = new Line2D.Float(xpoints[i], ypoints[i], (i == npoints - 1 ? xpoints[0] : xpoints[i + 1]), (i == npoints - 1 ? ypoints[0] : ypoints[i + 1]));
            if(line.intersects(x - 5, y - 5, 10, 10)) {
                closePointIndex = i;
            }

            //System.out.println();
        }

        System.out.println("Closest Point Index: " + closePointIndex);

        // Do the default polygon behaviour of adding a point at the end of the polygon
        super.addPoint(0, 0);

        // Loop over the points and after the closest point down
        for(int i=npoints - 1;i > closePointIndex + 1;i--) {
            System.out.println("Shifting point " + (i - 1) + "/" + npoints + "[X: " + xpoints[i - 1] + ", Y: " + ypoints[i - 1] + "] to " + i + "/" + npoints + "[X: " + xpoints[i] + ", Y: " + ypoints[i] + "]");
            xpoints[i] = xpoints[i - 1];
            ypoints[i] = ypoints[i - 1];
        }

        System.out.println("Setting point " + (closePointIndex + 1) + " to [X: " + x + ", Y: " + y + "]");
        xpoints[closePointIndex + 1] = x;
        ypoints[closePointIndex + 1] = y;

        System.out.println("xpoints: " + java.util.Arrays.toString(xpoints));
        System.out.println("ypoints: " + java.util.Arrays.toString(ypoints));

        System.out.println();
    }

    public void removePoint(int x, int y) {
        int pointIndex = 0;
        for(int i=0;i < npoints;i++) {
            if(xpoints[i] == x && ypoints[i] == y) {
                pointIndex = i;
            }
        }

        // Loop over the points from the point index until the end and move the points earlier one index
        for(int i=pointIndex;i < npoints - 1;i++) {
            System.out.println("Shifting point " + (i + 1) + "/" + npoints + "[X: " + xpoints[i + 1] + ", Y: " + ypoints[i + 1] + "] to " + i + "/" + npoints + "[X: " + xpoints[i] + ", Y: " + ypoints[i] + "]");
            xpoints[i] = xpoints[i + 1];
            ypoints[i] = ypoints[i + 1];
        }

        npoints--;
    }

    public Point getVertexInRange(int x, int y, int xRange, int yRange) {
        Point retPoint = null;

        ArrayList<Integer> closePoints = new ArrayList<Integer>();
        for (int i = 0; i < npoints; i++) {
            if ((xpoints[i] >= x - xRange && xpoints[i] <= x + xRange) && (ypoints[i] >= y - yRange && ypoints[i] <= y + yRange)) {
                closePoints.add(i);
            }
        }

        if(closePoints.size() > 1) {
            int closestPointIndex = closePoints.get(0);
            for(int i=1;i < closePoints.size();i++) {
                int distCloseX = xpoints[closestPointIndex] - x;
                int distCloseY = ypoints[closestPointIndex] - y;
                int distCloseTotal = distCloseX + distCloseY;
                int distCurrX = xpoints[closePoints.get(i)] - x;
                int distCurrY = ypoints[closePoints.get(i)] - y;
                int distCurrTotal = distCurrX + distCurrY;

                if(distCurrTotal < distCloseTotal) {
                    closestPointIndex = closePoints.get(i);
                }
            }

            retPoint = new Point(xpoints[closestPointIndex], ypoints[closestPointIndex]);
        }
        else if(closePoints.size() == 1) {
            retPoint = new Point(xpoints[closePoints.get(0)], ypoints[closePoints.get(0)]);
        }

        return retPoint;
    }

    public void moveVertex(int x, int y, int moveX, int moveY) {
        System.out.println("Being called with X: " + x + ", Y: " + y + ", MoveX: " + moveX + ", MoveY: " + moveY);
        for(int i=0;i < npoints;i++) {
            if(xpoints[i] == x && ypoints[i] == y) {
                System.out.println("Changing point");
                xpoints[i] += moveX;
                ypoints[i] += moveY;
            }
        }
    }

    public boolean onLine(int x, int y) {
        boolean isOnLine = false;

        for (int i = 0; i < npoints; i++) {
            Line2D line = new Line2D.Float(xpoints[i], ypoints[i], (i == npoints - 1 ? xpoints[0] : xpoints[i + 1]), (i == npoints - 1 ? ypoints[0] : ypoints[i + 1]));
            if(line.intersects(x - 5, y - 5, 10, 10)) {
                isOnLine = true;
            }
        }

        return isOnLine;
    }

    public MutablePolygon getZoomedPoly(double zoom) {
        int[] x = Arrays.copyOf(xpoints, xpoints.length);
        int[] y = Arrays.copyOf(ypoints, ypoints.length);
        for(int i=0;i < npoints;i++) {
            x[i] = (int)(x[i] * zoom);
            y[i] = (int)(y[i] * zoom);
        }
        return new MutablePolygon(x, y, npoints);
    }

    public void move(int moveX, int moveY) {
        for(int i=0;i < npoints;i++) {
            xpoints[i] += moveX;
            ypoints[i] += moveY;
        }
    }

    public void resetBounds() {
        int boundsMinX = Integer.MAX_VALUE;
        int boundsMinY = Integer.MAX_VALUE;
        int boundsMaxX = Integer.MIN_VALUE;
        int boundsMaxY = Integer.MIN_VALUE;

        for (int i = 0; i < npoints; i++) {
            int x = xpoints[i];
            boundsMinX = Math.min(boundsMinX, x);
            boundsMaxX = Math.max(boundsMaxX, x);
            int y = ypoints[i];
            boundsMinY = Math.min(boundsMinY, y);
            boundsMaxY = Math.max(boundsMaxY, y);
        }

        bounds = new Rectangle(boundsMinX - 5, boundsMinY - 5, boundsMaxX - boundsMinX + 5, boundsMaxY - boundsMinY + 5);
    }
}
