package eu.pmc.ntktool.natives.wrapper;

import eu.pmc.ntktool.CustomExecutor;
import eu.pmc.ntktool.natives.NativeBridge;
import eu.pmc.ntktool.natives.NativeReturn;
import eu.pmc.ntktool.natives.NativeReturnCode;
import org.apache.commons.exec.CommandLine;

import java.io.FileNotFoundException;
import java.io.IOException;

import static eu.pmc.ntktool.natives.NativeReturnCode.*;
import static eu.pmc.ntktool.natives.NativeReturnCode.EXEC_FAIL;

/**
 * Created by Tobi on 03.06.2017.
 */
public abstract class NativeProcessor {

    protected static NativeReturn execute(CommandLine cmd) {
        NativeReturnCode nrc;
        CustomExecutor ce = NativeBridge.getExecutor();
        try {
            System.out.println(cmd.toString());

            ce.execute(cmd);
            String stderr = ce.fetchStderr();
            if (stderr.trim().length() > 0) {
                if (stderr.contains("algorithm")) {
                    nrc = BFC_UNKNOWN_COMPRESSION_ALGO;
                } else if(stderr.contains("File error")) {
                    nrc = BFC_IO_ERROR;
                } else if(stderr.contains("Allocation error")) {
                    nrc = BFC_ALLOC_ERROR;
                } else {
                    nrc = EXEC_FAIL;
                }
            } else {
                nrc = EXEC_OK;
            }
        } catch (IOException e) {
            e.printStackTrace();
            nrc = EXEC_FAIL;
        }
        System.out.println(nrc);
        return new NativeReturn(nrc, ce.fetchStdout(), ce.fetchStderr());
    }

}
