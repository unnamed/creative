package team.unnamed.uracle.generate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents any object that can be written
 * to an {@link OutputStream}
 */
public interface Writeable {

    /**
     * Writes this object to the given {@code output}
     * stream
     * @throws IOException If writing fails
     */
    void write(OutputStream output) throws IOException;

    /**
     * Creates a {@link Writeable} from a resource.
     * @param loader The class loader holding the resource
     * @param name The resource name
     */
    static Writeable ofResource(ClassLoader loader, String name) {
        return output -> {
            try (InputStream input = loader.getResourceAsStream(name)) {
                if (input == null) {
                    throw new IOException("Resource '"
                            + name + "' doesn't exist");
                } else {
                    Streams.pipe(input, output);
                }
            }
        };
    }

    /**
     * Creates a {@link Writeable} representing the
     * given {@code file}.
     * @param file The wrapped file
     */
    static Writeable ofFile(File file) {
        return output -> {
            try (InputStream input = new FileInputStream(file)) {
                Streams.pipe(input, output);
            }
        };
    }

}