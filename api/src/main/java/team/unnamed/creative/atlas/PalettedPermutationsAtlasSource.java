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
package team.unnamed.creative.atlas;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;

/**
 * A type of {@link AtlasSource} used to dynamically generate
 * new textures in memory based on a set of color palettes.
 *
 * @sincePackFormat 13
 * @sinceMinecraft 1.19.4
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface PalettedPermutationsAtlasSource extends AtlasSource {

    /**
     * Gets the list of keys of the base textures. These textures will be used
     * to generate variants of them that have been modified by color palettes.
     *
     * @return The list of base textures.
     * @sincePackFormat 13
     * @sinceMinecraft 1.19.4
     * @since 1.0.0
     */
    @NotNull @Unmodifiable List<Key> textures();

    /**
     * Gets the key of a color palette key file. This is used to define the set of key
     * pixel colors to swap out with the color palettes defined.
     *
     * @return The key of the color palette key file.
     * @sincePackFormat 13
     * @sinceMinecraft 1.19.4
     * @since 1.0.0
     */
    @NotNull Key paletteKey();

    /**
     * Gets the map of permutations from suffix to a key of a color palette file.
     *
     * <p>The suffix is prepended to the key of the output variant textures, with a {@code _}
     * character separating the suffix and the base texture name.</p>
     *
     * <p>The number of pixels in each color palette must be the same as that of the {@link #paletteKey()}
     * defined for this source. Pixels are compared by RGB value. The alpha channel is ignored for key
     * matching, but in the resulting texture the alpha channel is multiplied with the color palette's
     * alpha channel. Pixels that do not match the {@link #paletteKey()} are copied over to the resulting
     * texture as-is.</p>
     *
     * @return The map of permutations from suffix to a key of a color palette file.
     * @sincePackFormat 13
     * @sinceMinecraft 1.19.4
     * @since 1.0.0
     */
    @NotNull @Unmodifiable Map<String, Key> permutations();

}
