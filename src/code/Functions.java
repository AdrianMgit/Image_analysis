package code;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Functions {


    //------------------------------------------- WCZYTANIE OBRAZU Z PLIKU -----------------------------
    public static BufferedImage readImage(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            return image;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //--------------------------------------- WCZYTANIE OBRAZU Z PLIKU TXT --------------------------------
    public static BufferedImage readImageTxt(String imageTxtPath, int width, int height) {
        try {
            File pictureFile = new File(imageTxtPath);

            BufferedReader br = new BufferedReader(new FileReader(pictureFile));
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //stowrzenie nowego pustego obrazu
            String buffer = br.readLine();
            int row = 0;
            Color pixelColor;
            int pixelValue;
            while (buffer != null) {
                String bufferSplit[] = buffer.split("\t");

                for (int column = 0; column < bufferSplit.length; column++) {
                    pixelValue = Integer.parseInt(bufferSplit[column]);
                    pixelColor = new Color(pixelValue, pixelValue, pixelValue);
                    image.setRGB(column, row, pixelColor.getRGB());
                }
                buffer = br.readLine();
                row++;
            }

            br.close();
            return image;


        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    //-------------------------------------- ROZJASNIENIE OBRAZU O ZADANA WARTOSC ----------------------------
    public static void brightenImage(BufferedImage image, int brightenValue)
            throws NullPointerException, NumberFormatException {

        if (image == null)
            throw new NullPointerException("Image = NULL");
        else if (brightenValue < 0 || brightenValue > 255)
            throw new NumberFormatException("Value must be [0;255]");
        else {

            Color pixelColor;
            int red, green, blue;
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    pixelColor = new Color(image.getRGB(x, y));
                    red = pixelColor.getRed();
                    green = pixelColor.getGreen();
                    blue = pixelColor.getBlue();
                    if (red + brightenValue > 255 || green + brightenValue > 255 || blue + brightenValue > 255) {
                        red = 255;
                        green = 255;
                        blue = 255;
                    } else {
                        red = red + brightenValue;
                        green = green + brightenValue;
                        blue = blue + brightenValue;
                    }
                    image.setRGB(x, y, new Color(red, green, blue).getRGB());
                }
            }
        }
    }


    //-------------------------------------- PRZYCIEMNIENIE OBRAZU O ZADANA WARTOSC ----------------------------
    public static void darkenImage(BufferedImage image, int darkenValue)
            throws NullPointerException, NumberFormatException {

        if (image == null)
            throw new NullPointerException("Image = NULL");
        else if (darkenValue < 0 || darkenValue > 255)
            throw new NumberFormatException("Value must be [0;255]");
        else {
            Color pixelColor;
            int red, green, blue;
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    pixelColor = new Color(image.getRGB(x, y));
                    red = pixelColor.getRed();
                    green = pixelColor.getGreen();
                    blue = pixelColor.getBlue();
                    if (red - darkenValue < 0 || green - darkenValue < 0 || blue - darkenValue < 0) {
                        red = 0;
                        green = 0;
                        blue = 0;
                    } else {
                        red = red - darkenValue;
                        green = green - darkenValue;
                        blue = blue - darkenValue;
                    }
                    image.setRGB(x, y, new Color(red, green, blue).getRGB());
                }
            }
        }
    }


    //--------------------------------------- BINARYZACJA OBRAZU --------------------------------------
    public static void binarizationImage(BufferedImage image, int limitValue)
            throws NullPointerException, NumberFormatException {

        if (image == null)
            throw new NullPointerException("Image = NULL");
        else if (limitValue < 0 || limitValue > 255)
            throw new NumberFormatException("Value must be [0;255]");
        else {
            Color pixelColor;
            int red;
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    pixelColor = new Color(image.getRGB(x, y));
                    red = pixelColor.getRed();
                    if (red < limitValue)
                        red = 0;
                    else
                        red = 255;

                    image.setRGB(x, y, new Color(red, red, red).getRGB());
                }
            }
        }
    }

    //-------------------------------------- TWORZENIE TABLICY HISTOGRAMU ----------------------------------
    public static int[] createHistogramTab(BufferedImage image, int maxValueInTab) {

        if (image == null)
            throw new NullPointerException("Image = NULL");
        else if (maxValueInTab < 0)
            throw new NumberFormatException("Value must be >0");
        else {
            int histogramTab[] = new int[256];
            Color pixelColor;
            //zliczam ilosc pikseli o danej jasnosi, jasnosc to indeks tablicy histogramu
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    pixelColor = new Color(image.getRGB(x, y));
                    histogramTab[pixelColor.getRed()]++;        //red green albo blue, obojetne
                }
            }

            //szukam jaka jest  najwieksza ilosc pikseli w tablicy aby odpowiednio zeskalowac cala tablice
            int maxIlosc = 0;
            for (int i = 0; i < histogramTab.length; i++) {
                if (histogramTab[i] >= maxIlosc) {
                    maxIlosc = histogramTab[i];
                }
            }
            //skaluje tablice, max bedzie maxValueInTab pikseli pod danym indeksem w tablicy
            int dzielnikSkali = maxIlosc / maxValueInTab;
            for (int i = 0; i < histogramTab.length; i++) {
                histogramTab[i] = histogramTab[i] / dzielnikSkali;
                System.out.println(histogramTab[i]);
            }
            return histogramTab;
        }
    }


    // --------------------------------------- FILTROWANIE OBRAZU -----------------------------------
    public static BufferedImage filr(BufferedImage originalImage, float matrix[][]) {


        if (originalImage == null)
            throw new NullPointerException("Image = NULL");
        else {
            //OBLICZAM SUME EL TABLICY FILTRU BY NA KONCU DOKONAC NORMALIZACJI
            float sumaElMatrix = 0.0f;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    sumaElMatrix = sumaElMatrix + matrix[i][j];
                }
            }

            Color pixelColor;
            float sumaRed, sumaGreen, sumaBlue;
            BufferedImage image = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB); //stworzenie nowego pustego obrazu

            for (int y = 0; y < originalImage.getHeight(); y++) {
                for (int x = 0; x < originalImage.getWidth(); x++) {
                    sumaRed = 0;
                    sumaGreen = 0;
                    sumaBlue = 0;
                    if (x > 0 && x < originalImage.getWidth() - 1 && y > 0 && y < originalImage.getHeight() - 1) {

                        for (int i = 0; i < matrix.length; i++) {
                            for (int j = 0; j < matrix[i].length; j++) {

                                pixelColor = new Color(originalImage.getRGB(x - 1 + j, y - 1 + i));
                                sumaRed = sumaRed + (pixelColor.getRed() * matrix[j][i]);
                                sumaBlue = sumaBlue + (pixelColor.getGreen() * matrix[j][i]);
                                sumaGreen = sumaGreen + (pixelColor.getBlue() * matrix[j][i]);

                            }
                        }

                        if (sumaElMatrix != 0) {
                            sumaRed = sumaRed / sumaElMatrix;
                            sumaGreen = sumaGreen / sumaElMatrix;
                            sumaBlue = sumaBlue / sumaElMatrix;
                        }

                        if (sumaRed > 255 || sumaGreen > 255 || sumaBlue > 255)
                            pixelColor = new Color(255, 255, 255);
                        else if (sumaRed < 0 || sumaGreen < 0 || sumaBlue < 0) {
                            pixelColor = new Color(0, 0, 0);
                        } else {
                            pixelColor = new Color((int) sumaRed, (int) sumaGreen, (int) sumaBlue);
                        }
                    } else            //zeby nie bylo czarnej ramki
                    {
                        pixelColor = new Color(originalImage.getRGB(x, y));
                    }
                    image.setRGB(x, y, pixelColor.getRGB());

                }
            }
            return image;
        }
    }


    // ----------------------------------------- PRZEKSZTALCENIA MORFOLOGICZNE -----------------------------
    public static BufferedImage morphTransfor(BufferedImage originalImage, char operationType) {

        if (originalImage == null)
            throw new NullPointerException("Image = NULL");
        else {
            int pixelValueSearched = 0;

            if (operationType == 'd')              //DYLATACJA
                pixelValueSearched = 0;
            else if (operationType == 'e')          //EROZJA
                pixelValueSearched = 255;

            Color pixelColor;

            BufferedImage image = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB); //stowrzenie nowego pustego obrazu
            Graphics2D g2d = image.createGraphics();
            g2d.drawImage(originalImage, 0, 0, null);

            boolean foundElement;

            for (int y = 0; y < originalImage.getHeight(); y++) {
                for (int x = 0; x < originalImage.getWidth(); x++) {
                    foundElement = false;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {

                            if (x > 0 && x < originalImage.getWidth() - 1 && y > 0 && y < originalImage.getHeight() - 1) {
                                pixelColor = new Color(originalImage.getRGB(x - 1 + j, y - 1 + i));

                                if (pixelColor.getRed() == pixelValueSearched) {
                                    image.setRGB(x, y, pixelColor.getRGB());
                                    foundElement = true;
                                    break;
                                }
                            }
                        }
                        if (foundElement == true)
                            break;
                    }
                    if (operationType == 'd' && foundElement == false) {   //dla dylatacji gdy nie znaleziono sasiada to 0
                        pixelColor = new Color(255, 255, 255);
                        image.setRGB(x, y, pixelColor.getRGB());
                    }

                }
            }
            return image;
        }

    }




    //----------------------------------------------------AUTOMAT--------------------------------------------
    public static Cell[][] automat1D(int sizeTab,int cellSize,int iterationAmount,int ruleType,int boundaryConditions){

        Cell endTab[][]=new Cell[iterationAmount][sizeTab];
        int centerElement=sizeTab/2;
        int leftElement=0;
        int rightElement=0;
        int middleElement;
        //USTAWIENIE TABLICY
        for(int iteration=0;iteration<iterationAmount;iteration++){
            for(int i=0;i<sizeTab;i++) {
                endTab[iteration][i]=new Cell(cellSize,0);
            }
        }
        endTab[0][centerElement].setValue(1);

        //konwersja na binarny
        String binaryValue=Integer.toBinaryString(ruleType);
        String binary8Value = String.format("%8s", binaryValue).replaceAll(" ", "0");  // 32-bit Integer

        for(int iteration=1;iteration<iterationAmount;iteration++){
            for(int i=0;i<sizeTab;i++){

                middleElement=endTab[iteration-1][i].getValue();

                if(i==0){
                    if(boundaryConditions==0)
                        leftElement=1;
                    else if(boundaryConditions==1)
                        leftElement=endTab[iteration-1][sizeTab-1].getValue();
                }
                else
                    leftElement=endTab[iteration-1][i-1].getValue();

                if(i==(sizeTab-1)){
                    if(boundaryConditions==0)
                        rightElement=1;
                    else if(boundaryConditions==1)
                        rightElement=endTab[iteration-1][0].getValue();
                }
                else
                    rightElement=endTab[iteration-1][i+1].getValue();


                if(leftElement==1&&middleElement==1&&rightElement==1){
                    endTab[iteration][i].setValue(Integer.parseInt(String.valueOf(binary8Value.charAt(0))));
                }
                if(leftElement==1&&middleElement==1&&rightElement==0){
                    endTab[iteration][i].setValue(Integer.parseInt(String.valueOf(binary8Value.charAt(1))));
                }
                if(leftElement==1&&middleElement==0&&rightElement==1){
                    endTab[iteration][i].setValue(Integer.parseInt(String.valueOf(binary8Value.charAt(2))));                }
                if(leftElement==1&&middleElement==0&&rightElement==0){
                    endTab[iteration][i].setValue(Integer.parseInt(String.valueOf(binary8Value.charAt(3))));
                }
                if(leftElement==0&&middleElement==1&&rightElement==1){
                    endTab[iteration][i].setValue(Integer.parseInt(String.valueOf(binary8Value.charAt(4))));
                }
                if(leftElement==0&&middleElement==1&&rightElement==0){
                    endTab[iteration][i].setValue(Integer.parseInt(String.valueOf(binary8Value.charAt(5))));
                }
                if(leftElement==0&&middleElement==0&&rightElement==1){
                    endTab[iteration][i].setValue(Integer.parseInt(String.valueOf(binary8Value.charAt(6))));                }
                if(leftElement==0&&middleElement==0&&rightElement==0){
                    endTab[iteration][i].setValue(Integer.parseInt(String.valueOf(binary8Value.charAt(7))));
                }
            }

        }
        return endTab;
    }

}
