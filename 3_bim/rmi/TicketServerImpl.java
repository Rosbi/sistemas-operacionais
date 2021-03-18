// TicketServerImpl.java
import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Scanner;


public class TicketServerImpl extends UnicastRemoteObject 
          implements TicketServer, Serializable
{
  
  TicketServerImpl() throws RemoteException { }

  public static BufferedImage toBufferedImage(byte[] bytes)
          throws IOException {

    InputStream is = new ByteArrayInputStream(bytes);
    BufferedImage bi = ImageIO.read(is);
    return bi;
  }

    public static byte[] toByteArray(BufferedImage bi, String format)
          throws IOException {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(bi, format, baos);
    byte[] bytes = baos.toByteArray();
    return bytes;

  }

  public byte[] getBWImage(byte[] byteImage) throws IOException {

    BufferedImage image = toBufferedImage(byteImage);

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

    // convert BufferedImage to byte[]
    byte[] bytes = toByteArray(image, "png");

    return bytes;

  }

  public static void main(String [] args) {
    // install RMI security manager
    System.setSecurityManager(new RMISecurityManager());
    // arg. 0 = rmi url
    if (args.length!=1) {
      System.err.println("Usage: TicketServerImpl <server-rmi-url>");
      System.exit(-1);
    }
    try {  
      // name with which we can find it = user name
      String name = args[0];
      //create new instance
      TicketServerImpl server = new TicketServerImpl();
      // register with nameserver
      Naming.rebind(name, server);
      System.out.println("Started TicketServer, registered as " + name);
    }
    catch(Exception e) {
      System.out.println("Caught exception while registering: " + e);
      System.exit(-1);
    }
  }
}
