package dag.bilder;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Dag on 10.12.2016.
 */
public class Filfilter {
    private static final List<String> exts = Arrays.asList("AVI", "JPEG", "JPG", "MOV", "MPG","PNG","GIF");


    public static boolean akseptert(String sti) {
        return exts.contains(finnFiltype(sti));
    }
    private static String finnFiltype(String sti) {
        String extension = "";
        int i = sti.lastIndexOf('.');
        if (i > 0) {
            extension =  sti.substring(i + 1).toUpperCase();
        }
        return extension;
    }

}
