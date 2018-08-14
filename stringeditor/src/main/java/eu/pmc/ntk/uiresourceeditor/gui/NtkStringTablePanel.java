package eu.pmc.ntk.uiresourceeditor.gui;

import eu.pmc.ntk.uiresourceeditor.GxStringTable;
import eu.pmc.ntk.uiresourceeditor.ResourceEditor;
import eu.pmc.ntk.uiresourceeditor.StringTableEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Tobi on 14.06.2017.
 */
public class NtkStringTablePanel extends JPanel {

    private StringTableEditor se;

    class LoadListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            List<GxStringTable> t = se.loadStrings();
            System.out.println(t);
            NtkStringTableModel stm = new NtkStringTableModel(t);
            jt.setModel(stm);
        }
    }

    JScrollPane tablePane;
    JTable jt;
    JButton lb, sb;

    public NtkStringTablePanel(StringTableEditor se) {
        this.se = se;
        this.setLayout(new BorderLayout());
        tablePane = new JScrollPane(jt = new JTable());
        tablePane.setPreferredSize(new Dimension(700, 600));
        this.add(tablePane);

        jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jt.setAutoCreateRowSorter(true);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        lb = new JButton("Load table from firmware");
        lb.addActionListener(new LoadListener());
        bottomPanel.add(lb);

        sb = new JButton("Save modifications");
        bottomPanel.add(sb);

        this.add(bottomPanel, BorderLayout.SOUTH);
        toggleButtons(lb, sb);
    }

    public void toggleButtons() {
        toggleButtons(lb, sb);
    }

    private void toggleButtons(JButton... btns) {
        for (JButton b : btns) {
            b.setEnabled(!b.isEnabled());
        }
    }


}
