package team.unnamed.uracle.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import team.unnamed.uracle.PackInfo;
import team.unnamed.uracle.PackMeta;
import team.unnamed.uracle.UraclePlugin;
import team.unnamed.uracle.event.ResourcePackGenerateEvent;

public class PackMetaWriter implements Listener {

    private final UraclePlugin plugin;

    public PackMetaWriter(UraclePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGenerate(ResourcePackGenerateEvent event) {
        PackInfo info = plugin.getInfo();

        if (info != null) {
            event.writer()
                .meta(PackMeta.of(info));
        }
    }

}
