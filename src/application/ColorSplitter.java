package application;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by paltho on 19/01/2019.
 */
public class ColorSplitter {

    private static int radius = 10;
    private static final String FILE_PATH = "D:\\80_Obrazy\\Shannon_Jungle_Tales\\indexed_01.png";
    private static final String PODKLAD_PATH = "D:\\80_Obrazy\\Shannon_Jungle_Tales\\podklad.png";
    private static final String DESTINATION_PATH = "D:\\80_Obrazy\\Shannon_Jungle_Tales\\wynik\\";
    private static final String FILE_PREFIX = "indexed_01_";
    private static final String FILE_EXT = ".png";

    public static void main(String[] args) {

        BufferedImage image = null;
        BufferedImage podkladCzysty = null;
        BufferedImage currentLayer = null;
        List<Color> uniqueColorList = new ArrayList<>();
        int layerCouner = 1;

        int width;
        int height;

        //odczytanie pliku
        String filepath = FILE_PATH;
        try {
            image = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        filepath = PODKLAD_PATH;
        try {
            podkladCzysty = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        width = image.getWidth();
        height = image.getHeight();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color localColor = new Color(image.getRGB(j, i));
                if (!localColor.equals(Color.WHITE) && !uniqueColorList.contains(localColor)) {
                    uniqueColorList.add(localColor);
                }
            }
        }
        Map<Color, Long> colorMap = new HashMap<>();

        for (Color aColor : uniqueColorList) {
            long occurance = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color localColor = new Color(image.getRGB(j, i));
                    if (localColor.equals(aColor)) {
                        ++occurance;
                    }
                }
            }
            colorMap.put(aColor, occurance);
            System.out.println(aColor.toString());
        }
        List<Map.Entry<Color, Long>> list = new ArrayList<>(colorMap.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<Color, Long> result = new LinkedHashMap<>();
        for (Map.Entry<Color, Long> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        List<Color> colorListSorted = new ArrayList<>();
        colorListSorted.addAll(result.keySet());
        Collections.reverse(colorListSorted);
        List<Color> colorsToRemove = new ArrayList<>();
        while (!colorListSorted.isEmpty()) {
            currentLayer = deepCopy(podkladCzysty);
            colorsToRemove.clear();

            for (Color aColor : colorListSorted) {
                if (canInsertColor(aColor, image, currentLayer, radius)) {
                    colorsToRemove.add(aColor);
                    for (int h = 0; h < height; h++) {
                        for (int w = 0; w < width; w++) {
                            Color localColor = new Color(image.getRGB(w, h));
                            if (aColor.equals(localColor)) {
                                currentLayer.setRGB(w, h, localColor.getRGB());
                            }
                        }
                    }
                }
            }
            saveImage(currentLayer, layerCouner);
            System.out.println("Created stencil " + layerCouner);
            layerCouner++;
            colorListSorted.removeAll(colorsToRemove);
        }
    }

    private static boolean canInsertColor(Color aColor, BufferedImage image, BufferedImage currentLayer, int radius) {
        for (int h = 0; h < image.getHeight(); h++) {
            for (int w = 0; w < image.getWidth(); w++) {
                Color localColor = new Color(image.getRGB(w, h));
                if (aColor.equals(localColor)) {
                    for (int h1 = h - radius; h1 < h + radius; h1++) {
                      for (int w1 = w - radius; w1 < w + radius; w1++) {
                          if (currentLayer.getRGB(roundW1(w1, currentLayer), roundH1(h1, currentLayer)) != Color.WHITE.getRGB()) {
                              return false;
                          }
                      }
                    }
                }
            }
        }
        return true;
    }

    private static int roundH1(int h1, BufferedImage image) {
        if (h1 <= 0) {
            return 0;
        } else if (h1 >= (image.getHeight() - 1)) {
            return image.getHeight() - 1;
        } else {
            return h1;
        }
    }

    private static int roundW1(int w1, BufferedImage image) {
        if (w1 <= 0) {
            return 0;
        } else if (w1 >= (image.getWidth() - 1)) {
            return image.getWidth() - 1;
        } else {
            return w1;
        }
    }

    private static void saveImage(BufferedImage currentLayer, int layerCouner) {
        //zapis pliku
        String outputPath = DESTINATION_PATH + FILE_PREFIX + layerCouner + FILE_EXT;
        try {
            ImageIO.write(currentLayer, "png", new File(outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage deepCopy(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
}
