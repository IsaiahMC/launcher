package launcher.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class ModernButton extends JButton {

    private static final long serialVersionUID = 1L;
    private JLabel label = new JLabel();
    private Color bg;

    public ModernButton(String text) {
        this();
        this.setText(text);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setText(text);
        label.setOpaque(false);
        setLayout(null);
        add(label, BorderLayout.CENTER);
    }

    public ModernButton(Icon i) {
        this();
        this.setIcon(i);
    }

    public ModernButton(Action a) {
        this();
        this.setAction(a);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        this.bg = bg;
    }

    public void setBorder(Border out, Border in) {
        this.setBorder(BorderFactory.createCompoundBorder(out, in));
    }

    public ModernButton() {
        super();
        this.setOpaque(true);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2), BorderFactory.createEmptyBorder(3,14,3,14));

        this.addMouseListener(new MouseAdapter() {
            int a = 10;

            @Override
            public void mouseClicked(MouseEvent arg0) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Color c = getBackground();
                int r = c.getRed() + a;
                int g = c.getGreen() + a;
                int b = c.getBlue() + a;
                if (r > 255) r = 255;
                if (g > 255) g = 255;
                if (b > 255) b = 255;
                bg = new Color(r, g, b);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Color c = getBackground();
                int r = c.getRed() - a;
                int g = c.getGreen() - a;
                int b = c.getBlue() - a;
                if (r < 0) r = 0;
                if (g < 0) g = 0;
                if (b < 0) b = 0;
                bg = new Color(r, g, b);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
            
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Rectangle rectangle = getBounds();
        rectangle.x = 3;
        rectangle.y = 3;
        rectangle.width -= rectangle.x * 2;
        rectangle.height -= rectangle.y * 2;

        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(bg);
        g2d.fill(rectangle);
    }

    @Override
    public void paint(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);

        label.setFont(getFont().deriveFont(Font.BOLD));
        label.setForeground(Color.WHITE);
        label.setBounds(getWidth()/2 - 25, 6, 100, 30);
    }

}