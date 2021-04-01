package code;

import javax.swing.*;
import java.util.Random;

public class GameOfLife {
    private int spaceSize;
    private Cell[][][] space;
    private int cellSize;
    private int iterationAmount;
    private int iterationNow=1;


    public GameOfLife(int spaceSize,int cellSize,int iterationAmount) {
        this.spaceSize = spaceSize;
        this.cellSize=cellSize;
        this.iterationAmount=iterationAmount;
        space=new Cell[iterationAmount+1][spaceSize][spaceSize];
    }

//------------------------------------------- OBLICZANIE KOLEJNYCH ITERACJI --------------------------
    public boolean play() {

        int livingCellAmount;

        if (iterationNow < iterationAmount) {
            //PRZECHDOZENIE PO MACIERZY
            for (int y = 0; y < spaceSize; y++) {
                for (int x = 0; x < spaceSize ; x++) {

                    livingCellAmount = 0;
                    //PRZECHODZENIE PO SASIADACH
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            if((y-1+i)==-1&&(x-1+j)==-1) {
                                if (space[iterationNow - 1][spaceSize - 1][spaceSize - 1].getValue() == 1)
                                    livingCellAmount++;
                            }
                            else if((y-1+i)==-1&&(x-1+j)==spaceSize) {
                                if (space[iterationNow - 1][spaceSize - 1][0].getValue() == 1)
                                    livingCellAmount++;
                            }
                            else if((y-1+i)==spaceSize&&(x-1+j)==spaceSize) {
                                if (space[iterationNow - 1][0][0].getValue() == 1)
                                    livingCellAmount++;
                            }
                            else if((y-1+i)==spaceSize&&(x-1+j)==-1) {
                                if (space[iterationNow - 1][0][spaceSize - 1].getValue() == 1)
                                    livingCellAmount++;
                            }
                            else if((y-1+i)==-1) {
                                if (space[iterationNow - 1][spaceSize - 1][x - 1 + j].getValue() == 1)
                                    livingCellAmount++;
                            }
                            else if((x-1+j)==spaceSize) {
                                if (space[iterationNow - 1][y - 1 + i][0].getValue() == 1)
                                    livingCellAmount++;
                            }
                            else if((y-1+i)==spaceSize) {
                                if (space[iterationNow - 1][0][x - 1 + j].getValue() == 1)
                                    livingCellAmount++;
                            }
                            else if((x-1+j)==-1) {
                                if (space[iterationNow - 1][y - 1 + i][spaceSize-1].getValue() == 1)
                                    livingCellAmount++;
                            }
                            else if (i == 1 && j == 1) ;         //NIE LICZE SAMEGO SIEBIE
                            else if (space[iterationNow - 1][y - 1 + i][x - 1 + j].getValue() == 1) { //nie na brzegach
                                    livingCellAmount++;
                                }
                        }
                    }
                    //kiedy komorka jest martwa
                    if (space[iterationNow - 1][y][x].getValue() == 0) {
                        if (livingCellAmount == 3)
                            space[iterationNow][y][x].setValue(1);
                    }
                    //kiedy komorka jest zywa
                    else if (space[iterationNow - 1][y][x].getValue() == 1) {
                        if (livingCellAmount > 3 || livingCellAmount < 2) {
                            space[iterationNow][y][x].setValue(0);
                        }
                        if (livingCellAmount == 2 || livingCellAmount == 3) {
                            space[iterationNow][y][x].setValue(1);
                        }
                    }
                }
            }
            iterationNow++;
            return true;
        }
        return false;
    }


    // ---------------------------------------- TWORZENIE PLANSZY POCZATKOWEJ ------------------------------------
    public void createSpace(String type) {

        // INICJALIZACJA MACIERZY
        for(int t=0;t<iterationAmount;t++) {
            for (int i = 0; i < spaceSize; i++) {
                for (int j = 0; j < spaceSize; j++) {
                    space[t][i][j] = new Cell(cellSize, 0);
                }
            }
        }
        int centerCoordinate=spaceSize/2;
        if(type.equals("Constant")){
            space[0][centerCoordinate-1][centerCoordinate-1].setValue(1);
            space[0][centerCoordinate-1][centerCoordinate].setValue(1);
            space[0][centerCoordinate][centerCoordinate-2].setValue(1);
            space[0][centerCoordinate][centerCoordinate+1].setValue(1);
            space[0][centerCoordinate+1][centerCoordinate-1].setValue(1);
            space[0][centerCoordinate+1][centerCoordinate].setValue(1);
        }
        else if(type.equals("Glider")){
            space[0][centerCoordinate-1][centerCoordinate-1].setValue(1);
            space[0][centerCoordinate-1][centerCoordinate].setValue(1);
            space[0][centerCoordinate][centerCoordinate-2].setValue(1);
            space[0][centerCoordinate][centerCoordinate-1].setValue(1);
            space[0][centerCoordinate+1][centerCoordinate].setValue(1);
        }
        else if(type.equals("Oscylator")){
            space[0][centerCoordinate-2][centerCoordinate].setValue(1);
            space[0][centerCoordinate-1][centerCoordinate].setValue(1);
            space[0][centerCoordinate][centerCoordinate].setValue(1);

        }

        else if(type.equals("Random")){
            Random rand = new Random();
            int amount = spaceSize;
            int sectionSize=spaceSize/2;
            int x;
            int y;
            int leftBoundary=spaceSize/2-sectionSize/2;
            int rightBoundary=spaceSize/2+sectionSize/2;
            int upBoundary=spaceSize/2-sectionSize/2;
            int downBoundary=spaceSize/2+sectionSize/2;
            for(int i=0;i<amount;i++){
                x=rand.nextInt(rightBoundary-leftBoundary+1)+leftBoundary;
                y=rand.nextInt(downBoundary-upBoundary+1)+upBoundary;
                space[0][x][y].setValue(1);
            }
        }
          else  return;
    }


    // -------------------------------------- COFANIE (RYSUJE DO iterationNow) ------------------------------------
    public boolean decrementIteration(){
        if(iterationNow>1) {
            iterationNow--;
            return true;
        }
        return false;
    }


    //-------------------------------------- ZMIANA WARTOSCI KOMORKI WYBRANEJ MYSZKA -------------------------------
    public void changeCell(int x,int y){
        if(space[0][y][x].getValue()==0)
            space[0][y][x].setValue(1);
        else
            space[0][y][x].setValue(0);
    }




    public int getSpaceSize() {
        return spaceSize;
    }

    public void setSpaceSize(int spaceSize) {
        this.spaceSize = spaceSize;
    }
    public Cell[][][] getSpace() {
        return space;
    }

    public void setSpace(Cell[][][] space) {
        this.space = space;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public int getIterationAmount() {
        return iterationAmount;
    }

    public void setIterationAmount(int iterationAmount) {
        this.iterationAmount = iterationAmount;
    }

    public int getIterationNow() {
        return iterationNow;
    }

    public void setIterationNow(int iterationNow) {
        this.iterationNow = iterationNow;
    }

}
