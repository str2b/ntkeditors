package eu.pmc.ntktool.natives;

import eu.pmc.ntktool.CustomExecutor;
import org.apache.commons.exec.CommandLine;

import java.io.*;
import java.util.Locale;

import static eu.pmc.ntktool.natives.NativeReturnCode.*;

/**
 * Created by Tobi on 02.06.2017.
 */

public class NativeBridge {

    private static File bfcExec;
    private static File ntkcalcExec;
    private static NativeReturnCode initState;

    private static File getExecFileByOs(String file, boolean internalFile) throws Exception {
        if(System.getProperty("os.name").toLowerCase(Locale.ENGLISH).indexOf("win") >= 0) {
            if(internalFile) {
                return new File(exportResource("win", file + ".exe"));
            } else {
                return new File(file + ".exe");
            }
        } else if(System.getProperty("os.name").toLowerCase(Locale.ENGLISH).indexOf("nux") >= 0) {
            if(internalFile) {
                return new File(exportResource("linux", file));
            } else {
                return new File(file);
            }
        } else {
            return null;
        }
    }

    public static File getBfcExec() {
        return bfcExec;
    }

    public static File getNtkcalcExec() {
        return ntkcalcExec;
    }

    public static NativeReturnCode sinitSetup() {
        System.out.println("Exporting native files...");
        try {

            if(new File(System.getProperty("user.dir") + File.separator, ".dontoverwrite").exists()) {
            bfcExec = getExecFileByOs("bfc", false);
            ntkcalcExec = getExecFileByOs("ntkcalc", false);
            if(bfcExec.exists() && ntkcalcExec.exists()) {
                System.out.println("Using provided files...");
                return initState = NATIVE_EXPORT_OK;
            }
        }
            bfcExec = getExecFileByOs("bfc4ntk", true);
            ntkcalcExec = getExecFileByOs("ntkcalc", true);
            bfcExec.deleteOnExit();
            ntkcalcExec.deleteOnExit();
            bfcExec.setExecutable(true);
            ntkcalcExec.setExecutable(true);
        } catch (Exception e) {
            initState = NATIVE_EXPORT_ERROR;
            e.printStackTrace();
            return initState;
        }
        return initState = (bfcExec.exists() && ntkcalcExec.exists()) ? NATIVE_EXPORT_OK : NATIVE_EXPORT_ERROR;
    }

    public static NativeReturnCode getInitState() {
        return initState;
    }

    public static CommandLine getCmd(String s) {
        return new CommandLine(s);
    }

    public static CustomExecutor getExecutor() {
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        CustomExecutor e = new CustomExecutor(stdout, stderr);
        return e;
    }


    private static String exportResource(String directory, String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = NativeBridge.class.getClassLoader().getResourceAsStream(directory +"/"+ resourceName);
            if (stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = System.getProperty("user.dir") + File.separator;
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (IOException ex) {
            System.out.println("NATIVE EXPORT ERROR");
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }

        return jarFolder + resourceName;
    }

}
