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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class EquipmentImpl implements Equipment {
    private final Key key;
    private final Map<EquipmentLayerType, List<EquipmentLayer>> layers;

    EquipmentImpl(final @NotNull Key key, final @NotNull Map<EquipmentLayerType, List<EquipmentLayer>> layers) {
        this.key = requireNonNull(key, "key");
        this.layers = requireNonNull(layers, "layers");
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull Map<EquipmentLayerType, List<EquipmentLayer>> layers() {
        return layers;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("key", key),
                ExaminableProperty.of("layers", layers)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EquipmentImpl equipment = (EquipmentImpl) o;
        return Objects.equals(key, equipment.key) && Objects.equals(layers, equipment.layers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, layers);
    }

    @Override
    public @NotNull String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    static final class BuilderImpl implements Builder {
        private Key key;
        private final Map<EquipmentLayerType, List<EquipmentLayer>> layers = new LinkedHashMap<>();

        @Override
        public @NotNull Builder key(final @NotNull Key key) {
            this.key = requireNonNull(key, "key");
            return this;
        }

        @Override
        public @NotNull Builder addLayer(final @NotNull EquipmentLayerType type, final @NotNull EquipmentLayer layer) {
            requireNonNull(type, "type");
            requireNonNull(layer, "layer");
            layers.computeIfAbsent(type, t -> new ArrayList<>()).add(layer);
            return this;
        }

        @Override
        public @NotNull Equipment build() {
            requireNonNull(key, "key");
            return new EquipmentImpl(key, layers);
        }
    }
}
