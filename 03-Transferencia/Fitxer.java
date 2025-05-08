import java.io.*;

public class Fitxer {
    private String nom;
    private byte[] contingut;
    public Fitxer(String nom) {
        this.nom = nom;
    }
    public byte[] getContingut() {
        File file = new File(nom);
        if (!file.exists()) {
            System.out.println("Nom del fitxer built o nul. Sortint...");
            return null;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            contingut = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return contingut;
    }
}
