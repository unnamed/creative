package team.unnamed.uracle.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import team.unnamed.uracle.UraclePlugin;
import team.unnamed.uracle.event.ResourcePackGenerateEvent;
import team.unnamed.uracle.io.Writeable;
import team.unnamed.uracle.resourcepack.PackMeta;

public class PackMetaWriter implements Listener {

    private final UraclePlugin plugin;

    public PackMetaWriter(UraclePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGenerate(ResourcePackGenerateEvent event) {

        PackMeta meta = plugin.getMetadata();

        if (meta == null) {
            // no metadata to write
            return;
        }

        event.write(
                "pack.mcmeta",
                "{" +
                        "\"pack\": {" +
                        "\"pack_format\": " + meta.getFormat() + "," +
                        "\"description\": \"" + meta.getDescription() + "\"" +
                        "}" +
                        "}"
        );

        Writeable icon = meta.getIcon();
        if (icon != null) {
            event.write("pack.png", icon);
        }
    }

}
