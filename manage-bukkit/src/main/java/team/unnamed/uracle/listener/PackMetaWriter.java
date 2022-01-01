package team.unnamed.uracle.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import team.unnamed.uracle.metadata.pack.PackMeta;
import team.unnamed.uracle.UraclePlugin;
import team.unnamed.uracle.event.ResourcePackGenerateEvent;

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
