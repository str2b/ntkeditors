package eu.pmc.ntk.uiresourceeditor;

import randomaccess.LERandomAccessByteArray;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by Tobi on 14.06.2017.
 */
public class NtkString {

    private static final int BOM_LEN = 2;
    private static final int TERM_LEN = 2;

    public NtkString(int offset, byte[] bytes, int absAddress) {
        this.offset = offset;
        length = (bytes.length - (BOM_LEN + TERM_LEN))/2;
        this.absAddress = absAddress;
        try {
            data = new String(bytes, "UTF16").substring(0, length);
        } catch (UnsupportedEncodingException e) {
            data = "";
            e.printStackTrace();
        }
    }

    private int absAddress;
    private int offset;
    private int length;
    private String data;
    private boolean modified;

    public String getString() {
        return data;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return data.length();
    }

    public void setString(String s) {
        System.out.println("len flag: " + length + "   new len:" + s.length());
        if(s.length() > length) {
            data = s.substring(0, length);
            System.out.println("Max size: " + length);
        } else {
            data = s;
        }
        modified = true;
        System.out.println(toString());
    }

    public byte[] toNtkBytes() {
        byte[] dataBytes = new byte[0];
        try {
            dataBytes = data.getBytes("UTF-16LE");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       byte[] retBytes = new byte[BOM_LEN + length * 2 + TERM_LEN];
        retBytes[0] = (byte) 0xFF;
        retBytes[1] = (byte) 0xFE;
        int i, j;
        for(i = 2, j = 0; j < length * 2; i++, j++) {
            retBytes[i] = dataBytes[j];
        }
        retBytes[i++] = 0;
        retBytes[i] = 0;

        return retBytes;
    }

    public String toString() {
        return String.format(this.getClass().getSimpleName() + "[address:%08x, offset:%02x, str:\"%s\", len:%d]", absAddress, offset, data, length);
    }

    public int getAbsAddress() {
        return absAddress;
    }

    public boolean isModified() {
        return modified;
    }
}
