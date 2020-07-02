package launcher;

import java.io.PrintStream;

/**
 * {@link java.lang.Exception} but with cleaner StackTrace
 */
public class Error extends Exception {

    private static final long serialVersionUID = 1L;

    public Error(String msg) {
        super(msg);
    }

    /**
     * Provides cleaner StackTrace
     */
    @Override
    public void printStackTrace(PrintStream err) {
        StackTraceElement[] stack = this.getStackTrace();
        err.println(getClass().getName() + ": " + getMessage());
        int javalib = 0;
        for (StackTraceElement s : stack) {
            String str = s.toString();
            if (str.startsWith("java"))
                javalib++;
            else javalib = 0;
            if (javalib > 5) break;

            err.println("\tat " + str); 
        }
    }

    public void printSimpleStackTrace() {
        StackTraceElement[] stack = this.getStackTrace();
        System.err.println(getClass().getName() + ": " + getMessage());
        int javalib = 0;
        for (StackTraceElement s : stack) {
            String str = s.toString();
            if (str.startsWith("java"))
                javalib++;
            else javalib = 0;
            if (javalib > 1) break;

            System.err.println("\tat " + str); 
        }
    }


}
