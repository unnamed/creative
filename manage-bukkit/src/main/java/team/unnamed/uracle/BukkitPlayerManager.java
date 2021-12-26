/*
 * This file is part of uracle, licensed under the MIT license
 *
 * Copyright (c) 2021 Unnamed Team
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
package team.unnamed.uracle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import team.unnamed.uracle.player.PlayerManager;

import java.util.UUID;

public class BukkitPlayerManager
        implements PlayerManager {

    private static final String METADATA_KEY = "uracle__pack";

    private final Plugin plugin;

    public BukkitPlayerManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean hasPack(UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);

        if (player == null) {
            // invalid player!
            return false;
        }

        return player.hasMetadata(METADATA_KEY);
    }

    @Override
    public void setHasPack(UUID playerId, boolean state) {
        Player player = Bukkit.getPlayer(playerId);

        if (player != null) {
            if (state) {
                if (player.hasMetadata(METADATA_KEY)) {
                    // only apply if they don't have the
                    // metadata already
                    player.setMetadata(
                            METADATA_KEY,
                            new FixedMetadataValue(plugin, METADATA_KEY)
                    );
                }
            } else {
                player.removeMetadata(METADATA_KEY, plugin);
            }
        }
    }

}
