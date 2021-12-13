package team.unnamed.uracle;

import team.unnamed.uracle.generate.ExportManager;
import team.unnamed.uracle.resourcepack.ResourcePackSender;

/**
 * Represents the entry point object for the Uracle
 * API
 *
 * <p>Everything is accessed via this interface, plugins
 * can read and update state and call actions</p>
 */
public interface Uracle {

    /**
     * Returns the {@link ResourcePackSender} that this
     * service can provide, it is responsible for sending
     * resource packs to players
     *
     * @return The resource pack sender
     */
    ResourcePackSender getSender();

    /**
     * Returns the {@link ExportManager} that this service
     * can provide, it is responsible for holding and exporting
     * the server resource pack
     *
     * @return The export manager for this service
     */
    ExportManager getExportManager();

}
