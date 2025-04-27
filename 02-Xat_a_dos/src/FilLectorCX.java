import java.io.ObjectInputStream;
public class FilLectorCX extends Thread {
    private ObjectInputStream ois;
    public FilLectorCX(ObjectInputStream ois) {
        this.ois = ois;
    }
    @Override
    public void run() {
        System.out.println("Missatge ('sortir' per tancar): "+"Fil de lectura iniciat");
        try {
            String missatge;
            while ((missatge = (String) ois.readObject()) != null) {
                System.out.println("Rebut: " + missatge);
            }
        } catch (Exception e) {
            System.out.println("El servidor ha tancat la connexió.");
        }
    }
}