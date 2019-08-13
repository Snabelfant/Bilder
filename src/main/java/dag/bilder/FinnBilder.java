package dag.bilder;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.opencsv.CSVWriter;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import static java.lang.System.exit;

/**
 * Created by Dag on 08.12.2016.
 */
public class FinnBilder {
    private static String RYDDEDIR = "C:\\Bilder ryddet";

    public static void main(String... args) throws IOException, NoSuchAlgorithmException, MetadataException, ImageProcessingException, ParseException {

        System.out.println("Ikke mer av dette");
        exit(1);
        List<String> startkataloger = Arrays.asList(
                "C:\\Users\\Dag\\Pictures\\Vildes kamera",
                "C:\\Bilder på CD",
                "C:\\Bilder - Kopi");

        Bildesamler bildesamler = new Bildesamler(startkataloger);

        bildesamler.finn();
        List<String> bildenavn = bildesamler.getBildenavn();

        System.out.println("Totalt=" + bildenavn.size());

        Klassifiserer klassifiserer = new Klassifiserer(bildenavn);
        klassifiserer.klassifiser();
        Map<String, Bilde> bilder = klassifiserer.getBilder();
        System.out.println("Totalt=" + bildenavn.size());
        System.out.println("Unike=" + bilder.size());

        skrivBilder(bilder);

    }

    private static void skrivBilder(Map<String, Bilde> bilder) throws IOException {
        FileWriter fw = new FileWriter(new File(new File(RYDDEDIR), "bilder.csv"));
        CSVWriter writer = new CSVWriter(fw);

        bilder.forEach(new BiConsumer<String, Bilde>() {
            @Override
            public void accept(String s, Bilde bilde) {
                writer.writeNext(bilde.toList().toArray(new String[0]));
            }
        });

        writer.close();
    }

}
