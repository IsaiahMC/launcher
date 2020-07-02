package launcher.api;

import java.util.List;

public class GameProfileData {

    public String id;
    public String inheritsFrom;
    public String time;
    public String releaseTime;
    public String type;
    public List<LibraryData> libraries;
    public String mainClass;
    public String minimumLauncherVersion;
    public Object arguments;

    @Override
    public String toString() {
        return String.format("ID:%s,inheritsFrom:%s,time:%s,releaseTime:%s,type:%s,libraries:%s,"
                + "mainClass:%s,minimumLauncherVersion:%s,arguments:%s",
                id, inheritsFrom, time, releaseTime, type, libraries, mainClass, minimumLauncherVersion, arguments);
    }

}