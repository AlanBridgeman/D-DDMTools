import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import java.io.File;
import java.io.IOException;

import java.awt.Point;
import java.awt.Color;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import javax.imageio.ImageIO;

public class ImageProcessor {
    private BufferedImage bufferedImage;

    public ImageProcessor() {
        try {
            bufferedImage = ImageIO.read(new File("world-map-continents.jpg"));
        }
        catch(IOException e) { e.printStackTrace(); }
    }

    public HashMap<Color, ArrayList<Point>> processImage() {
        System.out.println("Startin image processing");

        // Setup a return variable
        HashMap<Color, ArrayList<Point>> byColour = null;

        // We can only process an image if one exists
        if(bufferedImage != null) {
            // Some convenience variables for width and height of the image in pixels
            int picWidth = bufferedImage.getData().getWidth();
            int picHeight = bufferedImage.getData().getHeight();

            // Setup concurrent threads for mapping pixels to their colour
            ExecutorService executor = Executors.newFixedThreadPool(picHeight/100 + 1);
            Future<HashMap<Point, Color>>[] futures = new Future/*<HashMap<Point, double[]>>*/[picHeight/100 + 1];
            for(int i=0;i < futures.length;i++) {
                ImageProcessorThread imageThread = new ImageProcessorThread(0, i * 100, picWidth, (picHeight - (i * 100) >= 100 ? 100 : (picHeight - (i * 100))) , bufferedImage.getData());
                futures[i] = executor.submit(imageThread);
            }

            // The overall map for mapping pixels to their colours
            HashMap<Point, Color> combinedMap = new HashMap<Point, Color>();

            System.out.println("Getting pixel map");

            // Iteratively loop over the concurrent threads and get the results to "join"/insert them to the overall map
            for(int i=0;i < futures.length;i++) {
                HashMap<Point, Color> map = null;
                try {
                    System.out.println("Getting pixel sub map from threat " + i);
                    map = futures[i].get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                if (map != null) {
                    combinedMap.putAll(map);
                }
            }

            System.out.println("Getting unique colour set");
            // Get a set (only unique values) of the colours
            Set<Color> colourSet = new HashSet<Color>(combinedMap.values());
            System.out.println("Setting up colour to colour map");
            executor = Executors.newFixedThreadPool(colourSet.size()/100 + 1);
            Future<HashMap<Color, Color>>[] colourFutures = new Future[colourSet.size()/100 + 1];

            for(int i=0;i < colourSet.size()/100 + 1;i++) {
                Set<Color> localSet = new HashSet<Color>();
                Iterator<Color> iter = colourSet.iterator();
                int counter = 0;
                while(iter.hasNext()) {
                    Color clr = iter.next();
                    if(counter > i * 100 && counter <= i * 100 + 100) {
                        localSet.add(clr);
                    }
                    counter++;
                }
                ImageColourProcessorThread colourProcessThread = new ImageColourProcessorThread(localSet, colourSet);
                colourFutures[i] = executor.submit(colourProcessThread);
            }
            HashMap<Color, Color> colourToColour = new HashMap<Color, Color>();
            // Iteratively loop over the concurrent threads and get the results to "join"/insert them to the overall map
            for(int i=0;i < colourFutures.length;i++) {
                HashMap<Color, Color> map = null;
                try {
                    System.out.println("Getting pixel sub map from thread " + i);
                    map = colourFutures[i].get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                if (map != null) {
                    colourToColour.putAll(map);
                }
            }
            /*for(Color clr : colourSet) {
                System.out.println("Processing colour: " + clr);
                Color matchColor = null;
                for(Color clr2 : colourSet) {
                    if(!clr.equals(clr2)) {
                        double rDiff = Math.pow(clr.getRed() - clr2.getRed(), 2);
                        double gDiff = Math.pow(clr.getGreen() - clr2.getGreen(), 2);
                        double bDiff = Math.pow(clr.getBlue() - clr2.getBlue(), 2);
                        double totalDiff = Math.sqrt(rDiff + gDiff + bDiff);

                        if (totalDiff < 100) {
                            matchColor = clr2;
                        }
                    }
                }
                colourToColour.put(clr, matchColor);
            }*/

            /*ArrayList<Color>[] subColourLists = new ArrayList[colourCollection.size()/100 + 1];
            for(int i=0; i < subColourLists.length;i++) {
                subColourLists[i] = new ArrayList<Color>();
            }

            int counter = 0;
            Iterator<Color> iter = colourCollection.iterator();
            while(iter.hasNext()) {
                subColourLists[counter / 100].add(iter.next());
                counter++;
            }

            executor = Executors.newFixedThreadPool(subColourLists.length);
            Future<HashMap<Color, Color>>[] colourFutures = new Future[subColourLists.length];

            for(int i=0;i < subColourLists.length;i++) {
                ImageColourProcessorThread colourProcessThread = new ImageColourProcessorThread(subColourLists[i]);
                colourFutures[i] = executor.submit(colourProcessThread);
            }*/

            /*for(Color colour : colourCollection) {
                for(Color innerColour : colourCollection) {
                    if(!colour.equals(innerColour)) {
                        double rDiff = Math.pow(colour.getRed() - innerColour.getRed(), 2);
                        double gDiff = Math.pow(colour.getGreen() - innerColour.getGreen(), 2);
                        double bDiff = Math.pow(colour.getBlue() - innerColour.getBlue(), 2);
                        double totalDiff = Math.sqrt(rDiff + gDiff + bDiff);

                        if (totalDiff < 1) {
                            colourCollection.remove(innerColour);
                        }
                    }
                }
            }*/
            //TreeMap<Color, ArrayList<Point>> concurrByColour = new TreeMap<Color, ArrayList<Point>>();
            byColour = new HashMap<Color, ArrayList<Point>>();
            for(int i=0;i < picHeight;i++) {
                for(int j=0;j < picWidth;j++) {
                    Color pxlClr = combinedMap.get(new Point(j, i));
                    Color subClr = colourToColour.get(pxlClr);
                    //System.out.println("Looking at point [X: " + j + ", Y: " + i + "] with colour: " + combinedMap.get(new Point(j, i)));

                    if(subClr != null) {
                        if(!byColour.isEmpty()) {
                            if (byColour.containsKey(subClr)) {
                                byColour.get(subClr).add(new Point(j, i));
                            }
                            else {
                                ArrayList<Point> list = new ArrayList<Point>();
                                list.add(new Point(j, i));
                                byColour.put(subClr, list);
                            }
                        }
                        else {
                            ArrayList<Point> list = new ArrayList<Point>();
                            list.add(new Point(j, i));
                            byColour.put(subClr, list);
                        }
                    }
                    else {
                        if(!byColour.isEmpty()) {
                            if (byColour.containsKey(pxlClr)) {
                                byColour.get(pxlClr).add(new Point(j, i));
                            }
                            else {
                                ArrayList<Point> list = new ArrayList<Point>();
                                list.add(new Point(j, i));
                                byColour.put(pxlClr, list);
                            }
                        }
                        else {
                            ArrayList<Point> list = new ArrayList<Point>();
                            list.add(new Point(j, i));
                            byColour.put(pxlClr, list);
                        }
                    }
                }
            }

            //byColour = new HashMap<Color, ArrayList<Point>>();
            //byColour.putAll(concurrByColour);

            /*Iterator<Map.Entry<Color, ArrayList<Point>>> iter = byColor.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Color, ArrayList<Point>> entry = iter.next();
                Color colour = entry.getKey();
                ArrayList<Point> points = entry.getValue();
                System.out.println("For Colour: " + colour + ": " + points.toString());
            }*/

            /*for (int i = 0; i < bufferedImage.getData().getHeight(); i++) {
                for (int j = 0; j < bufferedImage.getData().getWidth(); j++) {
                    doubles = bufferedImage.getData().getPixel(j, i, doubles);
                    if(doubles[0] != doubles[1] || doubles[0] != doubles[2] || doubles[0] != 255.0) {
                        System.out.println("Checking X: " + j + ", Y: " + i + ", Colour: " + java.util.Arrays.toString(doubles));
                    }
                    else  {
                        System.out.println("Checking X: " + j + ", Y: " + i);
                    }
                    doubles = null;
                }
            }*/
        }

        return byColour;
    }

    private class ImageProcessorThread implements Callable<HashMap<Point, Color>> {
        private int x, y, width, height;
        private Raster raster;

        public ImageProcessorThread(int x, int y, int width, int height, Raster raster) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.raster = raster;
        }

        public HashMap<Point, Color> call() {
            HashMap<Point, Color> map = new HashMap<Point, Color>();

            double[] doubles = null;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    doubles = raster.getPixel(j + x, i + y, doubles);
                    /*if (doubles[0] != doubles[1] || doubles[0] != doubles[2] || doubles[0] != 255.0) {
                        System.out.println("Checking X: " + (j + x) + ", Y: " + (i + y) + ", Colour: " + java.util.Arrays.toString(doubles));
                    }
                    else {
                        System.out.println("Checking X: " + (j + x) + ", Y: " + (i + y));
                    }*/
                    map.put(new Point(j + x, i + y), new Color((int)doubles[0], (int)doubles[1], (int)doubles[2]));
                    doubles = null;
                }
            }

            return map;
        }
    }

    private class ImageColourProcessorThread implements Callable<HashMap<Color, Color>> {
        private Set<Color> localSet;
        private Set<Color> fullSet;

        public ImageColourProcessorThread(Set<Color> localSet, Set<Color> fullSet) {
            this.localSet = localSet;
            this.fullSet = fullSet;
        }
        public HashMap<Color, Color> call() {
            HashMap<Color, Color> colourToColour = new HashMap<Color, Color>();

            for(Color clr : localSet) {
                //System.out.println("Processing colour: " + clr);
                Color matchColor = null;
                for(Color clr2 : fullSet) {
                    if(!clr.equals(clr2)) {
                        double rDiff = Math.pow(clr.getRed() - clr2.getRed(), 2);
                        double gDiff = Math.pow(clr.getGreen() - clr2.getGreen(), 2);
                        double bDiff = Math.pow(clr.getBlue() - clr2.getBlue(), 2);
                        double totalDiff = Math.sqrt(rDiff + gDiff + bDiff);

                        float[] clrHSB = new float[3];
                        Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), clrHSB);
                        float[] clr2HSB = new float[3];
                        Color.RGBtoHSB(clr2.getRed(), clr2.getGreen(), clr2.getBlue(), clr2HSB);

                        double hDiff = Math.min(Math.abs(clrHSB[0] - clr2HSB[0]), 360 - (clrHSB[0] - clr2HSB[0]))/180.0;
                        double sDiff = Math.abs(clrHSB[1] - clr2HSB[1]);
                        double vDiff = Math.abs(clrHSB[2] - clr2HSB[2])/255.0;
                        double totalHSVDiff = Math.sqrt(Math.pow(hDiff, 2) + Math.pow(sDiff, 2) + Math.pow(vDiff, 2));

                        if (totalDiff < 105 || totalHSVDiff < 0.00275/*100*/) {
                            matchColor = clr2;
                        }
                    }
                }
                colourToColour.put(clr, matchColor);
            }

            return colourToColour;
        }
    }
}
