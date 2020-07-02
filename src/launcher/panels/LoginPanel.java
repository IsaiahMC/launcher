package launcher.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import launcher.Auth;
import launcher.Main;
import launcher.Utils;

public class LoginPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    public Image GRASS_BG, DIRT_BG;
    public Image LOGO;

    public LoginPanel() {
        try {
            this.GRASS_BG = ImageIO.read( this.getClass().getClassLoader().getResourceAsStream("assets/grass.png") );
            this.DIRT_BG = ImageIO.read( this.getClass().getClassLoader().getResourceAsStream("assets/dirt.png") );
            this.LOGO = ImageIO.read( this.getClass().getClassLoader().getResourceAsStream("assets/minecraft_logo.png") );
        } catch (IOException e) { e.printStackTrace(); }
        this.setOpaque(false);

        setLayout(new GridBagLayout());
        JTextField username = new JTextField();
        JPasswordField pass = new JPasswordField();
        JLabel fp = new JLabel("(Forgot Password?)");
        fp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel login_inner = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                super.paint(g);

                // Because JLabels are messed up
                g.drawImage(LOGO, (getWidth() / 2) - (LOGO.getWidth(null) / 2), 20, null);
                g.drawString("Login to your Mojang Account", LOGO.getWidth(null)/4, 32 + LOGO.getHeight(null));
                g.setFont(g.getFont().deriveFont(Font.BOLD));
                g.drawString("Email Address:", username.getX() + 3, username.getY() - 4);
                g.drawString("Password:", pass.getX() + 3, pass.getY() - 4);
                fp.setLocation(getWidth() - (fp.getText().length() * 5), fp.getLocation().y);
                fp.validate();
            }
        };
        login_inner.setLayout(new BoxLayout(login_inner, 1));
        login_inner.setBorder(BorderFactory.createEmptyBorder(54, 15, 10, 15));

        login_inner.add(Box.createVerticalStrut(LOGO.getHeight(null) + 30));
        login_inner.add(username);

        username.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY), 
                BorderFactory.createEmptyBorder(2, 3, 2, 3)));

        login_inner.add(Box.createVerticalStrut(30));

        login_inner.add(pass);
        pass.setBorder(username.getBorder());
        login_inner.add(fp);
        fp.setFont(fp.getFont().deriveFont(9f));
        fp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent m) {
                Utils.browse("https://my.minecraft.net/en-us/password/forgot/");
            }
        });

        login_inner.add(Box.createVerticalStrut(20));
        JPanel buttons  = (JPanel) login_inner.add(new JPanel());

        JButton reg = (JButton) buttons.add(new JButton("Register"));
        reg.setBorder(BorderFactory.createCompoundBorder(reg.getBorder(), BorderFactory.createEmptyBorder(1, 30, 1, 30)));

        JButton login = (JButton) buttons.add(new JButton("Login"));
        login.setBorder(BorderFactory.createCompoundBorder(login.getBorder(), BorderFactory.createEmptyBorder(1, 30, 1, 30)));

        reg.addActionListener(l -> Utils.browse("https://account.mojang.com/register"));
        login.addActionListener(l -> {
            try {
                Auth.setUser(username.getText(), String.valueOf(pass.getPassword()));
            } catch (launcher.Error e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Auth Error", JOptionPane.ERROR_MESSAGE);
                e.printSimpleStackTrace();
                return;
            }

            for (@SuppressWarnings("unused") char c : pass.getPassword()) c = (char) Math.random(); // Stronger Security

            Main.afterLogin();
        });
        JPanel login_border = new JPanel(new BorderLayout());
        login_border.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        login_border.add(login_inner, BorderLayout.CENTER);
        add(login_border);
    }

    @Override
    public void paint(Graphics g) {
        int tileWidth = DIRT_BG.getWidth(null);
        int tileHeight = DIRT_BG.getHeight(null);

        for (int y = 0; y < getHeight(); y += tileHeight)
            for (int x = 0; x < getWidth(); x += tileWidth)
                g.drawImage(y == 0 ? GRASS_BG : DIRT_BG, x, y, this);

        super.paint(g);

    }

}