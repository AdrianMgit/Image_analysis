package Gui;

import code.DrawData;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.util.Date;


public class AutDrawPanel extends JPanel {

    private DrawData drawData;
    int pointSize;
    int xScale;
    int yScale;
    int chosenScale;
    int upDownDistance=20;
    int leftAndRightDistance=60;        //po 20 z lewej i prawej


    public AutDrawPanel(DrawData dm) {

        this.drawData = dm;


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        if(drawData.getTabAutomat()!=null) {
            int difference;
            int cellSize=drawData.getTabAutomat()[0][0].getSize();
            int realLenghtX=drawData.getTabAutomat()[0].length*cellSize;
            int realLenghtY=drawData.getTabAutomat().length*cellSize;
            if(drawData.getScale()==0) {                //gdy uzytkownik wpisal 0 to automatyczna
                xScale = (this.getWidth() - leftAndRightDistance) / realLenghtX;  //dostosowanie do okna w poziomie
                yScale = (this.getHeight() - upDownDistance) / realLenghtY;       ////dostosowanie do okna w pionie

                //wybieram najmniejsza skale, zeby do niej dostosowac wielkosc
                if (xScale < yScale) {
                    chosenScale = xScale;
                } else
                    chosenScale = yScale;
            }
            //gdy uzytkownik podal skale
            else {
                chosenScale = drawData.getScale();
            }
            //przesunieceie tablicy na srodek okna
            difference=((this.getWidth()-leftAndRightDistance)-(realLenghtX*chosenScale))/2;
            //wielkosc komorki po przeskalowaniu
            pointSize = cellSize*chosenScale;

            //---------RYSOWANIE----------
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            //SIATKA PIONOWA
            for (int i = 0; (i * chosenScale)+difference < this.getWidth() ||(i * chosenScale) < this.getWidth()  ; i++) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(((i * chosenScale) + difference)%this.getWidth(), 0, ((i * chosenScale) + difference)%this.getWidth(), this.getHeight());
            }

            //SIATKA POZIOMA
            for (int i = 0; i * chosenScale < this.getHeight(); i++) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(0, i * chosenScale, this.getWidth(), i * chosenScale);
            }

            //rysowanie kwadratow
            g.setColor(Color.BLUE);
            for (int y = 0; y < drawData.getTabAutomat().length; y++) {
                for (int x = 0; x < drawData.getTabAutomat()[0].length;x++) {
                    if (drawData.getTabAutomat()[y][x].getValue()==1)
                        g.fillRect((pointSize * x)+difference, pointSize * y, pointSize, pointSize);

                }
            }
        }


        //------------------------------------- GAME OF LIFE -------------------------------------------
        if(drawData.getGameOfLife()!=null){

            int cellSize=drawData.getGameOfLife().getCellSize();
            int realMatrixSize=drawData.getGameOfLife().getSpaceSize()*cellSize;
            //--------SKALOWANIE------
            if(drawData.getScale()==0) {                //gdy uzytkownik wpisal 0 to automatyczna
                xScale = (this.getWidth() - leftAndRightDistance) / realMatrixSize;  //dostosowanie do okna w poziomie
                yScale = (this.getHeight() - upDownDistance) / realMatrixSize;       ////dostosowanie do okna w pionie

                //wybieram najmniejsza skale, zeby do niej dostosowac wielkosc
                if (xScale < yScale) {
                    chosenScale = xScale;
                } else
                    chosenScale = yScale;
            }
            //gdy uzytkownik podal skale
            else {
                chosenScale = drawData.getScale();
            }

            //wielkosc komorki po przeskalowaniu
            pointSize = cellSize*chosenScale;
            drawData.setChosenScale(pointSize);       //skala do drawdata

            //---------RYSOWANIE----------
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            //SIATKA PIONOWA
            for (int i = 0; (i * chosenScale) < this.getWidth(); i++) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine((i * chosenScale), 0, (i * chosenScale), this.getHeight());
            }

            //SIATKA POZIOMA
            for (int i = 0; i * chosenScale < this.getHeight(); i++) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(0, i * chosenScale, this.getWidth(), i * chosenScale);
            }

            //rysowanie kwadratow
            g.setColor(Color.BLUE);
            for(int iteration=0;iteration<drawData.getGameOfLife().getIterationNow();iteration++) {

                for (int y = 0; y < drawData.getGameOfLife().getSpaceSize(); y++) {
                    for (int x = 0; x < drawData.getGameOfLife().getSpaceSize(); x++) {
                        if(iteration!=0) {      //MAZE POPRZEDNIA ITERACJE
                            if (drawData.getGameOfLife().getSpace()[iteration - 1][y][x].getValue() == 1) {
                                g.setColor(Color.WHITE);
                                g.fillRect((pointSize * x), pointSize * y, pointSize, pointSize);
                                //odtworzenie linii siatki
                                g.setColor(Color.LIGHT_GRAY);
                                for(int i=0;i<pointSize;i++) {
                                    g.drawRect(((x* pointSize)+(i*chosenScale) ), pointSize * y, chosenScale, chosenScale);
                                    g.drawRect((x * pointSize ), (pointSize * y)+(i*chosenScale), chosenScale, chosenScale);
                                }

                            }
                        }
                        if (drawData.getGameOfLife().getSpace()[iteration][y][x].getValue() == 1) {  //RYSWOANIE OBECNEJ ITERACJI
                            g.setColor(Color.BLUE);
                            g.fillRect((pointSize * x), pointSize * y, pointSize, pointSize);
                        }

                    }
                }
                //PLANSZA
                g.setColor(Color.RED);
                g.drawRect(0,0, realMatrixSize*chosenScale, realMatrixSize*chosenScale);

            }
        }

    }

    @Override
    public void repaint() {
        super.repaint();
    }



}