import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class ClientXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static final String MSG_SORTIR = "sortir";
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public void enviarMissatge(String missatge) {
        try {
            oos.writeObject(missatge);
            System.out.println("Enviant missatge: " + missatge);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void tancarClient() {
        System.out.println("Tancant client...");
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
                }
            }
        }
        System.out.println("Client tancat.");
    }
    public void connecta() {
        try {
            clientSocket = new Socket(HOST, PORT);
            System.out.println("Client connectat a " + HOST + ":" + PORT);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("Flux d'entrada i sortida creat.");
        } catch (IOException e) {
            System.out.println("Error al connectar al servidor");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        client.connecta();
        FilLectorCX lector = new FilLectorCX(client.ois);
        lector.start();
        try (Scanner scanner = new Scanner(System.in)) {
            String missatge = "";
            while (!missatge.equalsIgnoreCase(MSG_SORTIR)) {
                System.out.print("Missatge ('sortir' per tancar): ");
                missatge = scanner.nextLine();
                client.enviarMissatge(missatge);
            }
        }
        client.tancarClient();
    }
        
}