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
package team.unnamed.creative.central.bukkit.request;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;
import team.unnamed.creative.central.bukkit.util.Version;
import team.unnamed.creative.central.request.ResourcePackRequest;
import team.unnamed.creative.central.request.ResourcePackRequestSender;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public final class BukkitResourcePackRequestSender implements ResourcePackRequestSender {

    private static final ResourcePackRequestSender INSTANCE = new BukkitResourcePackRequestSender();

    private static final Method SET_RESOURCE_PACK_METHOD;
    private static final Method GET_HANDLE_METHOD;
    private static final @Nullable Method DESERIALIZE_COMPONENT_METHOD;

    static {
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit."
                    + Version.CURRENT + ".entity.CraftPlayer");

            GET_HANDLE_METHOD = craftPlayerClass.getDeclaredMethod("getHandle");

            if (Version.CURRENT.minor() < 17) {

                // we use getHandle() and then .setResourcePack(String, String)
                // compatible with both Spigot and Paper
                SET_RESOURCE_PACK_METHOD = Class.forName("net.minecraft.server." + Version.CURRENT + ".EntityPlayer")
                        .getDeclaredMethod("setResourcePack", String.class, String.class);

                DESERIALIZE_COMPONENT_METHOD = null;
            } else {

                // we use getHandle() and then setResourcePack(String, String, boolean, IChatBaseComponent)
                // for 1.17 or "a" method for and 1.18, compatible with both Spigot and Paper
                SET_RESOURCE_PACK_METHOD = Class.forName("net.minecraft.server.level.EntityPlayer")
                        .getDeclaredMethod(
                                Version.CURRENT.minor() == 17 ? "setResourcePack" : "a",
                                String.class,
                                String.class,
                                boolean.class,
                                Class.forName("net.minecraft.network.chat.IChatBaseComponent")
                        );

                DESERIALIZE_COMPONENT_METHOD = Class.forName(
                        "net.minecraft.network.chat.IChatBaseComponent$ChatSerializer"
                ).getDeclaredMethod("a", String.class);
            }
        } catch (ReflectiveOperationException e) {
            // probably found an unsupported version of spigot
            throw new IllegalStateException(
                    "Cannot find setResourcePack method",
                    e
            );
        }
    }

    private BukkitResourcePackRequestSender() {
    }

    @Override
    @SuppressWarnings("JavaReflectionInvocation") // ide detects parameter mismatch
    public void send(Object playerObject, ResourcePackRequest request) {
        if (!(playerObject instanceof Player)) {
            throw new IllegalArgumentException("Provided 'player' is not an actual Bukkit Player: " + playerObject);
        }

        Player player = (Player) playerObject;
        try {
            Object handle = GET_HANDLE_METHOD.invoke(player);

            if (Version.CURRENT.minor() < 17) {
                // 'required' and 'prompt' fields not supported
                SET_RESOURCE_PACK_METHOD.invoke(
                        handle,
                        request.url(),
                        request.hash()
                );
            } else {
                Component prompt = request.prompt();
                String jsonPrompt = prompt == null ? null : GsonComponentSerializer.gson().serialize(prompt);
                SET_RESOURCE_PACK_METHOD.invoke(
                        handle,
                        request.url(),
                        request.hash(),
                        request.required(),
                        jsonPrompt == null ? null : DESERIALIZE_COMPONENT_METHOD.invoke(null, jsonPrompt)
                );
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "Cannot apply resource pack",
                    e
            );
        }
    }

    public static ResourcePackRequestSender bukkit() {
        return INSTANCE;
    }

}
