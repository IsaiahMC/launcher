package launcher.panels;

import static launcher.ui.Colors.LAUNCHER_LOG_BACKGROUND;
import static launcher.ui.Colors.LAUNCHER_LOG_ERROR_FORGROUND;
import static launcher.ui.Colors.LAUNCHER_LOG_TEXT_FORGROUND;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class LauncherLog extends JScrollPane {

    private static final long serialVersionUID = 1L;
    private static JTextPane txt = new JTextPane();

    public LauncherLog() {
        txt.setOpaque(true);
        txt.setBackground(LAUNCHER_LOG_BACKGROUND);
        txt.setBorder(BorderFactory.createEmptyBorder(12,12,0,12));
        txt.setEditable(false);
        this.setViewportView(txt);
    }

    public static void bind() {
        PrintStream m = System.out;
        StyledDocument d = txt.getStyledDocument();

        System.setOut(new PrintStream(new OutputStream() {
            @Override public void write(int b) throws IOException {
                m.write(b);
                Style style = d.getStyle(StyleContext.DEFAULT_STYLE);
                StyleConstants.setForeground(style, LAUNCHER_LOG_TEXT_FORGROUND);
                try {
                    d.insertString(d.getLength(), String.valueOf((char) b), style);
                } catch (BadLocationException e) { e.printStackTrace(); }
            }}));

        PrintStream err = System.err;
        System.setErr(new PrintStream(new OutputStream() {
            @Override public void write(int b) throws IOException {
                err.write(b);
                Style style = d.getStyle(StyleContext.DEFAULT_STYLE);
                StyleConstants.setForeground(style, LAUNCHER_LOG_ERROR_FORGROUND);
                try {
                    d.insertString(d.getLength(), String.valueOf((char) b), style);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }}));
    }

}