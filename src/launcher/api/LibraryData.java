package launcher.api;

public class LibraryData {

    public String name;
    public String path;
    public String sha1;
    public long size;
    public String url;

    @Override
    public String toString() {
        return name;
    }

}