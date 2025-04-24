import java.io.ObjectInputStream;

public class FilServidorXat extends Thread {
    private ObjectInputStream ois;

    public FilServidorXat(ObjectInputStream ois) {
        this.ois = ois;
    }

    @Override
    public void run() {
        try {
            String missatge;
            while (!(missatge = (String) ois.readObject()).equals(ServidorXat.getMsgSortir())) {
                System.out.println("Rebut: " + missatge);
            }
            System.out.println("Fil de xat finalitzat.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
