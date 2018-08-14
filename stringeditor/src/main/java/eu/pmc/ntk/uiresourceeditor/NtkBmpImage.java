package eu.pmc.ntk.uiresourceeditor;

/**
 * Created by Tobi on 16.06.2017.
 */

import eu.pmc.ntk.uiresourceeditor.io.BMP_BPP;
import eu.pmc.ntk.uiresourceeditor.io.NtkBmpIO;

import java.util.Arrays;

/**
 * Created by Tobi on 15.06.2017.
 */
public class NtkBmpImage extends NtkFntImage {

    public NtkBmpImage(int absAddress, int offset, int size, short width, short height, byte format, byte bpp, byte[] payload) {
        super(absAddress, offset, size, width, height, payload);
        this.format = format;
        this.bpp = bpp;
        this.mode = BMP_BPP.COLOR;
    }


    private byte format;
    private byte bpp;





    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" +
                "width=" + width +
                ", height=" + height +
                ", format=" + format +
                ", bpp=" + bpp +
                ", absAddress=" + absAddress +
                ", offset=" + offset +
                ", size=" + size +
                ']';
    }
}

