package team.unnamed.uracle;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.event.ResourcePackGenerateEvent;
import team.unnamed.uracle.export.ResourceExporter;
import team.unnamed.uracle.export.ResourceExporterFactory;
import team.unnamed.uracle.io.Writeable;
import team.unnamed.uracle.listener.ResourcePackInfoWriter;
import team.unnamed.uracle.resourcepack.UrlAndHash;
import team.unnamed.uracle.resourcepack.PackMeta;
import team.unnamed.uracle.util.Texts;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class UraclePlugin extends JavaPlugin {

    private PackMeta metadata;
    private ResourceExporter exporter;
    private UrlAndHash resource;

    private void loadConfiguration() {
        ConfigurationSection config = getConfig();

        // load 'metadata'
        if (config.contains("metadata")) {
            File iconFile = new File(getDataFolder(), "pack.png");
            metadata = new PackMeta(
                    config.getInt("metadata.format", 7),
                    Texts.colorize(Texts.escapeForJson(config.getString("metadata.description"))),
                    iconFile.exists() ? Writeable.ofFile(iconFile) : null
            );
        } else {
            metadata = null;
        }

        // load 'exporter'
        String exporterSource = config.getString("generation");

        try {
            exporter = ResourceExporterFactory.of(exporterSource);
        } catch (IOException e) {
            getLogger().log(
                    Level.SEVERE,
                    "Cannot create exporter from string '" + exporterSource + "'",
                    e
            );
        }
    }

    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", true);
            saveResource("pack.png", false);
        }

        loadConfiguration();

        if (metadata != null) {
            Bukkit.getPluginManager().registerEvents(
                    new ResourcePackInfoWriter(metadata),
                    this
            );
        }

        if (exporter != null) {
            try {
                resource = exporter.export(ResourcePackGenerateEvent::call);
                if (resource != null) {
                    getLogger().info("Uploaded resource-pack to " + resource.getUrl());
                }
            } catch (IOException e) {
                getLogger().log(
                        Level.SEVERE,
                        "Failed to export resource pack",
                        e
                );
            }
        }
    }

    @Nullable
    public PackMeta getMetadata() {
        return metadata;
    }

    public ResourceExporter getExporter() {
        return exporter;
    }

}
