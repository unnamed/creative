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
package team.unnamed.creative.equipment;

import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class EquipmentLayerImpl implements EquipmentLayer {
    private final Key texture;
    private final EquipmentLayerDye dye;
    private final boolean usePlayerTexture;

    EquipmentLayerImpl(final @NotNull Key texture, final @Nullable EquipmentLayerDye dye, final boolean usePlayerTexture) {
        this.texture = requireNonNull(texture, "texture");
        this.dye = dye;
        this.usePlayerTexture = usePlayerTexture;
    }

    @Override
    public @NotNull Key texture() {
        return texture;
    }

    @Override
    public @Nullable EquipmentLayerDye dye() {
        return dye;
    }

    @Override
    public boolean usePlayerTexture() {
        return usePlayerTexture;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("texture", texture),
                ExaminableProperty.of("dye", dye),
                ExaminableProperty.of("usePlayerTexture", usePlayerTexture)
        );
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EquipmentLayerImpl)) return false;
        final EquipmentLayerImpl that = (EquipmentLayerImpl) obj;
        return texture.equals(that.texture)
                && Objects.equals(dye, that.dye)
                && usePlayerTexture == that.usePlayerTexture;
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture, dye, usePlayerTexture);
    }

    @Override
    public @NotNull String toString() {
        return examine(StringExaminer.simpleEscaping());
    }
}
