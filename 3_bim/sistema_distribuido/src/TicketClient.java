// TicketClient.java

import java.rmi.*;
import java.util.Scanner;

public class TicketClient {
    public static void main(String[] args) {
        /* codigo necessario */
        // install RMI security manager
        System.setSecurityManager(new RMISecurityManager());
        // arg. 0 = name of server
        if (args.length != 1) {
            System.err.println("Usage: TicketClient <server-rmi-url>");
            System.exit(-1);
        }
        // look up in nameserver
        String fullname = args[0];
        TicketServer server = null;
        try {
            server = (TicketServer) Naming.lookup(fullname);
        } catch (Exception e) {
            System.out.println("Caught an exception doing name lookup on " + fullname
                    + ": " + e);
            System.exit(-1);
        }
        /* fim do codigo necessario */

        /* codigo exemplo (REMOVER) *
        // get ticket - remote method invocation!
        try {
            int ticket = server.getNextTicket("TicketClient");
            System.out.println("Got ticket " + ticket);
        } catch (Exception e) {
            System.out.println("Exception caught while getting ticket: " + e);
            System.exit(-1);
        }
        /* fim do codigo exemplo */

        Scanner sc = new Scanner(System.in);
        System.out.println("Insira o nome do usuario: ");
        String userName = sc.next();

        String path = sc.next();

        try {
            String s = server.listFromDirectory(path, userName);
            System.out.println(s);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

