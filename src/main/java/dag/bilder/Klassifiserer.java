package dag.bilder;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import javafx.collections.transformation.SortedList;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Dag on 08.12.2016.
 */
public class Klassifiserer {
    private Map<String, Bilde> bilder = new TreeMap<>();
    private List<String> bildenavner;

    public Klassifiserer(List<String> bildenavner) {
        this.bildenavner = bildenavner;
    }


    public void klassifiser() throws IOException, NoSuchAlgorithmException, MetadataException, ImageProcessingException {
        int i = 0;
        for (String bildenavn : bildenavner) {
            String hash = Hasher.hash(bildenavn);
            Bilde bilde = bilder.get(hash);
            if (bilde == null) {
                bilde = new Bilde(hash);
                bilder.put(hash, bilde);
            } else {
                System.out.println(bilde.toString() + ": duplikat " + bildenavn);
            }

            bilde.addSti(bildenavn);

            i++;
            if (i % 10 == 0) {
                System.out.print(".");
            }

            if (i % 100 == 0) {
                System.out.println(i + "-" + bilder.size());
            }
//            if (i > 1000) {
//                break;
//            }
        }

        System.out.println();
    }

    public Map<String, Bilde> getBilder() {
        return bilder;
    }

}
