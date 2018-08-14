package eu.pmc.ntktool.gui;

import eu.pmc.ntktool.BnManager;
import eu.pmc.ntktool.StringUtils;
import eu.pmc.ntktool.natives.NativeReturnCode;
import eu.pmc.ntktool.natives.wrapper.BfcMode;
import eu.pmc.ntktool.natives.wrapper.BfcProcessor;
import eu.pmc.ntktool.natives.NativeReturn;
import eu.pmc.ntktool.natives.wrapper.NtkcalcProcessor;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

enum ManualType {
    COMPRESS("Pack firmware (+ calculate checksum)"), EXTRACT("Unpack firmware");

    String name;

    ManualType(String s) {
        this.name = s;
    }

    public String toString() {
        return name;
    }
}

/**
 * Created by Tobi on 03.06.2017.
 */
public class ManualBfcPanel extends JPanel {


    private JTextArea logArea;
    private JFrame parent;

    public ManualBfcPanel(JFrame parent) {
        this.parent = parent;
        this.setLayout(new BorderLayout());

        JPanel pExtract = new ManualOperationComponent(ManualType.EXTRACT);
        JPanel pComp = new ManualOperationComponent(ManualType.COMPRESS);
        JPanel pLog = new JPanel();

        pLog.setBorder(BorderFactory.createTitledBorder("Log"));
        logArea = new JTextArea(7, 60);
        logArea.setEditable(false);
        logArea.setFont(new Font("monospaced", Font.PLAIN, 11));
        ((DefaultCaret) logArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        pLog.add(scrollPane);

        this.add(pExtract, BorderLayout.NORTH);
        this.add(pComp, BorderLayout.CENTER);
        this.add(pLog, BorderLayout.SOUTH);

    }

    private class ManualOperationComponent extends JPanel {

        private JTextField txtFile;
        private JTextField txtAdditional;
        private File selFile;
        private BfcModeButtonGroup bg;
        private JButton runBtn;
        private ImageIcon imgLoad;

        public ManualOperationComponent(ManualType mt) {
            this.setBorder(BorderFactory.createTitledBorder(mt.toString()));
            this.setLayout(new BorderLayout());

            imgLoad = new ImageIcon(this.getClass().getClassLoader().getResource("38-1.gif"));

            txtFile = new JTextField("", 30);
            txtFile.setEditable(false);
            this.add(txtFile);

            JButton selBtn;
            this.add(selBtn = new JButton("Open"), BorderLayout.EAST);
            selBtn.addActionListener(new SelectListener());

            JPanel modes = new JPanel();
            modes.setLayout(new FlowLayout());
            JRadioButton rb1 = null, rb2 = null;

            txtAdditional = new JTextField("00000000", 6);
            JLabel jlAdditional = null;
            switch (mt) {
                case EXTRACT:
                    rb1 = new JRadioButton("d");
                    rb1.setModel(new BfcModeButtonModel(BfcMode.D));
                    rb2 = new JRadioButton("x");
                    rb2.setModel(new BfcModeButtonModel(BfcMode.X));
                    jlAdditional = new JLabel(" Offset [hex]");
                    break;
                case COMPRESS:
                    rb1 = new JRadioButton("c");
                    rb1.setModel(new BfcModeButtonModel(BfcMode.C));
                    rb2 = new JRadioButton("p");
                    rb2.setModel(new BfcModeButtonModel(BfcMode.P));
                    jlAdditional = new JLabel("Baseval [hex]");
                    break;
                default:
                    System.err.println("Error!?");
            }
            bg = new BfcModeButtonGroup();
            bg.add(rb1);
            bg.add(rb2);
            rb1.setSelected(true);
            modes.add(new JLabel("Mode:"));
            modes.add(rb1);
            modes.add(rb2);
            modes.add(jlAdditional);
            modes.add(txtAdditional);
            modes.add(runBtn = new JButton("Run"));
            runBtn.addActionListener(new RunListener());
            runBtn.setEnabled(false);
            add(modes, BorderLayout.SOUTH);
        }

        private int getIntValue(JTextField t) {
            try {
                return Integer.parseInt(t.getText(), 16);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }

        public NativeReturn printNrLog(NativeReturn nr) {
            logArea.append(nr.getStdout() + "\n");
            if (nr.getStderr().length() > 1) {
                logArea.append("+++ NATIVE ERROR +++\n");
                logArea.append(nr.getStderr() + "\n");
            }
            return nr;
        }

        private void setBtnInfo(JButton btn, String txt, boolean enabled, ImageIcon icon) {
            btn.setEnabled(enabled);
            btn.setText(txt);
            btn.setIcon(icon);
        }

        class SelectListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(BnManager.getLastFile());
                int retVal = chooser.showDialog(null, "Open");

                if (retVal == JFileChooser.APPROVE_OPTION) {
                    selFile = chooser.getSelectedFile();
                    BnManager.setLastFile(selFile);
                    txtFile.setText(selFile.getAbsolutePath());
                    runBtn.setEnabled(true);
                }
            }
        }

        class RunListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Object>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            setBtnInfo(runBtn, "Running...", false, imgLoad);
                            NativeReturn nr = null;
                            String absFile = selFile.getAbsolutePath();
                            switch (bg.getValue()) {
                                case D:
                                    setBtnInfo(runBtn, "Unpacking...", false, imgLoad);

                                    printNrLog(BfcProcessor.bfc(bg.getValue(), absFile,
                                            StringUtils.removeExtension(absFile) + String.format("@%08x.rbn", getIntValue(txtAdditional)), getIntValue(txtAdditional)));
                                    break;
                                case X:
                                    setBtnInfo(runBtn, "Unpacking...", false, imgLoad);
                                    printNrLog(BfcProcessor.bfc(bg.getValue(), absFile,
                                            StringUtils.removeExtension(absFile) + ".rbn"));
                                    break;
                                case C:
                                    setBtnInfo(runBtn, "Calculating checksum...", false, imgLoad);
                                    nr = printNrLog(NtkcalcProcessor.cw(absFile, getIntValue(txtAdditional)));
                                    if (!nr.getNrc().equals(NativeReturnCode.EXEC_OK)) {
                                        setBtnInfo(runBtn, "Run", true, null);
                                        break;
                                    }
                                case P:
                                    setBtnInfo(runBtn, "BFC Compressing...", false, imgLoad);
                                    nr = printNrLog(BfcProcessor.bfc(bg.getValue(), absFile, StringUtils.removeExtension(absFile) + ".bcl"));
                                    if (!nr.getNrc().equals(NativeReturnCode.EXEC_OK)) {
                                        setBtnInfo(runBtn, "Run", true, null);
                                        break;
                                    }
                                    setBtnInfo(runBtn, "Calculating checksum...", false, imgLoad);
                                    printNrLog(NtkcalcProcessor.cw(StringUtils.removeExtension(absFile) + ".bcl", getIntValue(txtAdditional)));
                                    break;
                            }

                        } catch (IOException e1) {
                            logArea.append("+++ JAVA ERROR +++\n");
                            logArea.append(e1.toString() + "\n");
                        }

                        setBtnInfo(runBtn, "Run", true, null);
                        return null;
                    }
                }.execute();
            }
        }

    }


}
