package application;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by paltho on 19/01/2019.
 */
public class ColorSplitter {

    private static BufferedImage image = null;
    private static BufferedImage currentLayer;

    public static void main(String[] args) {

        int width;
        int height;

        //odczytanie pliku
        String filepath = "/Users/paltho/Pictures/Shannon/shannon_colors.png";
        try {
            image = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int colorCounter = 0;
        int layerCounter = 0;
        Color tempColor = null;

        width = image.getWidth();
        height = image.getHeight();

        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                Color localColor = new Color(image.getRGB(j, i));
                if(!localColor.equals(Color.WHITE)){
                    colorCounter++;
                    tempColor = localColor;
                    break;
                }

            }
        }
        if(layerCounter == 0) {
            ++layerCounter;
            currentLayer = new BufferedImage(width, height, image.getType());
            for(int h=1; h<height -1; ++h) {
                for(int w=1; w<width -1; ++w) {
                    Color localColor = new Color(image.getRGB(w, h));
                    if(localColor.equals(tempColor)){
                        currentLayer.setRGB(w, h, tempColor.getRGB());
                        image.setRGB(w, h, Color.WHITE.getRGB());
                    } else {
                        currentLayer.setRGB(w, h, Color.WHITE.getRGB());
                    }

                }
            }
            saveImage(currentLayer, layerCounter);
            tempColor = null;
        } else {


        }





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
