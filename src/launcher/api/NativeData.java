package launcher.api;

public class NativeData {

    public String path;
    public String sha1;
    public long size;
    public String url;

    @Override
    public String toString() {
        return url;
    }

}