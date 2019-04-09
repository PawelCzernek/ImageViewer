package application;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by paltho on 08/03/2019.
 */
public class DotsRemover {

    private static BufferedImage image = null;

    public static void main(String[] args) {

        //odczytanie pliku
        String filepath = "D:\\80_Obrazy\\Shannon_Jungle_Tales\\shannon_indexed_reduced.png";
        try {
            image = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage resultImage = image;

        resultImage = removeColors(resultImage);

        String outputpath = "D:\\80_Obrazy\\Shannon_Jungle_Tales\\shannon_indexed_reduced_2.png";
        try {
            ImageIO.write(resultImage, "png", new File(outputpath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static BufferedImage deepCopy(BufferedImage buffer) {
        ColorModel cm = buffer.getColorModel();
        boolean isAlphaPremultipied = cm.isAlphaPremultiplied();
        WritableRaster raster = buffer.copyData(null);

        return new BufferedImage(cm, raster, isAlphaPremultipied, null);
    }

    public static BufferedImage removeColors(BufferedImage input) {
        BufferedImage imageWorkedOn = deepCopy(input);
        List<Integer> row77 = new ArrayList<>();
        row77.add(-1);
        row77.add(0);
        row77.add(1);
        List<Integer> row66 = new ArrayList<>();
        row66.add(-3);
        row66.add(-2);
        row66.add(2);
        row66.add(3);
        List<Integer> row55 = new ArrayList<>();
        row55.add(-5);
        row55.add(-4);
        row55.add(4);
        row55.add(5);
        List<Integer> row44 = new ArrayList<>();
        row44.add(-5);
        row44.add(5);
        List<Integer> row3223 = new ArrayList<>();
        row3223.add(-6);
        row3223.add(6);
        List<Integer> row101 = new ArrayList<>();
        row101.add(-7);
        row101.add(7);

        for (int h = 0; h < imageWorkedOn.getHeight(); h++) {
            for (int w = 0; w < imageWorkedOn.getWidth(); w++) {
                Color[] rimColors = new Color[40];
                Color[] areaColors = new Color[121];
                int indexRim = 0;
                int indexArea = 0;
                for (int h1 = -7; h1 < 8; h1++) {
                    if (h1 == -7 || h1 == 7) {
                        indexRim = getIndexRim(input, imageWorkedOn, row77, h, w, rimColors, indexRim, h1);
                    } else if (h1 == -6 || h1 == 6) {
                        indexRim = getIndexRim(input, imageWorkedOn, row66, h, w, rimColors, indexRim, h1);
                        for (int w1 = -1; w1 < 2; w1++) {
                            if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                                    && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                                areaColors[indexArea] = new Color(input.getRGB(w + w1, h + h1));
                            } else {
                                areaColors[indexArea] = null;
                            }
                            indexArea++;
                        }
                    } else if (h1 == -5 || h1 == 5) {
                        indexRim = getIndexRim(input, imageWorkedOn, row55, h, w, rimColors, indexRim, h1);
                        for (int w1 = -3; w1 < 4; w1++) {
                            if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                                    && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                                areaColors[indexArea] = new Color(input.getRGB(w + w1, h + h1));
                            } else {
                                areaColors[indexArea] = null;
                            }
                            indexArea++;
                        }
                    } else if (h1 == -4 || h1 == 4) {
                        indexRim = getIndexRim(input, imageWorkedOn, row44, h, w, rimColors, indexRim, h1);
                        for (int w1 = -4; w1 < 5; w1++) {
                            if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                                    && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                                areaColors[indexArea] = new Color(input.getRGB(w + w1, h + h1));
                            } else {
                                areaColors[indexArea] = null;
                            }
                            indexArea++;
                        }
                    } else if (h1 == -3 || h1 == 3 || h1 == -2 || h1 == 2) {
                        indexRim = getIndexRim(input, imageWorkedOn, row3223, h, w, rimColors, indexRim, h1);
                        for (int w1 = -5; w1 < 6; w1++) {
                            if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                                    && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                                areaColors[indexArea] = new Color(input.getRGB(w + w1, h + h1));
                            } else {
                                areaColors[indexArea] = null;
                            }
                            indexArea++;
                        }
                    } else {
                        indexRim = getIndexRim(input, imageWorkedOn, row101, h, w, rimColors, indexRim, h1);
                        for (int w1 = -6; w1 < 7; w1++) {
                            if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                                    && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                                areaColors[indexArea] = new Color(input.getRGB(w + w1, h + h1));
                            } else {
                                areaColors[indexArea] = null;
                            }
                            indexArea++;
                        }
                    }
                }
                if (!Arrays.asList(rimColors).contains(areaColors[60])) {
                    Color nearestColor = findNearestColorWithoutColor(areaColors, areaColors[60]);
                    Color localColor;
                    for (int h1 = -6; h1 < 7; h1++) {
                        if (h1 == -6 || h1 == 6) {
                            for (int w1 = -1; w1 < 2; w1++) {
                                if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                                        && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                                    localColor =  new Color(imageWorkedOn.getRGB(w + w1, h + h1));
                                    if (localColor.equals(areaColors[60])) {
                                        imageWorkedOn.setRGB(w + w1, h + h1, new Color(nearestColor.getRed(), nearestColor.getGreen(), nearestColor.getBlue()).getRGB());
                                    }
                                }
                            }
                        } else if (h1 == -5 || h1 == 5) {
                            for (int w1 = -3; w1 < 4; w1++) {
                                if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                                        && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                                    localColor =  new Color(imageWorkedOn.getRGB(w + w1, h + h1));
                                    if (localColor.equals(areaColors[60])) {
                                        imageWorkedOn.setRGB(w + w1, h + h1, new Color(nearestColor.getRed(), nearestColor.getGreen(), nearestColor.getBlue()).getRGB());
                                    }
                                }
                            }
                        } else if (h1 == -4 || h1 == 4) {
                            for (int w1 = -4; w1 < 5; w1++) {
                                if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                                        && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                                    localColor =  new Color(imageWorkedOn.getRGB(w + w1, h + h1));
                                    if (localColor.equals(areaColors[60])) {
                                        imageWorkedOn.setRGB(w + w1, h + h1, new Color(nearestColor.getRed(), nearestColor.getGreen(), nearestColor.getBlue()).getRGB());
                                    }
                                }
                            }
                        } else if (h1 == -3 || h1 == 3 || h1 == -2 || h1 == 2) {
                            for (int w1 = -5; w1 < 6; w1++) {
                                if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                                        && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                                    localColor =  new Color(imageWorkedOn.getRGB(w + w1, h + h1));
                                    if (localColor.equals(areaColors[60])) {
                                        imageWorkedOn.setRGB(w + w1, h + h1, new Color(nearestColor.getRed(), nearestColor.getGreen(), nearestColor.getBlue()).getRGB());
                                    }
                                }
                            }
                        } else {
                            for (int w1 = -6; w1 < 7; w1++) {
                                if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                                        && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                                    localColor =  new Color(imageWorkedOn.getRGB(w + w1, h + h1));
                                    if (localColor.equals(areaColors[60])) {
                                        imageWorkedOn.setRGB(w + w1, h + h1, new Color(nearestColor.getRed(), nearestColor.getGreen(), nearestColor.getBlue()).getRGB());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("linia : " + h);
        }
        return imageWorkedOn;
    }

    private static int getIndexRim(BufferedImage input, BufferedImage imageWorkedOn, List<Integer> row3223, int h, int w, Color[] rimColors, int indexRim, int h1) {
        for (Integer w1 : row3223) {
            if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                    && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                rimColors[indexRim] = new Color(input.getRGB(w + w1, h + h1));
            } else {
                rimColors[indexRim] = null;
            }
            indexRim++;
        }
        return indexRim;
    }

    private static Color findNearestColorWithoutColor(Color[] colors, Color excludedColor) {
        int occurance;
        int maxOccurance = 0;
        Color result = null;

        for (Color color : colors) {
            if (color != null && !color.equals(excludedColor)) {
                occurance = Collections.frequency(Arrays.asList(colors), color);
                if (maxOccurance < occurance) {
                    maxOccurance = occurance;
                    result = color;
                }
            }
        }
        return result;
    }
}
