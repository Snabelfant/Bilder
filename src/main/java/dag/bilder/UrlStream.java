package dag.bilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlStream {

    public static InputStream getInputStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        return new BufferedInputStream(url.openStream());

    }

    public static int getLength(String urlString) throws IOException {
        return new URL(urlString).openConnection().getContentLength();
    }
}
