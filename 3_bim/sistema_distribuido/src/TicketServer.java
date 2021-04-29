// TicketServer.java

import java.io.File;
import java.io.IOException;
import java.rmi.*;

public interface TicketServer extends Remote {
    int getNextTicket(String name) throws RemoteException;
    String listFromDirectory(String name) throws RemoteException;
    void copyFileUsingStream(File source, String clientName) throws IOException, RemoteException;
    void renameFile(String name,String newName, String clientName) throws RemoteException;
    void deleteFile(String name, String clientName) throws RemoteException;
}

