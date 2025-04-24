import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class ClientXat {
    private static final String HOST = "localhost";
    private static final int PORT = 9999;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public void connecta() throws Exception {
        socket = new Socket(HOST, PORT);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Client connectat a " + HOST + ":" + PORT);
    }

    public void enviarMissatge(String missatge) throws Exception {
        oos.writeObject(missatge);
        oos.flush();
    }

    public void tancarClient() throws Exception {
        ois.close();
        oos.close();
        socket.close();
        System.out.println("Client tancat.");
    }

    public static void main(String[] args) {
        try {
            ClientXat client = new ClientXat();
            client.connecta();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Escriu el teu nom: ");
            String nom = scanner.nextLine();
            client.enviarMissatge(nom);

            FilLectorCX filLector = new FilLectorCX(client.ois);
            filLector.start();

            String missatge;
            while (!(missatge = scanner.nextLine()).equals("sortir")) {
                client.enviarMissatge(missatge);
            }

            client.enviarMissatge("sortir");
            filLector.join();
            client.tancarClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
