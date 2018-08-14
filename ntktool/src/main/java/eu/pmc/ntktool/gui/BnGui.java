package eu.pmc.ntktool.gui;

import eu.pmc.ntktool.*;
import eu.pmc.ntktool.natives.NativeBridge;
import eu.pmc.ntktool.natives.NativeReturnCode;
import eu.pmc.ntktool.natives.wrapper.BfcProcessor;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * Created by Tobi on 02.06.2017.
 */
public class BnGui {


    private JFrame f;
    private JPanel jpRight;
    private JPanel jpLeft;
    private static final String appName = "NtkTool (BnGui)";
    private static final String version = "v0.4.1";


    public BnGui() {
        initCheck();
        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setTitle(appName + " " + version + "   [" + "Commercial use is prohibited!" + "]" );

        jpRight = new JPanel();
        jpRight.setBorder(BorderFactory.createTitledBorder("Quick unpack"));
        jpRight.add(new QuickBfcPanel(f));

        jpLeft = new JPanel();
        jpLeft.setBorder(BorderFactory.createTitledBorder("Manual operation"));
        jpLeft.add(new ManualBfcPanel(f));
        f.add(jpLeft, BorderLayout.EAST);

        f.add(jpRight, BorderLayout.WEST);
        f.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.CENTER);


        f.add(new PartitionMergePanel(), BorderLayout.SOUTH);

        JMenuBar jBar = new JMenuBar();

        JMenu jMenu = new JMenu("Tools");
        jMenu.setEnabled(false);
        jBar.add(jMenu);

        jMenu = new JMenu("?");
        JMenuItem jmi;
        jMenu.add(jmi = new JMenuItem("Donate"));
        jmi.addActionListener(e -> {
            try {
                NetworkUtil.openWebpage(new URL("http://dc.p-mc.eu/donate"));
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        });
        jMenu.add(jmi = new JMenuItem("About"));
        jmi.addActionListener(e -> {
            JOptionPane.showMessageDialog(f,
                    String.format("<html><h2><b>%s %s</b></h2>"
                            + "<section>Thanks to BCHobbyist for testing!</section><br/><br/>"
                            + "<section><b>Bundled binaries:</b><ul><li>%s</li><li>%s</li></ul></section>"
                            + "<section><b>Released for:</b><ul><li>DASHCAMTALK</li><li>GoPrawn</li></ul></section>"
                            + "<section><b>Author:</b> Tobi@s</section></html>", appName, version, "bfc4ntk v3.3", "ntkcalc v0.7"),
                    "About", JOptionPane.INFORMATION_MESSAGE);
        });
        jBar.add(jMenu);

        f.setJMenuBar(jBar);

        f.pack();
        f.setVisible(true);
    }

    private void initCheck() {
        System.out.println(System.getProperty("os.name").toLowerCase(Locale.ENGLISH));
        new Thread(() -> {
            if(!NativeBridge.sinitSetup().equals(NativeReturnCode.NATIVE_EXPORT_OK)) {
                JOptionPane.showMessageDialog(null, String.format("<html><h3>This application is a wrapper application for bfc4ntk and ntkcalc</h3>" +
                        "<p>Therefore these native executables are required to run this software.</p>" +
                        "<br />" +
                        "<h3>Somehow this application was unable to extract the necessary files to its directory.</h3>" +
                        "<p>Make sure you have write permissions in the following directory:</p>" +
                        "<p><code>%s</code></p>" +
                        "<br />" +
                        "<p>If you still have issues with the application, please report it in our forums:</p>" +
                        "<p>http://www.goprawn.com</p>" +
                        "<p>http://dashcamtalk.com/forum</p></html>", System.getProperty("user.dir")), "Missing dependencies", JOptionPane.WARNING_MESSAGE);
                System.exit(-1);
            }
            System.out.println();
        }).start();

    }

    public static void main(String... args) throws Exception {
        new BnGui();
    }
}
