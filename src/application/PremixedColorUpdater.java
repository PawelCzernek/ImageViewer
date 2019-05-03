package application;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by paltho on 19/01/2019.
 */
public class PremixedColorUpdater {


    public static void main(String[] args) {

        StringBuilder notations = new StringBuilder("");
        Map<Color, String> munsellNotations = initMunsellNotations();
        Map<String, Color> munsellNotationsRev = initMunsellNotationsRev(munsellNotations);
        Map<Color, String> munsellPremixed = initMunsellPremixed(munsellNotationsRev);

        //load old data
        String fileNameDefined = "/Users/paltho/Pictures/Shannon/layers/munsell_notations.csv";
        File file = new File(fileNameDefined);

        try{
            // -read from filePooped with Scanner class
            Scanner inputStream = new Scanner(file);
            // hashNext() loops line-by-line
            int licznik = 1;
            while(inputStream.hasNextLine()) {
                //read single line, put in string
                String dataLine = inputStream.nextLine();
                String colorNotation = null;
                if (dataLine.contains("*")) {
                    String[] line = dataLine.split(":");
                    String notationOld = line[1];
                    colorNotation = notationOld.substring(1, notationOld.length() - 2);
                    if (munsellPremixed.containsValue(colorNotation)) {
                        notations.append(dataLine.substring(0, dataLine.length() - 2) + "\r\n");
                    } else {
                        notations.append(dataLine + "\r\n");
                    }
                } else {
                    notations.append(dataLine + "\r\n");
                }
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        try {
            FileWriter writer = new FileWriter("/Users/paltho/Pictures/Shannon/layers/munsell_notations.csv");
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

                String[] line = dataLine.split(",");
                hue = line[0];
                value = line[1];
                chroma = line[2];
                String colorNotation = hue + value + "/" + chroma;

                result.put(munsellNotationsRev.get(colorNotation), hue + value + "/" + chroma);
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }
        return result;
    }

}
