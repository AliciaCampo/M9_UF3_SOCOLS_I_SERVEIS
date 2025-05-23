import java.io.ObjectInputStream;
public class FilServidorXat extends Thread {
    private ObjectInputStream ois;
    private String nombre;
    private static final String MSG_SORTIR = "sortir";
    public FilServidorXat(ObjectInputStream ois, String nombre) {
        this.ois = ois;
        this.nombre = nombre;
    }
    @Override
    public void run() {
        try {
            String missatge;
            while ((missatge = (String) ois.readObject()) != null) {
                System.out.println("Rebut: " + missatge);
                if (missatge.equalsIgnoreCase(MSG_SORTIR)) break;
            }
        } catch (Exception e) {
            System.out.println("Error en la comunicació");
        }
        System.out.println("Fil de xat finalitzat.");
    }
}