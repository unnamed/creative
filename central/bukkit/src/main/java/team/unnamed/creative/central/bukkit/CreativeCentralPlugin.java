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

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.central.CreativeCentral;
import team.unnamed.creative.central.CreativeCentralProvider;
import team.unnamed.creative.central.bukkit.listener.ResourcePackStatusListener;
import team.unnamed.creative.central.bukkit.request.BukkitResourcePackRequestSender;
import team.unnamed.creative.central.common.event.EventBusImpl;
import team.unnamed.creative.central.common.server.CommonResourcePackServer;
import team.unnamed.creative.central.event.EventBus;
import team.unnamed.creative.central.event.pack.ResourcePackGenerateEvent;
import team.unnamed.creative.central.request.ResourcePackRequestSender;
import team.unnamed.creative.central.server.CentralResourcePackServer;

import java.io.IOException;
import java.util.logging.Level;

@SuppressWarnings("unused") // instantiated via reflection by the server
public final class CreativeCentralPlugin extends JavaPlugin implements CreativeCentral {

    private EventBus eventBus;
    private ResourcePackRequestSender requestSender;
    private CentralResourcePackServer resourcePackServer;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        eventBus = new EventBusImpl<>(Plugin.class, getLogger());
        requestSender = BukkitResourcePackRequestSender.bukkit();
        resourcePackServer = new CommonResourcePackServer();

        listen(
                new ResourcePackStatusListener(this)
        );

        // register service providers
        Bukkit.getServicesManager().register(CreativeCentral.class, this, this, ServicePriority.High);
        CreativeCentralProvider.set(this);

        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (eventBus == null) {
                getLogger().warning("Unexpected status, event bus was null when trying to" +
                        " generate the resource pack. Is the server shutting down?");
                return;
            }

            ResourcePack resourcePack = ResourcePack.create();
            // TODO: Load from the initial resources folder
            eventBus.call(ResourcePackGenerateEvent.class, new ResourcePackGenerateEvent(resourcePack));
            getLogger().info("The resource pack has been generated successfully");
        }, 1L);
    }

    @Override
    public void onDisable() {
        eventBus = null;
        requestSender = null;

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
        return null;
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
