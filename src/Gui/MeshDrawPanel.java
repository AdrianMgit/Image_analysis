package Gui;

import code.*;

import javax.swing.*;
import java.awt.*;


public class MeshDrawPanel extends JPanel {
    private DrawData drawData;

    public MeshDrawPanel(DrawData dm) {

        this.drawData = dm;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //------------------------------------------RYSOWANIE OBRAZU------------------------------------------
        if (drawData.getBgImg() != null)
            g2.drawImage(drawData.getBgImg(), 0, 0, this);

        //------------------------------------------

    }

    @Override
    public void repaint() {
        super.repaint();
    }


}
