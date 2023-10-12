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
package team.unnamed.creative.atlas;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.KeyPattern;
import team.unnamed.creative.base.Vector2Float;
import team.unnamed.creative.texture.Texture;

import java.util.List;
import java.util.Map;

/**
 * Represents a source for {@link Atlas}. Every atlas source
 * runs in during load, in order of definition, adding or
 * removing new files to the texture list, to be later referenced
 * by block models, item models, particles, etc.
 *
 * <p>There are multiple types of atlas sources. See implementations</p>
 *
 * @sincePackFormat 12
 * @sinceMinecraft 1.19.3
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface AtlasSource extends Examinable {

    /**
     * Creates a new {@link SingleAtlasSource}, single
     * atlas sources add a single file and maps it to
     * a custom name ({@code sprite}) if set
     *
     * @param resource The texture key (It can be {@link Texture#key()})
     * @param sprite   The sprite name
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @see SingleAtlasSource
     * @since 1.0.0
     */
    static @NotNull SingleAtlasSource single(final @NotNull Key resource, final @Nullable Key sprite) {
        return new SingleAtlasSourceImpl(resource, sprite);
    }

    /**
     * Creates a new {@link SingleAtlasSource}, single
     * atlas sources add a single file.
     *
     * @param resource The texture key (It can be {@link Texture#key()})
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @see SingleAtlasSource
     * @since 1.0.0
     */
    static @NotNull SingleAtlasSource single(final @NotNull Key resource) {
        return single(resource, null);
    }

    /**
     * Creates a new {@link DirectoryAtlasSource}, directory
     * atlas sources add all the files in a directory and its
     * subdirectories, across all namespaces.
     *
     * @param source The directory in the pack to be listed
     *               (relative to the {@code textures} directory)
     * @param prefix The prefix to be prepended to the sprite name
     *               when loaded (can be empty)
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @see DirectoryAtlasSource
     * @since 1.0.0
     */
    static @NotNull DirectoryAtlasSource directory(final @NotNull String source, final @NotNull String prefix) {
        return new DirectoryAtlasSourceImpl(source, prefix);
    }

    /**
     * Creates a new {@link FilterAtlasSource}, filter atlas sources
     * remove sprites matching the given pattern, only works for entries
     * already in the list.
     *
     * @param pattern The {@link KeyPattern} to match the resources
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @see FilterAtlasSource
     * @see KeyPattern
     * @since 1.0.0
     */
    static @NotNull FilterAtlasSource filter(final @NotNull KeyPattern pattern) {
        return new FilterAtlasSourceImpl(pattern);
    }

    /**
     * Creates a new {@link UnstitchAtlasSource}, unstitch atlas sources
     * copy rectangular regions from other images.
     *
     * @param resource The resource
     * @param regions  The regions to copy
     * @param divisor  The divisor, used to determine the units used by the regions
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.1.0
     */
    static @NotNull UnstitchAtlasSource unstitch(final @NotNull Key resource, final @NotNull List<UnstitchAtlasSource.Region> regions, final @NotNull Vector2Float divisor) {
        return new UnstitchAtlasSourceImpl(resource, regions, divisor);
    }

    /**
     * Creates a new {@link UnstitchAtlasSource}, unstitch atlas sources
     * copy rectangular regions from other images.
     *
     * @param resource The resource
     * @param regions  The regions to copy
     * @param xDivisor The X divisor, used to determine the units used by the regions
     * @param yDivisor The Y divisor, used to determine the units used by the regions
     * @sincePackFormat 12
     * @sinceMinecraft 1.19.3
     * @since 1.0.0
     * @deprecated Use {@link #unstitch(Key, List, Vector2Float)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    static @NotNull UnstitchAtlasSource unstitch(final @NotNull Key resource, final @NotNull List<UnstitchAtlasSource.Region> regions, final double xDivisor, final double yDivisor) {
        return unstitch(resource, regions, new Vector2Float((float) xDivisor, (float) yDivisor));
    }

    /**
     * Creates a new {@link PalettedPermutationsAtlasSource}, paletted permutations
     * atlas sources dynamically generate textures in-memory with specific color sets.
     *
     * @param textures     A list of the base textures
     * @param paletteKey   The key of the palette file that defines the set of key pixel
     *                     colors to swap out with the color palettes defined.
     * @param permutations A map of permutations from suffix to a key of a color palette file
     * @sincePackFormat 13
     * @sinceMinecraft 1.19.4
     * @since 1.0.0
     */
    static @NotNull PalettedPermutationsAtlasSource palettedPermutations(final @NotNull List<Key> textures, final @NotNull Key paletteKey, final @NotNull Map<String, Key> permutations) {
        return new PalettedPermutationsAtlasSourceImpl(textures, paletteKey, permutations);
    }

}
