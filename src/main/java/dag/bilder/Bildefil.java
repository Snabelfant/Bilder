package dag.bilder;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Dag on 09.12.2016.
 */
public class Bildefil {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
    private String sti;
    private Date tattDato;
    private Date fildato;
    private String kameramodell;
    public Bildefil(String sti, Date tattDato, Date fildato) {
        this.sti = sti;
        this.tattDato = tattDato;
        this.fildato = fildato;
    }

    public Bildefil(String sti) {
        this.sti = sti.replaceAll("\\\\", "/");
        File fil = new File(sti);
        fildato = new Date(fil.lastModified());


        boolean erJpeg = sti.endsWith(".jpg") || sti.endsWith(".JPG") || sti.endsWith(".JPEG") || sti.endsWith(".jpeg");
        if (!erJpeg) {
            return;
        }

        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(fil);
        } catch (ImageProcessingException e) {
            return;
        }
        Directory directory = metadata.getDirectory(ExifDirectory.class);
        if (directory != null) try {
            tattDato = directory.getDate(ExifDirectory.TAG_DATETIME);
            kameramodell = directory.getString(ExifDirectory.TAG_MAKE) + "/" + directory.getString(ExifDirectory.TAG_MODEL);
        } catch (MetadataException e) {
        }
    }

    public String getKameramodell() {
        return kameramodell;
    }

    @Override
    public String toString() {
        return sti + "/" + (tattDato == null ? "" : format.format(tattDato)) + "/" + (fildato == null ? "" : format.format(fildato));
    }

    public List<String> toList() {
        return Arrays.asList(sti, (tattDato == null ? "" : format.format(tattDato)), (fildato == null ? "" : format.format(fildato)));
    }

    public String getSti() {
        return sti;
    }

    public String getNewName() {
        Date besteDato = tattDato != null ? tattDato : fildato;
        String dato = format.format(besteDato);
        String navn = sti.replace("C:", "").replace("/", "_");
        return dato + "_" + navn;
    }

    public Date getTattDato() {
        return tattDato;
    }

    public Date getFildato() {
        return fildato;
    }
}
