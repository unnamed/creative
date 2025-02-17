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
package team.unnamed.creative.metadata.animation;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.metadata.MetadataPart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Block and item textures support animation by placing each
 * additional frame below the past. The animation is then controlled
 * using this {@link AnimationMeta} type
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface AnimationMeta extends MetadataPart {
    /**
     * Creates a new {@link AnimationMeta} instance
     * using the provided values
     *
     * @param interpolate If frames must be interpolated
     * @param width       Frame-texture width ratio
     * @param height      Frame-texture height ratio
     * @param frameTime   Default frame time
     * @param frames      Animation frames
     * @return A new {@link AnimationMeta} instance
     * @since 1.3.0
     */
    @Contract("_, _, _, _, _ -> new")
    static @NotNull AnimationMeta animation(final boolean interpolate, final int width, final int height, final int frameTime, final @NotNull List<AnimationFrame> frames) {
        return new AnimationMetaImpl(interpolate, width, height, frameTime, frames);
    }

    /**
     * Creates a new {@link AnimationMeta} instance
     * using the provided values
     *
     * @param interpolate If frames must be interpolated
     * @param width       Frame-texture width ratio
     * @param height      Frame-texture height ratio
     * @param frameTime   Default frame time
     * @param frames      Animation frames
     * @return A new {@link AnimationMeta} instance
     * @since 1.0.0
     * @deprecated Use {@link #animation(boolean, int, int, int, List)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    @Contract("_, _, _, _, _ -> new")
    static @NotNull AnimationMeta of(final boolean interpolate, final int width, final int height, final int frameTime, final @NotNull List<AnimationFrame> frames) {
        return animation(interpolate, width, height, frameTime, frames);
    }

    /**
     * Returns a new instance of our builder implementation
     * used to build {@link AnimationMeta} instances
     *
     * @return A new builder instance
     * @since 1.3.0
     */
    @Contract("-> new")
    static @NotNull Builder animation() {
        return new AnimationMetaImpl.BuilderImpl();
    }

    /**
     * Returns a new instance of our builder implementation
     * used to build {@link AnimationMeta} instances
     *
     * @return A new builder instance
     * @since 1.0.0
     * @deprecated Use {@link #animation()} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    @Contract("-> new")
    static @NotNull Builder builder() {
        return animation();
    }

    int DEFAULT_FRAMETIME = 1;
    int DEFAULT_WIDTH = -1;
    int DEFAULT_HEIGHT = -1;
    boolean DEFAULT_INTERPOLATE = false;

    /**
     * Determines if the animation is interpolated,
     * in that case, Minecraft will generate frames
     * between our frames
     *
     * @return True if animation is interpolated
     * @since 1.0.0
     */
    boolean interpolate();

    /**
     * Returns the width of the tile, as a direct ratio
     * rather than in pixels. Can be used by resource
     * packs to have frames that are not perfect squares
     *
     * @return The tile width
     * @since 1.0.0
     */
    int width();

    /**
     * Returns the height of the tile as a ratio rather than
     * in pixels. Can be used by resource packs to have frames
     * that are not perfect squares
     *
     * @return The tile height
     * @since 1.0.0
     */
    int height();

    /**
     * The global frame time for all frames, it can
     * be overwritten by specific {@link AnimationFrame} elements
     *
     * @return The fallback frame-time for frames that
     * don't specify one
     * @since 1.0.0
     */
    int frameTime();

    /**
     * Returns an unmodifiable list of the animation
     * {@link AnimationFrame} frames
     *
     * @return The animation frames
     * @since 1.0.0
     */
    @NotNull @Unmodifiable List<AnimationFrame> frames();

    /**
     * Mutable and fluent-style builder for {@link AnimationMeta}
     * and {@link AnimationFrame} instances
     *
     * @since 1.0.0
     */
    interface Builder {
        /**
         * Sets whether this animation should interpolate
         * frames or not.
         *
         * <p>Interpolated animations will have some generated
         * keyframes between the specified ones for a smoother
         * look.</p>
         *
         * @param interpolate Whether this animation should interpolate frames or not
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder interpolate(final boolean interpolate);

        /**
         * Sets the width of the tile, as a direct ratio
         * rather than in pixels. Can be used by resource
         * packs to have frames that are not perfect squares
         *
         * @param width The tile width
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder width(final int width);

        /**
         * Sets the height of the tile as a ratio rather than
         * in pixels. Can be used by resource packs to have frames
         * that are not perfect squares
         *
         * @param height The tile height
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder height(final int height);

        /**
         * Sets the global frame time for all frames, it can
         * be overwritten by specific {@link AnimationFrame} elements
         *
         * @param frameTime The fallback frame-time for frames that
         *                  don't specify one
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder frameTime(final int frameTime);

        /**
         * Sets the animation {@link AnimationFrame} frames
         *
         * @param frames The animation frames
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        @NotNull Builder frames(final @NotNull List<AnimationFrame> frames);

        /**
         * Sets the animation {@link AnimationFrame} frames
         *
         * @param frames The animation frames
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        default @NotNull Builder frames(final @NotNull AnimationFrame @NotNull ... frames) {
            requireNonNull(frames, "frames");
            return frames(Arrays.asList(frames));
        }

        /**
         * Sets the animation {@link AnimationFrame} frames
         *
         * @param indexes The animation frames indexes
         * @return This builder
         * @since 1.0.0
         */
        @Contract("_ -> this")
        default @NotNull Builder frames(final int @NotNull ... indexes) {
            requireNonNull(indexes, "indexes");
            final List<AnimationFrame> frames = new ArrayList<>();
            for (final int index : indexes) {
                frames.add(AnimationFrame.frame(index));
            }
            return frames(frames);
        }

        /**
         * Adds an animation {@link AnimationFrame} frame
         *
         * @param frame The animation frame
         * @return This builder
         * @since 1.3.0
         */
        @Contract("_ -> this")
        @NotNull Builder addFrame(final @NotNull AnimationFrame frame);

        /**
         * Adds an animation {@link AnimationFrame} frame index
         *
         * @param index The animation frame index
         * @return This builder
         * @since 1.3.0
         */
        @Contract("_ -> this")
        default @NotNull Builder addFrame(final int index) {
            return addFrame(AnimationFrame.frame(index));
        }

        /**
         * Adds an animation {@link AnimationFrame} frame index
         * with a specific frame time
         *
         * @param index     The animation frame index
         * @param frameTime The animation frame time
         * @return This builder
         * @since 1.3.0
         */
        @Contract("_, _ -> this")
        default @NotNull Builder addFrame(final int index, final int frameTime) {
            return addFrame(AnimationFrame.frame(index, frameTime));
        }

        /**
         * Finishes building the {@link AnimationMeta} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created animation meta
         * @since 1.0.0
         */
        @Contract("-> new")
        @NotNull AnimationMeta build();
    }
}