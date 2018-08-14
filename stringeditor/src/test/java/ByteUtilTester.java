import eu.pmc.ntk.uiresourceeditor.ByteUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Created by Tobi on 23.06.2017.
 */
public class ByteUtilTester {
    @Test
    void ByteUtilTest() {

        byte toMirror = (byte) 0xF0;
        byte result = 0x0F;
        byte calcRes = ByteUtil.mirror(toMirror);
        System.out.format("%04x <-> %04x (?:%04x)\n", toMirror, calcRes, result);
        assertSame(result, calcRes);
    }
}
