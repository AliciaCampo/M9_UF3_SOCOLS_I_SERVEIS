import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int PORT = 8888;
    private static final String HOST = "localhost";
    private Socket socket;
    private PrintWriter out;

    public void connecta() throws IOException {
        socket = new Socket(HOST, PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connectat a servidor en " + HOST + ":" + PORT);
    }

    public void envia(String missatge) {
        out.println(missatge);
        System.out.println("Enviat al servidor: " + missatge);
    }

    public void tanca() throws IOException {
        out.close();
        socket.close();
        System.out.println("Client tancat");
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.connecta();
            client.envia("Prova d'enviament 1");
            client.envia("Prova d'enviament 2");
            client.envia("Adéu!");
            
            System.out.println("Prem Enter per tancar el client...");
            new Scanner(System.in).nextLine();
            
            client.tanca();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}