package team.unnamed.uracle;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Class providing static access to a singleton {@link Uracle}
 * API entry point
 */
public final class UracleProvider {

    private static final String METADATA_KEY = "uracle_has_resourcepack";
    private static final Plugin PLUGIN = JavaPlugin.getPlugin(UraclePlugin.class);

    private static Uracle instance;

    private UracleProvider() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the held {@link Uracle} singleton instance to
     * access the entire library
     *
     * @throws IllegalStateException If API has not been initialized
     * yet
     */
    public static @NotNull Uracle get() {
        Uracle instance = UracleProvider.instance;
        if (instance == null) {
            throw new IllegalStateException(
                    "Cannot access Uracle API before it is initialized. This"
                    + " error can be caused by a plugin that doesn't declare its"
                    + " dependency on Uracle"
            );
        }
        return instance;
    }

    @ApiStatus.Internal
    void setService(Uracle instance) {
        UracleProvider.instance = instance;
    }

    @ApiStatus.Internal
    void unsetService() {
        UracleProvider.instance = null;
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
