import java.io.*;
import java.net.*;

public class GestorClients extends Thread {
    private Socket client;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ServidorXat servidorXat;
    private String nom;
    private boolean sortir = false;

    public GestorClients(Socket client, ServidorXat servidorXat) throws IOException {
        this.client = client;
        this.servidorXat = servidorXat;
        this.oos = new ObjectOutputStream(client.getOutputStream());
        this.ois = new ObjectInputStream(client.getInputStream());
    }

    public String getNom() {
        return nom;
    }

    public void run() {
        try {
            while (!sortir) {
                String missatge = (String) ois.readObject();
                processaMissatge(missatge);
            }
        } catch (IOException | ClassNotFoundException e) {
            // Gestionar errors de connexió
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                // Error al tancar el socket
            }
        }
    }

    public void enviarmissatge(String missatge) {
        try {
            oos.writeObject(missatge);
            oos.flush();
        } catch (IOException e) {
            // Error al enviar missatge
        }
    }

    private void processaMissatge(String missatge) {
        String codi = Missatge.getCodiMissatge(missatge);
        if (codi == null) return;

        String[] parts = Missatge.getPartsMissatge(missatge);
        if (parts == null) return;

        switch (codi) {
            case Missatge.CODI_CONECTAR:
                if (parts.length > 1) {
                    nom = parts[1];
                    servidorXat.afegirClient(this);
                }
                break;
            case Missatge.CODI_SORTIR_CLIENT:
                if (sortir) {
                    servidorXat.eliminarClient(nom);
                }
                sortir = true;
                break;
            case Missatge.CODI_SORTIR_TOTS:
                sortir = true;
                servidorXat.finalitzarXat();
                break;
            case Missatge.CODI_MSG_PERSONAL:
                if (parts.length > 2) {
                    String destinatari = parts[1];
                    String msg = parts[2];
                    servidorXat.enviarmissatgePersonal(destinatari, nom, msg);
                }
                break;
            case Missatge.CODI_MSG_GRP:
                if (parts.length > 1) {
                    servidorXat.enviarmissatgeGrup(nom + ": " + parts[1]);
                }
                break;
            default:
                // Cas d'error
                break;
        }
    }
}