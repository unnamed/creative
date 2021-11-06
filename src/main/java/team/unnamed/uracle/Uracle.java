package team.unnamed.uracle;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class holding static API methods for ease their
 * access
 */
public final class Uracle {

    private static final String METADATA_KEY = "uracle_has_resourcepack";
    private static final Plugin PLUGIN = JavaPlugin.getPlugin(UraclePlugin.class);

    private Uracle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Determines if the given {@code player} has the server
     * resource-pack applied
     */
    public static boolean hasResourcePack(Player player) {
        for (MetadataValue metadata : player.getMetadata(METADATA_KEY)) {
            if (metadata.getOwningPlugin().equals(PLUGIN)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Marks the given {@code player} as a player who has
     * the resource-pack applied
     */
    public static void setHasResourcePack(Player player) {
        player.setMetadata(METADATA_KEY, new FixedMetadataValue(PLUGIN, true));
    }

    /**
     * Un-marks the given {@code player} as a player who
     * has the server resource-pack applied
     */
    public static void removeHasResourcePack(Player player) {
        player.removeMetadata(METADATA_KEY, PLUGIN);
    }

}
