package dag.bilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dag on 08.12.2016.
 */
public class Bilde {
    private List<Bildefil> bildefiler = new ArrayList<Bildefil>();
    private String hash;

    public Bilde(String hash) {
        this.hash = hash;
    }

    public void addBildefil(String sti, Date tattDato, Date fildato) {
        Bildefil bildefil = new Bildefil(sti, tattDato, fildato);
        bildefiler.add(bildefil);
    }

    public void addSti(String sti) {
        Bildefil bildefil = new Bildefil(sti);
        bildefiler.add(bildefil);
    }

    public boolean harFlere() {
        return bildefiler.size() > 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(hash);
        for (Bildefil bildefil : bildefiler) {
            sb.append(",")
                    .append(bildefil.toString());
        }
        return sb.toString();
    }

    public List<String> toList() {
        List<String> s = new ArrayList<>();
        s.add(hash);
        for (Bildefil bildefil : bildefiler) {
            s.addAll(bildefil.toList());
        }

        return s;
    }

    public Bildefil finnbesteSti() {
        Bildefil beste = bildefiler.get(0);

        for (int i = 1; i < bildefiler.size(); i++) {
            beste = best(beste, bildefiler.get(i));
        }

        return beste;
    }

    private Bildefil best(Bildefil beste, Bildefil kandidat) {
        if (beste.getTattDato() == null) {
            if (kandidat.getTattDato() != null) {
                return kandidat;
            }
        }
        if (beste.getFildato().before(kandidat.getFildato())) {
            return beste;
        } else {
            return kandidat;
        }
    }
}
