import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static final String MSG_SORTIR = "sortir";
    private Hashtable<String, GestorClients> clients = new Hashtable<>();
    private boolean sortir = false;
    private ServerSocket serverSocket;
    public static int getPort() {
        return PORT;
    }

    public static String getHost() {
        return HOST;
    }
    public void servidorAEscoltar() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
    }

    public void pararServidor() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public void finalitzarXat() {
        enviarmissatgeGrup(MSG_SORTIR);
        clients.clear();
        System.exit(0);
    }

    public void afegirClient(GestorClients client) {
        String nom = client.getNom();
        clients.put(nom, client);
        System.out.println(nom + " connectat.");
        enviarmissatgeGrup("Entra: " + nom);
    }

    public void eliminarClient(String nom) {
        if (clients.containsKey(nom)) {
            clients.remove(nom);
        }
    }

    public void enviarmissatgeGrup(String missatge) {
        System.out.println("DEBUG: multicast " + missatge);
        for (GestorClients gc : clients.values()) {
            gc.enviarmissatge(Missatge.CODI_MSG_GRP + "#" + missatge);
        }
    }

    public void enviarmissatgePersonal(String nomDestinatari, String nomRemitent, String missatge) {
        GestorClients client = clients.get(nomDestinatari);
        if (client != null) {
            client.enviarmissatge(Missatge.CODI_MSG_PERSONAL + "#" + nomRemitent + "#" + missatge);
        }
    }

    public static void main(String[] args) throws IOException {
        ServidorXat servidor = new ServidorXat();
        servidor.servidorAEscoltar();
        while (!servidor.sortir) {
            Socket clientSocket = servidor.serverSocket.accept();
            System.out.println("Client connectat: " + clientSocket.getInetAddress());
            GestorClients gc = new GestorClients(clientSocket, servidor);
            gc.start();
        }
        servidor.pararServidor();
    }
}