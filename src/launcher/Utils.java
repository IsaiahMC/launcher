package launcher;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import com.google.gson.Gson;

public class Utils {

    public static Gson gson = new Gson();

    public static void browse(String url) {
        if (!Desktop.isDesktopSupported())
            return; // TODO

        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (IOException | URISyntaxException e) { e.printStackTrace(); }
    }

    public static File getDataFolder() {
        switch (getOS()) {
            case LINUX:
                return new File(new File(System.getProperty("user.home")), ".minecraft");
            case WIN:
                return new File(new File(System.getenv("APPDATA")), ".minecraft");
            case OSX:
                return new File(new File(System.getProperty("user.home")), File.separator + 
                        "Library" + File.separator + "Application Support" + File.separator + "/minecraft");
            default:
                return new File(new File(System.getProperty("user.home")), ".minecraft");
        }
    }

    public static File getAssetsFolder() {
        return new File(getDataFolder(), "assets");
    }

    public static String getNativeOsId() {
        switch (getOS()) {
            case LINUX:
                return "linux";
            case OSX:
                return "osx";
            case WIN:
                return "windows";
        }
        return null;
    }

    public enum OS { OSX, WIN, LINUX; }

    public static OS getOS() {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

        if ((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0)) {
            return OS.OSX;
        } else if (os.indexOf("win") >= 0) {
            return OS.WIN;
        } else if (os.indexOf("nux") >= 0) {
            return OS.LINUX;
        } else return OS.LINUX; // assume OS is based on Linux
    }

}