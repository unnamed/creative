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

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.metadata.animation.AnimationMeta;
import team.unnamed.creative.metadata.filter.FilterMeta;
import team.unnamed.creative.metadata.language.LanguageMeta;
import team.unnamed.creative.metadata.overlays.OverlaysMeta;
import team.unnamed.creative.metadata.pack.PackMeta;
import team.unnamed.creative.metadata.texture.TextureMeta;
import team.unnamed.creative.metadata.villager.VillagerMeta;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static team.unnamed.creative.util.MoreCollections.immutableMapOf;

/**
 * Represents a metadata file resource for a specific
 * metadata-able resource, or a top level metadata file
 * (pack.mcmeta)
 */
public class Metadata implements Examinable {

    public static final Metadata EMPTY = new Metadata(Collections.emptyMap());

    private final Map<Class<?>, MetadataPart> parts;

    private Metadata(Map<Class<?>, MetadataPart> parts) {
        requireNonNull(parts, "parts");
        this.parts = immutableMapOf(parts);
    }

    public Collection<MetadataPart> parts() {
        return parts.values();
    }

    public <T extends MetadataPart> @Nullable T meta(Class<T> type) {
        @SuppressWarnings("unchecked")
        T part = (T) parts.get(type);
        return part;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Metadata.Builder toBuilder() {
        Metadata.Builder builder = Metadata.builder();
        for (Map.Entry<Class<?>, MetadataPart> entry : parts.entrySet()) {
            builder.add(
                    (Class) entry.getKey(),
                    entry.getValue()
            );
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return parts.equals(metadata.parts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parts);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Map<Class<?>, MetadataPart> parts = new HashMap<>();

        private Builder() {
        }

        public <T extends MetadataPart> Builder add(Class<T> type, T part) {
            requireNonNull(type, "type");
            requireNonNull(part, "part");
            parts.put(type, part);
            return this;
        }

        // overloads of known metadata parts
        public Builder add(PackMeta meta) {
            return add(PackMeta.class, meta);
        }

        public Builder add(AnimationMeta meta) {
            return add(AnimationMeta.class, meta);
        }

        public Builder add(LanguageMeta meta) {
            return add(LanguageMeta.class, meta);
        }

        public Builder add(TextureMeta meta) {
            return add(TextureMeta.class, meta);
        }

        public Builder add(VillagerMeta meta) {
            return add(VillagerMeta.class, meta);
        }

        public Builder add(FilterMeta meta) {
            return add(FilterMeta.class, meta);
        }

        /**
         * Adds an overlay meta part.
         *
         * @param meta The added overlay meta part.
         * @return This builder.
         * @since 1.1.0
         * @sinceMinecraft 1.20.2
         * @sincePackFormat 18
         */
        public @NotNull Builder add(final @NotNull OverlaysMeta meta) {
            return add(OverlaysMeta.class, meta);
        }

        public Metadata build() {
            return new Metadata(parts);
        }

    }

}
