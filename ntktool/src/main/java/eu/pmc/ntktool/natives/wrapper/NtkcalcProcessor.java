package eu.pmc.ntktool.natives.wrapper;

import eu.pmc.ntktool.natives.NativeBridge;
import eu.pmc.ntktool.natives.NativeReturn;
import org.apache.commons.exec.CommandLine;

import java.io.File;

/**
 * Created by Tobi on 03.06.2017.
 */
public class NtkcalcProcessor extends NativeProcessor {
    public static NativeReturn cw(String f, int baseval) {
        return ntkcalc("-cw", f, baseval);
    }

    public static NativeReturn ntkcalc(String command, String f, int baseval) {
        CommandLine cmd = NativeBridge.getCmd(NativeBridge.getNtkcalcExec().getAbsolutePath());
        cmd.addArgument(command);
        cmd.addArgument(f);
        cmd.addArgument(String.format("%08x", baseval));
        return execute(cmd);
    }

}
