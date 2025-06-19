/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2025 Unnamed Team
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
package team.unnamed.creative.waypoint;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.equipment.Equipment;
import team.unnamed.creative.overlay.ResourceContainer;
import team.unnamed.creative.part.ResourcePackPart;

import java.util.List;

/**
 * Defines the appearance of waypoints in the locator bar
 *
 * <p>First added in 1.21.6, see <a href="https://www.minecraft.net/en-us/article/minecraft-java-edition-1-21-6">1.21.6 Changelog</a></p>
 *
 * @sinceMinecraft 1.21.6
 * @since 1.8.4
 */
public interface WaypointStyle extends ResourcePackPart, Keyed, Examinable {
    @ApiStatus.Internal
    int DEFAULT_NEAR_DISTANCE = 128;

    @ApiStatus.Internal
    int DEFAULT_FAR_DISTANCE = 332;

    /**
     * Returns the key of this style, which waypoints
     * can reference
     *
     * @return The key of this waypoint style
     */
    @Override
    @NotNull Key key();

    /**
     * The distance in blocks at which the waypoint icon begins its visual transition between sprites. The first
     * sprite in the sprites list is always used for any distance less than this value
     *
     * @return The current near distance
     */
    int nearDistance();

    /**
     * The distance in blocks at which the waypoint icon completes its visual transition. It must be greater than
     * nearDistance. At this distance and beyond, the last sprite in the sprites list is used
     *
     * @return The current far distance
     */
    int farDistance();

    /**
     * A list of sprites which cycle between the nearDistance and farDistance
     *
     * @return The current list of sprites
     */
    @NotNull List<Key> sprites();

    @Override
    default void addTo(final @NotNull ResourceContainer resourceContainer) {
        resourceContainer.waypointStyle(this);
    }

    /**
     * Creates a new builder for creating {@link WaypointStyle} instances.
     *
     * @return A new builder
     * @since 1.8.4
     */
    @Contract("-> new")
    static @NotNull WaypointStyle.Builder waypointStyle() {
        return new WaypointStyleImpl.BuilderImpl();
    }

    /**
     * Represents a builder for creating {@link Equipment} instances.
     *
     * @since 1.8.0
     */
    interface Builder {
        /**
         * Sets the key of the waypoint style.
         *
         * @param key The key of the waypoint style
         * @return This builder
         */
        @Contract("_ -> this")
        @NotNull WaypointStyle.Builder key(@NotNull Key key);

        /**
         * Sets the near distance value.
         *
         * @param nearDistance The near distance in blocks
         * @return This builder
         */
        @Contract("_ -> this")
        @NotNull WaypointStyle.Builder nearDistance(int nearDistance);

        /**
         * Sets the far distance value.
         *
         * @param farDistance The far distance in blocks
         * @return This builder
         */
        @Contract("_ -> this")
        @NotNull WaypointStyle.Builder farDistance(int farDistance);

        /**
         * Adds a new sprite.
         *
         * @param sprite The sprite key
         * @return This builder
         */
        @Contract("_ -> this")
        @NotNull WaypointStyle.Builder sprite(@NotNull Key sprite);

        /**
         * Creates a new {@link WaypointStyle} instance with the current builder values.
         *
         * @return A new waypoint style instance
         */
        @Contract("-> new")
        @NotNull WaypointStyle build();
    }
}
