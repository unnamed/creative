package team.unnamed.uracle.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import team.unnamed.uracle.UraclePlugin;
import team.unnamed.uracle.event.ResourcePackGenerateEvent;

import java.io.File;

public class PresetsWriter implements Listener {

    private final UraclePlugin plugin;

    public PresetsWriter(UraclePlugin plugin) {
        this.plugin = plugin;
    }

    private void writeRecursively(ResourcePackGenerateEvent event, File folder, String path) {
        File[] children = folder.listFiles();
        if (children == null) {
            // should never happen since
            // 'folder' always exists
            return;
        }

        for (File child : children) {
            String name = child.getName();
            String localPath = path + File.separator + name;

            if (child.isFile()) {
                if (!event.has(localPath)) {
                    event.write(localPath, Writeable.ofFile(child));
                }
            } else {
                writeRecursively(event, child, localPath);
            }
        }
    }

    private void writeIfExists(ResourcePackGenerateEvent event, File folder) {
        if (folder.exists()) {
            writeRecursively(event, folder, "");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void writeOverrides(ResourcePackGenerateEvent event) {
        writeIfExists(event, plugin.getOverridesFolder());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void writeOptionals(ResourcePackGenerateEvent event) {
        writeIfExists(event, plugin.getOptionalsFolder());
    }

}
