package launcher.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Natives {

    public static void unzipNatives(List<LibraryData> data) {

        for (LibraryData library : data) {
            if (library.path != null && library.path.contains("native")) {
                System.out.println("Natives: " + library.path);
            }
            if (library.path != null) System.out.println("lib: " + library.path);
        }
    }

    public static void unzipNative(String rawurl) throws IOException {
        URL url = new URL(rawurl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream in = connection.getInputStream();
        ZipInputStream zipIn = new ZipInputStream(in);
        ZipEntry entry = zipIn.getNextEntry();

        while(entry != null) {

            System.out.println(entry.getName());
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                System.out.println("===File===");
            } else {
                System.out.println("===Directory===");
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();

        }
    }

}