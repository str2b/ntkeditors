package eu.pmc.ntktool.natives.wrapper;

import eu.pmc.ntktool.CustomExecutor;
import eu.pmc.ntktool.natives.NativeBridge;
import eu.pmc.ntktool.natives.NativeReturn;
import eu.pmc.ntktool.natives.NativeReturnCode;
import org.apache.commons.exec.CommandLine;

import static eu.pmc.ntktool.natives.NativeReturnCode.*;

import java.io.IOException;

/**
 * Created by Tobi on 02.06.2017.
 */
public class BfcProcessor extends NativeProcessor {

    public static NativeReturn x(String inFile, String outFile) throws IOException {
        return bfc(BfcMode.X, inFile, outFile);
    }

    public static NativeReturn d(String inFile, String outFile, int offset) throws IOException {
        return bfc(BfcMode.D, inFile, outFile, offset);
    }

    public static NativeReturn c(String inFile, String outFile) throws IOException {
        return bfc(BfcMode.C, inFile, outFile);
    }

    public static NativeReturn p(String inFile, String outFile) throws IOException {
        return bfc(BfcMode.P, inFile, outFile);
    }

    public static NativeReturn bfc(BfcMode m, String inFile, String outFile) throws IOException {
        CommandLine cmd = NativeBridge.getCmd(NativeBridge.getBfcExec().getAbsolutePath());
        cmd.addArgument(m.cmd());
        cmd.addArgument(inFile);
        cmd.addArgument(outFile);
        return execute(cmd);
    }

    public static NativeReturn bfc(BfcMode m, String inFile, String outFile, int offset) throws IOException {
        CommandLine cmd = NativeBridge.getCmd(NativeBridge.getBfcExec().getAbsolutePath());
        cmd.addArgument(m.cmd());
        cmd.addArgument(inFile);
        cmd.addArgument(outFile);
        cmd.addArgument(String.format("%08x", offset));
        return execute(cmd);
    }

}
