package team.unnamed.uracle;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.command.UracleCommand;
import team.unnamed.uracle.event.ResourcePackGenerateEvent;
import team.unnamed.uracle.export.ResourceExporter;
import team.unnamed.uracle.export.ResourceExporterFactory;
import team.unnamed.uracle.io.Writeable;
import team.unnamed.uracle.listener.PackMetaWriter;
import team.unnamed.uracle.listener.PresetsWriter;
import team.unnamed.uracle.listener.ResourcePackApplyListener;
import team.unnamed.uracle.resourcepack.ResourcePack;
import team.unnamed.uracle.resourcepack.UrlAndHash;
import team.unnamed.uracle.resourcepack.PackMeta;
import team.unnamed.uracle.util.Texts;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class UraclePlugin extends JavaPlugin {

    private PackMeta metadata;
    private ResourceExporter exporter;
    private ResourcePack pack;

    private File overridesFolder;
    private File optionalsFolder;

    private Uracle service;

    public void loadConfiguration() {
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

        // load 'application'
        boolean required = config.getBoolean("application.force", false);
        String prompt = config.getString("application.prompt", null);

        if (prompt != null) {
            // colorize and translate to JSON
            prompt = ChatColor.translateAlternateColorCodes('&', prompt);
            prompt = ComponentSerializer.toString(TextComponent.fromLegacyText(prompt));
        }

        if (exporter != null) {
            try {
                getLogger().info("Exporting resource-pack...");
                UrlAndHash location = exporter.export(ResourcePackGenerateEvent::call);
                if (location != null) {
                    getLogger().info("Uploaded resource-pack to " + location.getUrl());

                    pack = new ResourcePack(
                            location.getUrl(),
                            location.getHash(),
                            required,
                            prompt
                    );
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

    @Override
    public void onEnable() {
        optionalsFolder = new File(getDataFolder(), "optionals");
        overridesFolder = new File(getDataFolder(), "overrides");
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveResource("config.yml", true);
            saveResource("pack.png", false);

            // create our folders
            optionalsFolder.mkdirs();
            overridesFolder.mkdirs();
        }

        listen(
                new ResourcePackApplyListener(this, service),
                new PresetsWriter(this),
                new PackMetaWriter(this)
        );

        getCommand("uracle").setExecutor(new UracleCommand(this, service));

        // make loadConfiguration() be called after the server finishes loading
        getServer()
                .getScheduler()
                .runTaskLater(this, this::loadConfiguration, 1L);
    }

    private void listen(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    public File getOptionalsFolder() {
        return optionalsFolder;
    }

    public File getOverridesFolder() {
        return overridesFolder;
    }

    @Nullable
    public PackMeta getMetadata() {
        return metadata;
    }

    @Nullable
    public ResourcePack getPack() {
        return pack;
    }

    public ResourceExporter getExporter() {
        return exporter;
    }

    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(
                "message." + key,
                "Missing message: " + key
        ));
    }

}
