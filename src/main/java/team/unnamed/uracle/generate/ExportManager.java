package team.unnamed.uracle.generate;

import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.resourcepack.ResourcePack;

/**
 * Responsible for managing resource-pack exports
 * and holding the server resource-pack
 */
public interface ExportManager {

    /**
     * Returns the object responsible for exporting
     * resource packs to the outside world
     *
     * @return The resource exporter linked to this
     * export manager
     */
    ResourceExporter getExporter();

    /**
     * Returns the current server resource-pack, may
     * be null if it wasn't correctly exported, or it
     * wasn't exported to a remote location (so we can't
     * get its URL)
     *
     * @return Current server resource-pack
     */
    @Nullable ResourcePack getResourcePack();

    /**
     * Exports the server resource-pack and updates
     * its state so {@link ExportManager#getResourcePack}
     * will return the new resource-pack if it was
     * correctly uploaded
     */
    void exportAndUpdate();

}
