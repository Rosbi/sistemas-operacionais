import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class main {
    public static void main(String args[]){
        BufferedImage image = null;
        Scanner sc = new Scanner(System.in);
//        String imageDir = sc.next();
        File imageFile = new File(sc.next());
        if(imageFile == null){
            System.out.println("Imagem nao encontrada");
            System.exit(-1);
        }

        try {
            image = ImageIO.read(imageFile);
        }catch (Exception e){
        }

        for(int y=0; y< image.getHeight(); y++){
            for(int x=0; x<image.getWidth(); x++){
                int color = image.getRGB(x, y);
                int red   = (color & 0xff0000) >> 16;
                int green = (color & 0x00ff00) >> 8;
                int blue  = (color & 0x0000ff);

                int grayscale = (red+green+blue)/3;

                color &= 0xff000000;
                color |= grayscale << 16;
                color |= grayscale << 8;
                color |= grayscale;
                image.setRGB(x, y, color);
            }
        }

        File grayImage = new File("new_image.png");
        try {
            ImageIO.write(image, "png", grayImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
