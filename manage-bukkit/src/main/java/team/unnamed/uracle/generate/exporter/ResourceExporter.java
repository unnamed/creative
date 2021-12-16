package team.unnamed.uracle.generate.exporter;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.generate.TreeWriteable;
import team.unnamed.uracle.resourcepack.UrlAndHash;

import java.io.IOException;

/**
 * Interface for exporting resources packs
 */
public interface ResourceExporter {

    /**
     * Exports the data written by the
     * given {@code writer}
     */
    @Nullable
    UrlAndHash export(TreeWriteable writer) throws IOException;

}