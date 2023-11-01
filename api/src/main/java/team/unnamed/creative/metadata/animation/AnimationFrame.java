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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a texture animation frame, includes the
 * index of the frame and its specific but optional
 * frame-time, it will use {@link AnimationMeta#frameTime()}
 * if it is not specified
 *
 * @since 1.0.0
 */
public interface AnimationFrame extends Examinable {
    /**
     * Creates a new animation frame with the specified
     * index and frame time
     *
     * @param index     The frame index
     * @param frameTime The frame specific time
     * @return A new and independent {@link AnimationFrame} instance
     * @since 1.3.0
     */
    @Contract("_, _ -> new")
    static @NotNull AnimationFrame frame(final int index, final int frameTime) {
        return new AnimationFrameImpl(index, frameTime);
    }

    /**
     * Creates a new animation frame with the specified
     * index and frame time
     *
     * @param index     The frame index
     * @param frameTime The frame specific time
     * @return A new and independent {@link AnimationFrame} instance
     * @since 1.0.0
     * @deprecated Use {@link #frame(int, int)} instead
     */
    @Contract("_, _ -> new")
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull AnimationFrame of(final int index, final int frameTime) {
        return frame(index, frameTime);
    }

    /**
     * Creates a new animation frame with the specified
     * index, no frame time is specified here, it depends
     * on {@link AnimationMeta}
     *
     * @param index The animation frame index
     * @return A new, dependent {@link AnimationFrame} instance
     * @since 1.3.0
     */
    @Contract("_ -> new")
    static @NotNull AnimationFrame frame(final int index) {
        return new AnimationFrameImpl(index, DELEGATE_FRAME_TIME);
    }

    /**
     * Creates a new animation frame with the specified
     * index, no frame time is specified here, it depends
     * on {@link AnimationMeta}
     *
     * @param index The animation frame index
     * @return A new, dependent {@link AnimationFrame} instance
     * @since 1.0.0
     * @deprecated Use {@link #frame(int)} instead
     */
    @Contract("_ -> new")
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull AnimationFrame of(final int index) {
        return frame(index);
    }

    /**
     * Special value for {@link AnimationFrame#frameTime}
     * to declare that the frameTime is delegated to
     * the container {@link AnimationMeta}
     *
     * @since 1.0.0
     */
    int DELEGATE_FRAME_TIME = -1;

    /**
     * Returns the animation frame index, which is number
     * corresponding to position of a frame from the top,
     * with the top frame being 0.
     *
     * @return The frame index
     * @since 1.0.0
     */
    int index();

    /**
     * Returns the animation frame time, specifies the time
     * in ticks to show this frame, uses {@link AnimationMeta#frameTime()}
     * when it is not specified.
     *
     * @return The frame time in ticks
     * @since 1.0.0
     */
    int frameTime();
}
