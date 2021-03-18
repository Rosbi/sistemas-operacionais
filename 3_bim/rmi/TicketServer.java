// TicketServer.java

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.*;

public interface TicketServer extends Remote 
{
  public byte[] getBWImage(byte[] image) throws IOException;
}

