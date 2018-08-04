package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {

        BufferedImage image = null;
        BufferedImage imageCopy;
        BufferedImage limitedPaletteImage;

        Color[] munsellColors = initMunsellColors();

        //Tablica kolorów munsella


        //odczytanie pliku
        String filepath = "/Users/paltho/Pictures/Shannon/shannon_small.jpg";
        try {
            image = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageCopy = deepCopy(image);
        limitedPaletteImage = limitPallette(imageCopy, munsellColors);



        //zapis pliku
        String outputPathA = "/Users/paltho/Pictures/Shannon/shannon_reduced.png";
        try {
            ImageIO.write(limitedPaletteImage, "png", new File(outputPathA));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Color[] initMunsellColors() {
        List<Color> colorList = new ArrayList<Color>();

        // -define .csv file in app
        String fileNameDefined = "/Users/paltho/Documents/real_sRGB/data-Table.csv";
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


    private static BufferedImage limitPallette(BufferedImage image, Color[] munsellColors) {
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

    private static BufferedImage deepCopy(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

//    private static Node<Integer> initColorTable(Map<Integer, String> munsellmap) {
//        Node<Integer> root = new Node<>(null);
//
//        // -define .csv file in app
//        String fileNameDefined = "/Users/paltho/Documents/real_sRGB/data-Table.csv";
//        // -File class needed to turn stringName to actual file
//        File file = new File(fileNameDefined);
//
//        try{
//            // -read from filePooped with Scanner class
//            Scanner inputStream = new Scanner(file);
//            // hashNext() loops line-by-line
//            int licznik = 1;
//            while(inputStream.hasNextLine()) {
//                //read single line, put in string
//                String dataLine = inputStream.nextLine();
//
//                String hue;
//                String value;
//                String chroma;
//                int red;
//                int green;
//                int blue;
//
//                String[] line = dataLine.split(",");
//                hue = line[0];
//                value = line[1];
//                chroma = line[2];
//
//                red = Integer.valueOf(line[6]);
//                green = Integer.valueOf(line[7]);
//                blue = Integer.valueOf(line[8]);
//
//                //System.out.println("Notation: "+ hue + " " + value +"/" + chroma + "-> (" +  red + "," + green + "," + blue +")");
//                String colorName = hue + " " + value +"/" + chroma;
//                munsellmap.put(licznik, colorName);
//
//                Node aRedNode = null;
//                for(Node node : root.getChildren()){
//                    if (node.getData().equals(red)){
//                        aRedNode = node;
//                    }
//                }
//                if (aRedNode == null) {
//                    Node munsellNode = new Node(licznik);
//                    Node blueNode = new Node(blue);
//                    Node greenNode = new Node(green);
//                    Node redNode = new Node(red);
//                    blueNode.addChild(munsellNode);
//                    greenNode.addChild(blueNode);
//                    redNode.addChild(greenNode);
//                    root.addChild(redNode);
//                } else {
//                    Node aGreenNode = null;
//                    for(Object node1 : aRedNode.getChildren()) {
//                        if (((Node)node1).getData().equals(green)) {
//                            aGreenNode = (Node)node1;
//                        }
//                    }
//                    if (aGreenNode == null) {
//                        Node munsellNode = new Node(licznik);
//                        Node blueNode = new Node(blue);
//                        Node greenNode = new Node(green);
//                        blueNode.addChild(munsellNode);
//                        greenNode.addChild(blueNode);
//                        aRedNode.addChild(greenNode);
//                    } else {
//                        Node munsellNode = new Node(licznik);
//                        Node blueNode = new Node(blue);
//                        blueNode.addChild(munsellNode);
//                        aGreenNode.addChild(blueNode);
//                    }
//                }
//                licznik++;
//                System.out.println("kolor: " + licznik);
//            }
//            // after loop, close scanner
//            inputStream.close();
//
//
//        }catch (FileNotFoundException e){
//
//            e.printStackTrace();
//        }
//
//        return root;
//    }

}
