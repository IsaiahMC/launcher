package launcher.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fungus_soft.ui.BetterButton;

import launcher.Auth;
import launcher.GameProfile;
import launcher.GameProfileManager;
import launcher.ui.ModernButton;

import static launcher.ui.Colors.*;

public class BottomPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    public static JComboBox<GameProfile> box;

    public BottomPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(BOTTOM_PANEL_BACKGROUND);
        JPanel p = new JPanel();
        p.setOpaque(false);
        JButton play = (JButton) p.add(new ModernButton("PLAY"));
        play.setPreferredSize(new Dimension(300, 45));
        play.setFont(play.getFont().deriveFont(18f));
        play.addActionListener(l -> GameProfileManager.getCurrentSelectedProfile().launch());
        play.setBackground(PLAY_BUTTON_BACKGROUND);

        this.add(p, BorderLayout.CENTER);

        JPanel left = new JPanel(new GridBagLayout());
        left.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = 2;
        constraints.gridy = 0;
        left.add(new JLabel("Profile: "), constraints);
        left.add(box = new JComboBox<GameProfile>(), constraints);
        for (GameProfile g : GameProfileManager.profiles.values())
            box.addItem(g);

        box.addItemListener(l -> GameProfileManager.current = (GameProfile)l.getItem());
        constraints.gridy++;
        constraints.gridwidth = 2;

        JPanel buttons = new JPanel(new GridLayout(1, 0));
        buttons.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        buttons.add(new JButton("New Profile"));
        buttons.add(new JButton("Edit Profile"));
        for (Component c : buttons.getComponents())
            ((JComponent)c).setOpaque(false);
        buttons.setOpaque(false);
        left.add(buttons, constraints);

        this.add(center(left), BorderLayout.WEST);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, 1));
        right.add(center(new JLabel("<html>Logged in as <b>" + Auth.getName() + "</b></html>")));
        JButton logout = new BetterButton("Logout");
        //logout.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(2,2,2,2));
        logout.setBackground(Color.GRAY);
        logout.setSize(new Dimension(50, 5));
        right.add(center(logout));
        logout.addActionListener(l -> Auth.logout());
        right.setOpaque(false);
        this.add(center(right), BorderLayout.EAST);
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        this.setBorder(BorderFactory.createEmptyBorder(0, 5, 4, 5));
    }

    private JComponent center(JComponent p) {
        JPanel cp = new JPanel();
        cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS));

        ((JComponent)cp.add(Box.createHorizontalGlue())).setOpaque(false);
        cp.add(p);
        ((JComponent)cp.add(Box.createHorizontalGlue())).setOpaque(false);
        cp.setOpaque(false);
        return cp;
    }

}