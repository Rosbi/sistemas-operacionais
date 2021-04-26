// TicketServerImpl.java

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class TicketServerImpl extends UnicastRemoteObject implements TicketServer {
    private static String dirPath = null;

    TicketServerImpl() throws RemoteException {
    }

    /* REMOVER */
    int nextTicket = 0;
    public int getNextTicket(String name) throws RemoteException {
        System.out.println("Issue a new ticket for " + name);
        return nextTicket++;
    }
    /* fim de REMOVER*/

    public String listFromDirectory(String name) throws RemoteException{
        // Checa se o diretorio existe
        File f = new File(dirPath);
        if(!f.exists()){
            System.out.println("Diretorio nao encontrado.");
            return "Diretorio nao encontrado";
        }

        String[] files = f.list();
        String str = new String("");
        for(String file : files){
            Path filePath = new File(file).toPath();
            if(Files.isDirectory(filePath)){
                str = str + "(dir) " + filePath + "\n";
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


        // Pega o caminho do diretorio compartilhado
        Scanner sc = new Scanner(System.in);
//        System.out.println(" *isso e obrigatorio para o funcionamento do programa*");
        System.out.print("Insira o caminho do diretorio compartilhado: ");
        dirPath = sc.next();

        File f = new File(dirPath);
        while(!f.exists()) {
            System.out.print("Insira um diretorio valido: ");
            dirPath = sc.next();
            f = new File(dirPath);
        }

        System.out.println("----------------------------------------\n");
    }
}
