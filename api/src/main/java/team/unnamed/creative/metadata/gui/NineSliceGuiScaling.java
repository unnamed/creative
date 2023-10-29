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
package team.unnamed.creative.metadata.gui;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A type of {@link GuiScaling} that slices the sprite into 4 corners,
 * 4 edges and 1 center slice, which will be tiled across the desired
 * space.
 *
 * @sinceMinecraft 1.20.2
 * @sincePackFormat 18
 * @see GuiScaling#nineSlice(int, int, GuiBorder)
 * @since 1.2.0
 */
@ApiStatus.NonExtendable
public interface NineSliceGuiScaling extends GuiScaling {
    /**
     * Returns the number of pixels for the sprite to cover
     * on-screen across its width, always positive.
     *
     * @return The number of pixels for the sprite to cover
     * on-screen across its width
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    int width();

    /**
     * Returns the number of pixels for the sprite to cover
     * on-screen across its height, always positive.
     *
     * @return The number of pixels for the sprite to cover
     * on-screen across its height
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    int height();

    /**
     * Returns the border sizes.
     *
     * @return The border sizes
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    @NotNull GuiBorder border();
}
