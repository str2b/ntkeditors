package eu.pmc.ntk.uiresourceeditor;

import randomaccess.LERandomAccessByteArray;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tobi on 15.06.2017.
 */
public class FontTableEditor {

    private ResourceEditor re;

    public FontTableEditor(ResourceEditor re) {
        this.re = re;
    }

    public List<NtkFntImage> loadImages() {
        if (re.getFirmwareBytes() == null) {
            throw new UnsupportedOperationException("No firmware loaded, yet!");
        }
        try {
            List <NtkFntImage> gxits = new LinkedList<>();

            LERandomAccessByteArray r = new LERandomAccessByteArray(re.getFirmwareBytes());
            Pattern toFind = Pattern.compile(new String(GxFontTable.gxFontTableSignature, "ISO-8859-1"));
            Matcher m = toFind.matcher(new String(re.getFirmwareBytes(), "ISO-8859-1"));


            while (m.find()) {
                int position = m.start();
                r.seek(position + GxFontTable.GX_FONT_TABLE_SIZE);
                //check if signature bytes actually belong to GxStringTable
                if (r.readInt() == GxTable.getSigAsBeInt(GxTable.gxTableSignature)) {
                    short offset = r.readShort();
                    short count = r.readShort();
                    System.out.println("GxFontTable: p:" +position + " o:" + offset + " c:" + count);
                    int fntTableAddr = position;
                    int gxTableAddr = fntTableAddr + GxFontTable.GX_FONT_TABLE_SIZE;
                    GxFontTable gft = new GxFontTable(fntTableAddr, gxTableAddr, offset, count);
                    gft.load(r);
                    //GxStringTable gst = new GxStringTable(gxTableAddr, strTableAddr, offset, count);
                    //gst.load(r);
                    //gxits.add(gst);
                    //System.out.println("Loaded " + gst);
                }
            }
            return gxits;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
