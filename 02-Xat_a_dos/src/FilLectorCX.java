import java.io.EOFException;
import java.io.ObjectInputStream;
public class FilLectorCX extends Thread {
    private ObjectInputStream ois;
    public FilLectorCX(ObjectInputStream ois) {
        this.ois = ois;
    }
    @Override
    public void run() {
        try {
            String missatge;
            while (true) {
                missatge = (String) ois.readObject();
                // si el servidor decide enviar “sortir”:
                if (missatge.equals("sortir")) break;
                System.out.println("Rebut: " + missatge);
            }
        } catch (EOFException eof) {
            // socket cerrado desde el otro extremo → fin de lectura
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Fil de lectura finalitzat.");
        }
    }
}