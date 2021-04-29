// TicketServerImpl.java

import java.io.*;
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

    public String listFromDirectory(String clientName) throws RemoteException{
        // Checa se o diretorio existe
        File f = new File(dirPath);
        if(!f.exists()){
            System.out.println("Diretorio nao encontrado.");
            return "Diretorio nao encontrado";
        }

        String[] files = f.list();
        String str = "";
        for(String file : files){
            Path filePath = new File(file).toPath();
            if(Files.isDirectory(filePath)){
                str = str + "(dir) " + filePath + "\n";
            }else{
                str = str + filePath + "\n";
            }
        }

        System.out.println("Listed files (" + clientName + ")");
        return str;
    }

    public void copyFileUsingStream(File source, String clientName) throws IOException, RemoteException {
        File dest = new File(dirPath + "/" + source.getName());

        InputStream is = null;
        OutputStream os = null;
         try {
             is = new FileInputStream(source);
             os = new FileOutputStream(dest);
             byte[] buffer = new byte[1024];
             int length;

             while ((length = is.read(buffer)) > 0) {
                 os.write(buffer, 0, length);
             }

             System.out.println("Copied '" + source.getName() + "' (" + clientName + ")");
         }catch (IOException e){
             System.out.println("Couldn't copy '" + source.getName() + "' (" + clientName + ")");
         }finally{
             if(is != null) {
                 is.close();
             }
             if(os != null) {
                 os.close();
             }
         }

    }

    public void deleteFile(String name, String clientName) throws RemoteException{
        File arq = new File(dirPath + "/" + name);
        if(arq.delete()){
            System.out.println("Arquivo deletado '" + name + "' (" + clientName + ")");
        }else{
            System.out.println("Erro ao deletar aquivo '" + name + "' (" + clientName + ")");
        }
    }

    public void renameFile(String name, String newName, String clientName) throws RemoteException{
        File arq = new File(dirPath + "/" + name);
        if(arq.renameTo(new File(dirPath+"/"+newName))){
            System.out.println("arquivo renomeado para '" + newName + "' (" + clientName + ")");
        }else{
            System.out.println("Erro ao renomear aquivo");
        }
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
