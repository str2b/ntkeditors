import eu.pmc.ntk.uiresourceeditor.GxStringTable;
import eu.pmc.ntk.uiresourceeditor.GxTable;
import org.junit.jupiter.api.Test;
import randomaccess.LERandomAccessByteArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tobi on 14.06.2017.
 */
public class StringLoader {

    @Test
    public void loadGxStringTables() throws IOException {
        byte[] b = Files.readAllBytes(Paths.get("G:\\Novatek\\0906_CAM\\FIRMWARE0527@00000000.rbn"));
        LERandomAccessByteArray r = new LERandomAccessByteArray(b);
        Pattern toFind = Pattern.compile(new String(GxStringTable.gxStringTableSignature, "ISO-8859-1"));
        Matcher m = toFind.matcher(new String(b, "ISO-8859-1"));

        while (m.find()) {
            int position = m.start();
            r.seek(position + GxStringTable.GX_STR_TABLE_SIZE);
            //check if signature bytes actually belong to eu.pmc.ntk.uiresourceeditor.GxStringTable
            if(r.readInt() == GxTable.getSigAsBeInt(GxTable.gxTableSignature)) {
                short offset = r.readShort();
                short count = r.readShort();
                int strTableAddr = position;
                int gxTableAddr = strTableAddr + GxStringTable.GX_STR_TABLE_SIZE;
                GxStringTable gst = new GxStringTable(gxTableAddr, strTableAddr, offset, count);
                System.out.println("Loaded " + gst.load(r));
            }
        }
    }
}
