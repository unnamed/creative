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
package team.unnamed.creative.metadata.gui;

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents border sizes for a GUI sprite.
 *
 * <p>Sizes are specified in pixels, pixels that
 * the border slices should cover on-screen.</p>
 *
 * @sinceMinecraft 1.20.2
 * @sincePackFormat 18
 * @since 1.2.0
 */
public interface GuiBorder extends Examinable {
    /**
     * Returns a {@link GuiBorder} instance that will have the
     * specified border sizes on each side, all sizes must must
     * be non-negative.
     *
     * @param top    The top border size in pixels
     * @param bottom The bottom border size in pixels
     * @param left   The left border size in pixels
     * @param right  The right border size in pixels
     * @return The created {@link GuiBorder} instance
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    static @NotNull GuiBorder border(final int top, final int bottom, final int left, final int right) {
        return new GuiBorderImpl(top, bottom, left, right);
    }

    /**
     * Returns a {@link GuiBorder} instance that will have a uniform
     * border size on all sides specified by the given {@code size},
     * must be non-negative.
     *
     * @return The created {@link GuiBorder} instance
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    static @NotNull GuiBorder border(final int size) {
        return border(size, size, size, size);
    }

    /**
     * Returns the number of pixels for the border to cover
     * on-screen across its top side, non-negative.
     *
     * @return The number of pixels for the border to cover
     * on-screen across its top side
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    int top();

    /**
     * Returns the number of pixels for the border to cover
     * on-screen across its bottom side, non-negative.
     *
     * @return The number of pixels for the border to cover
     * on-screen across its bottom side
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    int bottom();

    /**
     * Returns the number of pixels for the border to cover
     * on-screen across its left side, non-negative.
     *
     * @return The number of pixels for the border to cover
     * on-screen across its left side
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    int left();

    /**
     * Returns the number of pixels for the border to cover
     * on-screen across its right side, non-negative.
     *
     * @return The number of pixels for the border to cover
     * on-screen across its right side
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    int right();
}
