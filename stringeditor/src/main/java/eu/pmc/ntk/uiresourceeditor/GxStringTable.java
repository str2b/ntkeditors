package eu.pmc.ntk.uiresourceeditor;

import randomaccess.LERandomAccessByteArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 14.06.2017.
 */
public class GxStringTable extends GxTable {

    public static final byte[] gxStringTableSignature = {0x47, 0x58, 0x42, 0x01};
    public static final int GX_STR_TABLE_SIZE = 4;

    private int strTableAddr;
    private int payloadStart;
    private List<NtkString> ntkStrings;

    public GxStringTable(int gxTableAddr, int strTableAddr, short offset, short count) {
        super(gxTableAddr, offset, count);
        this.strTableAddr = strTableAddr;
        payloadStart = gxTableAddr + GX_TABLE_SIZE;
    }

    public int load(LERandomAccessByteArray r) throws IOException {
        ntkStrings = new ArrayList<>(count);
        int i = 0;
        while(i < count) {
            r.seek(payloadStart + i * 8);
            int offset = r.readInt();
            int size = r.readInt();
            //System.out.format("Reading #%d %08x @ %08x\n", i, size, offset);
            if(size > 0x100) {
                System.out.println("String #" + i + " size too big! (" + size + ")");
                return -1;
            }
            byte[] data = new byte[size];
            r.seek(gxTableAddress + offset);
            r.read(data);
            NtkString ns = new NtkString(offset, data, gxTableAddress + offset);
            ntkStrings.add(ns);

            i++;
        }
        return i;
    }

    public NtkString getStringAt(int id) {
        if(ntkStrings == null) {
            try {
                return new NtkString(-1, "ERROR=!WTF!?".getBytes("UTF-16LE"), -1);
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
                return null;
            }
        }
        return ntkStrings.get(id);
    }

    public String toString() {
        return String.format(this.getClass().getSimpleName() + "[strTableAddr:%08x, offset:%04x, count:%04x(%d)]", strTableAddr, offset, count, count);
    }


}
