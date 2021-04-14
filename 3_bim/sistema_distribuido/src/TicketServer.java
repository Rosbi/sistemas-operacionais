// TicketServer.java

import java.rmi.*;

public interface TicketServer extends Remote {
    int getNextTicket(String name) throws RemoteException;
    String listFromDirectory(String path, String name) throws RemoteException;
}

