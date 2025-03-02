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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a scaling mode for a GUI sprite.
 *
 * @sinceMinecraft 1.20.2
 * @sincePackFormat 18
 * @since 1.2.0
 */
@ApiStatus.NonExtendable
public interface GuiScaling extends Examinable {
    /**
     * Returns a {@link GuiScaling} instance that makes the sprite
     * stretch across the desired space.
     *
     * @return The created {@link GuiScaling} instance
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    static @NotNull StretchGuiScaling stretch() {
        return StretchGuiScalingImpl.INSTANCE;
    }

    /**
     * Returns a {@link GuiScaling} instance that makes the sprite
     * repeat itself across the desired space, starting from top-left.
     *
     * @param width  The number of pixels for the sprite to cover
     *               on-screen across its width, must be positive
     * @param height The number of pixels for the sprite to cover
     *               on-screen across its height, must be positive
     * @return The created {@link GuiScaling} instance
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    static @NotNull TileGuiScaling tile(final int width, final int height) {
        return new TileGuiScalingImpl(width, height);
    }

    /**
     * Returns a {@link GuiScaling} instance that slices the sprite into
     * 4 corners, 4 edges and 1 center slice, which will be tiled across
     * the desired space.
     *
     * @param width  The number of pixels for the sprite to cover
     *               on-screen across its width, must be positive
     * @param height The number of pixels for the sprite to cover
     *               on-screen across its height, must be positive
     * @param border The border sizes
     * @param stretchInner If the texture should be stretched instead of tiled
     * @return The created {@link GuiScaling} instance
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    static @NotNull NineSliceGuiScaling nineSlice(final int width, final int height, final @NotNull GuiBorder border, final boolean stretchInner) {
        return new NineSliceGuiScalingImpl(width, height, border, stretchInner);
    }

    /**
     * Returns a {@link GuiScaling} instance that slices the sprite into
     * 4 corners, 4 edges and 1 center slice, which will be tiled across
     * the desired space.
     *
     * @param width  The number of pixels for the sprite to cover
     *               on-screen across its width, must be positive
     * @param height The number of pixels for the sprite to cover
     *               on-screen across its height, must be positive
     * @param border The border size for all sides
     * @param stretchInner If the texture should be stretched instead of tiled
     * @return The created {@link GuiScaling} instance
     * @sinceMinecraft 1.20.2
     * @sincePackFormat 18
     * @since 1.2.0
     */
    static @NotNull NineSliceGuiScaling nineSlice(final int width, final int height, final int border, final boolean stretchInner) {
        return new NineSliceGuiScalingImpl(width, height, GuiBorder.border(border), stretchInner);
    }
}
