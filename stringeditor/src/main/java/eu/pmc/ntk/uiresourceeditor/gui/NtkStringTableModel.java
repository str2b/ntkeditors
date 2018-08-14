package eu.pmc.ntk.uiresourceeditor.gui;

import eu.pmc.ntk.uiresourceeditor.GxStringTable;
import eu.pmc.ntk.uiresourceeditor.NtkString;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Created by Tobi on 14.06.2017.
 */
public class NtkStringTableModel extends DefaultTableModel {

    private NtkStringTableModel() {
        System.out.println("ERRROROROROOR");
    }

    public NtkStringTableModel(List<GxStringTable> gxsts) {
        this.gxs = gxsts;
    }

    private List<GxStringTable> gxs;


    @Override
    public String getColumnName(int col) {
        return col == 0 ? "ID" : "Language #" + col;
    }

    @Override
    public int getRowCount() {
        if(gxs == null) {
            return 0;
        }
        if (getColumnCount() > 0)
            return gxs.get(0).getCount();
        else
            return 0;
    }

    @Override
    public int getColumnCount() {
        if(gxs == null) {
            return 0;
        }
        return 1 + gxs.size();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 0 ? false : true;
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return String.class;
    }

    @Override
    public void setValueAt(Object aValue, int row, int col) {
        String newData = aValue.toString();
        gxs.get(col - 1).getStringAt(row).setString(newData);
        this.fireTableCellUpdated(row, col);
    }

    @Override
    public Object getValueAt(int row, int col) {
        if(col == 0) {
            return String.format("%04x", row);
        }
        NtkString ns = gxs.get(col - 1).getStringAt(row);
        return ns.getString();
    }

}
