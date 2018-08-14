package eu.pmc.ntktool;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import randomaccess.LERandomAccessByteArray;

import javax.activation.UnsupportedDataTypeException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tobi on 02.06.2017.
 */
public class FirmwareInfo {

    private static Pattern bclPattern = Pattern.compile("BCL1", Pattern.DOTALL);

    private List<Integer> bclOffsets = new ArrayList<>();
    private FirmwareType type;
    private File fwFile;

    public FirmwareType getType() {
        return type;
    }

    private void setType(FirmwareType f) {
        type = f;
    }

    private void addOffset(int i) {
        bclOffsets.add(i);
    }

    public int getBclCount() {
        return bclOffsets.size();
    }

    public List<Integer> getOffsets() {
        return bclOffsets;
    }

    private byte[] fwBinary;

    public byte[] getFwBinary() {
        return fwBinary;
    }

    public static FirmwareInfo getInfo(File fwFile) throws IOException {
        RandomAccessFile f = new RandomAccessFile(fwFile, "r");
        byte[] fwBinary = new byte[(int) f.length()];
        f.read(fwBinary);
        LERandomAccessByteArray r = new LERandomAccessByteArray(fwBinary);
        r.seek(0x30c);
        int partcompAddr = r.readInt();
        r.close();
        FirmwareInfo fi = new FirmwareInfo();
        fi.fwFile = fwFile;
        String binaryAsString = new String(fwBinary, "ISO-8859-1");
        Matcher m = bclPattern.matcher(binaryAsString);
        while (m.find()) {
            int position = m.start();
            fi.addOffset(position);
        }
        switch (fi.getBclCount()) {
            case 0:
                fi.setType(FirmwareType.NONCOMP);
                break;
            case 1:
                if (fi.getOffsets().get(0).equals(partcompAddr)) {
                    fi.setType(FirmwareType.PARTCOMP);
                } else {
                    fi.setType(FirmwareType.FULLCOMP);
                }
                break;
            case 2:
                fi.setType(FirmwareType.FULLCOMP);
                break;
            default:
                throw new UnsupportedDataTypeException("Firmware not supported!");
        }

        fi.fwBinary = fwBinary;
        return fi;
    }

    public File getFwFile() {
        return fwFile;
    }

    @Override
    public String toString() {
        return type.toString() + "; " + getBclCount();
    }

}
