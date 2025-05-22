import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientXat extends Thread {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private boolean sortir = false;
    private String nom;

    public void connecta() throws IOException {
        socket = new Socket(ServidorXat.getHost(), ServidorXat.getPort());
        oos = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Client connectat a localhost:9999");
        System.out.println("Flux d'entrada i sortida creat.");
    }

    public void enviarMissatge(String missatge) throws IOException {
        oos.writeObject(missatge);
        oos.flush();
    }

    public void tancarClient() throws IOException {
        if (ois != null) ois.close();
        if (oos != null) oos.close();
        if (socket != null) socket.close();
    }

    public void run() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            while (!sortir) {
                String missatgeCru = (String) ois.readObject();
                String codi = Missatge.getCodiMissatge(missatgeCru);
                if (codi == null) continue;

                String[] parts = Missatge.getPartsMissatge(missatgeCru);
                if (parts == null) continue;

                if (codi.equals(Missatge.CODI_SORTIR_TOTS)) {
                    sortir = true;
                } else if (codi.equals(Missatge.CODI_MSG_PERSONAL) && parts.length > 2) {
                    String remitent = parts[1];
                    String missatge = parts[2];
                    System.out.println("Missatge personal per (" + nom + ") de (" + remitent + "): " + missatge);
                } else if (codi.equals(Missatge.CODI_MSG_GRP) && parts.length > 1) {
                    System.out.println("Enviant missatge:: " + parts[1]);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        } finally {
            try {
                tancarClient();
            } catch (IOException e) {
            }
        }
    }

    public void ajuda() {
        System.out.println("---------------------");
        System.out.println("Comandes disponibles:");
        System.out.println("1.- Conectar al servidor (primer pass obligatori)");
        System.out.println("2.- Enviar missatge personal");
        System.out.println("3.- Enviar missatge al grup");
        System.out.println("4.- (o linia en blanc) -> Sortir del client");
        System.out.println("5.- Finalitizar tothom");
        System.out.println("---------------------");
    }

    public String getLinea(Scanner scanner, String missatge, boolean obligatori) {
        while (true) {
            System.out.print(missatge);
            String linea = scanner.nextLine().trim();
            if (!obligatori || !linea.isEmpty()) {
                return linea;
            }
            System.out.println("Aquest camp és obligatori.");
        }
    }

    public static void main(String[] args) throws IOException {
        ClientXat client = new ClientXat();
        client.connecta();
        client.start();
        System.out.println("DEBUG: Iniciant rebuda de missatges...");
        client.ajuda();

        Scanner scanner = new Scanner(System.in);
        while (!client.sortir) {
            String opcio = client.getLinea(scanner, "", false);
            if (opcio.isEmpty() || opcio.equals("4")) {
                client.enviarMissatge(Missatge.getMissatgeSortirClient(""));
                client.sortir = true;
            } else {
                switch (opcio) {
                    case "1":
                        client.nom = client.getLinea(scanner, "Introdueix el teu nom: ", true);
                        client.enviarMissatge(Missatge.getMissatgeConectar(client.nom));
                        break;
                    case "2":
                        String destinatari = client.getLinea(scanner, "Introdueix el nom del destinatari: ", true);
                        String missatgePersonal = client.getLinea(scanner, "Introdueix el missatge: ", true);
                        client.enviarMissatge(Missatge.getMissatgePersonal(destinatari, missatgePersonal));
                        break;
                    case "3":
                        String missatgeGrup = client.getLinea(scanner, "Introdueix el missatge per al grup: ", true);
                        client.enviarMissatge(Missatge.getMissatgeGrup(missatgeGrup));
                        break;
                    case "5":
                        client.enviarMissatge(Missatge.getMissatgeSortirTots(""));
                        client.sortir = true;
                        break;
                    default:
                        System.out.println("Opció no vàlida");
                        break;
                }
            }
        }
        client.tancarClient();
    }
}