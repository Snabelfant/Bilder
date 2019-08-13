package dag.bilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UrlDownloader {
    private static final int BUFFER_SIZE = 8192;

    public static void download(String url, File file, ProgressListener progressListener) throws IOException {
        int length = UrlStream.getLength(url);
        InputStream inputStream = UrlStream.getInputStream(url);
        copy(inputStream, new BufferedOutputStream((new FileOutputStream(file))), progressListener, length);
    }

    private static long copy(InputStream source, OutputStream sink, ProgressListener progressListener, long length)
            throws IOException {
        long nread = 0L;
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ((n = source.read(buf)) > 0) {
            sink.write(buf, 0, n);
            nread += n;
            reportProgress(progressListener, nread, length);
        }

        reportProgress(progressListener, length, length);

        return nread;
    }

    private static void reportProgress(ProgressListener progressListener, long bytesRead, long length) {
        if (progressListener != null) {
            int progressPercentage;
            if (length == -1) {
                progressPercentage = -1;
            } else {
                progressPercentage = ((int) (bytesRead * 100 / length));
            }

            progressListener.reportProgress(progressPercentage);
        }
    }

    public interface ProgressListener {
        void reportProgress(int percentage);
    }
}
