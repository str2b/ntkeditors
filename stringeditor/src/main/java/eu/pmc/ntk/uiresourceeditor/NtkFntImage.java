package eu.pmc.ntk.uiresourceeditor;

import eu.pmc.ntk.uiresourceeditor.io.BMP_BPP;
import eu.pmc.ntk.uiresourceeditor.io.NtkBmpIO;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Tobi on 15.06.2017.
 */
public class NtkFntImage implements Iterable<Boolean> {

    public NtkFntImage(int absAddress, int offset, int size, short width, short height, byte[] payload) {
        this.absAddress = absAddress;
        this.offset = offset;
        this.size = size;
        this.width = width;
        this.height = height;
        this.rawPayload = payload;
        this.mode = BMP_BPP.MONO;
    }

    protected short width;
    protected short height;
    protected int absAddress;
    protected int offset;
    protected int size;
    protected BMP_BPP mode;

    private byte[] rawPayload;

    public byte[] getBmp() {
        return NtkBmpIO.createBmp(mode, width, height, rawPayload);
    }

    public void setBmp(byte[] bmp) {
        //TODO implement
        //rawPayload = NtkBmpIO.createNtkImage(bmp);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" +
                "width=" + width +
                ", height=" + height +
                ", absAddress=" + absAddress +
                ", offset=" + offset +
                ", size=" + size +
                ']';
    }


    @Override
    public Iterator<Boolean> iterator() {
        return new Iterator<Boolean>() {
            private int pos;

            @Override
            public boolean hasNext() {
                if(pos < (width * height)) {
                    return true;
                }
                return false;
            }

            @Override
            public Boolean next() {
                int byteNum = pos / 8;
                int bitNum = pos % 8;
                pos++;
                return ByteUtil.getBitAt((rawPayload[byteNum]), bitNum);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}


