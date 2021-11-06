package team.unnamed.uracle.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import team.unnamed.uracle.event.ResourcePackGenerateEvent;
import team.unnamed.uracle.io.Writeable;
import team.unnamed.uracle.resourcepack.PackMeta;

public class PackMetaWriter implements Listener {

    private final PackMeta meta;

    public PackMetaWriter(PackMeta meta) {
        this.meta = meta;
    }

    @EventHandler
    public void onGenerate(ResourcePackGenerateEvent event) {

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
