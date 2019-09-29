import java.util.Random;

public class RandomMutablePolygon extends MutablePolygon {
    private RandomMutablePolygon(int[] x, int[] y, int numPoints) {
        super(x, y, numPoints);
    }

    public static RandomMutablePolygon generate() {
        return generate(new Random().nextLong());
    }
    public static RandomMutablePolygon generate(long seed) {
        Random random = new Random(seed);

        int numPoints = random.nextInt(500) * 2;
        int[] x = new int[numPoints];//{0, 0, 5, 8, 10, 13, 18, 23, 28, 33, 35, 38, 40, 43, 46, 48, 55, 65, 73, 77, 80};
        int[] y = new int[numPoints];//{97, 87, 78, 80, 89, 80, 44, 74, 13, 15, 55, 74, 43, 58, 64, 22, 66, 70, 61, 80, 85};
        for(int i=0;i < numPoints/2;i++) {
            x[i] = random.nextInt(100);
            if (i > 0 && x[i - 1] > x[i]) {
                x[i] += random.nextInt(100) + (x[i - 1] - x[i]);
            }
            /*if (i > 0 && x[i] - x[i - 1] > 50) {
                x[i] -= random.nextInt(x[i] - x[i - 1]);
            }*/
            y[i] = random.nextInt(100);
            System.out.println("Point " + i + "/" + numPoints + " [X: " + x[i] + " Y: " + y[i] + "]");
        }
        if(numPoints % 2 == 0) {
            x[numPoints / 2] = x[numPoints / 2 - 1];
            y[numPoints / 2] = y[0];
        }
        for(int i=numPoints/2 - (numPoints % 2 == 0 ? 1 : 0);i > 0;i--) {
            x[numPoints - i] = x[i];
            int yMirror = -(y[i] - y[0]);
            y[numPoints - i] = yMirror + y[0];
            while(y[numPoints - i] - y[i] < 20) {
                y[numPoints - i] += 10;
            }
            System.out.println("Point " + (numPoints - i) + "/" + numPoints + " [X: " + x[numPoints - i] + " Y: " + y[numPoints - i] + "]");
        }

        return new RandomMutablePolygon(x, y, numPoints);
    }
}
