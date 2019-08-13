package dag.bilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dag on 08.12.2016.
 */
public class Bildesamler {
    private List<String> startkataloger;

    public Bildesamler(List<String> startkataloger) {
        this.startkataloger = startkataloger;
    }

    public List<String> getBildenavn() {
        return bildenavn;
    }

    private List<String> bildenavn = new ArrayList<>();

    public void finn() {
        for (String startkatalog : startkataloger) {
            int sizeNow = bildenavn.size();
            System.out.println("Katalog " + startkatalog);
            traverse(new File(startkatalog));
            System.out.println("Funnet " + (bildenavn.size()-sizeNow) + " i " + startkatalog);
        }
    }

    private void traverse(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; children != null && i < children.length; i++) {
                traverse(new File(dir, children[i]));
            }
        }
        if (dir.isFile()) {
            String sti = dir.getAbsolutePath();
            if (Filfilter.akseptert(sti)) {
                bildenavn.add(sti);
            } else {
                System.out.println("Ikke bilde:  " + sti);
            }
        }
    }
}
