package launcher.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
        this.json = Utils.parser.parse(reader).getAsJsonObject();
    }

    @Override
    public String toString() {
        return String.format("======%s/%s======\nURL:%s\nTime:%s\nreleaseTime:%s\nmainClass:%s\njarDownload:%s\nLibs:%s\n=============",
                id, type, url, time, releaseTime, getMainClass(), getJarDownload(), getLibraries());
    }

}