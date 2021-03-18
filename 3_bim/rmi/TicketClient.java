// TicketClient.java

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.rmi.*;
import java.util.Scanner;

public class TicketClient implements Serializable {

  public static byte[] toByteArray(BufferedImage bi, String format)
          throws IOException {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(bi, format, baos);
    byte[] bytes = baos.toByteArray();
    return bytes;

  }

  public static BufferedImage toBufferedImage(byte[] bytes)
          throws IOException {

    InputStream is = new ByteArrayInputStream(bytes);
    BufferedImage bi = ImageIO.read(is);
    return bi;

  }

  public static void main(String [] args) {

      String fileDir;
      Scanner sc = new Scanner(System.in);

      // install RMI security manager
      System.setSecurityManager(new RMISecurityManager());

      // arg. 0 = name of server
      if (args.length!=1) {
        System.err.println("Usage: TicketClient <server-rmi-url>");
        System.exit(-1);
      }

      // look up in nameserver
      String fullname = args[0];
      TicketServer server = null;
      try {
        server = (TicketServer)Naming.lookup(fullname);
      } catch (Exception e) {
        System.out.println("Caught an exception doing name lookup on "+fullname
               +": "+e);
        System.exit(-1);
      }

      System.out.println("Insira a o diretorio da imagem, em PNG, que deseja transformar: ");

      File imageFile = new File(sc.next());
      if(imageFile == null){
        System.out.println("Imagem nao encontrada");
        System.exit(-1);
      }

      BufferedImage image = null;

      try {
        image = ImageIO.read(imageFile);
      }catch (Exception e){
        System.out.println("Erro ao ler a imagem");
        System.exit(-1);
      }

      try {
          byte[] byteImage = toByteArray(image, "png");
          byteImage = server.getBWImage(byteImage);
          image = toBufferedImage(byteImage);
      } catch (Exception e) {
          System.out.println("Não foi possível converter a imagem: "+e);
          System.exit(-1);
      }

      File grayImage = new File(imageFile.getName().replace(".png", "") + "-bw.png");

      try {
        ImageIO.write(image, "png", grayImage);
        System.out.println(">> Imagem convertida e salva com sucesso!\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
  }
}

