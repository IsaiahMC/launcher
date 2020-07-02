package launcher.panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import launcher.Utils;

public class ModsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public ModsPanel() {
        JPanel modLoaders = new JPanel();
        modLoaders.setBorder(BorderFactory.createTitledBorder("Installed Mod Loaders"));
        File versions = new File(Utils.getDataFolder(), "versions");
        for (File f : versions.listFiles()) {
            if (!f.getName().contains("loader"))
                continue;
            JLabel title = new JLabel(f.getName() + ",");
            modLoaders.add(title);
        }

        JPanel installed = getInstalledMods(new File(Utils.getDataFolder(), "mods"));
        installed.setBorder(BorderFactory.createTitledBorder("Installed Mods"));
 
        JPanel promoted = new JPanel();
        promoted.add(getPromotedMod("WorldEdit", 0));
        promoted.add(getPromotedMod("Fabric API", 0));
        promoted.add(getPromotedMod("WorldEdit", 0));
        promoted.add(getPromotedMod("OptiFabric", 0));
        promoted.setBorder(BorderFactory.createTitledBorder("Recommended Mods"));

        this.setLayout(new GridLayout(0,1));
        this.add(modLoaders);
        this.add(installed);
        this.add(promoted);
    }

    public JPanel getInstalledMods(File folder) {
        JPanel all = new JPanel();
        for (File f : folder.listFiles()) {
            JPanel p = new JPanel();
            p.setLayout(new GridLayout(0,1));
            p.setBackground(new Color(161, 203, 255));
            JLabel title = (JLabel) p.add(new JLabel(f.getName()));
            title.setFont(title.getFont().deriveFont(16f));
    
            JButton dl = new JButton("Remove Mod");
            p.add(dl);
    
            p.setBorder(BorderFactory.createEmptyBorder(8,14,8,14));
    
            all.add(p);
        }
        return all;
    }

    public JPanel getPromotedMod(String name, int curseId) {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0,1));
        p.setBackground(new Color(161, 203, 255));
        JLabel title = (JLabel) p.add(new JLabel(name));
        title.setFont(title.getFont().deriveFont(16f));

        JButton dl = new JButton("Install Mod");
        p.add(dl);

        p.setBorder(BorderFactory.createEmptyBorder(8,14,8,14));

        return p;
    }

}