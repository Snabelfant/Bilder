package dag.bilder;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Dag on 10.12.2016.
 */
public class SorterBilder {
    static SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static List<Bilde> bilder = new ArrayList<>();
    static List<Bildefil> besteBilder = new ArrayList<>();
    private static final File tilDir = new File("C:\\Bilder ryddet\\Unike");

    public static void main(String... args) throws IOException, ParseException {
//        les();
//        finnBesteSti();
//        flytt();
        fjernDuplikater();
    }

    private static void fjernDuplikater() {
        File[] files = tilDir.listFiles();
        String[] navn = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            navn[i] = files[i].getName().toUpperCase();
        }
        Arrays.sort(navn);

        int fjernes = 0;
        for (int i = 1; i < navn.length; i++) {
            String startI = navn[i].substring(0, 20);
            String sluttI = navn[i].substring(navn[i].length()-10, navn[i].length() );
            String startI_1 = navn[i-1].substring(0, 20);
            String sluttI_1 = navn[i-1].substring(navn[i-1].length()-10, navn[i-1].length() );
            if (startI.equals(startI_1) && sluttI.equals(sluttI_1)) {
                fjernes++;
                File fileI = new File(tilDir, navn[i]);
                File fileI_1 = new File(tilDir, navn[i-1]);
                long lengthI = fileI.length();
                long lengthI_1 = fileI_1.length();
                System.out.println(navn[i]);
                System.out.println(navn[i - 1] + " " + lengthI + "/" + lengthI_1);
                if (lengthI < lengthI_1) {
                    System.out.println("U=" +navn[i]);
                    fileI.delete();
                } else {
                    System.out.println("U=" +navn[i-1]);
                    fileI_1.delete();
                }
            }
        }
        System.out.println("Fjernes=" + fjernes);

    }


    private static void les() throws IOException, ParseException {
        String[] line;
        CSVReader reader = new CSVReader(new FileReader("C:\\Bilder ryddet\\bilder.csv"));
        while ((line = reader.readNext()) != null) {
            Bilde bilde = tilBilde(line);
            bilder.add(bilde);
        }
        reader.close();
        System.out.println("Alle lest");
    }

    private static void finnBesteSti() {
        for (Bilde bilde : bilder) {
            Bildefil beste = bilde.finnbesteSti();
            besteBilder.add(beste);
        }
    }

    private static void flytt() throws IOException {
        int i = 0;
        for (Bildefil bildefil : besteBilder) {
            String fra = bildefil.getSti();
            String til = bildefil.getNewName();
//            System.out.println(fra + "  -> " + til);
            i++;

            File newFile = new File(tilDir, til);
            if (newFile.exists()) {
                System.out.println("Allerede flyttet " + newFile.getAbsolutePath());
            } else {
                File fraFile = new File(fra);
                fraFile.renameTo(newFile);
            }


            if (i % 100 == 0) {
                System.out.println("Flyttet " + i);
            }
        }
        System.out.println("Flyttet " + i);

    }

    private static Bilde tilBilde(String[] line) throws ParseException {
        String hash = line[0];
        Bilde bilde = new Bilde(hash);
        int i = 1;
        while (i < line.length) {
            String sti = line[i++];
            Date tattDato = toDate(line[i++]);
            Date fildato = toDate(line[i++]);
            bilde.addBildefil(sti, tattDato, fildato);
//            System.out.println(bilde.toString());
        }

        return bilde;

    }

    private static Date toDate(String s) throws ParseException {
        if ("".equals(s)) {
            return null;
        }

        if (s == null) {
            return null;
        }

        return parser.parse(s);
    }

}
