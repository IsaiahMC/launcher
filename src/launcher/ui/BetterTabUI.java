package launcher.ui;

import static launcher.ui.Colors.SELECTED_TAB_BORDER_COLOR;
import static launcher.ui.Colors.SELECTED_TAB_COLOR;
import static launcher.ui.Colors.SELECTED_TAB_TEXT_COLOR;
import static launcher.ui.Colors.UNSELECTED_TAB_BORDER_COLOR;
import static launcher.ui.Colors.UNSELECTED_TAB_COLOR;
import static launcher.ui.Colors.UNSELECTED_TAB_TEXT_COLOR;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.plaf.metal.MetalTabbedPaneUI;

public class BetterTabUI extends MetalTabbedPaneUI {

    private int w = 130, h = 34;
    private Insets EMPTY = new Insets(-1, -1, -1, -1);

    @Override
    public Insets getContentBorderInsets(int tabPlacement) {
        return EMPTY;
    }
    
    @Override
    public int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        return w;
    }

    @Override
    public int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        return h;
    }

    @Override
    protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
        boolean selected = tabPane.getSelectedIndex() == tabIndex;
        Rectangle rect = rects[tabIndex];

        g.setColor(selected ? SELECTED_TAB_COLOR : UNSELECTED_TAB_COLOR);
        g.fillRect(rect.x, selected ? rect.y : rect.y + 3, rect.width, rect.height);

        g.setColor(selected ? SELECTED_TAB_BORDER_COLOR : UNSELECTED_TAB_BORDER_COLOR);
        g.drawRect(rect.x, selected ? rect.y : rect.y + 3, rect.width, rect.height+rect.y);

        String title = tabPane.getTitleAt(tabIndex);
        g.setColor(selected ? SELECTED_TAB_TEXT_COLOR : UNSELECTED_TAB_TEXT_COLOR);
        g.drawString(title, (rect.x + rect.width/2) - ((title.length()-1)*(g.getFont().getSize()/3)), (rect.y + rect.height/2)+5);

    }

 }