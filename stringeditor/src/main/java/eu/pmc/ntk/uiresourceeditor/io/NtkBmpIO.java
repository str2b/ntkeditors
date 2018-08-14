package eu.pmc.ntk.uiresourceeditor.io;

import eu.pmc.ntk.uiresourceeditor.ByteUtil;
import randomaccess.LERandomAccessByteArray;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Formatter;

/**
 * Created by Tobi on 15.06.2017.
 */

public class NtkBmpIO {
    private static final String BMP_8BPP_HEADER = "BMP_8BPP.bmp";
    private static final String BMP_1BPP_HEADER = "BMP_1BPP.bmp";

    private static final int BMP_SIZE_ADDR = 0x02;
    private static final int BMP_WIDTH_ADDR = 0x12;
    private static final int BMP_HEIGHT_ADDR = 0x16;
    private static final int BMP_PL_SIZE_ADDR = 0x22;

    public static byte[] createBmp(BMP_BPP mode, int width, int height, byte[] payload) {
        try {
            URI uri = NtkBmpIO.class.getClassLoader().getResource(mode == BMP_BPP.MONO ? BMP_1BPP_HEADER : BMP_8BPP_HEADER).toURI();
            byte[] bmpHeader = Files.readAllBytes(Paths.get(uri));
            byte[] bmp = new byte[bmpHeader.length + (mode == BMP_BPP.MONO ? payload.length * 4 : payload.length)];
            System.arraycopy(bmpHeader, 0, bmp, 0, bmpHeader.length);
            LERandomAccessByteArray r = new LERandomAccessByteArray(bmp);
            if (mode == BMP_BPP.MONO) {
                int requiredBytes = (width / 8);
                int requiredMsbBits = width % 8;
                int requiredBits = width;

                r.seek(bmpHeader.length);
                for (int i = 0; i < payload.length; i++) {

                    r.writeByte(ByteUtil.mirror(payload[i]));
                    r.writeByte((byte) 0x00);
                    r.writeByte((byte) 0x00);
                    r.writeByte((byte) 0x00);
                }
            } else {
                System.arraycopy(payload, 0, bmp, bmpHeader.length, payload.length);
            }

            r.seek(BMP_SIZE_ADDR);
            r.writeInt(bmp.length);
            r.seek(BMP_WIDTH_ADDR);
            r.writeInt(width);
            r.seek(BMP_HEIGHT_ADDR);
            r.writeInt(height);
            r.seek(BMP_PL_SIZE_ADDR);
            r.writeInt(payload.length);

            r.close();
            return bmp;

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String prettyHexView(byte[] ba) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        for (int j = 1; j < ba.length + 1; j++) {
            if (j % 8 == 1 || j == 0) {
                if (j != 0) {
                    sb.append("\n");
                }
                formatter.format("0%d\t|\t", j / 8);
            }
            formatter.format("%02X", ba[j - 1]);
            if (j % 4 == 0) {
                sb.append(" ");
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
