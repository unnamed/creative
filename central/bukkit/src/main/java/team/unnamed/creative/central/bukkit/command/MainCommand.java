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
package team.unnamed.creative.central.bukkit.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.central.bukkit.CreativeCentralPlugin;
import team.unnamed.creative.central.bukkit.util.Components;
import team.unnamed.creative.central.request.ResourcePackRequest;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final CreativeCentralPlugin central;
    private final Map<String, Component> messageCache = new ConcurrentHashMap<>();

    public MainCommand(CreativeCentralPlugin central) {
        this.central = central;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        String subcommand = args.length == 0 ? "help" : args[0].toLowerCase(Locale.ROOT);

        switch (subcommand) {
            case "reload" -> {
                if (!sender.hasPermission("creative-central.command.reload")) {
                    send(sender, "command.no-permission.reload");
                    return true;
                }

                // more than one argument, we are being strict, show usage
                if (args.length != 1) {
                    send(sender, "command.usage.reload");
                    return true;
                }

                central.reloadConfig();
                messageCache.clear();
                central.generate().thenAccept(resourcePack ->
                    send(sender, "command.feedback.reload"));
            }

            case "apply" -> {
                if (!sender.hasPermission("creative-central.command.apply")) {
                    send(sender, "command.no-permission.apply.self");
                    return true;
                }

                Collection<? extends Player> targets;

                if (sender.hasPermission("creative-central.command.apply.others")) {
                    if (args.length != 1 && args.length != 2) {
                        send(sender, "command.usage.apply.others");
                        return true;
                    }

                    if (args.length == 2 && !args[1].equals("@p")) {
                        // other argument
                        if (args[1].equals("@a")) {
                            targets = Bukkit.getOnlinePlayers();
                            if (targets.isEmpty()) {
                                send(sender, "command.apply.no-players");
                                return true;
                            }
                        } else {
                            Player player = Bukkit.getPlayer(args[1]);
                            if (player == null) {
                                send(sender, "command.apply.player-not-found");
                                return true;
                            }

                            targets = Collections.singleton(player);
                        }
                    } else {
                        if (!(sender instanceof Player player)) {
                            send(sender, "command.apply.no-player");
                            return true;
                        }

                        targets = Collections.singleton(player);
                    }
                } else {
                    // does not have permission to send to others
                    if (args.length == 2) {
                        send(sender, "command.no-permission.apply.others");
                        return true;
                    } else if (args.length != 1) {
                        send(sender, "command.usage.apply.self");
                        return true;
                    }

                    if (!(sender instanceof Player player)) {
                        send(sender, "command.apply.no-player");
                        return true;
                    }

                    // apply to self
                    targets = Collections.singleton(player);
                }

                ResourcePackRequest request = central.serveOptions().request();
                if (request == null) {
                    send(sender, "command.apply.no-resource-pack");
                    return true;
                }

                // send
                for (Player target : targets) {
                    central.requestSender().send(target, request);
                }
                send(sender, "command.feedback.apply");
            }

            case "help", "?" -> {
                if (!sender.hasPermission("creative-central.command.help")) {
                    send(sender, "command.no-permission.help");
                    return true;
                }

                send(sender, "command.help");
            }

            // covers 'help', '?' and any other unknown subcommand
            default ->
                send(sender, "command.usage.unknown");
        }
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @Nullable List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        return null;
    }

    private void send(CommandSender sender, String messageKey) {
        Component message = messageCache.computeIfAbsent(messageKey, k ->
                Components.deserialize(central.getConfig().getString(k, k)));
        sender.sendMessage(message);
    }

}
