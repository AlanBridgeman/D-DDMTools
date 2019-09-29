import java.awt.Dimension;
import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JButton;

public class Main implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main());
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Test");

        frame.getContentPane().setLayout(new BorderLayout());

        //World world = new World();
        //world.addContinent(new Continent("Europe"));
        //MapEditor mapEditor = world.getMap().getEditor();//new MapEditor();
        //JScrollPane scrollPane  = new JScrollPane(mapEditor);
        //frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        ImageProcessor imageProcessor = new ImageProcessor();
        java.util.HashMap<java.awt.Color, java.util.ArrayList<java.awt.Point>> map = imageProcessor.processImage();
        System.out.println("Number of colours: " + map.size());
        frame.getContentPane().add(new javax.swing.JPanel() {
            public void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2D = (java.awt.Graphics2D)g;
                int counter = 0;
                java.util.Iterator<java.util.Map.Entry<java.awt.Color, java.util.ArrayList<java.awt.Point>>> iter = map.entrySet().iterator();
                while(iter.hasNext()) {
                    java.util.Map.Entry<java.awt.Color, java.util.ArrayList<java.awt.Point>> entry = iter.next();
                    java.awt.Color colour = entry.getKey();
                    java.util.ArrayList<java.awt.Point> points = entry.getValue();

                    if(/*counter == 135 || counter == 234 || counter == 320 || counter == 362 || counter == 396 || counter == 403 || */ counter == 50) {
                        //System.out.println(java.util.Arrays.toString(points.toArray()));

                        java.awt.Color currColor = g2D.getColor();
                        g2D.setColor(colour);

                        for (java.awt.Point point : points) {
                            if(point.y < 259) {
                                System.out.println("new Point(" + point.x + ", " + point.y + "),");
                                g2D.draw(new java.awt.geom.Rectangle2D.Float(point.x, point.y, 1, 1));
                            }
                        }
                        g2D.setColor(currColor);
                    }
                    counter++;
                }
            }
        });

        JButton button = new JButton("Draw Mode");
        //button.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){mapEditor.setEditMode(!mapEditor.getEditMode());mapEditor.repaint();}});
        frame.getContentPane().add(button, BorderLayout.SOUTH);

        frame.setSize(new Dimension(640, 480));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
