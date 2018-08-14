package eu.pmc.ntk.uiresourceeditor;

import randomaccess.LERandomAccessByteArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tobi on 14.06.2017.
 */
public class ResourceEditor {

    byte[] firmwareBytes;

    public byte[] getFirmwareBytes() {
        return firmwareBytes;
    }

    public ResourceEditor() {

    }

    public boolean loadFirmware() {
        try {
            firmwareBytes = Files.readAllBytes(Paths.get("G:\\Novatek\\0906_CAM\\FIRMWARE0527@00000000.rbn"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
