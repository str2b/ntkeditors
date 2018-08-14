package eu.pmc.ntktool;

import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;

/**
 * Created by Tobi on 02.06.2017.
 */
public class CustomExecutor extends DefaultExecutor {

    private ByteArrayOutputStream stdout;
    private ByteArrayOutputStream stderr;

    public CustomExecutor(ByteArrayOutputStream stdout, ByteArrayOutputStream stderr) {
        super();
        this.stderr = stderr;
        this.stdout = stdout;
        this.setStreamHandler(new PumpStreamHandler(stdout, stderr));
    }

    public String fetchStdout() {
        return stdout.toString();
    }

    public String fetchStderr() {
        return stderr.toString();
    }
}
