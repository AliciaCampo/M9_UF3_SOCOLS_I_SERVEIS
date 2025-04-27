import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class ServidorXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static final String MSG_SORTIR = "sortir";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    public void pararServidor() {
        try {
            if (ois != null) {
                ois.close();
            }
        } catch (IOException e) {
            System.out.println("Error al tancar l'ObjectInputStream");
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                System.out.println("Error al tancar l'ObjectOutputStream");
                e.printStackTrace();
            } finally {
                try {
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    System.out.println("Error al tancar el clientSocket");
                    e.printStackTrace();
                } finally {
                    try {
                        if (serverSocket != null && !serverSocket.isClosed()) {
                            serverSocket.close();
                        }
                    } catch (IOException e) {
                        System.out.println("Error al tancar el serverSocket");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public String getNom() {
        try {
            oos.writeObject("Escriu el teu nom: ");
            String nom = (String) ois.readObject();
            System.out.println("Nom rebut: " + nom);
            return nom;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en llegir dades");
            e.printStackTrace();
        }
        return null;
    }
    public void iniciarServidor() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
            clientSocket = serverSocket.accept();
            System.out.println("Client connectat: " + clientSocket.getInetAddress());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ServidorXat servidor = new ServidorXat();
        servidor.iniciarServidor();
        String nom = servidor.getNom();
        System.out.println("Fil de xat creat.");
        System.out.println("Fil de " + nom + " iniciat");
        FilServidorXat filServidor = new FilServidorXat(servidor.ois, nom);
        filServidor.start();
        try (BufferedReader term = new BufferedReader(new InputStreamReader(System.in))) {
            String missatge = "";
            while (!missatge.equalsIgnoreCase(MSG_SORTIR)) {
                System.out.print("Missatge ('sortir' per tancar): ");
                missatge = term.readLine();
                servidor.oos.writeObject(missatge);
            }
            filServidor.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            servidor.pararServidor();
            System.out.println("Servidor aturat.");
        }
    }
}