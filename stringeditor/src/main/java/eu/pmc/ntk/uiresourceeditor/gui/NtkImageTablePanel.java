package eu.pmc.ntk.uiresourceeditor.gui;

import eu.pmc.ntk.uiresourceeditor.FontTableEditor;
import eu.pmc.ntk.uiresourceeditor.ResourceEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tobi on 15.06.2017.
 */
public class NtkImageTablePanel extends JPanel {

    private FontTableEditor ite;

    public NtkImageTablePanel(ResourceEditor re) {
        ite = new FontTableEditor(re);
        this.setLayout(new BorderLayout());
        JButton lb = new JButton("test");
        lb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ite.loadImages();
            }
        });
        this.add(lb, BorderLayout.SOUTH);

    }


    class NtkImagePanel extends JPanel {

    }

}
