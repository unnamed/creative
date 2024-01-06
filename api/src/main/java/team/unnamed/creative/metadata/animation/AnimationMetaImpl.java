/*
 * This file is part of creative, licensed under the MIT license
 *
 * Copyright (c) 2021-2024 Unnamed Team
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
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.metadata.MetadataPart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableListOf;

final class AnimationMetaImpl implements AnimationMeta {
    private final boolean interpolate;
    private final int width;
    private final int height;
    private final int frameTime;
    private final List<AnimationFrame> frames;

    AnimationMetaImpl(
            final boolean interpolate,
            final int width,
            final int height,
            final int frameTime,
            final @NotNull List<AnimationFrame> frames
    ) {
        this.interpolate = interpolate;
        this.width = width;
        this.height = height;
        this.frameTime = frameTime;
        this.frames = immutableListOf(requireNonNull(frames, "frames"));
    }

    @Override
    public @NotNull Class<? extends MetadataPart> type() {
        return AnimationMeta.class;
    }

    @Override
    public boolean interpolate() {
        return interpolate;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public int frameTime() {
        return frameTime;
    }

    @Override
    public @NotNull @Unmodifiable List<AnimationFrame> frames() {
        return frames;
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
    public @NotNull String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AnimationMetaImpl that = (AnimationMetaImpl) o;
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

    static final class BuilderImpl implements Builder {
        private boolean interpolate = DEFAULT_INTERPOLATE;
        private int width = DEFAULT_WIDTH;
        private int height = DEFAULT_HEIGHT;
        private int frameTime = DEFAULT_FRAMETIME;
        private List<AnimationFrame> frames = Collections.emptyList();

        @Override
        public @NotNull Builder interpolate(final boolean interpolate) {
            this.interpolate = interpolate;
            return this;
        }

        @Override
        public @NotNull Builder width(final int width) {
            this.width = width;
            return this;
        }

        @Override
        public @NotNull Builder height(final int height) {
            this.height = height;
            return this;
        }

        @Override
        public @NotNull Builder frameTime(final int frameTime) {
            this.frameTime = frameTime;
            return this;
        }

        @Override
        public @NotNull Builder frames(final @NotNull List<AnimationFrame> frames) {
            requireNonNull(frames, "frames");
            this.frames = new ArrayList<>(frames);
            return this;
        }

        @Override
        public @NotNull Builder addFrame(final @NotNull AnimationFrame frame) {
            requireNonNull(frame, "frame");
            frames.add(frame);
            return this;
        }

        @Override
        public @NotNull AnimationMeta build() {
            return new AnimationMetaImpl(interpolate, width, height, frameTime, frames);
        }
    }
}
