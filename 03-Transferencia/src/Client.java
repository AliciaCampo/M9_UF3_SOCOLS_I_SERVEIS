import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client {
    private static final String DIR_ARRIBADA = "/tmp";
    private ObjectOutputStream OOS;
    private ObjectInputStream ois;
    private Socket socket;
    public void connectar() throws IOException {
        socket = new Socket(Servidor.HOST, Servidor.PORT);
        OOS = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connectant a -> " + Servidor.HOST + ":" + Servidor.PORT);
        System.out.println("Connexio acceptada: " + socket.getInetAddress());
    }
    public void rebreFitxers() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nom del fitxer a rebre ('sortir' per sortir): ");
        String nomFitxer = scanner.nextLine();
        if (nomFitxer.equals("sortir")) {
            System.out.println("Sortint...");
            return;
        }
        OOS.writeObject(nomFitxer);
        byte[] contingut = (byte[]) ois.readObject();
        System.out.print("Nom del fitxer a guardar: ");
        String nomFitxerGuardar = scanner.nextLine();
        FileOutputStream fos = new FileOutputStream(nomFitxerGuardar);
        fos.write(contingut);
        fos.close();
        System.out.println("Fitxer rebut i guardat com: " + nomFitxerGuardar);
    }
    public void tancarConnexio() throws IOException {
        if (socket != null) {
            System.out.println("Connexio tancada.");
            socket.close();
        }
    }
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.connectar();
            client.rebreFitxers();
            client.tancarConnexio();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}