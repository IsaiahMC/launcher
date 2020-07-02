package launcher.panels;

import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

public class SwingBrowser extends JScrollPane {

    private static final long serialVersionUID = 1L;

    public SwingBrowser() {
        JEditorPane ep = new JEditorPane();
        ep.setEditable(false);
        try {
            ep.setPage("https://isaiahpatton.github.io/mcupdate");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO hyperlinks

        setViewportView(ep);
        setVisible(true);
    }

}
