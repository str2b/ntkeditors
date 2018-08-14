package eu.pmc.ntktool.gui;

import eu.pmc.ntktool.BnManager;
import eu.pmc.ntktool.PartitionSeparator;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PartitionMergePanel extends JPanel {

    private FileChooseComponent fc1, fc2;
    private JButton jbMerge;
    private JTextField mergeName;

    public PartitionMergePanel() {
        this.setBorder(BorderFactory.createTitledBorder("Partition merge"));
        this.setLayout(new BorderLayout());
        JPanel pFile1 = new JPanel();
        pFile1.setLayout(new BorderLayout());
        JPanel pFile2 = new JPanel();
        pFile2.setLayout(new BorderLayout());
        pFile1.add(fc1 = new FileChooseComponent("Partition 0"), BorderLayout.NORTH);
        pFile1.add(fc2 = new FileChooseComponent("Partition 1"), BorderLayout.CENTER);
        pFile2.add(jbMerge = new JButton(" Merge "), BorderLayout.EAST);
        jbMerge.setEnabled(false);
        pFile2.add(mergeName = new JTextField("merged.bin"), BorderLayout.SOUTH);

        this.add(pFile1, BorderLayout.CENTER);
        this.add(pFile2, BorderLayout.EAST);

        jbMerge.addActionListener(new RunListener());
    }

    private class FileChooseComponent extends JPanel {

        private JTextField txtFile;
        private File selFile;

        private File getSelFile() {
            return selFile;
        }

        public FileChooseComponent(String title) {
            this.setBorder(BorderFactory.createTitledBorder(title));
            this.setLayout(new BorderLayout());

            txtFile = new JTextField("", 30);
            txtFile.setEditable(false);
            this.add(txtFile);

            JButton selBtn;
            this.add(selBtn = new JButton("Open"), BorderLayout.EAST);

            selBtn.addActionListener(new SelectListener());

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
                    if(fc1.getSelFile() != null && fc2.getSelFile() != null) {
                        jbMerge.setEnabled(true);
                    }
                }
            }
        }


    }

    class RunListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new SwingWorker<Void, Object>() {

                @Override
                protected Void doInBackground() {
                    System.out.println("Merging files....");
                    File fp1 = fc1.getSelFile();
                    File fp2 = fc2.getSelFile();
                    byte[] partition1;
                    byte[] partition2;
                    System.out.println("p0");

                    try {
                        RandomAccessFile raf = new RandomAccessFile(fp1, "r");
                        partition1 = new byte[(int) raf.getChannel().size()];
                        raf.read(partition1);
                        raf.close();
                        System.out.println("p1");

                        raf = new RandomAccessFile(fp2, "r");
                        partition2 = new byte[(int) raf.getChannel().size()];
                        raf.read(partition2);
                        raf.close();
                        System.out.println("p2");

                        File out = new File(fp1.getParentFile(), mergeName.getText());
                        System.out.println(out);
                        raf = new RandomAccessFile(out, "rw");
                        raf.write(partition1);
                        raf.write(PartitionSeparator.getSeparator(partition1.length, partition2.length));
                        raf.write(partition2);
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }
    }


}
