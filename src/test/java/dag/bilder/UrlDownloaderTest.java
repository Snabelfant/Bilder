package dag.bilder;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;

/**
 * Created by Dag on 03.05.2017.
 */
public class UrlDownloaderTest  {
    @Test
    public void testDownload() throws Exception {
        UrlDownloader.download("https://podkast.nrk.no/fil/ekko_-_et_aktuelt_samfunnsprogram/ekko_-_et_aktuelt_samfunnsprogram_2017-04-25_1250_2775.MP3?stat=1",
                new File("C:\\temp\\xx.yy"), new UrlDownloader.ProgressListener() {
                    public void reportProgress(int percentage) {

                    }
                });
    }

}