package team.unnamed.uracle;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.command.UracleCommand;
import team.unnamed.uracle.event.ResourcePackGenerateEvent;
import team.unnamed.uracle.generate.exporter.ResourceExporter;
import team.unnamed.uracle.generate.exporter.ResourceExporterFactory;
import team.unnamed.uracle.listener.PackMetaWriter;
import team.unnamed.uracle.listener.PresetsWriter;
import team.unnamed.uracle.listener.ResourcePackApplyListener;
import team.unnamed.uracle.resourcepack.ResourcePack;
import team.unnamed.uracle.resourcepack.ResourcePackApplication;
import team.unnamed.uracle.resourcepack.UrlAndHash;
import team.unnamed.uracle.util.Texts;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class UraclePlugin extends JavaPlugin {

    @Nullable private PackInfo info;
    @Nullable private ResourcePackApplication application;

    private ResourceExporter exporter;
    private ResourcePack pack;

    private File overridesFolder;
    private File optionalsFolder;

    private Uracle service;

    public void loadConfiguration() {
        ConfigurationSection config = getConfig();

        // load 'metadata'
        {
            if (config.contains("metadata")) {
                info = PackInfo.of(
                        config.getInt("metadata.format", 7),
                        Texts.colorize(config.getString("metadata.description"))
                );
            } else {
                info = null;
            }
        }

        // load 'exporter'
        {
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

        // load 'application'
        {
            boolean required = config.getBoolean("application.force", false);
            String prompt = config.getString("application.prompt", null);

            if (prompt != null) {
                // colorize and translate to JSON
                prompt = ChatColor.translateAlternateColorCodes('&', prompt);
                prompt = ComponentSerializer.toString(TextComponent.fromLegacyText(prompt));
            }

            application = ResourcePackApplication.of(required, prompt);
        }

        if (exporter != null) {
            try {
                getLogger().info("Exporting resource-pack...");
                UrlAndHash location = exporter.export(output ->
                        ResourcePackGenerateEvent.call(new DefaultResourcePackBuilder(output)));
                if (location != null) {
                    getLogger().info("Uploaded resource-pack to " + location.getUrl());

                    pack = new ResourcePack(
                            location.getUrl(),
                            location.getHash(),
                            application.required(),
                            application.prompt()
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
            // saveResource("pack.png", false);

            // create our folders
            optionalsFolder.mkdirs();
            overridesFolder.mkdirs();
        }

        listen(
                new ResourcePackApplyListener(this, service),
                new PresetsWriter(this),
                new PackMetaWriter(this)
        );

        registerCommand("uracle", new UracleCommand(this, service));

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

    private <T extends CommandExecutor & TabCompleter> void registerCommand(String name, T behavior) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            throw new IllegalArgumentException("Invalid command: "
                    + name + ". Did you forget to add it to plugin.yml?");
        }
        command.setExecutor(behavior);
        command.setTabCompleter(behavior);
    }

    public File getOptionalsFolder() {
        return optionalsFolder;
    }

    public File getOverridesFolder() {
        return overridesFolder;
    }

    @Nullable
    public PackInfo getInfo() {
        return info;
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
