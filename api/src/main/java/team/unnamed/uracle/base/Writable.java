package team.unnamed.uracle.base;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface for representing objects that can
 * be written to a {@link OutputStream}, this
 * class is util for representing assets that
 * can be exported anytime, without necessarily
 * loading them
 */
@FunctionalInterface
public interface Writable {

    /**
     * Writes this object information to a
     * {@link OutputStream}, this method can be
     * called anytime, it should be consistent
     * with its data and generate the exact same
     * output for the same input (attributes) of
     * the implementation
     *
     * @param output The target output stream
     * @throws IOException If write fails
     */
    void write(OutputStream output) throws IOException;

}
