package Gui;

import code.DrawData;
import code.Functions;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class GuiClass extends JFrame {
    private JPanel rootPanel;
    private JPanel drawPanel;
    private JPanel controllerPanel;
    private JButton ReadPicture;
    private JButton readTxtButton;
    private JButton brightenPictureButton;
    private JButton darkenPictureButton;
    private JButton binarizationButton;
    private JComboBox readBmpComboBox;
    private JComboBox readTxtComboBox;
    private JTextField lightPictureValueField;
    private JTextField darkPictureValueField;
    private JTextField limitValueField;
    private JButton histogramButton;
    private JComboBox contextTransComboBox;
    private JButton transformButton;
    private JButton automatButton;

    DrawData drawData;
    //wielkosc poczatkowa panelu
    private int minimumWidthPanel;
    private int minimumHeightPanel;
    //O ile panel będzie większy od wczytanego obrazka
    private int widthDifference;
    private int heightDifference;
    private int histogramPanelwidth;
    private int histogramPanelHeight;
    private final String lowPassFilterString = "Low pass filter";
    private final String highPassFilterString = "High Pass filter";
    private final String gaussFilterString = "Gauss filter";
    private final String embossString = "Emboss";
    private final String erosionString = "Erosion";
    private final String dilationString = "Dilation";
    private final String openingString = "Opening";
    private final String closingString = "Closing";


    public GuiClass() {

        //funkcja ustawiajaca wartosci poczatkowe atrybutow
        initialize();

        setMinimumSize(new Dimension(minimumWidthPanel, minimumHeightPanel));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("App");
        setLocationRelativeTo(null);
        drawPanel = new MeshDrawPanel(drawData);
        rootPanel.add(BorderLayout.CENTER, drawPanel);
        rootPanel.add(BorderLayout.EAST, controllerPanel);
        add(rootPanel);


        //-----------------------------------------BUTTON WCZYTANIA OBRAZU--------------------------------
        ReadPicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage image = Functions.readImage(readBmpComboBox.getSelectedItem().toString());
                if (image != null) {
                    drawData.setBgImg(image);
                    //USTAWIAM WIELKOSC PANELU WIEKSZA O ZADANA ROZNICE
                    setSize(drawData.getWidthPicture() + widthDifference, drawData.getHeightPicture() + heightDifference);
                    setLocationRelativeTo(null);
                    drawPanel.repaint();
                } else
                    JOptionPane.showMessageDialog(null, "PICTURE FILE NOT FOUND!");
            }
        });


        //-----------------------------------------BUTTON WCZYTANIA OBRAZU Z PLIKU TXT--------------------------------
        readTxtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BufferedImage image = Functions.readImageTxt(readTxtComboBox.getSelectedItem().toString()
                        , drawData.getWidthPicture(), drawData.getHeightPicture());

                if (image != null) {
                    drawData.setBgImg(image);
                    //USTAWIAM WIELKOSC PANELU WIEKSZA O ZADANA ROZNICE
                    setSize(drawData.getWidthPicture() + widthDifference, drawData.getHeightPicture() + heightDifference);
                    setLocationRelativeTo(null);
                    drawPanel.repaint();

                } else
                    JOptionPane.showMessageDialog(null, "PICTURE FILE NOT FOUND!");
            }
        });


        //----------------------------------BUTTON ROZJASNIENIA OBRAZU O ZADANA WARTOSC--------------------------------
        brightenPictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int brightenValue = Integer.parseInt(lightPictureValueField.getText());
                    Functions.brightenImage(drawData.getBgImg(), brightenValue);
                    drawPanel.repaint();
                } catch (NullPointerException nullEx) {
                    JOptionPane.showMessageDialog(null, "PICTURE NOT LOADED!");
                } catch (NumberFormatException numberEx) {
                    JOptionPane.showMessageDialog(null, "WRONG VALUE!");
                }

            }
        });


        //-----------------------------BUTTON PRZYCIEMNIENIA OBRAZU O PROCENTOWA WARTOSC--------------------------------
        darkenPictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int darkenValue = Integer.parseInt(darkPictureValueField.getText());
                    Functions.darkenImage(drawData.getBgImg(), darkenValue);
                    drawPanel.repaint();

                } catch (NullPointerException nullEx) {
                    JOptionPane.showMessageDialog(null, "PICTURE NOT LOADED!");
                } catch (NumberFormatException numberEx) {
                    JOptionPane.showMessageDialog(null, "WRONG VALUE!");
                }
            }
        });


        //------------BUTTON BINARYZACJI OBRAZU W ZALEZNOSCI OD ZADANEGO LIMITU JASNOSCI PIKSELA-------------------
        binarizationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int limitValue = Integer.parseInt(limitValueField.getText());
                    Functions.binarizationImage(drawData.getBgImg(), limitValue);
                    drawPanel.repaint();
                } catch (NullPointerException nullEx) {
                    JOptionPane.showMessageDialog(null, "PICTURE NOT LOADED!");
                } catch (NumberFormatException numberEx) {
                    JOptionPane.showMessageDialog(null, "WRONG VALUE!");
                }

            }
        });


        //------------------------------------BUTTON TWORZENIA HISTOGRAMU----------------
        histogramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    drawData.setHistogramTab(Functions.createHistogramTab(drawData.getBgImg(), histogramPanelHeight - 100));         //ustawienie tablicy histogramu w drawData
                    new HistogramPanel(drawData, histogramPanelwidth, histogramPanelHeight).show();  // uruchomienie nowego okna
                } catch (NullPointerException nullEx) {
                    JOptionPane.showMessageDialog(null, "PICTURE NOT LOADED!");
                } catch (NumberFormatException numberEx) {
                    JOptionPane.showMessageDialog(null, "WRONG VALUE!");
                }
            }
        });


        //---------------------------------- BUTTON TRANSFORMACJI ----------------------------------
        transformButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //GDY OBRAZ ZOSTAL WCZYTANY
                if (drawData.getBgImg() != null) {

                    switch (contextTransComboBox.getSelectedItem().toString()) {

                        case lowPassFilterString:
                            float[][] lowPassFilterMatrix = new float[3][3];
                            for (int i = 0; i < lowPassFilterMatrix.length; i++) {
                                for (int j = 0; j < lowPassFilterMatrix[i].length; j++) {
                                    lowPassFilterMatrix[i][j] = (float) 1.0;
                                }
                            }
                            drawData.setBgImg(Functions.filr(drawData.getBgImg(), lowPassFilterMatrix));
                            break;

                        case highPassFilterString:
                            float[][] highPassFilterMatrix = new float[3][3];
                            for (int i = 0; i < highPassFilterMatrix.length; i++) {
                                for (int j = 0; j < highPassFilterMatrix[i].length; j++) {
                                    highPassFilterMatrix[i][j] = (float) (-1.0);
                                    System.out.println(highPassFilterMatrix[i][j]);
                                }
                            }
                            highPassFilterMatrix[1][1] = 9.0f;
                            drawData.setBgImg(Functions.filr(drawData.getBgImg(), highPassFilterMatrix));
                            break;

                        case gaussFilterString:
                            float[][] gaussFilterMatrix = new float[3][3];
                            gaussFilterMatrix[0][0] = 1;
                            gaussFilterMatrix[0][1] = 4;
                            gaussFilterMatrix[0][2] = 1;
                            gaussFilterMatrix[1][0] = 4;
                            gaussFilterMatrix[1][1] = 32;
                            gaussFilterMatrix[1][2] = 4;
                            gaussFilterMatrix[2][0] = 1;
                            gaussFilterMatrix[2][1] = 4;
                            gaussFilterMatrix[2][2] = 1;
                            drawData.setBgImg(Functions.filr(drawData.getBgImg(), gaussFilterMatrix));
                            break;

                        case embossString:
                            float[][] embosMatrix = new float[3][3];
                            embosMatrix[0][0] = -2;
                            embosMatrix[0][1] = -1;
                            embosMatrix[0][2] = 0;
                            embosMatrix[1][0] = 1;
                            embosMatrix[1][1] = 1;
                            embosMatrix[1][2] = 1;
                            embosMatrix[2][0] = 0;
                            embosMatrix[2][1] = 1;
                            embosMatrix[2][2] = 2;
                            drawData.setBgImg(Functions.filr(drawData.getBgImg(), embosMatrix));
                            break;

                        case erosionString:
                            drawData.setBgImg(Functions.morphTransfor(drawData.getBgImg(), 'e'));
                            break;

                        case dilationString:
                            drawData.setBgImg(Functions.morphTransfor(drawData.getBgImg(), 'd'));
                            break;

                        case openingString:
                            drawData.setBgImg(Functions.morphTransfor(drawData.getBgImg(), 'e'));
                            drawData.setBgImg(Functions.morphTransfor(drawData.getBgImg(), 'd'));
                            break;

                        case closingString:
                            drawData.setBgImg(Functions.morphTransfor(drawData.getBgImg(), 'd'));
                            drawData.setBgImg(Functions.morphTransfor(drawData.getBgImg(), 'e'));
                            break;

                        default:
                            JOptionPane.showMessageDialog(null, "Operation not found in switch");
                    }
                    drawPanel.repaint();
                } else
                    JOptionPane.showMessageDialog(null, "PICTURE NOT LOADED!");
            }
        });




        //-------------------------------- AUTOMAT ----------------------------
        automatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // uruchomienie nowego okna
                java.awt.EventQueue.invokeLater(() -> new AutomatFrame(drawData, 600, 600).setVisible(true));
            }
        });
    }


    //-----------------------FUNKCJA NADAJACA POCZATKOWE WARTOSCI ATRYBUTOM-----------------
    private void initialize() {
        drawData = new DrawData();
        readBmpComboBox.addItem("Mapa_MD_no_terrain_low_res_dark_Gray.bmp");
        readBmpComboBox.addItem("Mapa_MD_no_terrain_low_res_Gray.bmp");

        readTxtComboBox.addItem("Mapa_MD_no_terrain_low_res_dark_Gray.txt");
        readTxtComboBox.addItem("Mapa_MD_no_terrain_low_res_Gray.txt");

        contextTransComboBox.addItem(lowPassFilterString);
        contextTransComboBox.addItem(highPassFilterString);
        contextTransComboBox.addItem(gaussFilterString);
        contextTransComboBox.addItem(embossString);
        contextTransComboBox.addItem(erosionString);
        contextTransComboBox.addItem(dilationString);
        contextTransComboBox.addItem(openingString);
        contextTransComboBox.addItem(closingString);

        //wielkosc poczatkowa panelu
        minimumWidthPanel = 600;
        minimumHeightPanel = 600;
        //O ile panel będzie większy od wczytanego obrazka
        widthDifference = 400;
        heightDifference = 100;
        histogramPanelwidth = 900;
        histogramPanelHeight = 500;

    }

}
