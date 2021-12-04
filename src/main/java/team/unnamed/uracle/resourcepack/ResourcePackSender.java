package team.unnamed.uracle.resourcepack;

import org.bukkit.entity.Player;

/**
 * Represents the object responsible for sending
 * resource packs to specific players
 */
@FunctionalInterface
public interface ResourcePackSender {

    /**
     * Sends a {@link ResourcePack} to a {@link Player}
     *
     * @param player The resource pack receiver
     * @param pack The resource pack to be sent
     */
    void send(Player player, ResourcePack pack);

}
