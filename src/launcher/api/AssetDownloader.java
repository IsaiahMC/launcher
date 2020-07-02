package launcher.api;

import java.util.List;

import launcher.GameProfile;

public class AssetDownloader {

    public static void download(GameProfile profile) {
        GameProfileData data = profile.data;
        List<LibraryData> libs = data.libraries;
        for (LibraryData lib : libs) {
            System.out.println(lib.path);
        }
    }

}