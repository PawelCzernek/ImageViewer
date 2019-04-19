package application;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by paltho on 08/03/2019.
 */
public class ColorRemover {

    private static BufferedImage image = null;

    public static void main(String[] args) {

        //odczytanie pliku
        String filepath = "/Users/paltho/Pictures/Shannon/shannon_indexed.png";
        try {
            image = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage resultImage = image;

        for (int i = 1; i < 2; i++) {
            System.out.println("Przebieg: " + i);
            resultImage = removeColors(resultImage, 25);
        }

        String outputpath = "/Users/paltho/Pictures/Shannon/shannon_indexed_reduced.png";
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

    public static BufferedImage removeColors(BufferedImage input, int pixelsLimit) {
        BufferedImage imageWorkedOn = deepCopy(input);

        for (int h = 0; h < imageWorkedOn.getHeight(); h++) {
            for (int w = 0; w <imageWorkedOn.getWidth(); w++) {
                int occurance = 0;
                Color[] colors = new Color[121];
                int index = 0;
                for (int h1 = -5; h1 < 6; h1++) {
                    for (int w1 = -5; w1 < 6; w1++) {
                        if (h + h1 > -1 && h + h1 < imageWorkedOn.getHeight()
                                && w + w1 > -1 && w + w1 < imageWorkedOn.getWidth()) {
                            colors[index] = new Color(input.getRGB(w + w1, h + h1));
                        } else {
                            colors[index] = null;
                        }
                        index++;
                    }
                }
                occurance = Collections.frequency(Arrays.asList(colors), colors[60]);
                if (occurance < pixelsLimit) {
                    Color nearestColor = findNearestColor(colors);
                    imageWorkedOn.setRGB(w, h, new Color(nearestColor.getRed(), nearestColor.getGreen(), nearestColor.getBlue()).getRGB());
                }
            }
            System.out.println("line : " + h);
        }
        return imageWorkedOn;
    }

    private static Color findNearestColor(Color[] colors) {
        int occurance;
        int maxOccurance = 0;
        Color result = null;

        for (Color color : colors) {
            if (color != null) {
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
