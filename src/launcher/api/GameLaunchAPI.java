package launcher.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import launcher.Utils;

public class GameLaunchAPI {

    public static HashMap<String, VersionData> versionData;

    public static final String LAUNCHER_META = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    public static void downloadVersionData() throws IOException {
        versionData = new HashMap<>();
        InputStreamReader reader = new InputStreamReader(new URL(LAUNCHER_META).openStream());
        JsonObject o = JsonParser.parseReader(reader).getAsJsonObject();
        JsonArray e = o.get("versions").getAsJsonArray();
        e.forEach(a -> {
            VersionData d = Utils.gson.fromJson(a, VersionData.class);
            if (null != d)
                versionData.put(d.id, d);
        });
    }

    // testing
    public static void main(String[] args) throws IOException {
        downloadVersionData();
    }

}