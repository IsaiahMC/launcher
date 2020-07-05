package launcher;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GameProfileManager {

    public File file;
    public static HashMap<String, GameProfile> profiles = new HashMap<String, GameProfile>();

    public static GameProfile current = null;

    public static GameProfile getCurrentSelectedProfile() {
        if (current == null)
            current = profiles.values().toArray(new GameProfile[0])[0]; // TODO
        return current;
    }

    public GameProfileManager() throws FileNotFoundException {
        this.file = new File(Utils.getDataFolder(), "launcher_profiles.json");

        // load local versions
        JsonObject o = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
        JsonObject e = o.get("profiles").getAsJsonObject();
        JsonObject settings = o.has("settings") ? o.getAsJsonObject("settings") : null;
        Set<Entry<String, JsonElement>> s = e.entrySet();
        for (Entry<String, JsonElement> data : s) {
            JsonObject obj = data.getValue().getAsJsonObject();
            String name = obj.has("name") ? obj.get("name").getAsString() : obj.get("lastVersionId").getAsString();
            boolean old = obj.has("allowedReleaseTypes");

            try {
                GameProfile profile = new GameProfile(
                        name.length() > 0 ? name : obj.get("lastVersionId").getAsString(),
                        obj.has("gameDir") ? new File(obj.get("gameDir").getAsString()) : Utils.getDataFolder(),
                        obj.has("resolution") ? new Dimension(obj.get("resolution").getAsJsonObject().get("width").getAsInt(),
                                obj.get("resolution").getAsJsonObject().get("height").getAsInt()) : null, 
                        false,
                        0,
                        old ? obj.get("allowedReleaseTypes").getAsJsonObject().has("snapshot") : (null != settings ? settings.get("enableSnapshots").getAsBoolean() : false),
                        old ? obj.get("allowedReleaseTypes").getAsJsonObject().has("old_beta") : (null != settings ? settings.get("enableHistorical").getAsBoolean() : false),
                        old ? obj.get("allowedReleaseTypes").getAsJsonObject().has("old_alpha") : (null != settings ? settings.get("enableHistorical").getAsBoolean() : false),
                        obj.has("lastVersionId") ? obj.get("lastVersionId").getAsString() : "ERROR", // todo
                        obj.has("javaDir") ? obj.get("javaDir").getAsString() : null,
                        obj.has("javaArgs") ? obj.get("javaArgs").getAsString() : null);
                profiles.put(data.getKey(), profile);
            } catch (ProfileNotFoundError e1) {
                String msg = e1.getMessage();
                System.err.println("Unable to load profile: ..." + msg.substring(msg.indexOf(File.separator + "versions")));
            }
        }
        System.out.println("\nRefreshed profiles from files.");
    }

}