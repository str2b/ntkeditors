package eu.pmc.ntktool.gui;

import eu.pmc.ntktool.natives.wrapper.BfcMode;

import javax.swing.*;

/**
 * Created by Tobi on 03.06.2017.
 */
public class BfcModeButtonModel extends JToggleButton.ToggleButtonModel {
    private BfcMode value;

    public BfcModeButtonModel(BfcMode m) {
        this.value = m;
    }

    public BfcMode getValue() {
        return value;
    }
}
