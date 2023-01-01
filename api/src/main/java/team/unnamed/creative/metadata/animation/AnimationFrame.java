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

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a texture animation frame, includes the
 * index of the frame and its specific but optional
 * frame-time, it will use {@link AnimationMeta#frameTime()}
 * if it is not specified
 *
 * @since 1.0.0
 */
public class AnimationFrame implements Examinable {

    /**
     * Special value for {@link AnimationFrame#frameTime}
     * to declare that the frameTime is delegated to
     * the container {@link AnimationMeta}
     */
    public static final int DELEGATE_FRAME_TIME = -1;

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

    private AnimationFrame(int index, int frameTime) {
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
        AnimationFrame frame = (AnimationFrame) o;
        return index == frame.index
                && frameTime == frame.frameTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, frameTime);
    }

    /**
     * Creates a new animation frame with the specified
     * index and frame time
     *
     * @param index The frame index
     * @param frameTime The frame specific time
     * @return A new and independent {@link AnimationFrame} instance
     * @since 1.0.0
     */
    public static AnimationFrame of(int index, int frameTime) {
        return new AnimationFrame(index, frameTime);
    }

    /**
     * Creates a new animation frame with the specified
     * index, no frame time is specified here, it depends
     * on {@link AnimationMeta}
     *
     * @param index The animation frame index
     * @return A new, dependent {@link AnimationFrame} instance
     * @since 1.0.0
     */
    public static AnimationFrame of(int index) {
        return new AnimationFrame(index, DELEGATE_FRAME_TIME);
    }

}
