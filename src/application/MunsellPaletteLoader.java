package application;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by paltho on 20/03/2019.
 */
public class MunsellPaletteLoader {

    public static void main(String[] args) {

        Map<Integer, String> colorMap = initColorMap();


        BufferedImage image = null;

        //odczytanie pliku
        String filepath = "/Users/paltho/Pictures/Munsell/szablony_do_javy/half_2/50.png";
        try {
            image = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder csvContent = new StringBuilder();

        for (int w = 86; w <= 1872; w = w + 94) {
            for (int h = 96; h <= 1036; h = h + 94) {
                Color col = new Color(image.getRGB(w, h));
                int red = col.getRed();
                int green = col.getGreen();
                int blue = col.getBlue();

                csvContent.append(colorMap.get(w) + ",");
            }
        }

    }

    private static Map<Integer,String> initColorMap() {
        Map<Integer, String> result = new LinkedHashMap<>();
        result.put(86, "5R");
        result.put(86 + 94, "10R");
        result.put(86 + 94*2, "5YR");
        result.put(86 + 94*3, "10YR");
        result.put(86 + 94*4, "5Y");
        result.put(86 + 94*5, "10Y");
        result.put(86 + 94*6, "5GY");
        result.put(86 + 94*7, "10GY");
        result.put(86 + 94*8, "5G");
        result.put(86 + 94*9, "10G");
        result.put(86 + 94*10, "5BG");
        result.put(86 + 94*11, "10BG");
        result.put(86 + 94*12, "5B");
        result.put(86 + 94*13, "10B");
        result.put(86 + 94*14, "5PB");
        result.put(86 + 94*15, "10PB");
        result.put(86 + 94*16, "5P");
        result.put(86 + 94*17, "10P");
        result.put(86 + 94*18, "5RP");
        result.put(86 + 94*19, "10RP");
        return result;
    }
}
