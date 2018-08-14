package eu.pmc.ntktool.natives.wrapper;

/**
 * Created by Tobi on 03.06.2017.
 */
public enum BfcMode {
    C("-c"), X("-x"), D("-d"), P("-p");
    String cmd;
    BfcMode(String s) {
        cmd = s;
    }

    public String cmd() {
        return cmd;
    }
}