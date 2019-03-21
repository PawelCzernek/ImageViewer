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

    public static void main(String[] args) {

        BufferedImage image = null;
        BufferedImage podkladCzysty = null;
        BufferedImage currentLayer = null;
        List<Color> colorList = new ArrayList<>();
        List<Color> uniqueColorList = new ArrayList<>();
        int layerCouner = 1;

        int width;
        int height;

        //odczytanie pliku
        String filepath = "/Users/paltho/Pictures/Shannon/kolory_01.png";
        try {
            image = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        filepath = "/Users/paltho/Pictures/Shannon/podklad_01.png";
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
        colorListSorted.addAll(result.keySet()); // to do odwrócenia kollejności!
        System.out.println("");
    }

    private static void saveImage(BufferedImage currentLayer, int layerCouner) {
        //zapis pliku
        String outputPathA = "/Users/paltho/Pictures/Shannon/layers/shannon_reduced" + layerCouner + ".png";
        try {
            ImageIO.write(currentLayer, "png", new File(outputPathA));
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
