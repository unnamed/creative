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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.central.CreativeCentral;
import team.unnamed.creative.central.CreativeCentralProvider;
import team.unnamed.creative.central.bukkit.action.ActionManager;
import team.unnamed.creative.central.bukkit.listener.CreativeResourcePackStatusListener;
import team.unnamed.creative.central.bukkit.listener.ResourcePackSendListener;
import team.unnamed.creative.central.bukkit.listener.ResourcePackStatusListener;
import team.unnamed.creative.central.bukkit.request.BukkitResourcePackRequestSender;
import team.unnamed.creative.central.bukkit.util.Components;
import team.unnamed.creative.central.common.event.EventBusImpl;
import team.unnamed.creative.central.common.export.ResourcePackExporterFactory;
import team.unnamed.creative.central.common.server.CommonResourcePackServer;
import team.unnamed.creative.central.common.util.LocalAddressProvider;
import team.unnamed.creative.central.common.util.Streams;
import team.unnamed.creative.central.event.EventBus;
import team.unnamed.creative.central.event.pack.ResourcePackGenerateEvent;
import team.unnamed.creative.central.event.pack.ResourcePackStatusEvent;
import team.unnamed.creative.central.export.ResourcePackExporter;
import team.unnamed.creative.central.export.ResourcePackLocation;
import team.unnamed.creative.central.request.ResourcePackRequest;
import team.unnamed.creative.central.request.ResourcePackRequestSender;
import team.unnamed.creative.central.server.CentralResourcePackServer;
import team.unnamed.creative.central.server.ServeOptions;
import team.unnamed.creative.metadata.pack.PackMeta;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
        ActionManager actionManager = new ActionManager();
        ConfigurationSection feedbackSection = getConfig().getConfigurationSection("feedback");
        actionManager.load(feedbackSection);
        eventBus.listen(this, ResourcePackStatusEvent.class, new CreativeResourcePackStatusListener(actionManager));

        // start resource pack server if enabled
        loadResourcePackServer();

        // register service providers
        registerService();

        Bukkit.getScheduler().runTaskLater(this, this::callGenerate, 1L);
    }

    private void registerService() {
        Bukkit.getServicesManager().register(CreativeCentral.class, this, this, ServicePriority.High);
        CreativeCentralProvider.set(this);
    }

    private void unregisterService() {
        CreativeCentralProvider.unset();
        Bukkit.getServicesManager().unregister(CreativeCentral.class, this);
    }

    private void loadResourcePackServer() {
        ConfigurationSection config = getConfig().getConfigurationSection("export.localhost");
        boolean enabled = config != null && config.getBoolean("enabled");

        if (enabled) {
            String address = getConfig().getString("export.localhost.address", "");
            int port = getConfig().getInt("export.localhost.port");

            // if address is empty, automatically detect the server's address
            if (address.trim().isEmpty()) {
                try {
                    address = LocalAddressProvider.getLocalAddress(getConfig().getStringList("--what-is-my-ip-services"));
                } catch (IOException e) {
                    getLogger().log(Level.SEVERE, "An exception was caught when trying to get the local server address", e);
                }

                if (address == null) {
                    getLogger().log(Level.SEVERE, "Couldn't get the local server address");
                }
            }

            if (address != null) {
                try {
                    resourcePackServer.open(address, port);
                } catch (IOException e) {
                    getLogger().log(Level.SEVERE, "Failed to open the resource pack server", e);
                }
            }
        }
    }

    private void callGenerate() {
        if (eventBus == null) {
            getLogger().warning("Unexpected status, event bus was null when trying to" +
                    " generate the resource pack. Is the server shutting down?");
            return;
        }

        String exportType = getConfig().getString("export.type", "mcpacks");
        ResourcePackExporter exporter = ResourcePackExporterFactory.create(exportType, getDataFolder(), resourcePackServer, getLogger());

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

        unregisterService();
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
