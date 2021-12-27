package team.unnamed.uracle;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import team.unnamed.uracle.event.ResourcePackGenerateEvent;
import team.unnamed.uracle.export.ResourceExporter;
import team.unnamed.uracle.export.ResourceExporterFactory;
import team.unnamed.uracle.pack.ResourcePack;
import team.unnamed.uracle.pack.ResourcePackApplication;
import team.unnamed.uracle.pack.ResourcePackLocation;
import team.unnamed.uracle.player.PlayerManager;
import team.unnamed.uracle.resourcepack.ResourcePackSender;

import java.io.IOException;
import java.util.logging.Level;

public class BukkitUracle implements Uracle {

    private final Plugin plugin;
    private final PlayerManager playerManager;

    private ResourceExporter exporter;

    @Nullable private ResourcePack pack;
    private ResourcePackApplication application;

    public BukkitUracle(Plugin plugin) {
        this.plugin = plugin;
        this.playerManager = new BukkitPlayerManager(plugin);
    }

    @Override
    public PlayerManager players() {
        return playerManager;
    }

    @Override
    public @Nullable ResourcePack getPack() {
        return pack;
    }

    @Override
    public void setPackApplication(ResourcePackApplication application) {
        this.application = application;

        if (pack == null) {
            // pack not created, return
            return;
        }

        // update pack properties
        pack = pack.withProperties(application);

        // re-send to players who don't have the server
        // resource-pack if it is required, so they get
        // kicked
        if (application.required()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!playerManager.hasPack(player.getUniqueId())) {
                    ResourcePackSender.send(player, pack);
                }
            }
        }
    }

    @Override
    public void setPackLocation(ResourcePackLocation location) {
        pack = pack.withLocation(location);
    }

    @Override
    public void fireGenerate() {
        loadConfiguration();
        if (exporter != null) {
            try {
                plugin.getLogger().info("Exporting resource-pack...");
                ResourcePackLocation location = exporter.export(output ->
                        ResourcePackGenerateEvent.call(new DefaultResourcePackBuilder(output)));
                if (location != null) {
                    plugin.getLogger().info("Uploaded resource-pack to " + location.url());

                    pack = ResourcePack.of(location, application);
                }
            } catch (IOException e) {
                plugin.getLogger().log(
                        Level.SEVERE,
                        "Failed to export resource pack",
                        e
                );
            }
        }
    }

    private void loadConfiguration() {
        ConfigurationSection config = plugin.getConfig();

        // load 'exporter'
        {
            String exporterSource = config.getString("generation");

            try {
                exporter = ResourceExporterFactory.of(
                        plugin.getDataFolder(),
                        exporterSource
                );
            } catch (IOException e) {
                plugin.getLogger().log(
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
    }


}
