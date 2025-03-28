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

/**
 * Special render that renders a single chest.
 *
 * @since 1.8.0
 * @sinceMinecraft 1.21.4
 * @sincePackFormat 43
 */
public interface ChestSpecialRender extends SpecialRender {
    Key NORMAL_CHEST_TEXTURE = Key.key(Key.MINECRAFT_NAMESPACE, "normal");
    Key TRAPPED_CHEST_TEXTURE = Key.key(Key.MINECRAFT_NAMESPACE, "trapped");
    Key ENDER_CHEST_TEXTURE = Key.key(Key.MINECRAFT_NAMESPACE, "ender");
    Key GIFT_CHEST_TEXTURE = Key.key(Key.MINECRAFT_NAMESPACE, "christmas");

    @ApiStatus.Experimental
    float DEFAULT_OPENNESS = 0;

    /**
     * Returns the key for the texture to use, without {@code textures/entity/chest} prefix
     * and {@code .png} suffix.
     *
     * @return The texture key
     */
    @NotNull Key texture();

    /**
     * Returns a float value between 0 and 1 representing the openness of the chest.
     * (0.0 (default) means fully closed and 1.0 means fully open)
     *
     * @return The openness of the chest
     */
    float openness();
}
