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
package team.unnamed.creative.item.special;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.HeadType;

/**
 * Represents a special render for heads.
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 */
public interface HeadSpecialRender extends SpecialRender {
    @ApiStatus.Internal
    float DEFAULT_ANIMATION = 0;

    /**
     * Returns the head type of this special render.
     *
     * @return The head type
     */
    @NotNull HeadType kind();

    /**
     * Returns the key for the texture to use, without {@code textures/entity}
     * prefix and {@code .png} suffix.
     * <ul>
     *     <li>If absent, default texture will be used, depending on the {@link #kind()}</li>
     *     <lI>If present, {@code minecraft:profile} component is ignored</lI>
     * </ul>
     *
     * @return The texture key
     */
    @Nullable Key texture();

    /**
     * Returns the float value that controls the head animation
     * (like Piglin ears or Ender Dragon jaw), defaults to zero.
     *
     * @return The animation value
     */
    float animation();
}
