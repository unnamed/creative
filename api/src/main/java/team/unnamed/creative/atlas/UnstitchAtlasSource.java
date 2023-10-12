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
package team.unnamed.creative.atlas;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * An {@link AtlasSource} that copies rectangular regions from
 * other images.
 *
 * @sincePackFormat 12
 * @sinceMinecraft 1.19.3
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface UnstitchAtlasSource extends AtlasSource {

    double DEFAULT_X_DIVISOR = 1.0D;
    double DEFAULT_Y_DIVISOR = 1.0D;

    /**
     * Returns the location of the resource to copy from
     * within the pack (relative to textures directory,
     * implied .png extension).
     *
     * @return The resource location
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     */
    @NotNull Key resource();

    /**
     * Returns the list of regions to copy from the source
     * image.
     *
     * @return The list of regions
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     */
    @NotNull @Unmodifiable List<Region> regions();

    /**
     * Returns the X divisor, used for determining the units used
     * by in the x coordinate of regions.
     *
     * @return The X divisor
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     */
    double xDivisor();

    /**
     * Returns the Y divisor, used for determining the units used
     * by in the y coordinate of regions.
     *
     * @return The Y divisor
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     */
    double yDivisor();

    /**
     * A region to copy from a {@link UnstitchAtlasSource} image.
     *
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     */
    @ApiStatus.NonExtendable
    interface Region extends Examinable {

        /**
         * Gets the sprite name.
         *
         * @return The sprite name
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.0.0
         */
        @NotNull Key sprite();

        /**
         * Gets the X coordinate of the top-left
         * corner of the region
         *
         * @return The X coordinate
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.0.0
         */
        double x();

        /**
         * Gets the Y coordinate of the top-left
         * corner of the region
         *
         * @return The Y coordinate
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.0.0
         */
        double y();

        /**
         * Gets the width of the region
         *
         * @return The width
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.0.0
         */
        double width();

        /**
         * Gets the height of the region
         *
         * @return The height
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.0.0
         */
        double height();

        /**
         * Creates a new {@link Region} instance.
         *
         * @param sprite The sprite name
         * @param x      The X coordinate of the top-left corner of the region
         * @param y      The Y coordinate of the top-left corner of the region
         * @param width  The width of the region
         * @param height The height of the region
         * @return The new region instance
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.1.0
         */
        static @NotNull Region region(final @NotNull Key sprite, final double x, final double y, final double width, final double height) {
            return new UnstitchAtlasSourceImpl.RegionImpl(sprite, x, y, width, height);
        }

        /**
         * Creates a new {@link Region} instance.
         *
         * @param sprite The sprite name
         * @param x      The X coordinate of the top-left corner of the region
         * @param y      The Y coordinate of the top-left corner of the region
         * @param width  The width of the region
         * @param height The height of the region
         * @return The new region instance
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.0.0
         * @deprecated In favor of {@link #region(Key, double, double, double, double)}
         */
        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
        static @NotNull Region of(final @NotNull Key sprite, final double x, final double y, final double width, final double height) {
            return region(sprite, x, y, width, height);
        }

    }

}
