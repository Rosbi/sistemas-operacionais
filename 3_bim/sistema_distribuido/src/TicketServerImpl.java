// TicketServerImpl.java

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class TicketServerImpl extends UnicastRemoteObject
        implements TicketServer {
    int nextTicket = 0;

    TicketServerImpl() throws RemoteException {
    }

    /* REMOVER */
    public int getNextTicket(String name) throws RemoteException {
        System.out.println("Issue a new ticket for " + name);
        return nextTicket++;
    }
    /* fim de REMOVER*/

    public String listFromDirectory(String path, String name) throws RemoteException{
        File f = new File(path);
        if(f == null){
            System.out.println("Diretorio nao encontrado.");
            return null;
        }

        String[] files = f.list();
        String str = new String("");
        for(String file : files){
            Path filePath = new File(file).toPath();
            if(Files.isDirectory(filePath)){
                str = str + filePath + " (dir)\n";
            }else{
                str = str + filePath + "\n";
            }
        }

        System.out.println("Listed files to '" + name + "'");
        return str;
    }

    public static void main(String[] args) {
        // install RMI security manager
        System.setSecurityManager(new RMISecurityManager());
        // arg. 0 = rmi url
        if (args.length != 1) {
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
        } catch (Exception e) {
            System.out.println("Caught exception while registering: " + e);
            System.exit(-1);
        }
    }
}
