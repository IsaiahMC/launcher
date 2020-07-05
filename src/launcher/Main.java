package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import jthemes.StyledJFrame;
import jthemes.ThemeUtils;

import launcher.api.GameLaunchAPI;
import launcher.panels.BottomPanel;
import launcher.panels.LauncherLog;
import launcher.panels.LoginPanel;
import launcher.panels.MainPanel;
import launcher.panels.ModsPanel;
import launcher.ui.LoginTheme;
import launcher.ui.NormalTheme;
import launcher.ui.BetterTabUI;
import launcher.ui.Colors;

public class Main {

    public static final String VERSION = "1.0";

    public static File DIR = new File(new File(System.getProperty("user.home")), ".openlauncher");
    public static StyledJFrame f;

    public static void main(String[] args) {
        LauncherLog.bind();
        System.out.println("OpenMC Launcher version " + VERSION + " started");
        System.out.println("Copyright (C) 2020 Fungus Software");
        System.out.println("Current time is " + LocalDateTime.now() + "\n");

        System.out.println("Minecraft data folder set to: \"" + Utils.getDataFolder().getAbsolutePath() + "\"");
        System.out.println("Downloading version data...");
        try {
            GameLaunchAPI.downloadVersionData();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println("Downloading version data complete.");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        ThemeUtils.setCurrentTheme(new LoginTheme());
        f = new StyledJFrame();
        f.setTitle("OpenLauncher " + VERSION);
        try {
            f.setIconImage(ImageIO.read( Main.class.getClassLoader().getResourceAsStream("assets/icon.png") ));
        } catch (IOException e) { e.printStackTrace(); }

        boolean login = true;
        File auth = new File(Main.DIR, "user.dat");
        if (auth.exists()) {
            try {
                Auth.setUser(auth);
                login = false;
                Main.afterLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LoginPanel p = new LoginPanel();
        if (login) f.setContentPane(p);
        f.setDefaultCloseOperation(3);
        f.pack();
        f.setSize(new Dimension(900, 570));
        f.setMinimumSize(new Dimension(700,440));
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    public static void showLogin() {
        ThemeUtils.setCurrentTheme(new LoginTheme());
        f.setContentPane(new LoginPanel());
        f.revalidate();
    }

    public static void afterLogin() {
        ThemeUtils.setCurrentTheme(new NormalTheme());
        try {
            new GameProfileManager();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, 1));
        JTabbedPane tabs = new JTabbedPane();

        tabs.setUI(new BetterTabUI());
        tabs.setBorder(null);
        Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
        insets.top = -1;
        insets.set(-1, -1, -1, -1);
        UIManager.put("TabbedPane.contentBorderInsets", insets);
        tabs.setOpaque(true);
        tabs.setBackground(new Color(50,50,50));
        //tabs.setForeground(Color.GREEN);

        tabs.add("Update Log", new MainPanel());
        //tabs.add("Update Log", Browser.get());
        tabs.add("Launcher Log", new LauncherLog());
        tabs.add("Mods", new ModsPanel());
        tabs.setBackgroundAt(0, Color.GREEN);
        p.add(tabs);
        p.add(new BottomPanel());
        p.setBackground(Colors.BOTTOM_PANEL_BACKGROUND);
        f.setContentPane(p);
        f.validate();
    }

}