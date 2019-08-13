package dag.bilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dag on 23.05.2017.
 */
public class KorrigerDatoer {

    public static void main(String... args) {
        File JADIR = new File("C:\\Bilder ryddet\\Unike\\behold");

        File[] files = JADIR.listFiles(pathname -> !pathname.isDirectory());
        System.out.println("Filer=" + files.length);

        for (File file : files) {
            String navn = file.getName();

            Pattern datoPattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d)\\D");
            Matcher matcher = datoPattern.matcher(navn);

            List<String> matches = new ArrayList<>();
            while (matcher.find()) {
                String group = matcher.group(1);
                matches.add(group);
            }

            if (matches.size() > 1 && !matches.get(0).equals(matches.get(1))) {
                System.out.println(matches);
            }
        }


    }
}
