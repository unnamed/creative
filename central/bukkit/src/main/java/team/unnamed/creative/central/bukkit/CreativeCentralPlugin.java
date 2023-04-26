/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.creative.central.bukkit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.central.CreativeCentral;
import team.unnamed.creative.central.CreativeCentralProvider;
import team.unnamed.creative.central.bukkit.action.Action;
import team.unnamed.creative.central.bukkit.action.ActionParser;
import team.unnamed.creative.central.bukkit.listener.ResourcePackSendListener;
import team.unnamed.creative.central.bukkit.listener.ResourcePackStatusListener;
import team.unnamed.creative.central.bukkit.request.BukkitResourcePackRequestSender;
import team.unnamed.creative.central.bukkit.util.Components;
import team.unnamed.creative.central.common.event.EventBusImpl;
import team.unnamed.creative.central.common.export.MCPacksHttpExporter;
import team.unnamed.creative.central.common.server.CommonResourcePackServer;
import team.unnamed.creative.central.common.util.Streams;
import team.unnamed.creative.central.event.EventBus;
import team.unnamed.creative.central.event.pack.ResourcePackGenerateEvent;
import team.unnamed.creative.central.event.pack.ResourcePackStatusEvent;
import team.unnamed.creative.central.export.ResourcePackExporter;
import team.unnamed.creative.central.export.ResourcePackLocation;
import team.unnamed.creative.central.pack.ResourcePackStatus;
import team.unnamed.creative.central.request.ResourcePackRequest;
import team.unnamed.creative.central.request.ResourcePackRequestSender;
import team.unnamed.creative.central.server.CentralResourcePackServer;
import team.unnamed.creative.central.server.ServeOptions;
import team.unnamed.creative.metadata.pack.PackMeta;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

@SuppressWarnings("unused") // instantiated via reflection by the server
public final class CreativeCentralPlugin extends JavaPlugin implements CreativeCentral {

    private ServeOptions serveOptions;
    private EventBus eventBus;
    private ResourcePackRequestSender requestSender;
    private CentralResourcePackServer resourcePackServer;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        serveOptions = new ServeOptions();
        eventBus = new EventBusImpl<>(Plugin.class, getLogger());
        requestSender = BukkitResourcePackRequestSender.bukkit();
        resourcePackServer = new CommonResourcePackServer();

        // load serve/send options
        serveOptions.serve(true);
        serveOptions.delay(getConfig().getInt("send.delay"));

        // register event listeners
        listen(
                new ResourcePackStatusListener(this),
                new ResourcePackSendListener(this)
        );

        // load actions
        List<Action> declineActions = ActionParser.parse(getConfig(), "feedback.declined");
        List<Action> acceptActions = ActionParser.parse(getConfig(), "feedback.accepted");
        List<Action> failedActions = ActionParser.parse(getConfig(), "feedback.failed");
        List<Action> successActions = ActionParser.parse(getConfig(), "feedback.success");
        eventBus.listen(this, ResourcePackStatusEvent.class, event -> {
            Player player = (Player) event.player();
            ResourcePackStatus status = event.status();


            List<Action> actions = switch (status) {
                case FAILED -> failedActions;
                case ACCEPTED -> acceptActions;
                case DECLINED -> declineActions;
                case LOADED -> successActions;
            };

            for (Action action : actions) {
                action.execute(player);
            }
        });

        String exportType = getConfig().getString("export.type", "mcpacks");
        ResourcePackExporter exporter = switch (exportType.toLowerCase(Locale.ROOT)) {
            case "mcpacks", "mc-packs" -> new MCPacksHttpExporter(getLogger());
            // case "localhost" -> new LocalExporter(this);
            default ->
                throw new IllegalArgumentException("Unknown export method: " + exportType);
        };

        // register service providers
        Bukkit.getServicesManager().register(CreativeCentral.class, this, this, ServicePriority.High);
        CreativeCentralProvider.set(this);

        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (eventBus == null) {
                getLogger().warning("Unexpected status, event bus was null when trying to" +
                        " generate the resource pack. Is the server shutting down?");
                return;
            }

            File resourcesFolder = new File(getDataFolder(), "resources");
            if (!resourcesFolder.exists()) {
                resourcesFolder.mkdirs();
                // copy pack.mcmeta and pack.png inside resources
                try {
                    try (InputStream meta = getResource("resources/pack.mcmeta")) {
                        Streams.pipeToFile(meta, new File(resourcesFolder, "pack.mcmeta"));
                    }

                    try (InputStream icon = getResource("resources/pack.png")) {
                        Streams.pipeToFile(icon, new File(resourcesFolder, "pack.png"));
                    }

                    getLogger().info("Successfully generated resources folder");
                } catch (IOException e) {
                    getLogger().log(Level.WARNING, "Failed to copy pack.mcmeta and pack.png files" +
                            " inside the resources folder", e);
                }
            }

            ResourcePack resourcePack = resourcesFolder.exists()
                    ? MinecraftResourcePackReader.minecraft().readFromDirectory(resourcesFolder)
                    : ResourcePack.create();

            // process the pack meta
            {
                PackMeta meta = resourcePack.packMeta();
                if (meta == null) {
                    getLogger().warning("Couldn't find pack metadata in the generated resource-pack");
                } else {
                    // TODO: We do this because creative doesn't support components for descriptions yet
                    String description = meta.description();
                    String legacyColoredDescription = LegacyComponentSerializer.legacySection()
                            .serialize(Components.deserialize(description));
                    resourcePack.packMeta(
                            meta.format(),
                            legacyColoredDescription
                    );
                }
            }

            getLogger().info("The initial resource pack resources have been loaded");

            eventBus.call(ResourcePackGenerateEvent.class, new ResourcePackGenerateEvent(resourcePack));
            getLogger().info("The resource pack has been generated successfully");

            try {
                ResourcePackLocation location = exporter.export(resourcePack);
                boolean required = getConfig().getBoolean("send.request.required");
                String prompt = getConfig().getString("send.request.prompt");
                serveOptions.request(ResourcePackRequest.of(
                        location.url(),
                        location.hash(),
                        required,
                        Component.text(prompt)
                ));
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Failed to export resource pack", e);
            }
        }, 1L);
    }

    @Override
    public void onDisable() {
        eventBus = null;
        requestSender = null;
        serveOptions = null;

        if (resourcePackServer != null) {
            try {
                resourcePackServer.close();
            } catch (IOException e) {
                getLogger().log(Level.WARNING, "Failed to close resource pack server", e);
            }
            resourcePackServer = null;
        }

        CreativeCentralProvider.unset();
    }

    private void listen(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public CentralResourcePackServer server() {
        return resourcePackServer;
    }

    @Override
    public ServeOptions serveOptions() {
        return serveOptions;
    }

    @Override
    public ResourcePackRequestSender requestSender() {
        return requestSender;
    }

    @Override
    public EventBus eventBus() {
        return eventBus;
    }

}
