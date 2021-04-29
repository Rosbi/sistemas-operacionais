// TicketClient.java

import java.io.File;
import java.io.IOException;
import java.rmi.*;
import java.util.Scanner;

public class TicketClient {
    /* REMOVER *
    private enum Operacoes{
        Listar(1), Criar(2), Renomear(3), Remover(4);

        private final int valor;
        Operacoes(int valorOpcao){
            valor = valorOpcao;
        }
        public int getValor(){
            return valor;
        }
    }
    /* fim de REMOVER */

    public static void printMainMenu(){
        System.out.println("------------------------------");
        System.out.println("Insira a opcao desejada (qualquer outro numero para sair)");
        System.out.println("1. Listar arquivos");
        System.out.println("2. Importar arquivo");
        System.out.println("3. Renomear arquivo");
        System.out.println("4. Remover arquivo");
        System.out.println("");
    }

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

//        System.out.println("Insira o caminho do diretorio compartilhado");
//        String rootDirectory = sc.next();

        Integer opcao;
        String name;
        String newName;
        try{
            do {
                printMainMenu();
                opcao = sc.nextInt();

                switch (opcao) {
                    case 1: //Listar
                        String s = server.listFromDirectory(userName);
                        System.out.println(s);
                        break;
                    case 2: //Criar
                        System.out.print("Escreva o caminho para o arquivo a ser adicionado: ");
                        name = sc.next();
                        File source = new File(name);
                        server.copyFileUsingStream(source, userName);
                        break;
                    case 3: //Renomear
                        System.out.print("Escreva o nome do arquivo a ser renomeado: ");
                        name = sc.next();
                        System.out.println("Escreva o novo nome do arquivo:");
                        newName = sc.next();
                        server.renameFile(name, newName, userName);
                        break;
                    case 4: //Remover
                        System.out.print("Escreva o nome do arquivo a ser deletado: ");
                        name = sc.next();
                        server.deleteFile(name, userName);
                        break;
                }
            } while (opcao >= 1 && opcao <= 4);
        }catch(RemoteException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

