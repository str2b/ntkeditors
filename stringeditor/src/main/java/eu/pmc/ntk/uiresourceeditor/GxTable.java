package eu.pmc.ntk.uiresourceeditor;

import randomaccess.LERandomAccessByteArray;

import java.io.IOException;

/**
 * Created by Tobi on 14.06.2017.
 */
public abstract class GxTable {
    public static int getSigAsLeInt(byte[] b) {
        return ((0xFF & b[0]) << 24) | ((0xFF & b[1]) << 16) |
                ((0xFF & b[2]) << 8) | (0xFF & b[3]);
    }

    public static int getSigAsBeInt(byte[] b) {
        return ((0xFF & b[3]) << 24) | ((0xFF & b[2]) << 16) |
                ((0xFF & b[1]) << 8) | (0xFF & b[0]);
    }

    public static final byte[] gxTableSignature = {0x47, 0x58, (byte) 0xF0, 0x00};

    public static final int GX_TABLE_SIZE = 8;


    public GxTable(int gxTableAddress, short offset, short count) {
        this.offset = offset;
        this.count = count;
        this.gxTableAddress = gxTableAddress;
    }

    protected int gxTableAddress;
    protected short offset;
    protected short count;

    public String toString() {
        return String.format(this.getClass().getSimpleName() + "[offset:%04x, count:%04x(%d)]", offset, count, count);
    }

    public short getCount() {
        return count;
    }

    public short getOffset() {
        return offset;
    }

    public abstract int load(LERandomAccessByteArray r) throws IOException;
}

/*
//GX_IMAGETABLE
        0x47, 0x58, //GX signature
        0x00, //format
        0x00, //bpp
//GX_TABLE
        0x47, 0x58, //GX signature
        0xF0, //format
        0x00, //resv
        0x00, 0x00, //id offset
        0x82, 0x01, //id coun


// GX_STRINGTABLE
        0x47, 0x58, //GX signature
        0x42, //format
        0x01, //bpc
//GX_TABLE
        0x47, 0x58, //GX signature
        0xF0, //format
        0x00, //resv
        0x00, 0x00, //id offset
        0xC0, 0x03, //id count*/
