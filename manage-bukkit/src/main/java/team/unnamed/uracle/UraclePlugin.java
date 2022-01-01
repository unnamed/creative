package team.unnamed.uracle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.command.UracleCommand;
import team.unnamed.uracle.export.Streams;
import team.unnamed.uracle.listener.PackMetaWriter;
import team.unnamed.uracle.listener.PresetsWriter;
import team.unnamed.uracle.listener.ResourcePackApplyListener;
import team.unnamed.uracle.metadata.PackMeta;
import team.unnamed.uracle.util.Texts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UraclePlugin extends JavaPlugin {

    @Nullable private PackMeta info;
    private Uracle service;

    @Override
    public void onEnable() {

        // set up the api entry point
        service = new BukkitUracle(this);
        UracleProvider.setService(service);
        Bukkit.getServicesManager().register(
                Uracle.class,
                service,
                this,
                ServicePriority.High
        );

        // set up configuration files and folders
        File optionalsFolder = new File(getDataFolder(), "optionals");
        File overridesFolder = new File(getDataFolder(), "overrides");
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveResource("config.yml", true);
            // saveResource("pack.png", false);

            // create our folders
            optionalsFolder.mkdirs();
            overridesFolder.mkdirs();

            // copy our pack.png to overrides/
            File iconFile = new File(overridesFolder, "icon.png");
            try (InputStream input = getResource("icon.png")) {
                if (!iconFile.exists()) {
                    iconFile.createNewFile();
                }

                try (OutputStream output = new FileOutputStream(iconFile)) {
                    Streams.pipe(input, output);
                }
            } catch (IOException e) {
                getLogger().warning("Failed to copy default icon.png to overrides/");
            }
        }

        listen(
                new ResourcePackApplyListener(this, service),
                new PresetsWriter(overridesFolder, optionalsFolder),
                new PackMetaWriter(this)
        );

        registerCommand("uracle", new UracleCommand(this, service));

        // fire resource pack generation after the server finishes loading
        Bukkit.getScheduler().runTaskLater(this, () -> service.fireGenerate(), 1L);
    }

    @Override
    public void onDisable() {
        UracleProvider.unsetService();
    }

    public void loadInfo() {
        ConfigurationSection config = getConfig();

        // load 'metadata'
        if (config.contains("metadata")) {
            info = PackMeta.of(
                    config.getInt("metadata.format", 7),
                    Texts.colorize(config.getString("metadata.description"))
            );
        } else {
            info = null;
        }
    }

    @Nullable
    public PackMeta getInfo() {
        return info;
    }

    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(
                "message." + key,
                "Missing message: " + key
        ));
    }

    //#region Helper functions
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
    //#endregion

}
