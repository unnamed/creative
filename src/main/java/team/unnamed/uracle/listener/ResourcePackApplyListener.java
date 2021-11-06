package team.unnamed.uracle.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import team.unnamed.uracle.Uracle;
import team.unnamed.uracle.UraclePlugin;
import team.unnamed.uracle.resourcepack.ResourcePack;
import team.unnamed.uracle.resourcepack.ResourcePackApplier;

import java.util.List;

public class ResourcePackApplyListener implements Listener {

    private static final String RETRIES_KEY = "uracle_retries";
    private static final int RETRIES = 3;

    private final UraclePlugin plugin;

    public ResourcePackApplyListener(UraclePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ResourcePack pack = plugin.getPack();

        if (pack == null) {
            // there is not a resource pack set
            return;
        }

        ResourcePackApplier.setResourcePack(player, pack);
    }

    @EventHandler
    public void onStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        ResourcePack pack = plugin.getPack();
        PlayerResourcePackStatusEvent.Status status = event.getStatus();

        if (pack == null) {
            // there is not a resource pack set
            return;
        }

        switch (status) {
            case SUCCESSFULLY_LOADED: {
                // successfully loaded, mark player
                // as resource-pack-ed
                Uracle.setHasResourcePack(player);
                // remove retry metadata
                player.removeMetadata(RETRIES_KEY, plugin);
                break;
            }

            case DECLINED: {
                // player declined resource pack
                if (pack.isRequired()) {
                    // kick player since resource-pack
                    // must be accepted
                    player.kickPlayer(plugin.getMessage("declined"));
                } else {
                    // remove retry metadata
                    player.removeMetadata(RETRIES_KEY, plugin);
                }
                break;
            }

            case FAILED_DOWNLOAD: {
                int retries = getRetries(player);
                if (retries < RETRIES) {
                    // retry download
                    setRetries(player, retries + 1);
                    ResourcePackApplier.setResourcePack(player, pack);
                } else if (pack.isRequired()) {
                    // max retries exceeded, pack is required
                    // kick player
                    player.kickPlayer(plugin.getMessage("failed"));
                } else {
                    // pack is optional and cannot be applied,
                    // remove metadata
                    player.removeMetadata(RETRIES_KEY, plugin);
                }
                break;
            }

            case ACCEPTED: {
                // no-op in this case
                break;
            }
        }
    }

    private void setRetries(Player player, int retries) {
        player.setMetadata(RETRIES_KEY, new FixedMetadataValue(plugin, retries));
    }

    private int getRetries(Player player) {
        List<MetadataValue> values = player.getMetadata(RETRIES_KEY);
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().equals(plugin)) {
                return value.asInt();
            }
        }
        return -1;
    }

}
