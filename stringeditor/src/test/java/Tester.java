import eu.pmc.ntk.uiresourceeditor.ByteUtil;
import eu.pmc.ntk.uiresourceeditor.GxFontTable;
import eu.pmc.ntk.uiresourceeditor.GxTable;
import eu.pmc.ntk.uiresourceeditor.NtkFntImage;
import org.junit.jupiter.api.Test;
import randomaccess.LERandomAccessByteArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tobi on 16.06.2017.
 */
public class Tester {

    @Test
    void byteSplitTest() {
        byte test = (byte) 0xAF;
        byte[] ret = ByteUtil.split(test, 7);
        System.out.format("%08x -%d> %08x:%08x\n", test, 4, ret[0], ret[1]);
    }

    @Test
    public void findAndExportBmpAt() throws IOException {
        byte[] fwbytes = Files.readAllBytes(Paths.get("G:\\Novatek\\0906_CAM\\FIRMWARE0527@00000000.rbn"));
        try {
            List<NtkFntImage> gxits = new LinkedList<>();

            LERandomAccessByteArray r = new LERandomAccessByteArray(fwbytes);
            Pattern toFind = Pattern.compile(new String(GxFontTable.gxFontTableSignature, "ISO-8859-1"));
            Matcher m = toFind.matcher(new String(fwbytes, "ISO-8859-1"));


            while (m.find()) {
                int position = m.start();
                r.seek(position + GxFontTable.GX_FONT_TABLE_SIZE);
                //check if signature bytes actually belong to GxStringTable
                if (r.readInt() == GxTable.getSigAsBeInt(GxTable.gxTableSignature)) {
                    short offset = r.readShort();
                    short count = r.readShort();
                    System.out.println("GxFontTable: p:" + position + " o:" + offset + " c:" + count);
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

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private String toBinaryString(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }


    @Test
    void byteToInt() {

        byte bytes[] =  {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xF0, (byte) 0x80, (byte) 0x1F, (byte) 0x98, (byte) 0xC1, (byte) 0x39, (byte) 0x0C, (byte) 0xC3, (byte) 0x30, (byte) 0x0C, (byte) 0xC3, (byte) 0x30, (byte) 0x0C,
                (byte) 0xC3, (byte) 0x30, (byte) 0x0C, (byte) 0xC3, (byte) 0x30, (byte) 0x0C, (byte) 0xC3, (byte) 0x30, (byte) 0x0C, (byte) 0xC3, (byte) 0x39, (byte) 0x98, (byte) 0x81, (byte) 0x1F, (byte) 0xF0, (byte) 0x00,
                (byte) 0x00};
        for (int i = 0; i < bytes.length; i++) {
            System.out.format("%s\n", (toBinaryString(ByteUtil.mirror(bytes[i]))));
        }

    }
}
