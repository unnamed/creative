package team.unnamed.uracle.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import team.unnamed.uracle.ResourcePackBuilder;
import team.unnamed.uracle.event.ResourcePackGenerateEvent;
import team.unnamed.uracle.export.Streams;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class PresetsWriter implements Listener {

    private final File overridesFolder;
    private final File optionalsFolder;

    public PresetsWriter(
            File overridesFolder,
            File optionalsFolder
    ) {
        this.overridesFolder = overridesFolder;
        this.optionalsFolder = optionalsFolder;
    }

    private void writeRecursively(ResourcePackBuilder builder, File folder, String path) {
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
                    builder.file(localPath, output -> {
                        try (InputStream input = new FileInputStream(child)) {
                            Streams.pipe(input, output);
                        }
                    });
                }
            } else {
                writeRecursively(builder, child, localPath);
            }
        }
    }

    private void writeIfExists(ResourcePackGenerateEvent event, File folder) {
        if (folder.exists()) {
            writeRecursively(event.builder(), folder, "");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void writeOverrides(ResourcePackGenerateEvent event) {
        writeIfExists(event, overridesFolder);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void writeOptionals(ResourcePackGenerateEvent event) {
        writeIfExists(event, optionalsFolder);
    }

}
