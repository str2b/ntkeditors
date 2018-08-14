package eu.pmc.ntk.uiresourceeditor.gui;

import eu.pmc.ntk.uiresourceeditor.ResourceEditor;
import eu.pmc.ntk.uiresourceeditor.StringTableEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tobi on 14.06.2017.
 */
public class MainFrame extends JFrame {

    private ResourceEditor re = new ResourceEditor();
    private NtkStringTablePanel stp;
    private NtkImageTablePanel itp;

    public MainFrame() {
        this.setSize( 700, 700);
        this.setTitle("NtkRE");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenuBar bar = new JMenuBar();
        this.setJMenuBar(bar);
        JMenu me;
        bar.add(me = new JMenu("File"));
        JMenuItem mi;
        me.add(mi = new JMenuItem("Load raw Ntk firmware"));
        mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                re.loadFirmware();
                stp.toggleButtons();
            }
        });
        me.add(mi = new JMenuItem("Exit"));
        mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JTabbedPane jtp = new JTabbedPane();
        jtp.add("Strings editor", stp = new NtkStringTablePanel(new StringTableEditor(re)));
        jtp.add("Image editor", itp = new NtkImageTablePanel(re));

        this.add(jtp);

        this.pack();
        this.setVisible(true);
    }

    public static void main(String...args) {
        new MainFrame();
    }
}
