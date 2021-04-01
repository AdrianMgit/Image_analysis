package Gui;

import code.DrawData;
import code.Functions;
import code.GameOfLife;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;


public class AutomatFrame extends JFrame {

    DrawData drawData;
    private JPanel rootPanel;
    private JPanel controllPanel;
    private JTextField ruleTextField;
    private JTextField tableSizeTextField;
    private JTextField iterationAmountTextField;
    private JComboBox boundaryConditionsComboBox;
    private JButton createAutomatButton;
    private JPanel drawPanel;
    private JTextField scaleTextField;
    private JTextField cellSizeTextField;
    private JTextField spaceSizeTextField;
    private JComboBox initialStateComboBox;
    private JButton gameOfLifeButton;
    private JTextField iterationAmountGameOfLifetextField;
    private JButton startButton;
    private JButton stopButton;
    private JComboBox directionComboBox;
    private JPanel upPanel;
    private JPanel automatPanel;
    private JSlider timeSlider;
    private JLabel timeSliderValueLabel;
    private JButton previousButton;
    private JButton previousStepButton;
    private JProgressBar iterationProgressBar;
    private Timer gameOfLifeTimer;


    public AutomatFrame(DrawData drawData, int width, int height) {

        initialize();
        setMinimumSize(new Dimension(width, height));
        setTitle("Aut");

        setLocationRelativeTo(null);
        drawPanel = new AutDrawPanel(drawData);
        drawPanel.setMinimumSize(new Dimension(width-300, height-300));
        drawPanel.setBackground(Color.LIGHT_GRAY);

        rootPanel.add(BorderLayout.CENTER, drawPanel);
        rootPanel.add(BorderLayout.EAST, controllPanel);
        rootPanel.add(BorderLayout.NORTH, upPanel);

        add(rootPanel);


        //-------------------------------------- BUTTON CREATE AUTOMAT --------------------------------
        createAutomatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    drawData.setScale(0);
                    drawData.setGameOfLife(null);
                try {
                    int rule = Integer.parseInt(ruleTextField.getText());
                    int tabSize = Integer.parseInt(tableSizeTextField.getText());
                    int iterationAmount = Integer.parseInt(iterationAmountTextField.getText());
                    int scale=Integer.parseInt(scaleTextField.getText());
                    int cellSize=Integer.parseInt(cellSizeTextField.getText());
                    if(rule<0 || rule>256 || tabSize<=0 || iterationAmount<=0 || scale<0 || cellSize<0)
                        JOptionPane.showMessageDialog(null, "WRONG VALUE!");
                    else if(tabSize*cellSize>1000 || iterationAmount*cellSize>1000) {     //gdy wieksze od rozdz. ekranu
                        JOptionPane.showMessageDialog(null, "Too many!");
                    }
                    else{
                        drawData.setScale(scale);
                        if((tabSize*cellSize)+600>width || (iterationAmount*cellSize)+600>height){            //gdy rozmiary tab wieksze od rozm
                            setSize((tabSize*cellSize)+600,(iterationAmount*cellSize)+100);   //minimalnych okna
                            setLocationRelativeTo(null);
                        }
                        else{                                                       //jesli nie to przywwracam wielkosc
                            setSize(width,height);                                  //panelu do rysowania
                            drawPanel.setSize(width-180,height);
                            setLocationRelativeTo(null);

                        }


                        if(boundaryConditionsComboBox.getSelectedItem().equals("1"))
                        drawData.setTabAutomat(Functions.automat1D(tabSize,cellSize, iterationAmount, rule, 0));
                        else drawData.setTabAutomat(Functions.automat1D(tabSize,cellSize, iterationAmount, rule, 1));
                        drawPanel.repaint();
                    }

                }catch (NumberFormatException numberEx) {
                    JOptionPane.showMessageDialog(null, "WRONG VALUE!");
                }
            }
        });



        //----------------------------------------- ZADANIE TMERA ---------------------------------------
        ActionListener timerFunction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if(directionComboBox.getSelectedItem().toString().equals("Forward")) {
                    boolean stillPlay=drawData.getGameOfLife().play();
                    if(stillPlay)
                        iterationProgressBar.setValue(iterationProgressBar.getValue()+1);
                    else{
                        gameOfLifeTimer.stop();
                        JOptionPane.showMessageDialog(null, "FINISH");
                    }


                }
                if(directionComboBox.getSelectedItem().toString().equals("Backward")) {
                    boolean canBack=drawData.getGameOfLife().decrementIteration();
                    if(canBack)
                    iterationProgressBar.setValue(iterationProgressBar.getValue()-1);
                    else{
                        gameOfLifeTimer.stop();
                        JOptionPane.showMessageDialog(null, "FINISH");
                    }
                }

                    drawPanel.repaint();
            }
        };



        //----------------------------------- BUTTON CREATE GAME OF LIFE ----------------------------------------
        gameOfLifeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawData.setTabAutomat(null);
                drawData.setScale(0);
                gameOfLifeTimer.removeActionListener(timerFunction);

                try {
                    int scale = Integer.parseInt(scaleTextField.getText());
                    int cellSize = Integer.parseInt(cellSizeTextField.getText());
                    String initialState = initialStateComboBox.getSelectedItem().toString();
                    int spaceSize = Integer.parseInt(spaceSizeTextField.getText());
                    int iterationAmountGameofLife = Integer.parseInt(iterationAmountGameOfLifetextField.getText());
                    if (scale < 0 || cellSize < 0 || spaceSize <= 0 || iterationAmountGameofLife < 0)
                        JOptionPane.showMessageDialog(null, "WRONG VALUE!");
                    else if (spaceSize * cellSize > 800) {     //gdy wieksze od rozdz. ekranu
                        JOptionPane.showMessageDialog(null, "Too many!");
                    } else {
                        drawData.setScale(scale);
                        if ((spaceSize * cellSize) + 600 > width || (spaceSize * cellSize) > height) {    //gdy rozmiary tab wieksze od rozm
                            setSize((spaceSize * cellSize) + 600, (spaceSize * cellSize) + 100);   //minimalnych okna
                            setLocationRelativeTo(null);
                        } else {                                                       //jesli nie to przywwracam wielkosc
                            setSize(width, height);                                  //panelu do rysowania
                            drawPanel.setSize(width - 180, height);
                            setLocationRelativeTo(null);

                        }
                        drawData.setGameOfLife(new GameOfLife(spaceSize, cellSize, iterationAmountGameofLife));
                        drawData.getGameOfLife().createSpace(initialState);
                        gameOfLifeTimer.addActionListener(timerFunction);
                        iterationProgressBar.setValue(0);
                        iterationProgressBar.setMaximum(iterationAmountGameofLife-1);
                        drawPanel.repaint();
                    }
                }
                catch (NumberFormatException numberEx) {
                    JOptionPane.showMessageDialog(null, "WRONG VALUE!");
                }

            }
        });



        //---------------------------------- BUTTON START ---------------------------------------------
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(drawData.getGameOfLife()!=null)
                    gameOfLifeTimer.start();


            }
        });



        //---------------------------------- BUTTON STOP ------------------------------------------
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOfLifeTimer.stop();
            }
        });



        // --------------------------------- MOUSE LISTENER ---------------------------------------
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(drawData.getGameOfLife()!=null) {
                    int x = (getMousePosition().x - 10) / drawData.getChosenScale();
                    int y = (getMousePosition().y - 50) / drawData.getChosenScale();

                    drawData.getGameOfLife().changeCell(x, y);
                    drawPanel.repaint();
                }

            }
        });

        // ----------------------------------- SLIDER --------------------------------------------
        timeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                gameOfLifeTimer.setDelay(timeSlider.getValue());
                timeSliderValueLabel.setText("Delay: "+timeSlider.getValue()+"ms");

            }
        });

        //----------------------------------- PREVIOUS STEP BUTTON ---------------------------------
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(drawData.getGameOfLife()!=null) {
                    gameOfLifeTimer.stop(); // STOPUJE TIMER
                    boolean canBack=drawData.getGameOfLife().decrementIteration();
                    if(canBack) {
                        iterationProgressBar.setValue(iterationProgressBar.getValue() - 1);
                        drawPanel.repaint();
                    }
                    else
                        JOptionPane.showMessageDialog(null, "FINISH");

                }
            }
        });


        //------------------------------------- NEXT STEP BUTTON ------------------------------------
        previousStepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(drawData.getGameOfLife()!=null) {
                    gameOfLifeTimer.stop(); // STOPUJE TIMER
                    boolean stillPlay=drawData.getGameOfLife().play();
                    if(stillPlay) {
                        iterationProgressBar.setValue(iterationProgressBar.getValue() + 1);
                        drawPanel.repaint();
                    }
                    else
                        JOptionPane.showMessageDialog(null, "FINISH");
                }
            }
        });
    }



    //-----------------------FUNKCJA NADAJACA POCZATKOWE WARTOSCI ATRYBUTOM-----------------
    private void initialize() {

        this.drawData = drawData;
        boundaryConditionsComboBox.addItem("1");
        boundaryConditionsComboBox.addItem("Periodic");

        initialStateComboBox.addItem("Random");
        initialStateComboBox.addItem("Glider");
        initialStateComboBox.addItem("Oscylator");
        initialStateComboBox.addItem("Constant");
        initialStateComboBox.addItem("Custom");

        directionComboBox.addItem("Forward");
        directionComboBox.addItem("Backward");

        gameOfLifeTimer=new Timer(100,null);

        timeSlider.setMinimum(0);
        timeSlider.setMaximum(500);
        timeSlider.setValue(250);
        timeSlider.setMajorTickSpacing(100);

        timeSliderValueLabel.setText("Delay: "+timeSlider.getValue()+"ms");
        iterationProgressBar.setMinimum(0);
        iterationAmountGameOfLifetextField.setBackground(Color.LIGHT_GRAY);
        cellSizeTextField.setBackground(Color.LIGHT_GRAY);
        ruleTextField.setBackground(Color.LIGHT_GRAY);
        scaleTextField.setBackground(Color.LIGHT_GRAY);
        spaceSizeTextField.setBackground(Color.LIGHT_GRAY);
        tableSizeTextField.setBackground(Color.LIGHT_GRAY);
        iterationAmountTextField.setBackground(Color.LIGHT_GRAY);
        initialStateComboBox.setBackground(Color.LIGHT_GRAY);
        boundaryConditionsComboBox.setBackground(Color.LIGHT_GRAY);
        directionComboBox.setBackground(Color.LIGHT_GRAY);
        iterationProgressBar.setBackground(Color.LIGHT_GRAY);

    }

}