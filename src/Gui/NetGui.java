package Gui;


import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class NetGui {
    int width;
    int height;
    int podzialka;
    int skalaPozioma;
    int srodekX;
    int srodekY;


    public NetGui(int width, int height, int podzialka) {
        this.width = width;
        this.height = height;
        this.srodekX = height - 40;
        this.srodekY = 40;
        this.podzialka = podzialka;
        skalaPozioma = width / 255;         //255 bo tyle el w tablicy histogramu
    }

    public void drawMainCoordinates(Graphics g) {
        //GLOWNE OSIE I PODPISY
        g.setColor(Color.BLUE);
        g.drawLine(0, srodekX, width, srodekX);          //linia iksow
        g.drawLine(srodekY, 0, srodekY, height);          //lina y
        g.setColor(Color.BLACK);
        ((Graphics2D) g).drawString("X", width - 20, srodekX + 20);
        ((Graphics2D) g).drawString("Y", srodekY + 10, 60);


    }


    public void drawNet(Graphics g) {
        //SIATKA PIONOWA
        for (int i = 0; i * podzialka < width; i++) {
            g.setColor(Color.RED);
            g.drawLine(srodekY + i * podzialka, 0, srodekY + i * podzialka, height);
            g.setColor(Color.BLACK);
            ((Graphics2D) g).drawString("" + (podzialka) / 3 * i, srodekY + (podzialka * i) - 6, srodekX + 20);
        }

        //SIATKA POZIOMA
        for (int i = 0; i * podzialka < height; i++) {
            g.setColor(Color.RED);
            g.drawLine(0, srodekX - i * podzialka, width, srodekX - i * podzialka);
            g.setColor(Color.BLACK);
            ((Graphics2D) g).drawString("" + podzialka * i, srodekY + 10, srodekX - (podzialka * i));
        }
    }

    public void drawArrowX(Graphics g) {
        drawArrowHead(g, new Point2D.Double(width - 20, srodekX), new Point2D.Double(width - 30, srodekX), Color.BLACK);    //x    }
    }

    public void drawArrowY(Graphics g) {
        drawArrowHead(g, new Point2D.Double(srodekY, 35), new Point2D.Double(srodekY, 60), Color.BLACK);
    }

    private void drawArrowHead(Graphics g2, Point2D tip, Point2D tail, Color color) {
        double phi = Math.toRadians(40);
        int barb = 20;


        g2.setColor(color);
        double dy = tip.getY() - tail.getY();
        double dx = tip.getX() - tail.getX();
        double theta = Math.atan2(dy, dx);
        double x, y, rho = theta + phi;
        for (int j = 0; j < 2; j++) {
            x = tip.getX() - barb * Math.cos(rho);
            y = tip.getY() - barb * Math.sin(rho);


            Graphics2D g2d = (Graphics2D) g2;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            Shape l = new Line2D.Double(tip.getX(), tip.getY(), x, y);
            g2d.draw(l);
            rho = theta - phi;
        }

    }


}

