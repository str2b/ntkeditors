package eu.pmc.ntk.uiresourceeditor;

import randomaccess.LERandomAccessByteArray;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tobi on 15.06.2017.
 */
public class StringTableEditor {
    private ResourceEditor re;
    private List<GxStringTable> gxsts;

    public StringTableEditor(ResourceEditor re) {
        this.re = re;
    }


    public List<GxStringTable> loadStrings() {
        if (re.getFirmwareBytes() == null) {
            throw new UnsupportedOperationException("No firmware loaded, yet!");
        }

        try {
            gxsts = new LinkedList<>();

            LERandomAccessByteArray r = new LERandomAccessByteArray(re.getFirmwareBytes());
            Pattern toFind = Pattern.compile(new String(GxStringTable.gxStringTableSignature, "ISO-8859-1"));
            Matcher m = toFind.matcher(new String(re.getFirmwareBytes(), "ISO-8859-1"));


            while (m.find()) {
                int position = m.start();
                r.seek(position + GxStringTable.GX_STR_TABLE_SIZE);
                //check if signature bytes actually belong to GxStringTable
                if (r.readInt() == GxTable.getSigAsBeInt(GxTable.gxTableSignature)) {
                    short offset = r.readShort();
                    short count = r.readShort();
                    int strTableAddr = position;
                    int gxTableAddr = strTableAddr + GxStringTable.GX_STR_TABLE_SIZE;
                    GxStringTable gst = new GxStringTable(gxTableAddr, strTableAddr, offset, count);
                    gst.load(r);
                    gxsts.add(gst);
                    System.out.println("Loaded " + gst);
                }
            }
            return gxsts;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }
}

