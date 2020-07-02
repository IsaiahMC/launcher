package launcher.panels;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventTarget;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import launcher.Utils;

public class JfxBrowser extends JFXPanel {

    private static final long serialVersionUID = 1L;

    public JfxBrowser() {
        Platform.runLater(() -> {
            WebView w = new WebView();
            WebEngine e = w.getEngine();
            e.setUserAgent(e.getUserAgent() + " ZunoZap/1.0");
            e.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
                public void changed(ObservableValue<? extends Worker.State> ov, State oldState, State newState) {
                    if (newState == Worker.State.SUCCEEDED) {
                        NodeList list = e.getDocument().getElementsByTagName("a");

                        for (int i = 0; i < list.getLength(); i++) {
                            ((EventTarget)list.item(i)).addEventListener("click", ev -> {
                                ev.preventDefault();
                                Utils.browse( ((Element)ev.getTarget()).getAttribute("href") );
                            }, false);
                        }
                        System.out.println("Registered listener on " + list.getLength() + " hyperlinks");
                    }
                }
            });
            e.load( this.getClass().getClassLoader().getResource("mcupdate/index.html").toString() );
            setScene(new Scene(w));
        });
        this.setBorder(null);
        setVisible(true);
    }

}
