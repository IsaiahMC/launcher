package launcher;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import launcher.api.AssetDownloader;
import launcher.api.GameLaunchAPI;
import launcher.api.GameProfileData;
import launcher.api.LibraryData;
import launcher.api.Natives;
import launcher.api.VersionData;

public class GameProfile {

    // Profile Info
    public String name;
    public File gameDir;
    public Dimension resolution;
    public boolean askMojangAboutCrashFix;
    public int launcherVisiblity;

    // Version Selection
    public boolean enableSnapshots;
    public boolean enableBeta;
    public boolean enableAlpha;
    public String version;

    // Java Settings (Advanced)
    public String exe;
    public String jvmArgs;

    public GameProfileData data;

    public GameProfile(String name, File gameDir, Dimension resolution, boolean askMojangAboutCrashFix, int launcherVisiblity,
            boolean enableSnapshots, boolean enableBeta, boolean enableAlpha, String version, String exe, String jvmArgs) throws ProfileNotFoundError {
        this.name = name;
        this.gameDir = gameDir; // TODO def
        this.resolution = (null == resolution) ? new Dimension(854, 480) : resolution;
        this.askMojangAboutCrashFix = askMojangAboutCrashFix;
        this.launcherVisiblity = launcherVisiblity;
        this.enableSnapshots = enableSnapshots;
        this.enableBeta = enableBeta;
        this.enableAlpha = enableAlpha;
        this.version = version;
        this.exe = (null == exe) ? getDefaultJavaExe() : exe;
        this.jvmArgs = (null == jvmArgs) ? (System.getProperty("os.arch").contains("64") ?
                "-Xmx1G -XX:+UseConcMarkSweepGC -XX:-UseAdaptiveSizePolicy -Xmn128M" :
              "-Xmx512M -XX:+UseConcMarkSweepGC -XX:-UseAdaptiveSizePolicy -Xmn128M") : jvmArgs;

        File file = new File(new File(new File(Utils.getDataFolder(), "versions"), version), version + ".json");
        if (!file.isFile() && !file.exists())
            throw new ProfileNotFoundError(file.getAbsolutePath() + " does not exist!");

        try {
            this.data = Utils.gson.fromJson(Utils.parser.parse(new FileReader(file)).getAsJsonObject(), GameProfileData.class);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded profile \"" + name + "\".");
    }

    public void launch() {
        ArrayList<String> args = new ArrayList<>();
        AssetDownloader.download(this);

        args.add("\"" + getDefaultJavaExe() + "\"");
        for (String s : jvmArgs.split(" "))
            args.add(s);

        File libsFile = new File(Utils.getDataFolder(), "libraries");
        libsFile.mkdir();
        String libPath = libsFile.getAbsolutePath() + File.separator;
        String libs = "";
        //String natives = "";

        Natives.unzipNatives(data.libraries);

        for (LibraryData lib : data.libraries) {
            if (null == lib.path) {
                String lname = lib.name.split(":")[0].replace(".", File.separator);
                lname += lib.name.substring(lname.length());
                lib.path = lname.replace(":", File.separator) + File.separator +
                        lname.split(":")[1] + "-" + lname.substring(lname.lastIndexOf(":") + 1) + ".jar";
            }
            libs += libPath + lib.path + ";";
        }
        File natives = null;
        if (null != data.inheritsFrom) {
            VersionData vdat = GameLaunchAPI.versionData.get(data.inheritsFrom);
            natives = vdat.unzipNatives();
            for (LibraryData lib : vdat.getLibraries())
                if (null != lib)
                    libs += libPath + lib.path.replace("/", File.separator) + ";";
        }
        args.add("-Dminecraft.client.jar=" + getPathToGameJar());
        args.add("-Djava.library.path=" + natives.getAbsolutePath());
        args.add("-cp");
        args.add(libs + ";" + libsFile.getAbsolutePath() + ";" + getPathToGameJar());

        args.add(null == data.mainClass ? "net.minecraft.client.main.Main" : data.mainClass);

        args.add("--uuid");
        args.add(Auth.getName());
        args.add("--accessToken");
        args.add(Auth.getToken());
        System.out.println(Auth.getToken());
        args.add("--username");
        args.add(Auth.getName());
        args.add("--gameDir");
        args.add(Utils.getDataFolder().getAbsolutePath());
        args.add("--assetsDir");
        args.add(Utils.getAssetsFolder().getAbsolutePath());
        args.add("-version");
        args.add("1.12.2-OpenMCLauncher");

        args.add("--tweakClass");
        args.add("optifine.OptiFineTweaker");

        ProcessBuilder pb = new ProcessBuilder(args);
        pb.inheritIO();
        System.out.println(pb.command());
        try {
            Process s = pb.start();
            s.isAlive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPathToGameJar() {
        File appdat = Utils.getDataFolder();
        return new File(new File(new File(appdat, "versions"), version), version + ".jar").getAbsolutePath();
    }

    public String getDefaultJavaExe() {
        final String JAVA_HOME = System.getProperty("java.home");
        final File BIN = new File(JAVA_HOME, "bin");
        File exe = new File(BIN, "java");

        if (!exe.exists())
            exe = new File(BIN, "java.exe");

        if (exe.exists())
            return exe.getAbsolutePath();

        try {
            final String NAKED_JAVA = "java";
            new ProcessBuilder(NAKED_JAVA).start();

            return NAKED_JAVA;
        } catch (IOException e) { return null; }
    }

    @Override
    public String toString() {
        return name;
    }

}