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
package team.unnamed.uracle.texture;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Block and item textures support animation by placing each
 * additional frame below the past. The animation is then controlled
 * using this {@link AnimationMeta} type
 *
 * @since 1.0.0
 */
public class AnimationMeta implements Examinable {

    private final boolean interpolate;
    private final int width;
    private final int height;
    private final int frameTime;
    private final List<Frame> frames;

    private AnimationMeta(
            boolean interpolate,
            int width,
            int height,
            int frameTime,
            List<Frame> frames
    ) {
        requireNonNull(frames, "frames");
        this.interpolate = interpolate;
        this.width = width;
        this.height = height;
        this.frameTime = frameTime;
        // create a copy to avoid modifications
        this.frames = new ArrayList<>(frames);
    }

    /**
     * Determines if the animation is interpolated,
     * in that case, Minecraft will generate frames
     * between our frames
     *
     * @return True if animation is interpolated
     */
    public boolean interpolate() {
        return interpolate;
    }

    /**
     * Returns the width of the tile, as a direct ratio
     * rather than in pixels. Can be used by resource
     * packs to have frames that are not perfect squares
     *
     * @return The tile width
     */
    public int width() {
        return width;
    }

    /**
     * Returns the height of the tile as a ratio rather than
     * in pixels. Can be used by resource packs to have frames
     * that are not perfect squares
     *
     * @return The tile height
     */
    public int height() {
        return height;
    }

    /**
     * The global frame time for all frames, it can
     * be overwritten by specific {@link Frame} elements
     *
     * @return The fallback frame-time for frames that
     * don't specify one
     */
    public int frameTime() {
        return frameTime;
    }

    /**
     * Returns an unmodifiable list of the animation
     * {@link Frame} frames
     *
     * @return The animation frames
     */
    public List<Frame> frames() {
        return Collections.unmodifiableList(frames);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("interpolate", interpolate),
                ExaminableProperty.of("width", width),
                ExaminableProperty.of("height", height),
                ExaminableProperty.of("frameTime", frameTime),
                ExaminableProperty.of("frames", frames)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimationMeta that = (AnimationMeta) o;
        return interpolate == that.interpolate
                && width == that.width
                && height == that.height
                && frameTime == that.frameTime
                && frames.equals(that.frames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interpolate, width, height, frameTime, frames);
    }

    /**
     * Creates a new {@link AnimationMeta} instance
     * using the provided values
     *
     * @param interpolate If frames must be interpolated
     * @param width Frame-texture width ratio
     * @param height Frame-texture height ratio
     * @param frameTime Default frame time
     * @param frames Animation frames
     * @return A new {@link AnimationMeta} instance
     */
    public static AnimationMeta of(
            boolean interpolate,
            int width,
            int height,
            int frameTime,
            List<Frame> frames
    ) {
        return new AnimationMeta(
                interpolate,
                width,
                height,
                frameTime,
                frames
        );
    }

    /**
     * Returns a new instance of our builder implementation
     * used to build {@link AnimationMeta} instances
     *
     * @return A new fresh builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Represents a texture animation frame, includes the
     * index of the frame and its specific but optional
     * frame-time, it will use {@link AnimationMeta#frameTime()}
     * if it is not specified
     *
     * @since 1.0.0
     */
    public static class Frame implements Examinable {

        /**
         * A number corresponding to position of a frame from the
         * top, with the top frame being 0.
         */
        private final int index;

        /**
         * The time in ticks to show this frame, uses
         * {@link AnimationMeta#frameTime()} when it is
         * not specified
         */
        private final int frameTime;

        /**
         * Instantiated via {@link AnimationMeta.Builder#frame},
         * not by the end-user
         */
        private Frame(int index, int frameTime) {
            this.index = index;
            this.frameTime = frameTime;
        }

        /**
         * Returns the animation frame index
         *
         * @return The frame index
         */
        public int index() {
            return index;
        }

        /**
         * Returns the animation frame time
         *
         * @return The frame time in ticks
         */
        public int frameTime() {
            return frameTime;
        }

        @Override
        public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(
                    ExaminableProperty.of("index", index),
                    ExaminableProperty.of("frameTime", frameTime)
            );
        }

        @Override
        public String toString() {
            return examine(StringExaminer.simpleEscaping());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Frame frame = (Frame) o;
            return index == frame.index
                    && frameTime == frame.frameTime;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, frameTime);
        }

        /**
         * Creates a new frame using the given index and
         * frameTime
         */
        public static Frame of(int index, int frameTime) {
            return new Frame(index, frameTime);
        }

    }

    /**
     * Mutable and fluent-style builder for {@link AnimationMeta}
     * and {@link Frame} instances, since animation meta has a lot
     * of parameters and frame depends on meta, we must use this
     *
     * @since 1.0.0
     */
    public static class Builder {

        private boolean interpolate;
        private int width;
        private int height;
        private int frameTime;
        private List<Frame> frames;

        private Builder() {
        }

        public Builder interpolate(boolean interpolate) {
            this.interpolate = interpolate;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder frameTime(int frameTime) {
            this.frameTime = frameTime;
            return this;
        }

        public Builder frames(List<Frame> frames) {
            requireNonNull(frames, "frames");
            // copy the list in case we need to modify
            // it on frame(int) or frame(int, int)
            this.frames = new ArrayList<>(frames);
            return this;
        }

        public Builder frame(int index, int frameTime) {
            if (frames == null) {
                // create frames if null
                frames = new ArrayList<>();
            }
            frames.add(new Frame(index, frameTime));
            return this;
        }

        public Builder frame(int index) {
            // use default frame time
            return frame(index, this.frameTime);
        }

        /**
         * Finishes building the {@link AnimationMeta} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created animation meta
         */
        public AnimationMeta build() {
            // frames can be initially null, in that
            // case we use an empty list instead
            if (frames == null) {
                frames = Collections.emptyList();
            }
            return new AnimationMeta(
                    interpolate,
                    width,
                    height,
                    frameTime,
                    frames
            );
        }
    }

}