import java.io.*;
import java.net.*;
public class Servidor {
    public static final int PORT = 9999;
    public static final String HOST = "localhost";
    public Socket connectar() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
        System.out.println("Esperant connexio...");
        Socket socket = serverSocket.accept();
        System.out.println("Connexio acceptada: " + socket.getInetAddress());
        serverSocket.close();
        return socket;
    }
    public void tancarConnexio(Socket socket) throws IOException {
        if (socket != null) {
            System.out.println("Tancant connexió amb el client: " + socket.getInetAddress());
            socket.close();
        }
    }
    public void enviarFitxers(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Esperant el nom del fitxer del client...");
        String nomFitxer = (String) in.readObject();
        System.out.println("Nom fitxer rebut: " + nomFitxer);
        Fitxer fitxer = new Fitxer(nomFitxer);
        byte[] contingut = fitxer.getContingut();
        if (contingut != null) {
            System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");
            out.writeObject(contingut);
            System.out.println("Fitxer enviat al client: " + nomFitxer);
        } else {
            System.out.println("Error llegint el fitxer del client: null");
        }
    }
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        try {
            Socket socket = servidor.connectar();
            servidor.enviarFitxers(socket);
            servidor.tancarConnexio(socket);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
