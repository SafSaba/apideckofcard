package utilities;

import java.io.Closeable;
import java.io.IOException;

public class JavaUtil {


    /**
     *
     * @param closeable destination of data that we need to close
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ex) {
            }
        }
    }
}
