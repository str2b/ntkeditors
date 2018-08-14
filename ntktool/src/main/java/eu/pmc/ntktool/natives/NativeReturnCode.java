package eu.pmc.ntktool.natives;

/**
 * Created by Tobi on 02.06.2017.
 */
public enum NativeReturnCode {
    NATIVE_EXPORT_ERROR(439, "Could not export native files to system!"),
    NATIVE_EXPORT_OK(1, "All files present!"),
    EXEC_OK(0, "Execution ok!"),
    EXEC_FAIL(-1, "Execution error!"),
    BFC_UNKNOWN_COMPRESSION_ALGO(-2, "Unknown compression algorithm!"),
    BFC_IO_ERROR(-3, "File cannot be opened with bfc!"),
    BFC_ALLOC_ERROR(-4, "Allocation error!");


    private final int code;
    private final String description;

    private NativeReturnCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}