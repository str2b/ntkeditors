package eu.pmc.ntktool;

import randomaccess.IRandomAccess;
import randomaccess.LERandomAccessByteArray;
import randomaccess.RandomAccessByteArray;

import java.io.IOException;

public class PartitionSeparator {

    private static byte[] padding = {
            (byte) 0x90, (byte) 0xbe, 0x27, (byte) 0x88, (byte) 0xcd, 0x36, (byte) 0xc2, 0x4f, (byte) 0xa9, (byte) 0x87,
            0x73, (byte) 0xa8, 0x48, 0x4e, (byte) 0x84, (byte) 0xb1, 0x28, 0x00, 0x00, 0x00, 0x7e, 0x3d, 0x00, 0x00, 0x01,
            0x00, 0x00, 0x00, 0x0c, 0x12, 0x29, 0x00, 0x26, (byte) 0xb2, 0x11, 0x00, 0x01, 0x00, 0x00, 0x00
    };

    public static byte[] getSeparator(int address, int length) throws IOException {
        byte[] modifiedPadding;
        LERandomAccessByteArray rab = new LERandomAccessByteArray(modifiedPadding = padding.clone());
        rab.seek(0x1c);
        rab.writeInt(address + padding.length);
        rab.seek(0x20);
        rab.writeInt(length);
        rab.close();
        return modifiedPadding;
    }
}
