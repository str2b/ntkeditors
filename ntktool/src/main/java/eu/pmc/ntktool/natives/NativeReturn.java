package eu.pmc.ntktool.natives;

/**
 * Created by Tobi on 02.06.2017.
 */
public class NativeReturn {

    private NativeReturnCode nrc;
    private String stdout;
    private String stderr;

    public NativeReturn(NativeReturnCode nrc, String stdout, String stderr) {
        this.nrc = nrc;
        this.stderr = stderr;
        this.stdout = stdout;
    }

    public String getStdout() {
        return stdout.trim();
    }

    public String getStderr() {
        return stderr.trim();
    }

    public NativeReturnCode getNrc() {
        return nrc;
    }
}
