package eu.pmc.ntktool;

import randomaccess.RandomAccessByteArray;

import java.io.File;
import java.io.IOException;

/**
 * Created by Tobi on 02.06.2017.
 */
public class Partition {
    private int num;
    private int offset;
    private int compressedSize;
    private int extractedSize;
    private short algo;
    private FirmwareType type;
    private File fwFile;

    public Partition(int num, int offset, FirmwareType t, File fwFile, RandomAccessByteArray r) throws IOException {
        this.num = num;
        this.fwFile = fwFile;
        this.offset = offset;
        this.type = t;
        r.seek(offset);
        r.seekBy(6);
        algo = r.readShort();
        extractedSize = r.readInt();
        compressedSize = r.readInt();
    }

    public int getOffset() {
        return offset;
    }

    public int getCompressedSize() {
        return compressedSize;
    }

    public int getExtractedSize() {
        return extractedSize;
    }

    public short getAlgo() {
        return algo;
    }

    public int getNum() {
        return num;
    }

    public FirmwareType getType() {
        return type;
    }


    public String getAlgoText() {
        switch (algo) {
            case 1:
                return "RLE";
            case 2:
                return "Huffman";
            case 3:
                return "Rice 8-bit";
            case 4:
                return "Rice 16-bit";
            case 5:
                return "Rice 32-bit";
            case 6:
                return "Rice 8-bit signed";
            case 7:
                return "Rice 16-bit signed";
            case 8:
                return "Rice 32-bit signed";
            case 9:
                return "LZ77";
            case 10:
                return "Shannon-Fano";
            default:
                return "Unknown";
        }
    }

    public File getFwFile() {
        return fwFile;
    }
}