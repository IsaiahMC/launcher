package launcher.panels;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

    public Image BG;

    public MainPanel() {
        try {
            this.BG = ImageIO.read( this.getClass().getClassLoader().getResourceAsStream("mcupdate/stone.png") ).getScaledInstance(48, 48, 0);
        } catch (IOException e) { e.printStackTrace(); }
        this.setOpaque(false);

        setLayout(new BorderLayout());
        JPanel content = new JPanel();
        content.setOpaque(false);
        JButton test = new JButton("test");
        content.add(test);
        content.add(Box.createVerticalStrut(100));
        add(content, BorderLayout.CENTER);
        this.setBorder(null);
    }

    @Override
    public void paint(Graphics g) {
        int tileWidth = BG.getWidth(null);
        int tileHeight = BG.getHeight(null);

        for (int y = 0; y < getHeight(); y += tileHeight)
            for (int x = 0; x < getWidth(); x += tileWidth)
                g.drawImage(BG, x, y, this);

        super.paint(g);

    }

}