package team.unnamed.creative.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.creative.UraclePlugin;
import team.unnamed.creative.event.ResourcePackGenerateEvent;

public class PackMetaWriter implements Listener {

    private final UraclePlugin plugin;

    public PackMetaWriter(UraclePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGenerate(ResourcePackGenerateEvent event) {
        PackMeta info = plugin.getInfo();

        if (info != null) {
            event.writer()
                .meta(PackMeta.of(info));
        }
    }

}
