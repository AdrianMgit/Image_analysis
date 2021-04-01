package code;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawData {

    private BufferedImage bgImg;
    private int widthPicture = 600;
    private int heightPicture = 330;
    private int histogramTab[] = new int[256];
    private Cell tabAutomat[][];
    private int scale;
    private GameOfLife gameOfLife;
    private int chosenScale;


    public GameOfLife getGameOfLife() {
        return gameOfLife;
    }

    public void setGameOfLife(GameOfLife gameOfLife) {
        this.gameOfLife = gameOfLife;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public Cell[][] getTabAutomat() {
        return tabAutomat;
    }

    public void setTabAutomat(Cell[][] tabAutomat) {
        this.tabAutomat = tabAutomat;
    }

    public DrawData() {
        bgImg = null;
    }


    public int[] getHistogramTab() {
        return histogramTab;
    }

    public void setHistogramTab(int[] histogramTab) {
        this.histogramTab = histogramTab;
    }

    public int getWidthPicture() {
        return widthPicture;
    }

    public void setWidthPicture(int widthPicture) {
        this.widthPicture = widthPicture;
    }

    public int getHeightPicture() {
        return heightPicture;
    }

    public void setHeightPicture(int heightPicture) {
        this.heightPicture = heightPicture;
    }

    public void setBgImg(BufferedImage bgImg) {
        this.bgImg = bgImg;
    }

    public BufferedImage getBgImg() {
        return bgImg;
    }

    public int getChosenScale() {
        return chosenScale;
    }

    public void setChosenScale(int chosenScale) {
        this.chosenScale = chosenScale;
    }
}