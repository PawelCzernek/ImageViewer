package application;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by paltho on 20/03/2019.
 */
public class MunsellPaletteLoader {

    public static void main(String[] args) {

        Map<Integer, String> colorMap = initColorMap();
        Map<Integer, Integer> saturationMap = initSaturationMap();
        Map<Integer, Double> valueMap = initValueMap();


        BufferedImage image = null;
        StringBuilder csvContent = new StringBuilder();

        //odczytanie pliku
        for (int i = 5; i < 100 ; i = i + 5) {
            String filepath = "/Users/paltho/Pictures/Munsell/szablony_do_javy/half_2_done/" + i + ".png";
            try {
                image = ImageIO.read(new File(filepath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int w = 86; w <= 1872; w = w + 94) {
                for (int h = 96; h <= 1036; h = h + 94) {
                    Color col = new Color(image.getRGB(w, h));
                    int red = col.getRed();
                    int green = col.getGreen();
                    int blue = col.getBlue();

                    if (red == 163 && green == 163 && blue == 163) {
                        continue;
                    }

                    csvContent.append(colorMap.get(w) + ",");
                    csvContent.append(valueMap.get(i) + ",");
                    csvContent.append(saturationMap.get(h) + "\r\n");
//                    csvContent.append(red + ",");
//                    csvContent.append(green + ",");
//                    csvContent.append(blue + "\r\n");
                }
            }
        }

        try {
            FileWriter writer = new FileWriter("/Users/paltho/Pictures/Munsell/real_sRGB/munsell_gimp_palette_done.csv");
            writer.write(csvContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Map<Integer,Double> initValueMap() {
        Map<Integer,Double> result = new LinkedHashMap<>();
        result.put(5, 0.5);
        result.put(10, 1.0);
        result.put(15, 1.5);
        result.put(20, 2.0);
        result.put(25, 2.5);
        result.put(30, 3.0);
        result.put(35, 3.5);
        result.put(40, 4.0);
        result.put(45, 4.5);
        result.put(50, 5.0);
        result.put(55, 5.5);
        result.put(60, 6.0);
        result.put(65, 6.5);
        result.put(70, 7.0);
        result.put(75, 7.5);
        result.put(80, 8.0);
        result.put(85, 8.5);
        result.put(90, 9.0);
        result.put(95, 9.5);
        return result;
    }

    private static Map<Integer,Integer> initSaturationMap() {
        Map<Integer, Integer> result = new LinkedHashMap<>();
        result.put(96, 22);
        result.put(96 + 94, 20);
        result.put(96 + 94 * 2, 18);
        result.put(96 + 94 * 3, 16);
        result.put(96 + 94 * 4, 14);
        result.put(96 + 94 * 5, 12);
        result.put(96 + 94 * 6, 10);
        result.put(96 + 94 * 7, 8);
        result.put(96 + 94 * 8, 6);
        result.put(96 + 94 * 9, 4);
        result.put(96 + 94 * 10, 2);
        return result;
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
