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
package team.unnamed.creative.metadata.animation;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.metadata.MetadataPart;
import team.unnamed.creative.file.ResourceWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableListOf;

/**
 * Block and item textures support animation by placing each
 * additional frame below the past. The animation is then controlled
 * using this {@link AnimationMeta} type
 *
 * @since 1.0.0
 */
public class AnimationMeta implements MetadataPart {

    public static final int DEFAULT_FRAMETIME = 1;
    public static final int DEFAULT_WIDTH = -1;
    public static final int DEFAULT_HEIGHT = -1;
    public static final boolean DEFAULT_INTERPOLATE = false;

    private final boolean interpolate;
    private final int width;
    private final int height;
    private final int frameTime;
    private final List<AnimationFrame> frames;

    private AnimationMeta(
            boolean interpolate,
            int width,
            int height,
            int frameTime,
            List<AnimationFrame> frames
    ) {
        requireNonNull(frames, "frames");
        this.interpolate = interpolate;
        this.width = width;
        this.height = height;
        this.frameTime = frameTime;
        this.frames = immutableListOf(frames);
    }

    @Override
    public String name() {
        return "animation";
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
     * be overwritten by specific {@link AnimationFrame} elements
     *
     * @return The fallback frame-time for frames that
     * don't specify one
     */
    public int frameTime() {
        return frameTime;
    }

    /**
     * Returns an unmodifiable list of the animation
     * {@link AnimationFrame} frames
     *
     * @return The animation frames
     */
    public List<AnimationFrame> frames() {
        return Collections.unmodifiableList(frames);
    }

    @Override
    public void serialize(ResourceWriter writer) {

        writer.startObject();

        if (interpolate != DEFAULT_INTERPOLATE) {
            writer.key("interpolate").value(interpolate);
        }

        if (width != DEFAULT_WIDTH) {
            writer.key("width").value(width);
        }

        if (height != DEFAULT_HEIGHT) {
            writer.key("height").value(height);
        }

        if (frameTime != DEFAULT_FRAMETIME) {
            writer.key("frametime").value(frameTime);
        }

        if (!frames.isEmpty()) {
            writer.key("frames").startArray();

            for (AnimationFrame frame : frames) {
                int index = frame.index();
                int time = frame.frameTime();

                if (frameTime == time || frameTime == AnimationFrame.DELEGATE_FRAME_TIME) {
                    // same as default frameTime, we can skip it
                    writer.value(index);
                } else {
                    // specific frameTime, write as an object
                    writer.startObject()
                            .key("index").value(index)
                            .key("time").value(time)
                            .endObject();
                }
            }

            writer.endArray();
        }

        writer.endObject();
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
            List<AnimationFrame> frames
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
     * @return A new builder instance
     * @since 1.0.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Mutable and fluent-style builder for {@link AnimationMeta}
     * and {@link AnimationFrame} instances
     *
     * @since 1.0.0
     */
    public static class Builder {

        private boolean interpolate;
        private int width;
        private int height;
        private int frameTime;
        private List<AnimationFrame> frames = Collections.emptyList();

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

        public Builder frames(List<AnimationFrame> frames) {
            this.frames = requireNonNull(frames, "frames");
            return this;
        }

        public Builder frames(AnimationFrame... frames) {
            requireNonNull(frames, "frames");
            this.frames = Arrays.asList(frames);
            return this;
        }

        public Builder frames(int... indexes) {
            this.frames = new ArrayList<>();
            for (int index : indexes) {
                this.frames.add(AnimationFrame.of(index));
            }
            return this;
        }

        /**
         * Finishes building the {@link AnimationMeta} instance,
         * this method may fail if values were not correctly
         * provided
         *
         * @return The recently created animation meta
         */
        public AnimationMeta build() {
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