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
            while (!(missatge = (String) ois.readObject()).equals("sortir")) {
                System.out.println("Rebut: " + missatge);
            }
            System.out.println("Fil de lectura finalitzat.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
