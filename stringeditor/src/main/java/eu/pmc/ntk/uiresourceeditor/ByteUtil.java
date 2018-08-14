package eu.pmc.ntk.uiresourceeditor;

/**
 * Created by Tobi on 17.06.2017.
 */
public class ByteUtil {

    @Deprecated
    public static byte mirror(byte x) {
        return (byte) (Integer.reverse(x) >>> (Integer.SIZE - Byte.SIZE));
    }

    public static byte[] split(byte split, int exPos) {
        if (exPos >= 8) {
            return new byte[]{split, 0};
        }
        if (exPos <= 0) {
            return new byte[]{0, split};
        }
        byte[] res = new byte[2];
        byte leftMask = 0xFFFFFFFF;
        leftMask <<= (8 - exPos);
        System.out.format("leftmask: %08x\n", leftMask);
        byte rightMask = (byte) (~leftMask & 0xFF);
        System.out.format("rightmask: %08x\n", rightMask);

        res[0] = (byte) ((split & leftMask) & 0xFF);
        res[1] = (byte) ((split & rightMask) & 0xFF);
        return res;
    }

    public static Boolean getBitAt(byte a, int pos) {
        if (pos < 0 || pos > 8) {
            return null;
        }
        return ((a >> pos) & 1) == 1;

    }
}
