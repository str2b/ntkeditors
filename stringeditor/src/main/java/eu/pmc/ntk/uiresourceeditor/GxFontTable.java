package eu.pmc.ntk.uiresourceeditor;

import randomaccess.LERandomAccessByteArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 15.06.2017.
 */
public class GxFontTable extends GxTable {
    public GxFontTable(int gxFontTableAddress, int gxTableAddress, short offset, short count) {
        super(gxTableAddress, offset, count);
        this.gxImageTableAddress = gxImageTableAddress;
    }

    public static final byte[] gxFontTableSignature = {0x47, 0x58, 0x30, 0x01};
    public static final int GX_FONT_TABLE_SIZE = 4;
    private static final int META_INF_SIZE = 4;

    private int gxImageTableAddress;
    private List<NtkFntImage> ntkImages;

    @Override
    public int load(LERandomAccessByteArray r) throws IOException {
        ntkImages = new ArrayList<>(count);
        int i = 0;
        int payloadStart = gxTableAddress + GX_TABLE_SIZE;
        while(i < count) {
            int currentPos = payloadStart + i * 8;
            r.seek(currentPos);
            int offset = r.readInt();
            int size = r.readInt();
            System.out.format("curpos:%08x -> processing %08x %08x", currentPos, offset, size);
            if((offset == 0xFFFFFFFF) && (size == 0x00000000)) {
                System.out.format(" (Skipping...)\n", currentPos);
                i++;
                continue;
            } else {

                System.out.format(" - pl at %08x\n", gxTableAddress + offset);
            }
            //System.out.format("Reading #%d %08x @ %08x\n", i, size, offset);
            if(size > 0xA00) {
                System.out.println("FNTBMP #" + i + " size too big! (" + size + ")");
                return -1;
            }
            byte[] data = new byte[size - META_INF_SIZE];
            int pAddress = gxTableAddress + offset;
            r.seek(pAddress);
            short width = r.readShort();
            short height = r.readShort();
            r.read(data);
            NtkFntImage img = new NtkFntImage(pAddress, offset, size, width, height, data);
            Files.write(Paths.get("G:\\Novatek\\fntdump\\" + pAddress + ".bmp"), img.getBmp());
            System.out.println(img);
            i++;
        }
        return i;
    }
}
