package Gui;

import code.DrawData;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;


public class HistogramPanel extends JFrame {
    DrawData drawData;
    JPanel panel;
    private int srodekY;
    private int srodekX;
    NetGui net;
    int width;
    int height;
    int skalaPozioma;


    public HistogramPanel(DrawData drawData, int width, int height) {

        setSize(width, height);
        skalaPozioma = width / 255;             //bo 255 el w tablicy histogramu
        System.out.println(skalaPozioma);
        setResizable(false);
        setTitle("HISTOGRAM");
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        panel.setOpaque(true);
        panel.setBackground(Color.orange);
        add(BorderLayout.CENTER, panel);
        setLocationRelativeTo(null);


        this.drawData = drawData;
        this.srodekY = 40;
        this.srodekX = getHeight() - 40;
        this.width = width;
        this.height = height;
        net = new NetGui(width, height, 40);
    }


    public void paint(Graphics g) {
        // RYSOWANIE SIATKI
        super.paint(g);
        net.drawNet(g);
        net.drawMainCoordinates(g);
        net.drawArrowX(g);
        net.drawArrowY(g);


        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //RYSOWANIE HISTOGRAMU
        for (int i = 0; i < drawData.getHistogramTab().length; i++) {
            g.setColor(new Color(i, i, i));
            Shape rectangle;
            rectangle = new Rectangle2D.Double((i * skalaPozioma) + srodekY, srodekX - drawData.getHistogramTab()[i], skalaPozioma, drawData.getHistogramTab()[i]);
            g2d.draw(rectangle);
            g2d.fill(rectangle);

        }


    }
}



