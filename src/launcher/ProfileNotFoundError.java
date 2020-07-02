package launcher;

public class ProfileNotFoundError extends Error {

    private static final long serialVersionUID = 1L;

    public ProfileNotFoundError(String msg) {
        super(msg);
    }

}
