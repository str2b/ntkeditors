import eu.pmc.ntk.uiresourceeditor.NtkFntImage;
import org.junit.jupiter.api.Test;

/**
 * Created by Tobi on 23.06.2017.
 */
public class NtkFntImageTester {

    @Test
    void testFntImage() {
        NtkFntImage zero = new NtkFntImage(0, 0, 0x21, (short)12, (short)22, payload);
        for(Boolean b : zero) {
            System.out.print(b ? "1" : "0");
        }

    }

    byte[] payload = {(byte) 0x2a, (byte) 0x00, (byte) 0x00, (byte) 0xF0, (byte) 0x80, (byte) 0x1F, (byte) 0x98, (byte) 0xC1, (byte) 0x39, (byte) 0x0C, (byte) 0xC3, (byte) 0x30, (byte) 0x0C, (byte) 0xC3, (byte) 0x30, (byte) 0x0C,
            (byte) 0xC3, (byte) 0x30, (byte) 0x0C, (byte) 0xC3, (byte) 0x30, (byte) 0x0C, (byte) 0xC3, (byte) 0x30, (byte) 0x0C, (byte) 0xC3, (byte) 0x39, (byte) 0x98, (byte) 0x81, (byte) 0x1F, (byte) 0xF0, (byte) 0x00,
            (byte) 0x00};
}
