package launcher.ui;

import java.awt.Color;
import jthemes.themes.JTheme;
import jthemes.themes.JThemeInfo;
import jthemes.themes.Resource;

@JThemeInfo(name="Normal", version=1, dataPath="normal")
public class NormalTheme implements JTheme {

    @Override
    public Resource getTitleBar() {
        return new Resource(new Color(50,50,50));
    }

    @Override
    public Resource getBorder() {
        return new Resource(new Color(0,0,0));
    }

}