package eu.pmc.ntktool.gui;

import eu.pmc.ntktool.*;
import eu.pmc.ntktool.natives.wrapper.BfcMode;
import eu.pmc.ntktool.natives.wrapper.BfcProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tobi on 03.06.2017.
 */
public class QuickBfcPanel extends JPanel {

    private BnManager fa = new BnManager();

    private JLabel jlType, jlCount;
    private JTextField jtType, jtCount;
    private JPanel jpParts;
    private JFrame parent;

    public QuickBfcPanel(JFrame parent) {
        this.parent = parent;
        this.setLayout(new BorderLayout());

        JPanel jpGeneral = new JPanel();
        jpGeneral.setLayout(new FlowLayout());
        this.add(jpGeneral, BorderLayout.NORTH);

        jlType = new JLabel("Firmware type:");
        jtType = new JTextField("  -  ");
        jtType.setEditable(false);
        jlCount = new JLabel("BCL partitions:");
        jtCount = new JTextField("  -  ");
        jtCount.setEditable(false);

        jpGeneral.add(jlType);
        jpGeneral.add(jtType);
        jpGeneral.add(jlCount);
        jpGeneral.add(jtCount);


        JButton qLoadBtn;
        this.add(qLoadBtn = new JButton("Load firmware"), BorderLayout.SOUTH);
        qLoadBtn.addActionListener(new OpenListener());

    }


    private void addFirmwareInfo(FirmwareInfo fi) {
        jtType.setText(fi.getType().toString());
        jtCount.setText(fi.getBclCount() + "");

    }

    private void addPartitionInfo(Partition[] partitionInfo) {
        if(jpParts != null) {
            this.remove(jpParts);
        }
        jpParts = new JPanel();
        jpParts.setLayout(new GridLayout(0, 1));
        this.add(jpParts, BorderLayout.CENTER);
        for(Partition p : partitionInfo) {
            jpParts.add(new PartitionInfoPanel(p));
        }
    }


    class OpenListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser(BnManager.getLastFile());
            int retVal = chooser.showDialog(null, "Load compressed Ntk firmware");
            File fwFile;

            if (retVal == JFileChooser.APPROVE_OPTION) {
                fwFile = chooser.getSelectedFile();
                BnManager.setLastFile(fwFile);
                try {
                    FirmwareInfo fi = fa.loadFirmware(fwFile);
                    addFirmwareInfo(fi);
                    addPartitionInfo(fa.getPartitionInfo());
                    parent.pack();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(QuickBfcPanel.this, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        }
    }


    class PartitionInfoPanel extends JPanel {

        private Partition pi;

        public PartitionInfoPanel(Partition p) {
            this.pi = p;
            this.setBorder(BorderFactory.createTitledBorder("Partition " + pi.getNum()));
            this.setLayout(new BorderLayout());
            if (!(pi.getNum() == 0)) {
                this.setEnabled(false);
            }
            JPanel jpInner = new JPanel();
            JPanel jpBtns = new JPanel();
            jpInner.setLayout(new GridLayout(0, 2));


            JLabel lbl;
            JTextField txt;
            jpInner.add(lbl = new JLabel("Offset:"));
            jpInner.add(txt = new JTextField(String.format("%08x", pi.getOffset())));
            txt.setEditable(false);
            jpInner.add(lbl = new JLabel("Compressed size:"));
            jpInner.add(txt = new JTextField(String.format("%08x", pi.getCompressedSize())));
            txt.setEditable(false);
            jpInner.add(lbl = new JLabel("Extracted size:"));
            jpInner.add(txt = new JTextField(String.format("%08x", pi.getExtractedSize())));
            txt.setEditable(false);
            jpInner.add(lbl = new JLabel("Algorithm:"));
            jpInner.add(txt = new JTextField(pi.getAlgoText() + " (" + pi.getAlgo() + ")"));
            txt.setEditable(false);

            jpBtns.setLayout(new FlowLayout());
            JButton jb;
            jpBtns.add(jb = new JButton("Unpack"));
            jb.addActionListener(new UnpackListener());

            this.add(jpInner, BorderLayout.CENTER);
            this.add(jpBtns, BorderLayout.SOUTH);
        }

        class UnpackListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (pi.getType().equals(FirmwareType.PARTCOMP)) {
                        BfcProcessor.bfc(BfcMode.X, pi.getFwFile().getAbsolutePath(), StringUtils.removeExtension(pi.getFwFile().getAbsolutePath()) + ".rbn");
                    } else {
                        BfcProcessor.bfc(BfcMode.D, pi.getFwFile().getAbsolutePath(), StringUtils.removeExtension(pi.getFwFile().getAbsolutePath()) + String.format("@%08x.rbn", pi.getOffset()), pi.getOffset());
                    }
                } catch (IOException e1) {
                    System.err.println(e1);
                    JOptionPane.showMessageDialog(null, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        }

    }




}
