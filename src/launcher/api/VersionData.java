package launcher.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import launcher.Main;
import launcher.Utils;

public class VersionData {

    public String id;
    public String type;
    public String url;
    public String time;
    public String releaseTime;
    
    public ArrayList<LibraryData> libraries;

    private JsonObject json;

    public List<LibraryData> getLibraries() {
        if (null == libraries) {
            libraries = new ArrayList<>();
            parseJson();
            JsonArray list = json.get("libraries").getAsJsonArray();
            list.forEach(lib -> {
                JsonObject obj = lib.getAsJsonObject();
                JsonObject o = obj.get("downloads").getAsJsonObject();
                if (o.has("artifact")) {
                    JsonElement artifact = o.get("artifact");
                    LibraryData data = Utils.gson.fromJson(artifact, LibraryData.class);
                    libraries.add(data);
                }

                if (o.has("classifiers")) {
                    JsonObject classifiers = o.get("classifiers").getAsJsonObject();
                    JsonElement nativ = classifiers.get("natives-" + Utils.getNativeOsId());
                    LibraryData data = Utils.gson.fromJson(nativ, LibraryData.class);
                    if (null != data)
                        libraries.add(data);
                }
            });
        }
        return libraries;
    }

    public File unzipNatives() {
        File dir = new File(new File(Main.DIR, "bin"), "natives-" + id);
        dir.mkdirs();

        for (LibraryData library : getLibraries()) {
            if (library.path.contains("native")) {
                System.out.println("Natives: " + library.path);
                try {
                    unzipNative(dir, library.url);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return dir;
    }

    public void unzipNative(File dir, String rawurl) throws IOException {
        URL url = new URL(rawurl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream in = connection.getInputStream();
        ZipInputStream zipIn = new ZipInputStream(in);
        ZipEntry entry = zipIn.getNextEntry();

        while(entry != null) {
            System.out.println(entry.getName());
            if (!entry.isDirectory()) {
                saveEntry(dir, entry, new FilterInputStream(zipIn) {
                    @Override
                    public void close() throws IOException {
                        zipIn.closeEntry();
                    }
                });
            } else {
                System.out.println("===Directory===");
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
    }

    public void saveEntry(File dest, ZipEntry target, InputStream is) throws IOException {
        try {
            File file = new File(dest.getAbsolutePath(), target.getName());
            file.delete();

            if (target.isDirectory())
                file.mkdirs();
            else {
                BufferedInputStream bis = new BufferedInputStream(is);
                File dir = new File(file.getParent());
                dir.mkdirs();
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int c;
                while ((c = bis.read()) != -1)
                    bos.write((byte) c);

                bos.close();
                fos.close();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public String getMainClass() {
        parseJson();
        return json.get("mainClass").getAsString();
    }

    public String getMinecraftArguments() {
        parseJson();
        return json.get("minecraftArguments").getAsString();
    }

    public String getJarDownload() {
        parseJson();
        return json.get("downloads").getAsJsonObject().get("client").getAsJsonObject().get("url").getAsString();
    }

    private void parseJson() {
        // TODO: is slow, make faster

        if (null != json)
            return;

        System.out.println(url);
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new URL(url).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.json = JsonParser.parseReader(reader).getAsJsonObject();
    }

    @Override
    public String toString() {
        return String.format("======%s/%s======\nURL:%s\nTime:%s\nreleaseTime:%s\nmainClass:%s\njarDownload:%s\nLibs:%s\n=============",
                id, type, url, time, releaseTime, getMainClass(), getJarDownload(), getLibraries());
    }

}