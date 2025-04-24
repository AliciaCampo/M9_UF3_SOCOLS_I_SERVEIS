import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class ServidorXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static final String MSG_SORTIR = "sortir";
    public static String getMsgSortir() {
        return MSG_SORTIR;
    }

    private ServerSocket serverSocket;
    
    public void iniciarServidor() throws Exception {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciat en " + HOST + ":" + PORT);
    }

    public void pararServidor() throws Exception {
        serverSocket.close();
        System.out.println("Servidor tancat");
    }

    public String getNom(ObjectInputStream ois) throws Exception {
        return (String) ois.readObject();
    }

    public static void main(String[] args) {
        try {
            ServidorXat servidor = new ServidorXat();
            servidor.iniciarServidor();

            Socket clientSocket = servidor.serverSocket.accept();
            System.out.println("Client connectat: " + clientSocket.getInetAddress());

            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());

            String nomClient = servidor.getNom(ois);
            System.out.println("Nom rebut: " + nomClient);

            FilServidorXat filServidor = new FilServidorXat(ois);
            filServidor.start();

            Scanner scanner = new Scanner(System.in);
            String missatge;
            while (!(missatge = scanner.nextLine()).equals(MSG_SORTIR)) {
                oos.writeObject("Servidor: " + missatge);
                oos.flush();
            }

            filServidor.join();
            clientSocket.close();
            servidor.pararServidor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
