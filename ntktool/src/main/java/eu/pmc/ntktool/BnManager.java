package eu.pmc.ntktool;

import eu.pmc.ntktool.natives.NativeBridge;
import randomaccess.RandomAccessByteArray;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Tobi on 02.06.2017.
 */
public class BnManager {

    {
        setLastFile(new File(System.getProperty("user.dir")));
    }

    private FirmwareInfo fi;

    public FirmwareInfo loadFirmware(File fwFile) throws IOException {
        fi = FirmwareInfo.getInfo(fwFile);
        System.out.println(fi);
        return fi;
    }

    public Partition[] getPartitionInfo() throws IOException {
        if (fi == null) {
            throw new UnsupportedOperationException("Valid firmware not loaded yet!");
        }
        Partition ret[] = new Partition[fi.getBclCount()];

        RandomAccessByteArray rb = new RandomAccessByteArray(fi.getFwBinary());
        int c = 0;
        for (Integer i : fi.getOffsets()) {
            ret[c] = new Partition(c++, i, fi.getType(), fi.getFwFile(), rb);
        }
        return ret;
    }

    public static void setLastFile(File f) {
        lastFile = f;
    }

    private static File lastFile = null;

    public static File getLastFile() {
        return lastFile;
    }


}

