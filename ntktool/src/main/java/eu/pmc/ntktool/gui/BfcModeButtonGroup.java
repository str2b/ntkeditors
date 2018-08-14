package eu.pmc.ntktool.gui;

import eu.pmc.ntktool.natives.wrapper.BfcMode;

import javax.swing.*;

/**
 * Created by Tobi on 03.06.2017.
 */
public class BfcModeButtonGroup extends ButtonGroup {
    public BfcMode getValue() {
        BfcModeButtonModel model = (BfcModeButtonModel)getSelection();
        return model.getValue();
    }

    public void setMode(BfcMode m) {
        for (AbstractButton b : buttons) {
            BfcModeButtonModel model = (BfcModeButtonModel)b.getModel();
            if(model.getValue() == m) b.setSelected(true);
        }
    }

}
