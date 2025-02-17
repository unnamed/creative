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
package team.unnamed.creative.atlas;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.base.Vector2Float;

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

    /**
     * The default divisor value for the coordinates of
     * the regions to copy from the source image of a
     * {@link UnstitchAtlasSource unstitch atlas source}.
     *
     * @since 1.1.0
     */
    Vector2Float DEFAULT_DIVISOR = Vector2Float.ONE;

    /**
     * @since 1.0.0
     * @deprecated Use {@link #DEFAULT_DIVISOR} X component instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    double DEFAULT_X_DIVISOR = 1.0D;

    /**
     * @since 1.0.0
     * @deprecated Use {@link #DEFAULT_DIVISOR} Y component instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
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
     * Returns the divisor used for determining the units used
     * by in the coordinates of regions.
     *
     * @return The divisor
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.1.0
     */
    @NotNull Vector2Float divisor();

    /**
     * Returns the X divisor, used for determining the units used
     * by in the x coordinate of regions.
     *
     * @return The X divisor
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     * @deprecated Use {@link #divisor()} X component instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    default double xDivisor() {
        return divisor().x();
    }

    /**
     * Returns the Y divisor, used for determining the units used
     * by in the y coordinate of regions.
     *
     * @return The Y divisor
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     * @deprecated Use {@link #divisor()} Y component instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    default double yDivisor() {
        return divisor().y();
    }

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
         * Gets the position of the top-left corner
         * of the region.
         *
         * @return The region top-left corner coordinates
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.1.0
         */
        @NotNull Vector2Float position();

        /**
         * Gets the X coordinate of the top-left
         * corner of the region
         *
         * @return The X coordinate
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.0.0
         * @deprecated Use {@link #position()} X component instead
         */
        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
        default double x() {
            return position().x();
        }

        /**
         * Gets the Y coordinate of the top-left
         * corner of the region
         *
         * @return The Y coordinate
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.0.0
         * @deprecated Use {@link #position()} Y component instead
         */
        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
        default double y() {
            return position().y();
        }

        /**
         * Gets the dimensions of the region
         * (X = width, Y = height)
         *
         * @return The region dimensions
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.1.0
         */
        @NotNull Vector2Float dimensions();

        /**
         * Gets the width of the region
         *
         * @return The width
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.0.0
         * @deprecated Use {@link #dimensions()} X component instead
         */
        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
        default double width() {
            return dimensions().x();
        }

        /**
         * Gets the height of the region
         *
         * @return The height
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.0.0
         * @deprecated Use {@link #dimensions()} Y component instead
         */
        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
        default double height() {
            return dimensions().y();
        }

        /**
         * Creates a new {@link Region} instance.
         *
         * @param sprite     The sprite name
         * @param position   The coordinates of the top-left corner of the region
         * @param dimensions The dimensions of the region (width, height)
         * @return The new region instance
         * @sincePackFormat 12
         * @sinceMinecraft 1.19.3
         * @since 1.1.0
         */
        static @NotNull Region region(final @NotNull Key sprite, final @NotNull Vector2Float position, final @NotNull Vector2Float dimensions) {
            return new UnstitchAtlasSourceImpl.RegionImpl(sprite, position, dimensions);
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
         * @deprecated In favor of {@link #region(Key, Vector2Float, Vector2Float)}
         */
        @Deprecated
        @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
        static @NotNull Region of(final @NotNull Key sprite, final double x, final double y, final double width, final double height) {
            return region(sprite, new Vector2Float((float) x, (float) y), new Vector2Float((float) width, (float) height));
        }

    }

}
