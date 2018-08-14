package eu.pmc.ntktool;

import java.io.File;

/**
 * Created by Tobi on 03.06.2017.
 */
public class StringUtils {
    public static String removeExtension(String s) {
        String parent = new File(s).getParent();

        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return parent + File.separator + filename;

        return parent + File.separator + filename.substring(0, extensionIndex);
    }
}
