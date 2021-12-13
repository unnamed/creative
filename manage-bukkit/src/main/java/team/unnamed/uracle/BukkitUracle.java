package team.unnamed.uracle;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import team.unnamed.uracle.event.ResourcePackGenerateEvent;
import team.unnamed.uracle.generate.ExportManager;
import team.unnamed.uracle.generate.StandardExportManager;
import team.unnamed.uracle.generate.exporter.ResourceExporter;
import team.unnamed.uracle.generate.exporter.ResourceExporterFactory;
import team.unnamed.uracle.resourcepack.ReflectiveResourcePackSender;
import team.unnamed.uracle.resourcepack.ResourcePack;
import team.unnamed.uracle.resourcepack.ResourcePackSender;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BukkitUracle implements Uracle {

    private final ResourcePackSender sender = new ReflectiveResourcePackSender();
    private final ExportManager exportManager;

    public BukkitUracle(ConfigurationSection config, Logger logger) {
        this.exportManager = new StandardExportManager(
                location -> {
                    boolean required = config.getBoolean("application.force", false);
                    String prompt = config.getString("application.prompt", null);

                    if (prompt != null) {
                        // colorize and translate to JSON
                        prompt = ChatColor.translateAlternateColorCodes('&', prompt);
                        prompt = ComponentSerializer.toString(TextComponent.fromLegacyText(prompt));
                    }

                    return new ResourcePack(
                            location.getUrl(),
                            location.getHash(),
                            required,
                            prompt
                    );
                },
                ResourcePackGenerateEvent::call,
                content -> {
                    String exporterSource = config.getString("generation");
                    try {
                        ResourceExporter delegate = ResourceExporterFactory.of(exporterSource);
                        if (delegate == null) {
                            return null;
                        } else {
                            return delegate.export(content);
                        }
                    } catch (IOException e) {
                        logger.log(
                                Level.SEVERE,
                                "Cannot create exporter from string '" + exporterSource + "'",
                                e
                        );
                        return null;
                    }
                }
        );
    }

    @Override
    public ResourcePackSender getSender() {
        return sender;
    }

    @Override
    public ExportManager getExportManager() {
        return exportManager;
    }

}
