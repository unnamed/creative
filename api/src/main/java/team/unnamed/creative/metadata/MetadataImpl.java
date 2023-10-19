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
package team.unnamed.creative.metadata;

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableMapOf;

final class MetadataImpl implements Metadata {
    static final Metadata EMPTY = new MetadataImpl(Collections.emptyMap());

    private final Map<Class<?>, MetadataPart> parts;

    MetadataImpl(final @NotNull Map<Class<?>, MetadataPart> parts) {
        this.parts = immutableMapOf(requireNonNull(parts, "parts"));
    }

    @Override
    public @Unmodifiable @NotNull Collection<MetadataPart> parts() {
        return parts.values();
    }

    @Override
    public <T extends MetadataPart> @Nullable T meta(final @NotNull Class<T> type) {
        MetadataPart metadataPart = parts.get(type);
        if (metadataPart == null) {
            return null;
        } else {
            return type.cast(metadataPart);
        }
    }

    @Override
    public @NotNull Builder toBuilder() {
        Builder builder = Metadata.metadata();
        for (Map.Entry<Class<?>, MetadataPart> entry : parts.entrySet()) {
            //noinspection unchecked,rawtypes
            builder.add((Class) entry.getKey(), entry.getValue());
        }
        return builder;
    }


    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("parts", parts)
        );
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetadataImpl metadata = (MetadataImpl) o;
        return parts.equals(metadata.parts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parts);
    }

    static final class BuilderImpl implements Builder {
        private final Map<Class<?>, MetadataPart> parts = new HashMap<>();

        @Override
        public @NotNull <T extends MetadataPart> Builder add(@NotNull Class<T> type, @NotNull T part) {
            requireNonNull(type, "type");
            requireNonNull(part, "part");
            parts.put(type, part);
            return this;
        }

        @Override
        public @NotNull Metadata build() {
            return new MetadataImpl(parts);
        }
    }
}