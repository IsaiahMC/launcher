package launcher.ui;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;

import jthemes.themes.JTheme;
import jthemes.themes.JThemeInfo;
import jthemes.themes.Resource;

@JThemeInfo(name="Green", version=1, dataPath="login")
public class LoginTheme implements JTheme {

    @Override
    public Resource getTitleBar() {
        try {
            return new Resource(ImageIO.read( getClass().getClassLoader().getResourceAsStream("assets/titlebar.png") ));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Resource getBorder() {
        return new Resource(new Color(0,0,0));
    }

}