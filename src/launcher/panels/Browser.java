package launcher.panels;

import javax.swing.JComponent;

public class Browser {

    public static JComponent get() {
        return hasFx() ? new JfxBrowser() : new SwingBrowser();
    }
    
    public static boolean hasFx() {
        try {
            Class.forName("javafx.scene.web.WebView");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}