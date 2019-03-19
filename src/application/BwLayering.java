package application;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by paltho on 08/03/2019.
 */
public class BwLayering {

    private static BufferedImage image = null;

    public static void main(String[] args) {

        //odczytanie pliku
        String filepath = "/Users/paltho/Pictures/Shannon/shannon_bw.png";
        try {
            image = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Gimp 8.2 luminosity formula
        // not used in gimp 8.10!
        // better load desaturated image
//         BufferedImage resultImage = desaturate(image);

        Color[] munsellGrays = initMunsellGrays();

        BufferedImage resultImage = limitPallette(image, munsellGrays);

        String outputpath = "/Users/paltho/Pictures/Shannon/shannon_bw_layers2.png";
        try {
            ImageIO.write(resultImage, "png", new File(outputpath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static BufferedImage desaturate(BufferedImage input) {
        BufferedImage imageWorkedOn = deepCopy(input);

        for (int h = 0; h < imageWorkedOn.getHeight(); h++) {
            for (int w = 0; w < imageWorkedOn.getWidth(); w++) {
            Color currenColor = new Color(input.getRGB(w, h));

                int red = currenColor.getRed();
                int green = currenColor.getGreen();
                int blue = currenColor.getBlue();

                int luminance = ((Long) Math.round((0.22 * red) + (0.72 * green) + (0.06 * blue))).intValue();

                imageWorkedOn.setRGB(w, h, new Color(luminance, luminance, luminance).getRGB());
            }
        }
        return imageWorkedOn;
    }

    public static BufferedImage deepCopy(BufferedImage buffer) {
        ColorModel cm = buffer.getColorModel();
        boolean isAlphaPremultipied = cm.isAlphaPremultiplied();
        WritableRaster raster = buffer.copyData(null);

        return new BufferedImage(cm, raster, isAlphaPremultipied, null);
    }

    private static BufferedImage limitPallette(BufferedImage source, Color[] munsellColors) {
        BufferedImage image = deepCopy(source);
        ColorFinder colorFinder = new ColorFinder(munsellColors);
        Color currentRgbColor;
        for(int h = 0; h < image.getHeight(); ++h) {
            //System.out.println("wykonanie: " + (h/image.getHeight())*100 + "%");
            for (int w = 0; w < image.getWidth(); ++w) {
                System.out.println("linia: " + h + ", kolumna: " + w);
                currentRgbColor = new Color(image.getRGB(w, h));
                int index = colorFinder.GetNearestColorIndex(currentRgbColor);
                image.setRGB(w, h, munsellColors[index].getRGB());
            }
        }

        return image;
    }

    private static Color[] initMunsellGrays() {
        java.util.List<Color> colorList = new ArrayList<>();

        // -define .csv file in app
        String fileNameDefined = "/Users/paltho/Pictures/Munsell/real_sRGB/data-Table-greys.csv";
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

                red = Integer.valueOf(line[6]);
                green = Integer.valueOf(line[7]);
                blue = Integer.valueOf(line[8]);

                colorList.add(new Color(red, green, blue));
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        if(!colorList.isEmpty()){
            colorList.sort((o1,o2) -> (o2.getBlue()-o1.getBlue()));
            colorList.sort((o1,o2) -> (o2.getGreen()-o1.getGreen()));
            colorList.sort((o1,o2) -> (o2.getRed()-o1.getRed()));
        }
        Color[] result = colorList.toArray(new Color[colorList.size()]);
        return result;
    }
}
