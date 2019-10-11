package application;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by paltho on 19/01/2019.
 */
public class ColorSplitter {


    public static void main(String[] args) {

        StringBuilder notations = new StringBuilder("Munsell notations: \r\n");
        Map<Color, String> munsellNotations = initMunsellNotations();
        Map<String, Color> munsellNotationsRev = initMunsellNotationsRev(munsellNotations);
        Map<Color, String> munsellPremixed = initMunsellPremixed(munsellNotationsRev);

        for (int part = 1 ; part < 2 ; part ++) {
            notations.append("Part 0" + part + "\r\n");

            int radius = 15; // promien odstepu miedzy kolorami org 10
            int pixel_limit = 100; //pomija kolory o ilości pikseli poniżej
            final String FILE_PATH = "/Users/paltho/Pictures/Thayer/lilies_2/layers/col_0" + part + ".png";
            final String PODKLAD_PATH = "/Users/paltho/Pictures/Thayer/lilies_2/layers/podkl_0" + part + ".png";
            final String DESTINATION_FOLDER = "/Users/paltho/Pictures/Thayer/lilies_2/layers/0" + part;
            final String DESTINATION_PATH = DESTINATION_FOLDER + "/";
            final String FILE_PREFIX = "indexed_0" + part + "_layer";
            final String FILE_EXT = ".png";

            BufferedImage image = null;
            BufferedImage podkladCzysty = null;
            BufferedImage currentLayer;
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

            //Sortowanie według ilości pikseli
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
                if (entry.getValue() > pixel_limit) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
            List<Color> colorListSorted = new ArrayList<>();
            colorListSorted.addAll(result.keySet());
            Collections.reverse(colorListSorted);
            List<Color> colorsToRemove = new ArrayList<>();
            // koniec sortowania

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
                new File(DESTINATION_FOLDER).mkdirs();
                saveImage(currentLayer, layerCouner, DESTINATION_PATH, FILE_PREFIX, FILE_EXT);
                System.out.println("Created stencil " + layerCouner);

                colorListSorted.removeAll(colorsToRemove);
                //saving notations
                notations.append("layer : "+ layerCouner + "\r\n");
                int colorCounter = 1;
                for (Color aColor : colorsToRemove) {
                    boolean premixed = munsellPremixed.containsKey(aColor);
                    notations.append(colorCounter + ": " + munsellNotations.get(aColor) + (premixed ? "" : " *") + "\r\n");
                    colorCounter++;
                    if (munsellNotations.get(aColor) == null) {
                        System.out.println(aColor);
                    }
                }
                notations.append("\r\n");
                layerCouner++;
            }
        }

        try {
            FileWriter writer = new FileWriter("/Users/paltho/Pictures/Thayer/lilies_2/layers/munsell_notations.csv");
            writer.write(notations.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String,Color> initMunsellNotationsRev(Map<Color, String> munsellNotations) {
        Map<String,Color> result = new HashMap<>();
        munsellNotations.entrySet().stream().forEach(entry -> result.put(entry.getValue(), entry.getKey()));
        return result;
    }

    private static Map<Color,String> initMunsellNotations() {
        Map<Color,String> result = new HashMap<>();
        // -define .csv file in app
        String fileNameDefined = "/Users/paltho/Pictures/Munsell/real_sRGB/munsell_gimp_palette.csv";
        // -File class needed to turn stringName to actual file
        File file = new File(fileNameDefined);

        try{
            // -read from filePooped with Scanner class
            Scanner inputStream = new Scanner(file);
            // hashNext() loops line-by-line
            int licznik = 1;
            while(inputStream.hasNextLine()) {
                //read single line, put in string
                String dataLine = inputStream.nextLine();

                String hue;
                String value;
                String chroma;
                int red;
                int green;
                int blue;

                String[] line = dataLine.split(",");
                hue = line[0];
                value = line[1];
                chroma = line[2];

                red = Integer.valueOf(line[3]);
                green = Integer.valueOf(line[4]);
                blue = Integer.valueOf(line[5]);

                result.put(new Color(red, green, blue), hue + value + "/" + chroma);
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }
        return result;
    }

    private static Map<Color,String> initMunsellPremixed(Map<String, Color> munsellNotationsRev) {
        Map<Color,String> result = new HashMap<>();
        // -define .csv file in app
        String fileNameDefined = "/Users/paltho/Pictures/Munsell/real_sRGB/munsell_gimp_palette_done.csv";
        // -File class needed to turn stringName to actual file
        File file = new File(fileNameDefined);

        try{
            // -read from filePooped with Scanner class
            Scanner inputStream = new Scanner(file);
            // hashNext() loops line-by-line
            int licznik = 1;
            while(inputStream.hasNextLine()) {
                //read single line, put in string
                String dataLine = inputStream.nextLine();

                String hue;
                String value;
                String chroma;
//                int red;
//                int green;
//                int blue;

                String[] line = dataLine.split(",");
                hue = line[0];
                value = line[1];
                chroma = line[2];
                String colorNotation = hue + value + "/" + chroma;
//                red = Integer.valueOf(line[3]);
//                green = Integer.valueOf(line[4]);
//                blue = Integer.valueOf(line[5]);

                result.put(munsellNotationsRev.get(colorNotation), hue + value + "/" + chroma);
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }
        return result;
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

    private static void saveImage(BufferedImage currentLayer, int layerCouner, String DESTINATION_PATH, String FILE_PREFIX, String FILE_EXT) {
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
