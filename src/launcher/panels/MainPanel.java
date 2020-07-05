package launcher.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.fungus_soft.ui.ModernScrollPane;

public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public Image BG, BEDROCK;
    private JPanel right;

    public MainPanel() {
        try {
            this.BG = ImageIO.read( this.getClass().getClassLoader().getResourceAsStream("assets/stone.png") ).getScaledInstance(48, 48, 0);
            this.BEDROCK = ImageIO.read( this.getClass().getClassLoader().getResourceAsStream("assets/bedrock.png") ).getScaledInstance(48, 48, 0);
        } catch (IOException e) { e.printStackTrace(); }
        this.setOpaque(false);

        setLayout(new BorderLayout());
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(24,24,16,24));

        right = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                int tileWidth = BG.getWidth(null);
                int tileHeight = BG.getHeight(null);

                for (int y = 0; y < getHeight(); y += tileHeight)
                    for (int x = 0; x < getWidth(); x += tileWidth)
                        g.drawImage(BEDROCK, x, y, this);

                super.paint(g);
            }
        };
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(new JLabel("Links:")).setForeground(Color.LIGHT_GRAY);;

        JPanel links = new JPanel();
        links.setLayout(new BoxLayout(links, BoxLayout.Y_AXIS));
        links.add(new LinkLabel("Minecraft.net", "https://minecraft.net/"));
        links.add(new LinkLabel("Minecraft on Discord", "https://discord.gg/minecraft"));
        links.add(new LinkLabel("Mojang Support", "https://twitter.com/mojangsupport"));
        links.add(new LinkLabel("Minecraft Wiki", "https://minecraftwiki.net/"));
        right.add(links);
        right.setBorder(BorderFactory.createEmptyBorder(16,16,8,8));
        links.setOpaque(false);
        right.setBackground(new Color(50,50,50));

        for (Component c : right.getComponents())
            c.setFont(c.getFont().deriveFont(16f));

        JLabel title = new JLabel("Update News");
        title.setForeground(new Color(220,220,220));
        title.setFont(title.getFont().deriveFont(32f));
        content.add(title);
        content.add(Box.createVerticalStrut(32));

        InputStream is = getClass().getClassLoader().getResourceAsStream("assets/updatenotes.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    content.add(Box.createVerticalStrut(18));
                    continue;
                }
                boolean isUrl = line.startsWith("[URL=");
                JLabel label = isUrl ? new LinkLabel(line.substring(line.indexOf("]")+1), line.substring(line.indexOf("=")+1, line.indexOf("]"))) : new JLabel("<html>- " + line + "</html>");
                label.setFont(label.getFont().deriveFont((float)(isUrl ? 18 : 14)));
                if (!isUrl) label.setForeground(new Color(230, 230, 230));
                label.setBorder(BorderFactory.createEmptyBorder(2, isUrl ? 8 : 32, 2, 2));
                content.add(label);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        right.setOpaque(false);
        ModernScrollPane sc = new ModernScrollPane(content);
        sc.getViewport().setOpaque(false);
        sc.setOpaque(false);
        sc.setBorder(BorderFactory.createEmptyBorder(0,0,0,8));
        add(sc, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);
        this.setBorder(null);
    }

    public class LinkLabel extends JLabel {
        private static final long serialVersionUID = 1L;

        public LinkLabel(String text, String url) {
            super(text);
            this.setForeground(new Color(130, 150, 255));
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    try {
                        Desktop.getDesktop().browse(new URL(url).toURI());
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }

            });
            Font font = getFont();
            Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            setBorder(BorderFactory.createEmptyBorder(2,8,2,2));
            setFont(font.deriveFont(attributes).deriveFont(14f));
        }
    } 

    @Override
    public void paint(Graphics g) {
        int tileWidth = BG.getWidth(null);
        int tileHeight = BG.getHeight(null);

        for (int y = 0; y < getHeight(); y += tileHeight)
            for (int x = 0; x < getWidth(); x += tileWidth) {
                if (x >= right.getX()) break;
                g.drawImage(BG, x, y, this);
            }

        super.paint(g);
    }

}