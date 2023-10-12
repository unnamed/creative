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
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.util.MoreCollections;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class PalettedPermutationsAtlasSourceImpl implements PalettedPermutationsAtlasSource {

    private final List<Key> textures;
    private final Key paletteKey;
    private final Map<String, Key> permutations;

    PalettedPermutationsAtlasSourceImpl(
            final @NotNull List<Key> textures,
            final @NotNull Key paletteKey,
            final @NotNull Map<String, Key> permutations
    ) {
        requireNonNull(textures, "textures");
        requireNonNull(paletteKey, "paletteKey");
        requireNonNull(permutations, "permutations");
        this.textures = MoreCollections.immutableListOf(textures);
        this.paletteKey = paletteKey;
        this.permutations = MoreCollections.immutableMapOf(permutations);
    }

    @Override
    public @NotNull @Unmodifiable List<Key> textures() {
        return textures;
    }

    @Override
    public @NotNull @Unmodifiable Key paletteKey() {
        return paletteKey;
    }

    @Override
    public @NotNull @Unmodifiable Map<String, Key> permutations() {
        return permutations;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("textures", textures),
                ExaminableProperty.of("paletteKey", paletteKey),
                ExaminableProperty.of("permutations", permutations)
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
        PalettedPermutationsAtlasSourceImpl that = (PalettedPermutationsAtlasSourceImpl) o;
        if (!textures.equals(that.textures)) return false;
        if (!paletteKey.equals(that.paletteKey)) return false;
        return permutations.equals(that.permutations);
    }

    @Override
    public int hashCode() {
        int result = textures.hashCode();
        result = 31 * result + paletteKey.hashCode();
        result = 31 * result + permutations.hashCode();
        return result;
    }

}
